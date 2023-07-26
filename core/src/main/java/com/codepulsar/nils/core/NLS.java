package com.codepulsar.nils.core;

import java.util.Locale;

/**
 * The <strong>NLS</strong> provides access to "national language support" related function for a
 * specific <code>Locale</code>.
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
   * <p>The <code>Class</code> is used a base key. The subKey as a further key. Both, the key and
   * subKey, will be concatenated like 'MyClass.attrbute'.
   *
   * @param key A <code>Class</code> as base key for the translation
   * @param subKey The sub key, like an attribute of the class
   * @return The translation
   */
  String get(Class<?> key, String subKey);

  /**
   * Gets the translation for the requested key with arguments that should be replaced in the
   * translation.
   *
   * <p>The <code>Class</code> is used a base key. The subKey as a further key. Both, the key and
   * subKey, will be concatenated like 'MyClass.attrbute'.
   *
   * @param key A <code>Class</code> as base key for the translation
   * @param subKey The sub key, like an attribute of the class
   * @param args Arguments for the translation
   * @return The translation
   */
  String get(Class<?> key, String subKey, Object... args);

  /**
   * Gets the <code>Locale</code> the NLS is responsible for.
   *
   * @return A <code>Locale</code> object.
   */
  Locale getLocale();

  /**
   * Gets the {@link Formats} for the <code>Locale</code> of <strong>NLS</strong>.
   *
   * @return A {@link Formats} object.
   */
  Formats getFormats();
}
