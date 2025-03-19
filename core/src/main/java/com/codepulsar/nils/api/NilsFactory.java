package com.codepulsar.nils.api;

import java.util.Locale;

import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.impl.NilsFactoryImpl;

/** Factory for getting access to the provided NLS. A requested NLS object is cached. */
public interface NilsFactory {
  /**
   * Get the NLS object for the default <code>Locale</code>.
   *
   * @return The NLS object for the default <code>Locale</code>.
   */
  NLS nls();

  /**
   * Get the NLS object for a language tag.
   *
   * @param lang The language tag.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   * @deprecated Use {@link #nls(Locale)} instead. This method will be removed in 5.x or later.
   */
  @Deprecated
  NLS nls(String lang);

  /**
   * Get the NLS object for a specific <code>Locale</code>.
   *
   * @param locale The <code>Locale</code>.
   * @return The NLS object for the <code>Locale</code>.
   */
  NLS nls(Locale locale);

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param context The context key.
   * @return The NLS object for the default <code>Locale</code>.
   * @deprecated use {@link #nlsForContext(String)} instead. This method will be removed in 5.x or
   *     later.
   */
  @Deprecated
  NLS nlsWithContext(String context);

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param context The context key.
   * @return The NLS object for the default <code>Locale</code>.
   */
  NLS nlsForContext(String context);

  /**
   * Get the NLS object for a language tag and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param lang The language tag.
   * @param context The context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   * @deprecated Use {@link #nlsForContext(Locale, String)} instead. This method will be removed in
   *     5.x or later.
   */
  @Deprecated
  NLS nlsWithContext(String lang, String context);

  /**
   * Get the NLS object for a specific <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param locale The <code>Locale</code>.
   * @param context The context key.
   * @return The NLS object for the <code>Locale</code>.
   * @deprecated use {@link #nlsForContext(Locale, String)} instead. This method will be removed in
   *     5.x or later.
   */
  @Deprecated
  NLS nlsWithContext(Locale locale, String context);

  /**
   * Get the NLS object for a specific <code>Locale</code> and a specific context.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param locale The <code>Locale</code>.
   * @param context The context key.
   * @return The NLS object for the <code>Locale</code>.
   */
  NLS nlsForContext(Locale locale, String context);

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the default <code>Locale</code>. * @deprecated use {@link
   *     #nlsForContext(Class)} instead. This method will be removed in 5.x or later.
   */
  @Deprecated
  NLS nlsWithContext(Class<?> context);

  /**
   * Get the NLS object for the default <code>Locale</code> and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the default <code>Locale</code>.
   */
  NLS nlsForContext(Class<?> context);

  /**
   * Get the NLS object for a language tag and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * @param lang The language tag.
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   * @deprecated Use {@link #nlsForContext(Locale, Class)} instead. This method will be removed in
   *     5.x or later.
   */
  @Deprecated
  NLS nlsWithContext(String lang, Class<?> context);

  /**
   * Get the NLS object for a language tag and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param locale The <code>Locale</code>.
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   * @deprecated use {@link #nlsForContext(Locale, String)} instead. This method will be removed in
   *     5.x or later.
   */
  @Deprecated
  NLS nlsWithContext(Locale locale, Class<?> context);

  /**
   * Get the NLS object for a language tag and a specific context based on a <code>
   * Class</code>.
   *
   * <p>Only the keys beneath the context will be examined.
   *
   * <p>The determination of the Class as key will be done with the configured {@link
   * ClassPrefixResolver}.
   *
   * @param locale The <code>Locale</code>.
   * @param context The <code>Class</code> for the context key.
   * @return The NLS object for the language tag.
   * @see Locale#forLanguageTag(String)
   */
  NLS nlsForContext(Locale locale, Class<?> context);

  /**
   * Reset the {@link NilsFactory} and all its cached elements.
   *
   * <p><em>Note: The implementation is synchronized.</em>
   */
  void reset();

  /**
   * Initialize the factory using a {@link NilsConfig}.
   *
   * @param config A {@link NilsConfig} object.
   * @return The create factory.
   */
  static NilsFactory init(NilsConfig<?> config) {
    return new NilsFactoryImpl(config);
  }
}
