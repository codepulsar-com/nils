package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.error.ErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.Formats;
import com.codepulsar.nils.api.NLS;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.config.BaseNilsConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

/**
 * A {@link NLS} implementation used by the {@link NilsFactoryImpl} if suppressError is {@code true}
 * and an error occurs requesting a {@link NLS} object.
 */
public class FallsaveNLSImpl implements NLS {
  private static final Logger LOG = LoggerFactory.getLogger(FallsaveNLSImpl.class);
  private final NilsConfig<?> config;
  private final ErrorHandler errorHandler;
  private final NLSKeyUtil keyUtil;
  private Formats formats;
  private int counter = 0;

  FallsaveNLSImpl() {
    this.config = new FallsaveNilsConfig();
    this.errorHandler = new ErrorHandler(config);
    this.keyUtil = new NLSKeyUtil(config);
  }

  @Override
  public String get(String key) {
    counter++;
    if (counter >= 50) {
      LOG.warn("Using fallsave NLS due to a previous error creating a NLS object.");
      counter = 0;
    }
    return keyUtil.buildMissingKey(key);
  }

  @Override
  public String get(String key, Object... args) {
    return get(key);
  }

  @Override
  public String get(Class<?> key, String subKey) {

    try {
      ParameterCheck.notNull(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return keyUtil.buildMissingKey(String.format("%s.%s", key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      String keyBase = keyUtil.resolveKeyPrefix(key);
      return keyUtil.buildMissingKey(String.format("%s.%s", keyBase, subKey));
    }

    String keyBase = keyUtil.resolveKeyPrefix(key);
    return get(String.format("%s.%s", keyBase, subKey));
  }

  @Override
  public String get(Class<?> key, String subKey, Object... args) {
    try {
      ParameterCheck.notNull(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return keyUtil.buildMissingKey(String.format("%s.%s", key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      String keyBase = keyUtil.resolveKeyPrefix(key);
      return keyUtil.buildMissingKey(String.format("%s.%s", keyBase, subKey));
    }
    String keyBase = keyUtil.resolveKeyPrefix(key);
    return get(String.format("%s.%s", keyBase, subKey), args);
  }

  @Override
  public Locale getLocale() {
    return Locale.ENGLISH;
  }

  @Override
  public Formats getFormats() {
    if (formats == null) {
      formats = new FormatsImpl(getLocale(), config);
    }
    return formats;
  }

  @Override
  public NLS context(String context) {
    try {
      ParameterCheck.notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return this;
    }
    return new ContextNLSImpl(this, config, context);
  }

  @Override
  public NLS context(Class<?> context) {
    try {
      ParameterCheck.notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(ex);
      return this;
    }
    return new ContextNLSImpl(this, config, keyUtil.resolveKeyPrefix(context));
  }

  private class FallsaveNilsConfig extends BaseNilsConfig<FallsaveNilsConfig> {

    @Override
    public Class<? extends AdapterFactory<?>> getFactoryClass() {
      return null;
    }
  }
}
