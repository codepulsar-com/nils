package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.NLS;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.NilsFactory;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.error.ErrorTypes;
import com.codepulsar.nils.core.util.ParameterCheck;
/** Factory for getting access to the provided NLS. A requested NLS object is cached. */
public class NilsFactoryImpl implements NilsFactory {
  private static final Logger LOG = LoggerFactory.getLogger(NilsFactoryImpl.class);
  private final NilsConfig config;
  private AdapterFactory<?> adapterFactory;

  private final Map<Locale, NLS> translationCache = new HashMap<>();

  public NilsFactoryImpl(NilsConfig config) {
    this.config = ParameterCheck.notNull(config, "config", NILS_CONFIG);
  }

  @Override
  public NLS nls() {
    return nls(Locale.getDefault());
  }

  @Override
  public NLS nls(String lang) {
    ParameterCheck.notNullEmptyOrBlank(lang, "lang");
    lang = lang.replace("_", "-");
    return nls(Locale.forLanguageTag(lang));
  }

  @Override
  public NLS nls(Locale locale) {

    NLS result =
        translationCache.computeIfAbsent(
            locale,
            (l) -> {
              return createImpl(locale, config);
            });
    return result;
  }

  @Override
  public NLS nlsWithContext(String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls().context(context);
  }

  @Override
  public NLS nlsWithContext(String lang, String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls(lang).context(context);
  }

  @Override
  public NLS nlsWithContext(Locale locale, String context) {
    ParameterCheck.notNullEmptyOrBlank(context, "context");
    return nls(locale).context(context);
  }

  @Override
  public NLS nlsWithContext(Class<?> context) {
    ParameterCheck.notNull(context, "context");
    return nls().context(context);
  }

  @Override
  public NLS nlsWithContext(String lang, Class<?> context) {
    ParameterCheck.notNull(context, "context");
    return nls(lang).context(context);
  }

  @Override
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
        throw new NilsException(ErrorTypes.ADAPTER_ERROR, "Could not create AdapterFactory.", e);
      }
    }
    return adapterFactory;
  }
}
