package com.codepulsar.nils.adapter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

import com.codepulsar.nils.NilsConfig;
import com.codepulsar.nils.NilsException;
import com.codepulsar.nils.util.ParameterCheck;
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
