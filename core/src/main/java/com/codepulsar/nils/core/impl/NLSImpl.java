package com.codepulsar.nils.core.impl;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

import com.codepulsar.nils.core.NLS;
import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.NilsException;
import com.codepulsar.nils.core.adapter.Adapter;
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

  public NLSImpl(Adapter adapter, NilsConfig config, Locale locale) {
    this.adapter = ParameterCheck.notNull(adapter, "adapter");
    this.config = ParameterCheck.notNull(config, "config");
    this.locale = ParameterCheck.notNull(locale, "locale");
  }

  @Override
  public String get(String key) {
    ParameterCheck.notNullEmptyOrBlank(key, "key");
    Optional<String> translation = adapter.getTranslation(key);
    if (translation.isEmpty()) {
      if (!config.isEscapeIfMissing()) {
        throw new NilsException("Could not find translation for key '" + key + "'.");
      }
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
}
