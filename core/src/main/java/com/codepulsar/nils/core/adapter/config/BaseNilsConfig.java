package com.codepulsar.nils.core.adapter.config;

import static com.codepulsar.nils.core.util.ParameterCheck.NILS_CONFIG;

import java.text.MessageFormat;
import java.time.format.FormatStyle;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.util.ParameterCheck;

public abstract class BaseNilsConfig<CFG extends BaseNilsConfig<?>> implements NilsConfig<CFG> {
  private String escapePattern = "[{0}]";
  private String includeTag = "_include";
  private boolean suppressErrors = false;
  private ClassPrefixResolver classPrefixResolver = ClassPrefixResolver.SIMPLE_CLASSNAME;
  private TranslationFormatter translationFormatter = TranslationFormatter.MESSAGE_FORMAT;
  private FormatStyle dateFormatStyle = FormatStyle.MEDIUM;

  @Override
  public String getEscapePattern() {
    return escapePattern;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG escapePattern(String escapePattern) {
    ParameterCheck.notNullEmptyOrBlank(escapePattern, "escapePattern", NILS_CONFIG);
    this.escapePattern = checkEscapePattern(escapePattern);
    return (CFG) this;
  }

  @Override
  public String getIncludeTag() {
    return includeTag;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG includeTag(String includeTag) {
    ParameterCheck.notNullEmptyOrBlank(includeTag, "includeTag", NILS_CONFIG);
    this.includeTag = includeTag;
    return (CFG) this;
  }

  @Override
  public boolean isSuppressErrors() {
    return suppressErrors;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG suppressErrors(boolean suppressErrors) {
    this.suppressErrors = suppressErrors;
    return (CFG) this;
  }

  @Override
  public ClassPrefixResolver getClassPrefixResolver() {
    return classPrefixResolver;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG classPrefixResolver(ClassPrefixResolver classPrefixResolver) {
    ParameterCheck.notNull(classPrefixResolver, "classPrefixResolver", NILS_CONFIG);
    this.classPrefixResolver = classPrefixResolver;
    return (CFG) this;
  }

  @Override
  public TranslationFormatter getTranslationFormatter() {
    return translationFormatter;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG translationFormatter(TranslationFormatter translationFormatter) {
    ParameterCheck.notNull(translationFormatter, "translationFormatter", NILS_CONFIG);
    this.translationFormatter = translationFormatter;
    return (CFG) this;
  }

  @Override
  public FormatStyle getDateFormatStyle() {
    return dateFormatStyle;
  }

  @SuppressWarnings("unchecked")
  @Override
  public CFG dateFormatStyle(FormatStyle dateFormatStyle) {
    ParameterCheck.notNull(dateFormatStyle, "dateFormatStyle", NILS_CONFIG);
    this.dateFormatStyle = dateFormatStyle;
    return (CFG) this;
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
}
