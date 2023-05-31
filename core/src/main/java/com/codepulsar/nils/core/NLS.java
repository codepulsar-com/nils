package com.codepulsar.nils.core;

import java.util.Locale;

/**
 * The <code>NLS</code> provides access to "national language support" related function for a
 * specific <tt>Locale</tt>.
 */
public interface NLS {
  /**
   * Gets the translation for the requested key.
   *
   * @param key The key for the translation
   * @return The translation
   */
  String get(String key);
  /**
   * Gets the translation for the requested key with arguments that should be replaced in the
   * translation.
   *
   * @param key The key for the translation
   * @param args Arguments for the translation
   * @return The translation
   */
  String get(String key, Object... args);

  /**
   * Gets the <tt>Locale</tt> the NLS is responsible for.
   *
   * @return A <tt>Locale</tt> object.
   */
  Locale getLocale();
}
