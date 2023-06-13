package com.codepulsar.nils.core.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.config.SuppressableErrorTypes;
import com.codepulsar.nils.core.error.NilsException;
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

  private Map<String, Optional<String>> cache = new HashMap<>();

  public NLSImpl(Adapter adapter, NilsConfig config, Locale locale) {
    this.adapter = ParameterCheck.notNull(adapter, "adapter");
    this.config = ParameterCheck.notNull(config, "config");
    this.locale = ParameterCheck.notNull(locale, "locale");
    this.includeHandler = new IncludeHandler(config, adapter::getTranslation);
    this.errorHandler = new ErrorHandler(config);
  }

  @Override
  public String get(String key) {
    ParameterCheck.notNullEmptyOrBlank(key, "key");
    Optional<String> translation = resolveTranslation(key);
    if (translation.isEmpty()) {
      errorHandler.handle(
          SuppressableErrorTypes.MISSING_TRANSLATION,
          "Could not find translation for key '" + key + "'.");
      return buildMissingKey(key);
    }
    return translation.get();
  }

  @Override
  public String get(String key, Object... args) {
    String unformattedValue = get(key);
    if (unformattedValue.equals(buildMissingKey(key))) {
      return unformattedValue;
    }
    return MessageFormat.format(unformattedValue, args);
  }

  @Override
  public String get(Class<?> key, String subKey) {
    ParameterCheck.notNull(key, "key");
    ParameterCheck.notNullEmptyOrBlank(subKey, "subKey");
    String keyBase = key.getSimpleName();
    return get(String.format("%s.%s", keyBase, subKey));
  }

  @Override
  public String get(Class<?> key, String subKey, Object... args) {
    ParameterCheck.notNull(key, "key");
    ParameterCheck.notNullEmptyOrBlank(subKey, "subKey");
    String keyBase = key.getSimpleName();
    return get(String.format("%s.%s", keyBase, subKey), args);
  }

  protected String buildMissingKey(String key) {
    return MessageFormat.format(config.getEscapePattern(), key);
  }

  @Override
  public Locale getLocale() {
    return locale;
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
