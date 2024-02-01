package com.codepulsar.nils.api;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import java.text.MessageFormat;
import java.time.format.FormatStyle;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.util.ParameterCheck;
/** The configuration of the Nils library. */
public class NilsConfig {
  private AdapterConfig adapterConfig;
  private String escapePattern = "[{0}]";
  private String includeTag = "@include";
  private boolean suppressErrors = false;
  private ClassPrefixResolver classPrefixResolver = ClassPrefixResolver.SIMPLE_CLASSNAME;
  private TranslationFormatter translationFormatter = TranslationFormatter.MESSAGE_FORMAT;
  private FormatStyle dateFormatStyle = FormatStyle.MEDIUM;

  private NilsConfig(AdapterConfig adapterConfig) {
    this.adapterConfig = adapterConfig;
  }
  /**
   * Gets the {@link AdapterConfig} object.
   *
   * @return The {@link AdapterConfig} object.
   */
  public AdapterConfig getAdapterConfig() {
    return adapterConfig;
  }

  /**
   * Gets the escape pattern if a translation is missing, but no exception should be thrown.
   *
   * <p>The escape pattern must contain <code>{0}</code>.
   *
   * @return The pattern.
   * @see #getSuppressErrors()
   * @see #escapePattern(String)
   */
  public String getEscapePattern() {
    return escapePattern;
  }

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
  public NilsConfig escapePattern(String escapePattern) {
    ParameterCheck.notNullEmptyOrBlank(escapePattern, "escapePattern", NILS_CONFIG);

    this.escapePattern = checkEscapePattern(escapePattern);
    return this;
  }
  /**
   * Gets the tag for include translations from other key.
   *
   * <p>The default value is <code>@include</code>.
   *
   * @return The tag used including translations.
   * @see #includeTag(String)
   */
  public String getIncludeTag() {
    return includeTag;
  }
  /**
   * Sets the tag for include translations from other key.
   *
   * @param includeTag The tag used including translations.
   * @return This config object.
   * @see #getIncludeTag()
   */
  public NilsConfig includeTag(String includeTag) {
    ParameterCheck.notNullEmptyOrBlank(includeTag, "includeTag", NILS_CONFIG);
    this.includeTag = includeTag;
    return this;
  }
  /**
   * Gets the flag, if errors and exceptions should be suppressed during runtime.
   *
   * <p>The default value is <code>false</code>.
   *
   * @return The value of the flag
   * @see #suppressErrors(boolean)
   */
  public boolean isSuppressErrors() {
    return suppressErrors;
  }
  /**
   * Sets the flag, if errors and exceptions should be suppressed during runtime.
   *
   * <p>The default value is <code>false</code>.
   *
   * @param suppressErrors The value of the flag
   * @see #isSuppressErrors()
   */
  public NilsConfig suppressErrors(boolean suppressErrors) {
    this.suppressErrors = suppressErrors;
    return this;
  }

  /**
   * Gets the {@link ClassPrefixResolver} object.
   *
   * <p>The default is {@link ClassPrefixResolver#SIMPLE_CLASSNAME}.
   *
   * @return The {@link ClassPrefixResolver}.
   * @see #classPrefixResolver(ClassPrefixResolver)
   */
  public ClassPrefixResolver getClassPrefixResolver() {
    return classPrefixResolver;
  }

  /**
   * Sets the {@link ClassPrefixResolver} object.
   *
   * @param classPrefixResolver The {@link ClassPrefixResolver}.
   * @return This config object.
   * @see #getClassPrefixResolver()
   */
  public NilsConfig classPrefixResolver(ClassPrefixResolver classPrefixResolver) {
    ParameterCheck.notNull(classPrefixResolver, "classPrefixResolver", NILS_CONFIG);
    this.classPrefixResolver = classPrefixResolver;
    return this;
  }

  /**
   * Gets the {@link TranslationFormatter} object.
   *
   * <p>The default is {@link TranslationFormatter#MESSAGE_FORMAT}.
   *
   * @return The {@link TranslationFormatter}.
   * @see #translationFormatter(TranslationFormatter)
   */
  public TranslationFormatter getTranslationFormatter() {
    return translationFormatter;
  }

  /**
   * Sets the {@link TranslationFormatter} object.
   *
   * @param translationFormatter The {@link TranslationFormatter}.
   * @return This config object.
   * @see #getTranslationFormatter()
   */
  public NilsConfig translationFormatter(TranslationFormatter translationFormatter) {
    ParameterCheck.notNull(translationFormatter, "translationFormatter", NILS_CONFIG);
    this.translationFormatter = translationFormatter;
    return this;
  }
  /**
   * Gets the <code>FormatStyle</code> used in {@link Formats} objects, defining the style for date,
   * time and date with time value.
   *
   * <p>The default is <code>FormatStyle.MEDIUM</code>.
   *
   * @return The <code>FormatStyle</code>.
   * @see #dateFormatStyle(FormatStyle)
   */
  public FormatStyle getDateFormatStyle() {
    return dateFormatStyle;
  }
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
  public NilsConfig dateFormatStyle(FormatStyle dateFormatStyle) {
    ParameterCheck.notNull(dateFormatStyle, "dateFormatStyle", NILS_CONFIG);
    this.dateFormatStyle = dateFormatStyle;
    return this;
  }

  private String checkEscapePattern(String pattern) {
    if (!pattern.contains("{0}")) {
      throw new NilsConfigException(
          "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".");
    }
    if (pattern.contains("'{0}'")) {
      throw new NilsConfigException(
          "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".");
    }
    try {
      MessageFormat.format(pattern, "TEST");
    } catch (IllegalArgumentException ex) {
      throw new NilsConfigException("Parameter 'escapePattern' is invalid: " + ex.getMessage());
    }
    return pattern;
  }

  /**
   * Create a <strong>NilsConfig</strong> from an {@link AdapterConfig}.
   *
   * @param adapterConfig An {@link AdapterConfig} object.
   * @return The created NilsConfig.
   */
  public static NilsConfig init(AdapterConfig adapterConfig) {
    ParameterCheck.notNull(adapterConfig, "adapterConfig", NILS_CONFIG);
    return new NilsConfig(adapterConfig);
  }
}
