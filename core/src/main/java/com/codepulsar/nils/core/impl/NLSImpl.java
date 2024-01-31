package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.TRANSLATION_FORMAT_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.api.Formats;
import com.codepulsar.nils.api.NLS;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.core.config.SuppressableErrorTypes;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * Implementation of the {@link NLS} interface.
 *
 * <p>Providing functionalities to
 *
 * <ul>
 *   <li>Check parameters
 *   <li>Replace arguments in translations
 *   <li>Handle unknown translation keys
 * </ul>
 */
public class NLSImpl implements NLS {

  private final Adapter adapter;
  private final Locale locale;
  private final NilsConfig config;
  private final IncludeHandler includeHandler;
  private final ErrorHandler errorHandler;
  private final TranslationFormatter translationFormatter;
  private final NLSKeyUtil keyUtil;
  private Formats formats;
  private Map<String, Optional<String>> cache = new HashMap<>();

  NLSImpl(Adapter adapter, NilsConfig config, Locale locale) {
    this.adapter = ParameterCheck.notNull(adapter, "adapter");
    this.config = ParameterCheck.notNull(config, "config");
    this.locale = ParameterCheck.notNull(locale, "locale");
    this.includeHandler = new IncludeHandler(config, adapter::getTranslation);
    this.errorHandler = new ErrorHandler(config);
    this.translationFormatter = config.getTranslationFormatter();
    this.keyUtil = new NLSKeyUtil(config);
  }

  @Override
  public String get(String key) {
    try {
      ParameterCheck.notNullEmptyOrBlank(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(key);
    }

    Optional<String> translation = resolveTranslation(key);
    if (translation.isEmpty()) {
      errorHandler.handle(
          SuppressableErrorTypes.MISSING_TRANSLATION,
          "Could not find a translation for key '" + key + "' and locale '" + locale + "'.");
      return keyUtil.buildMissingKey(key);
    }
    return translation.get();
  }

  @Override
  public String get(String key, Object... args) {
    String unformattedValue = get(key);
    if (unformattedValue.equals(keyUtil.buildMissingKey(key))) {
      return unformattedValue;
    }

    try {
      return translationFormatter.format(getLocale(), unformattedValue, args);
    } catch (Exception ex) {
      errorHandler.handle(
          TRANSLATION_FORMAT_ERROR,
          new NilsException(
              TRANSLATION_FORMAT_ERROR, "Error in key '" + key + "': " + ex.getMessage(), ex));
      return keyUtil.buildMissingKey(key);
    }
  }

  @Override
  public String get(Class<?> key, String subKey) {
    try {
      ParameterCheck.notNull(key, "key", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(String.format("%s.%s", key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
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
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return keyUtil.buildMissingKey(String.format("%s.%s", key, subKey));
    }
    try {
      ParameterCheck.notNullEmptyOrBlank(subKey, "subKey", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      String keyBase = keyUtil.resolveKeyPrefix(key);
      return keyUtil.buildMissingKey(String.format("%s.%s", keyBase, subKey));
    }
    String keyBase = keyUtil.resolveKeyPrefix(key);
    return get(String.format("%s.%s", keyBase, subKey), args);
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

  @Override
  public Formats getFormats() {
    if (formats == null) {
      formats = new FormatsImpl(locale, config);
    }
    return formats;
  }

  @Override
  public NLS context(String context) {
    try {
      ParameterCheck.notNullEmptyOrBlank(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return this;
    }
    return new ContextNLSImpl(this, config, context);
  }

  @Override
  public NLS context(Class<?> context) {
    try {
      ParameterCheck.notNull(context, "context", nilsException(NLS_PARAMETER_CHECK));
    } catch (NilsException ex) {
      errorHandler.handle(NLS_PARAMETER_CHECK, ex);
      return this;
    }
    return new ContextNLSImpl(this, config, keyUtil.resolveKeyPrefix(context));
  }

  private Optional<String> resolveTranslation(String key) {
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    Optional<String> directRequest = adapter.getTranslation(key);
    if (directRequest.isPresent()) {
      cache.put(key, directRequest);
      return directRequest;
    }
    try {
      Optional<String> includeRequest = includeHandler.findKey(key);
      if (includeRequest.isPresent()) {
        cache.put(key, includeRequest);
        return includeRequest;
      }
    } catch (NilsException ex) {
      errorHandler.handle(SuppressableErrorTypes.INCLUDE_LOOP_DETECTED, ex);
      return Optional.empty();
    }
    return Optional.empty();
  }
}
