package com.codepulsar.nils.core;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.ALL;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.INCLUDE_LOOP_DETECTED;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.MISSING_TRANSLATION;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.error.ErrorType;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class NilsConfigTest {

  @Test
  public void initAdapterConfigNull() {
    // Arrange
    AdapterConfig nullAdapterConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> NilsConfig.init(nullAdapterConfig))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'adapterConfig' cannot be null.");
  }

  @Test
  public void initFromAdapterConfig() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    // Act
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getAdapterConfig()).isEqualTo(adapterConfig);
    assertThat(underTest.getSuppressErrors()).containsExactly(ALL);
    assertThat(underTest.getEscapePattern()).isEqualTo("[{0}]");
    assertThat(underTest.getIncludeTag()).isEqualTo("@include");
  }

  @Test
  public void suppressErrors_nullParam() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act / Assert
    assertThatThrownBy(() -> underTest.suppressErrors(null))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'type' cannot be null.");
  }

  @Test
  public void suppressErrors_noneAndOthers() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act / Assert
    assertThatThrownBy(
            () -> underTest.suppressErrors(NONE, INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(
            "NILS-004: Parameter 'type' is invalid: ErrorType.NONE cannot combined with other ErrorTypes.");
  }

  @Test
  public void suppressErrors_noneAndOthers2() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act / Assert
    assertThatThrownBy(
            () -> underTest.suppressErrors(INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION, NONE))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(
            "NILS-004: Parameter 'type' is invalid: ErrorType.NONE cannot combined with other ErrorTypes.");
  }

  @Test
  public void suppressErrors_allAndOthers() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act / Assert
    assertThatThrownBy(
            () -> underTest.suppressErrors(ALL, INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(
            "NILS-004: Parameter 'type' is invalid: ErrorType.ALL cannot combined with other ErrorTypes.");
  }

  @Test
  public void suppressErrors_allAndOthers2() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act / Assert
    assertThatThrownBy(
            () -> underTest.suppressErrors(INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION, ALL))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(
            "NILS-004: Parameter 'type' is invalid: ErrorType.ALL cannot combined with other ErrorTypes.");
  }

  @Test
  public void suppressErrors_noneAlone() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue = underTest.suppressErrors(NONE);

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getSuppressErrors()).containsExactly(NONE);
  }

  @Test
  public void suppressErrors_allAlone() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue = underTest.suppressErrors(ALL);

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getSuppressErrors()).containsExactly(ALL);
  }

  @Test
  public void suppressErrors_some() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue =
        underTest.suppressErrors(INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION, NLS_PARAMETER_CHECK);

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getSuppressErrors())
        .containsOnly(INCLUDE_LOOP_DETECTED, MISSING_TRANSLATION, NLS_PARAMETER_CHECK);
  }

  @Test
  public void suppressErrors_invalidErrorType() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);
    ErrorType invalid = new ErrorType("X-999", false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.suppressErrors(INCLUDE_LOOP_DETECTED, invalid))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(
            "NILS-004: Parameter 'type' is invalid: ErrorType 'X-999' is not suppressable.");
  }

  @ParameterizedTest
  @MethodSource("escapePattern_invalidInputSource")
  public void escapePattern_invalidInput(String pattern, String errMsg) {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);
    // Act / Assert
    assertThatThrownBy(() -> underTest.escapePattern(pattern))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void escapePattern() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue = underTest.escapePattern(">>{0}<<");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getEscapePattern()).isEqualTo(">>{0}<<");
  }

  @Test
  public void includeTag() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue = underTest.includeTag("TEST");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getIncludeTag()).isEqualTo("TEST");
  }

  @ParameterizedTest
  @MethodSource("includeTag_invalidInputSource")
  public void includeTag_invalidInput(String tag, String errMsg) {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);
    // Act / Assert
    assertThatThrownBy(() -> underTest.includeTag(tag))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage(errMsg);
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
