package com.codepulsar.nils.api;

import java.time.format.FormatStyle;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
/** The configuration of the Nils library. */
public interface NilsConfig<CFG extends NilsConfig<?>> {

  /**
   * Gets the escape pattern if a translation is missing, but no exception should be thrown.
   *
   * <p>The escape pattern must contain <code>{0}</code>.
   *
   * @return The pattern.
   * @see #getSuppressErrors()
   * @see #escapePattern(String)
   */
  String getEscapePattern();

  /**
   * Sets the escape pattern if a translation is missing, but no exception should be thrown.
   *
   * <p>The escape pattern must contain <code>{0}</code>.
   *
   * @param escapePattern the pattern
   * @return This config object.
   * @see #getSuppressErrors()
   * @see #getEscapePattern()
   */
  CFG escapePattern(String escapePattern);
  /**
   * Gets the tag for include translations from other key.
   *
   * <p>The default value is <code>_include</code>.
   *
   * @return The tag used including translations.
   * @see #includeTag(String)
   */
  String getIncludeTag();
  /**
   * Sets the tag for include translations from other key.
   *
   * @param includeTag The tag used including translations.
   * @return This config object.
   * @see #getIncludeTag()
   */
  CFG includeTag(String includeTag);
  /**
   * Gets the flag, if errors and exceptions should be suppressed during runtime.
   *
   * <p>The default value is <code>false</code>.
   *
   * @return The value of the flag
   * @see #suppressErrors(boolean)
   */
  boolean isSuppressErrors();
  /**
   * Sets the flag, if errors and exceptions should be suppressed during runtime.
   *
   * <p>The default value is <code>false</code>.
   *
   * @param suppressErrors The value of the flag
   * @see #isSuppressErrors()
   */
  CFG suppressErrors(boolean suppressErrors);

  /**
   * Gets the {@link ClassPrefixResolver} object.
   *
   * <p>The default is {@link ClassPrefixResolver#SIMPLE_CLASSNAME}.
   *
   * @return The {@link ClassPrefixResolver}.
   * @see #classPrefixResolver(ClassPrefixResolver)
   */
  ClassPrefixResolver getClassPrefixResolver();
  /**
   * Sets the {@link ClassPrefixResolver} object.
   *
   * @param classPrefixResolver The {@link ClassPrefixResolver}.
   * @return This config object.
   * @see #getClassPrefixResolver()
   */
  CFG classPrefixResolver(ClassPrefixResolver classPrefixResolver);
  /**
   * Gets the {@link TranslationFormatter} object.
   *
   * <p>The default is {@link TranslationFormatter#MESSAGE_FORMAT}.
   *
   * @return The {@link TranslationFormatter}.
   * @see #translationFormatter(TranslationFormatter)
   */
  TranslationFormatter getTranslationFormatter();

  /**
   * Sets the {@link TranslationFormatter} object.
   *
   * @param translationFormatter The {@link TranslationFormatter}.
   * @return This config object.
   * @see #getTranslationFormatter()
   */
  CFG translationFormatter(TranslationFormatter translationFormatter);
  /**
   * Gets the <code>FormatStyle</code> used in {@link Formats} objects, defining the style for date,
   * time and date with time value.
   *
   * <p>The default is <code>FormatStyle.MEDIUM</code>.
   *
   * @return The <code>FormatStyle</code>.
   * @see #dateFormatStyle(FormatStyle)
   */
  FormatStyle getDateFormatStyle();
  /**
   * Sets the <code>FormatStyle</code> used in {@link Formats} objects, defining the style for date,
   * time and date with time value.
   *
   * <p>The default is <code>FormatStyle.MEDIUM</code>.
   *
   * @param dateFormatStyle The <code>FormatStyle</code>.
   * @see #getDateFormatStyle()
   * @see com.codepulsar.nils.api.Formats
   */
  CFG dateFormatStyle(FormatStyle dateFormatStyle);

  /**
   * Class of the {@link AdapterFactory}.
   *
   * <p>the class must have an parameter less constructor.
   *
   * @return The Class of the AdapterFactory.
   */
  Class<? extends AdapterFactory<?>> getFactoryClass();
}
