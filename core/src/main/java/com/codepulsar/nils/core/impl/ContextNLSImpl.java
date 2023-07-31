package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;

import java.util.Locale;

import com.codepulsar.nils.core.Formats;
import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;
/** Implementation of {@link NLS} for context base {@link NLS}. */
public class ContextNLSImpl implements NLS {

  private final NLS parent;
  private final String context;
  private final ErrorHandler errorHandler;
  private final NLSKeyUtil keyUtil;

  ContextNLSImpl(NLS parent, NilsConfig config, String context) {
    this.parent = ParameterCheck.notNull(parent, "parent");
    ParameterCheck.notNull(config, "config");
    this.context = ParameterCheck.notNullEmptyOrBlank(context, "context");
    this.errorHandler = new ErrorHandler(config);
    this.keyUtil = new NLSKeyUtil(config);
  }

  @Override
  public String get(String key) {
    try {
      ParameterCheck.notNullEmptyOrBlank(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key));
    }

    return parent.get(buildFullRequestKey(key));
  }

  @Override
  public String get(String key, Object... args) {
    try {
      ParameterCheck.notNullEmptyOrBlank(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key));
    }

    return parent.get(buildFullRequestKey(key), args);
  }

  @Override
  public String get(Class<?> key, String subKey) {
    try {
      ParameterCheck.notNull(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key, subKey));
    }
    return parent.get(buildFullRequestKey(key, subKey));
  }

  @Override
  public String get(Class<?> key, String subKey, Object... args) {
    try {
      ParameterCheck.notNull(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(buildFullRequestKey(key, subKey));
    }
    return parent.get(buildFullRequestKey(key, subKey), args);
  }

  @Override
  public Locale getLocale() {
    return parent.getLocale();
  }

  @Override
  public Formats getFormats() {
    return parent.getFormats();
  }

  @Override
  public NLS context(String context) {
    try {
      ParameterCheck.notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return this;
    }
    return parent.context(buildFullRequestKey(context));
  }

  @Override
  public NLS context(Class<?> context) {
    try {
      ParameterCheck.notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return this;
    }
    return parent.context(buildFullRequestKey(keyUtil.resolveKeyPrefix(context)));
  }

  private String buildFullRequestKey(String key) {
    return String.format("%s.%s", context, key);
  }

  private String buildFullRequestKey(String key, String subKey) {
    return String.format("%s.%s.%s", context, key, subKey);
  }

  private String buildFullRequestKey(Class<?> key, String subKey) {
    String keyBase = key != null ? keyUtil.resolveKeyPrefix(key) : "null";
    return buildFullRequestKey(keyBase, subKey);
  }
}
