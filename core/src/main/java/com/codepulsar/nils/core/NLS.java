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
   * Gets the translation for the requested key.
   *
   * <p>The <tt>Class</tt> is used a base key. The subKey as a further key. Both, the key and
   * subKey, will be concatenated like 'MyClass.attrbute'.
   *
   * @param key A <tt>Class</tt> as base key for the translation
   * @param subKey The sub key, like an attribute of the class
   * @return The translation
   */
  String get(Class<?> key, String subKey);

  /**
   * Gets the translation for the requested key with arguments that should be replaced in the
   * translation.
   *
   * <p>The <tt>Class</tt> is used a base key. The subKey as a further key. Both, the key and
   * subKey, will be concatenated like 'MyClass.attrbute'.
   *
   * @param key A <tt>Class</tt> as base key for the translation
   * @param subKey The sub key, like an attribute of the class
   * @return The translation
   */
  String get(Class<?> key, String subKey, Object... args);

  /**
   * Gets the <tt>Locale</tt> the NLS is responsible for.
   *
   * @return A <tt>Locale</tt> object.
   */
  Locale getLocale();
}
