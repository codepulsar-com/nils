package com.codepulsar.nils.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.AdapterFactory;
import com.codepulsar.nils.core.util.ParameterCheck;
/** Factory for getting access to the provided NLS. A requested NLS object is cached. */
public class NilsFactory {
  private static final Logger LOG = LoggerFactory.getLogger(NilsFactory.class);
  private final NilsConfig config;
  private AdapterFactory<?> adapterFactory;

  private final Map<Locale, NLS> translationCache = new HashMap<>();

  private NilsFactory(NilsConfig config) {
    this.config = ParameterCheck.notNull(config, "config");
  }
  /**
   * Gets the NLS object for the default <tt>Locale</tt>.
   *
   * @return The NLS object for the default <tt>Locale</tt>.
   */
  public NLS nls() {
    return nls(Locale.getDefault());
  }
  /**
   * Gets the NLS object for a language tag.
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
   * Gets the NLS object for a specific <tt>Locale</tt>.
   *
   * @param locale The <tt>Locale</tt>.
   * @return The NLS object for the <tt>Locale</tt>.
   */
  public NLS nls(Locale locale) {

    NLS result =
        translationCache.computeIfAbsent(
            locale,
            (l) -> {
              return createAdapter(locale, config);
            });
    return result;
  }

  private Adapter createAdapter(Locale locale, NilsConfig config) {
    return getAdapterFactory().create(config, locale);
  }

  private AdapterFactory<?> getAdapterFactory() {
    if (adapterFactory == null) {
      try {
        this.adapterFactory =
            config.getAdapterConfig().getFactoryClass().getConstructor().newInstance();
      } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
        LOG.error("Could not create AdapterFactory. Reason {}", e.getMessage(), e);
        throw new NilsException("Could not create AdapterFactory.", e);
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
