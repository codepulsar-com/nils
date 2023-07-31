package com.codepulsar.nils.core;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
import com.codepulsar.nils.core.error.ErrorType;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.impl.NLSImpl;
import com.codepulsar.nils.core.util.ParameterCheck;
/** Factory for getting access to the provided NLS. A requested NLS object is cached. */
public class NilsFactory {
  private static final Logger LOG = LoggerFactory.getLogger(NilsFactory.class);
  private final NilsConfig config;
  private AdapterFactory<?> adapterFactory;

  private final Map<Locale, NLS> translationCache = new HashMap<>();

  private NilsFactory(NilsConfig config) {
    this.config = ParameterCheck.notNull(config, "config", NILS_CONFIG);
  }
  /**
   * Get the NLS object for the default <code>Locale</code>.
   *
   * @return The NLS object for the default <code>Locale</code>.
   */
  public NLS nls() {
    return nls(Locale.getDefault());
  }
  /**
   * Get the NLS object for a language tag.
   *
   * @param lang The language tag.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   */
  public NLS nls(String lang) {
    ParameterCheck.notNullEmptyOrBlank(lang, "lang");
    lang = lang.replace("_", "-");
    return nls(Locale.forLanguageTag(lang));
  }
  /**
   * Get the NLS object for a specific <code>Locale</code>.
   *
   * @param locale The <code>Locale</code>.
   * @return The NLS object for the <code>Locale</code>.
   */
  public NLS nls(Locale locale) {

    NLS result =
        translationCache.computeIfAbsent(
            locale,
            (l) -> {
              return createImpl(locale, config);
            });
    return result;
  }

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param context The context key.
   * @return The NLS object for the default <code>Locale</code>.
   */
  public NLS nlsWithContext(String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls().context(context);
  }

  /**
   * Get the NLS object for a language tag and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param lang The language tag.
   * @param context The context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   */
  public NLS nlsWithContext(String lang, String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls(lang).context(context);
  }

  /**
   * Get the NLS object for a specific <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param locale The <code>Locale</code>.
   * @param context The context key.
   * @return The NLS object for the <code>Locale</code>.
   */
  public NLS nlsWithContext(Locale locale, String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls(locale).context(context);
  }

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the default <code>Locale</code>.
   */
  public NLS nlsWithContext(Class<?> context) {
    ParameterCheck.notNull(context, "context");
    return nls().context(context);
  }

  /**
   * Get the NLS object for a language tag and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param lang The language tag.
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   */
  public NLS nlsWithContext(String lang, Class<?> context) {
    ParameterCheck.notNull(context, "context");
    return nls(lang).context(context);
  }

  /**
   * Get the NLS object for a language tag and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param locale The <code>Locale</code>.
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   */
  public NLS nlsWithContext(Locale locale, Class<?> context) {
    ParameterCheck.notNull(context, "context");
    return nls(locale).context(context);
  }

  private NLSImpl createImpl(Locale locale, NilsConfig config) {
    return new NLSImpl(
        getAdapterFactory().create(config.getAdapterConfig(), locale), config, locale);
  }

  private AdapterFactory<?> getAdapterFactory() {
    if (adapterFactory == null) {
      try {
        this.adapterFactory =
            config.getAdapterConfig().getFactoryClass().getConstructor().newInstance();
      } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
        LOG.error("Could not create AdapterFactory. Reason {}", e.getMessage(), e);
        throw new NilsException(ErrorType.ADAPTER_ERROR, "Could not create AdapterFactory.", e);
      }
    }
    return adapterFactory;
  }

  /**
   * Initialize the factory for an adapter using its {@link AdapterConfig}.
   *
   * @param adapterConfig A {@link AdapterConfig} object.
   * @return The create factory.
   */
  public static NilsFactory init(AdapterConfig adapterConfig) {
    return new NilsFactory(NilsConfig.init(adapterConfig));
  }

  /**
   * Initialize the factory using a {@link NilsConfig}.
   *
   * @param config A {@link NilsConfig} object.
   * @return The create factory.
   */
  public static NilsFactory init(NilsConfig config) {
    return new NilsFactory(config);
  }
}
