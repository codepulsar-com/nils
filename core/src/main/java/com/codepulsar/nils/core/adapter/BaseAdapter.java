package com.codepulsar.nils.core.adapter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.NilsException;
import com.codepulsar.nils.core.util.ParameterCheck;
/**
 * Base class for <tt>Adapter</tt> implementations, providing functionalities to
 *
 * <ul>
 *   <li>Check parameters
 *   <li>Replace arguments in translations
 *   <li>Handle unknown translation keys
 * </ul>
 *
 * <p>An implementation can extend this base class.
 */
public abstract class BaseAdapter implements Adapter {

  private final Locale locale;
  private final NilsConfig config;

  protected BaseAdapter(NilsConfig config, Locale locale) {
    this.config = ParameterCheck.notNull(config, "config");
    this.locale = ParameterCheck.notNull(locale, "locale");
  }

  /**
   * Resolve the translation value from the adapter implementation.
   *
   * @param key The translation key
   * @return The value or an empty <tt>Optional</tt>
   */
  protected abstract Optional<String> resolveTranslation(String key);

  @Override
  public String get(String key) {
    ParameterCheck.notNullEmptyOrBlank(key, "key");
    Optional<String> translation = resolveTranslation(key);
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

  /**
   * Gets the <tt>NilsConfig</tt>.
   *
   * @return A <tt>NilsConfig</tt> object.
   */
  protected NilsConfig getConfig() {
    return config;
  }
}
