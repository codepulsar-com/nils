package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.error.ErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;
import static com.codepulsar.nils.core.util.ParameterCheck.notNull;
import static com.codepulsar.nils.core.util.ParameterCheck.notNullEmptyOrBlank;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

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
  private static final NLS FALLSAVE = new FallsaveNLSImpl();
  private final NilsConfig<?> config;
  private final ErrorHandler errorHandler;
  private AdapterFactory<?> adapterFactory;

  private final Map<Locale, NLS> translationCache = new HashMap<>();

  public NilsFactoryImpl(NilsConfig<?> config) {
    this.config = ParameterCheck.notNull(config, "config", nilsException(CONFIG_ERROR));
    this.errorHandler = new ErrorHandler(config);
  }

  @Override
  public NLS nls() {
    return nls(Locale.getDefault());
  }

  @Override
  public NLS nls(String lang) {
    try {
      notNullEmptyOrBlank(lang, "lang", nilsException(NLS_PARAMETER_CHECK));
      lang = lang.replace("_", "-");
      return nls(Locale.forLanguageTag(lang));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nls(Locale locale) {
    try {
      notNull(locale, "locale", nilsException(NLS_PARAMETER_CHECK));
      return translationCache.computeIfAbsent(locale, (l) -> createImpl(locale, config));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(String context) {
    return nlsForContext(context);
  }

  @Override
  public NLS nlsForContext(String context) {
    try {
      notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls().context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(String lang, String context) {
    try {
      notNullEmptyOrBlank(lang, "lang", nilsException(NLS_PARAMETER_CHECK));
      notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls(lang).context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(Locale locale, String context) {
    return nlsForContext(locale, context);
  }

  // TODO change test to the nlsFor methods
  @Override
  public NLS nlsForContext(Locale locale, String context) {
    try {
      notNull(locale, "locale", nilsException(NLS_PARAMETER_CHECK));
      notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls(locale).context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(Class<?> context) {
    return nlsForContext(context);
  }

  @Override
  public NLS nlsForContext(Class<?> context) {
    try {
      notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls().context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(String lang, Class<?> context) {
    try {
      notNullEmptyOrBlank(lang, "lang", nilsException(NLS_PARAMETER_CHECK));
      notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls(lang).context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public NLS nlsWithContext(Locale locale, Class<?> context) {
    return nlsForContext(locale, context);
  }

  @Override
  public NLS nlsForContext(Locale locale, Class<?> context) {
    try {
      notNull(locale, "locale", nilsException(NLS_PARAMETER_CHECK));
      notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
      return nls(locale).context(context);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  @Override
  public void reset() {
    var lock = new ReentrantLock();
    lock.lock();
    try {
      adapterFactory = null;
      translationCache.clear();
    } finally {
      lock.unlock();
    }
  }

  private NLS createImpl(Locale locale, NilsConfig<?> config) {
    try {
      return new NLSImpl(getAdapterFactory().create(config, locale), config, locale);
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return FALLSAVE;
    }
  }

  private AdapterFactory<?> getAdapterFactory() {
    if (adapterFactory == null) {
      try {
        this.adapterFactory = config.getFactoryClass().getConstructor().newInstance();
      } catch (Exception e) {
        LOG.error("Could not create AdapterFactory. Reason {}", e.getMessage(), e);
        throw new NilsException(ErrorTypes.ADAPTER_ERROR, "Could not create AdapterFactory.", e);
      }
    }
    return adapterFactory;
  }
}
