package com.codepulsar.nils.api;

import java.util.Locale;

import com.codepulsar.nils.core.handler.ClassPrefixResolver;

/**
 * The <strong>NLS</strong> provides access to "national language support" related function for a
 * specific <code>Locale</code>.
 */
public interface NLS {
  /**
   * Get the translation for the requested key.
   *
   * @param key The key for the translation
   * @return The translation
   */
  String get(String key);
  /**
   * Get the translation for the requested key with arguments that should be replaced in the
   * translation.
   *
   * @param key The key for the translation
   * @param args Arguments for the translation
   * @return The translation
   */
  String get(String key, Object... args);
  /**
   * Get the translation for the requested key.
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
   * Get the translation for the requested key with arguments that should be replaced in the
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
   * Get the <code>Locale</code> the NLS is responsible for.
   *
   * @return A <code>Locale</code> object.
   */
  Locale getLocale();

  /**
   * Get the {@link Formats} for the <code>Locale</code> of <strong>NLS</strong>.
   *
   * @return A {@link Formats} object.
   */
  Formats getFormats();

  /**
   * Get a {@link NLS} object for a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param context The context key.
   * @return A {@link NLS} object.
   */
  NLS context(String context);

  /**
   * Get a {@link NLS} object for a specific context based on a <code>Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param context The <code>Class</code> for the context key.
   * @return A {@link NLS} object.
   * @see NilsConfig#getClassPrefixResolver()
   */
  NLS context(Class<?> context);
}
