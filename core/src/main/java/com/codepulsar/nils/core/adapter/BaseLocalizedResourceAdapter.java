package com.codepulsar.nils.core.adapter;

import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.adapter.config.LocalizedResourceConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.adapter.util.LocalizedResourceResolver;
import com.codepulsar.nils.core.adapter.util.TranslationRetriever;
import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * The {@linkplain BaseLocalizedResourceAdapter} is a base implementation for resource based
 * adapters, which can have a fallback to other localized resources ("de_DE" &gt; "de" &gt; "").
 *
 * <p>The class provides common checks and methods using localized resources more easy.
 *
 * <p><em>Note:</em> The {@link NilsConfig} must also implement {@link LocalizedResourceConfig}.
 *
 * @param <A> The type of the {@link Adapter}
 * @param <C> The type of the {@link NilsConfig}
 */
public abstract class BaseLocalizedResourceAdapter<A extends Adapter, C extends NilsConfig<?>>
    implements Adapter {
  /** Constant for an empty {@code Locale} */
  protected static final Locale ROOT_LOCALE = new Locale("");
  /** An {@link Adapter} object using if the translation was not found by these adapter. */
  protected A fallbackAdapter;
  /** Flag, if a fallback adapter is available. */
  protected boolean fallbackPossible = true;
  /** The {@link NilsConfig} of the adapter. */
  protected final C adapterConfig;
  /** The current {@code Locale} of the adapter. */
  protected final Locale locale;
  /** The {@link AdapterContext} of the adapter. */
  protected final AdapterContext<A> adapterContext;
  /** The {@link TranslationRetriever} of the adapter. */
  protected TranslationRetriever translation;
  /** The name of the use resource. */
  protected String resourceName;
  /**
   * Creates a new instance of this class.
   *
   * @param context An {@link AdapterContext} object.
   */
  @SuppressWarnings("unchecked")
  protected BaseLocalizedResourceAdapter(AdapterContext<A> context) {
    this.adapterContext = ParameterCheck.notNull(context, "context");
    ParameterCheck.notNull(context.getConfig(), "context.config");
    ParameterCheck.notNull(context.getLocale(), "context.locale");
    if (!(context.getConfig() instanceof LocalizedResourceConfig)) {
      throw new NilsConfigException(
          "The provided AdapterConfig (%s) does not implement %s",
          context.getConfig(), LocalizedResourceConfig.class.getName());
    }
    adapterConfig = (C) context.getConfig();
    locale = context.getLocale();
    initTranslationsInternally();
    initFallbackAvailable();
  }

  /**
   * Get the translation for a key.
   *
   * @param key The key to look up.
   * @return An {@code Optional} containing the result. Maybe an empty result.
   */
  @Override
  public Optional<String> getTranslation(String key) {
    ParameterCheck.notNullEmptyOrBlank(key, "key");
    var value = translation.retrieve(key);

    if (value.isPresent()) {
      return value;
    }

    if (fallbackPossible) {
      value = getFallbackAdapter().getTranslation(key);
    }

    return value;
  }

  /**
   * Gets the {@link Adapter} for the fallback to another {@code Locale}.
   *
   * @return An {@link Adapter} object.
   */
  protected A getFallbackAdapter() {
    if (fallbackAdapter == null) {
      var variant = locale.getVariant();
      var country = locale.getCountry();
      var language = locale.getLanguage();
      if (variant.isEmpty() && !country.isEmpty() && !language.isEmpty()) {
        country = "";
      } else if (variant.isEmpty() && country.isEmpty() && !language.isEmpty()) {
        language = "";
      }
      Locale parent = new Locale(language, country);
      fallbackAdapter = adapterContext.getFactory().create(adapterContext.getConfig(), parent);
    }

    return fallbackAdapter;
  }

  /**
   * Check if a fallback adapter could be possible .
   *
   * <p>A fallback is possible if the locale is not an empty locale and the used resource is not the
   * same as the basefilename from the config.
   */
  protected void initFallbackAvailable() {
    var config = (LocalizedResourceConfig) adapterConfig;
    this.fallbackPossible =
        config.isFallbackActive()
            && (!ROOT_LOCALE.equals(locale) || !resourceName.endsWith(config.getBaseFileName()));
  }

  /**
   * Resolve the input stream for a resource.
   *
   * <p><strong>Note:</strong>Inherited class must implement the method, due to the limitation of
   * accessing resources from other module directly.
   *
   * @param resource The name of the resource in the origin module.
   * @return An {@link InputStream} object or {@code null}
   */
  protected abstract InputStream resolveInputStream(String resource);
  /**
   * Initialize the translation using a concrete implementation how to access the file resource.
   *
   * @param resolver A {@link LocalizedResourceResolver} object
   */
  protected abstract void initTranslations(LocalizedResourceResolver resolver);

  private void initTranslationsInternally() {
    var config = (LocalizedResourceConfig) adapterConfig;
    var resolver = new LocalizedResourceResolver(config, locale, this::resolveInputStream);
    initTranslations(resolver);
  }
}
