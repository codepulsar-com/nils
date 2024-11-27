package com.codepulsar.nils.core.adapter.config;

import static com.codepulsar.nils.core.error.ErrorTypes.CONFIG_ERROR;
import static com.codepulsar.nils.core.util.ParameterCheck.nilsException;

import java.text.MessageFormat;
import java.time.format.FormatStyle;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.util.ParameterCheck;

/**
 * The {@link BaseNilsConfig} is a base implementation of {@link NilsConfig}.
 *
 * <p>Adapter, uses the {@link NilsConfig} should use this class as base class for their
 * configuration, instead of implementing the interfaces directly.
 *
 * @param <CFG> The class extending {@link BaseNilsConfig}.
 */
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
    ParameterCheck.notNullEmptyOrBlank(escapePattern, "escapePattern", nilsException(CONFIG_ERROR));
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
    ParameterCheck.notNullEmptyOrBlank(includeTag, "includeTag", nilsException(CONFIG_ERROR));
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
    ParameterCheck.notNull(classPrefixResolver, "classPrefixResolver", nilsException(CONFIG_ERROR));
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
    ParameterCheck.notNull(
        translationFormatter, "translationFormatter", nilsException(CONFIG_ERROR));
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
    ParameterCheck.notNull(dateFormatStyle, "dateFormatStyle", nilsException(CONFIG_ERROR));
    this.dateFormatStyle = dateFormatStyle;
    return (CFG) this;
  }

  private String checkEscapePattern(String pattern) {
    if (!pattern.contains("{0}")) {
      throw CONFIG_ERROR
          .asException()
          .message("Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".")
          .go();
    }
    if (pattern.contains("'{0}'")) {
      throw CONFIG_ERROR
          .asException()
          .message("Parameter 'escapePattern' is invalid: It must contain the string \"{0}\".")
          .go();
    }
    try {
      MessageFormat.format(pattern, "TEST");
    } catch (IllegalArgumentException ex) {
      throw CONFIG_ERROR
          .asException()
          .message("Parameter 'escapePattern' is invalid: %s.")
          .args(ex.getMessage())
          .go();
    }
    return pattern;
  }
}
