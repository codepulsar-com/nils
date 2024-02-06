package com.codepulsar.nils.core.adapter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.format.FormatStyle;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class BaseNilsConfigTest {

  @Test
  public void initFromAdapterConfig() {
    // Act
    var underTest = new StaticAdapterConfig();

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.isSuppressErrors()).isEqualTo(false);
    assertThat(underTest.getEscapePattern()).isEqualTo("[{0}]");
    assertThat(underTest.getIncludeTag()).isEqualTo("_include");
    assertThat(underTest.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.SIMPLE_CLASSNAME);
    assertThat(underTest.getTranslationFormatter()).isEqualTo(TranslationFormatter.MESSAGE_FORMAT);
    assertThat(underTest.getDateFormatStyle()).isEqualTo(FormatStyle.MEDIUM);
  }

  @Test
  public void suppressErrors_true() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act
    var result = underTest.suppressErrors(true);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.isSuppressErrors()).isEqualTo(true);
  }

  @Test
  public void suppressErrors_false() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act
    var result = underTest.suppressErrors(false);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.isSuppressErrors()).isEqualTo(false);
  }

  @ParameterizedTest
  @MethodSource("escapePattern_invalidInputSource")
  public void escapePattern_invalidInput(String pattern, String errMsg) {
    // Arrange
    var underTest = new StaticAdapterConfig();
    // Act / Assert
    assertThatThrownBy(() -> underTest.escapePattern(pattern))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void escapePattern() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act
    var result = underTest.escapePattern(">>{0}<<");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.getEscapePattern()).isEqualTo(">>{0}<<");
  }

  @Test
  public void includeTag() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act
    var result = underTest.includeTag("TEST");

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.getIncludeTag()).isEqualTo("TEST");
  }

  @ParameterizedTest
  @MethodSource("includeTag_invalidInputSource")
  public void includeTag_invalidInput(String tag, String errMsg) {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act / Assert
    assertThatThrownBy(() -> underTest.includeTag(tag))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void translationFormatter() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    assertThat(underTest.getTranslationFormatter()).isEqualTo(TranslationFormatter.MESSAGE_FORMAT);

    // Act
    var result = underTest.translationFormatter(TranslationFormatter.STRING_FORMAT);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.getTranslationFormatter()).isEqualTo(TranslationFormatter.STRING_FORMAT);
  }

  @Test
  public void translationFormatter_null() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act / Assert
    assertThatThrownBy(() -> underTest.translationFormatter(null))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'translationFormatter' cannot be null.");
  }

  @Test
  public void dateFormatStyle() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    assertThat(underTest.getDateFormatStyle()).isEqualTo(FormatStyle.MEDIUM);

    // Act
    var result = underTest.dateFormatStyle(FormatStyle.LONG);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.getDateFormatStyle()).isEqualTo(FormatStyle.LONG);
  }

  @Test
  public void dateFormatStyle_null() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act / Assert
    assertThatThrownBy(() -> underTest.dateFormatStyle(null))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'dateFormatStyle' cannot be null.");
  }

  @Test
  public void classPrefixResolver() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    assertThat(underTest.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.SIMPLE_CLASSNAME);

    // Act
    var result = underTest.classPrefixResolver(ClassPrefixResolver.FQN_CLASSNAME);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(underTest);
    assertThat(underTest.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.FQN_CLASSNAME);
  }

  @Test
  public void classPrefixResolver_null() {
    // Arrange
    var underTest = new StaticAdapterConfig();

    // Act / Assert
    assertThatThrownBy(() -> underTest.classPrefixResolver(null))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'classPrefixResolver' cannot be null.");
  }

  private static Stream<Arguments> escapePattern_invalidInputSource() {
    return Stream.of(
        arguments(null, "NILS-004: Parameter 'escapePattern' cannot be null."),
        arguments("", "NILS-004: Parameter 'escapePattern' cannot be empty or blank."),
        arguments(" ", "NILS-004: Parameter 'escapePattern' cannot be empty or blank."),
        arguments(
            ">><<",
            "NILS-004: Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>0",
            "NILS-004: Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>{0",
            "NILS-004: Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>0}",
            "NILS-004: Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            "{{0}}",
            "NILS-004: Parameter 'escapePattern' is invalid: can't parse argument number: {0}"),
        arguments(
            "Missing: '{0}'",
            "NILS-004: Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."));
  }

  private static Stream<Arguments> includeTag_invalidInputSource() {
    return Stream.of(
        arguments(null, "NILS-004: Parameter 'includeTag' cannot be null."),
        arguments("", "NILS-004: Parameter 'includeTag' cannot be empty or blank."),
        arguments(" ", "NILS-004: Parameter 'includeTag' cannot be empty or blank."));
  }
}
