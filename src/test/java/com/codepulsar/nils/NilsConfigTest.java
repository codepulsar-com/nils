package com.codepulsar.nils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.adapter.AdapterConfig;
import com.codepulsar.nils.testadapter.StaticAdapterConfig;

public class NilsConfigTest {

  @Test
  public void initAdapterConfigNull() {
    // Arrange
    AdapterConfig nullAdapterConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> NilsConfig.init(nullAdapterConfig))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'adapterConfig' cannot be null.");
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
    assertThat(underTest.isEscapeIfMissing()).isTrue();
    assertThat(underTest.getEscapePattern()).isEqualTo("[{0}]");
  }

  @Test
  public void throwWhenMissing() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    // Act
    NilsConfig underTest = NilsConfig.init(adapterConfig);

    // Act
    NilsConfig returnValue = underTest.escapeIfMissing(false);
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.isEscapeIfMissing()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("escapePattern_invalidInputSource")
  public void escapePattern_invalidInput(String pattern, String errMsg) {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsConfig underTest = NilsConfig.init(adapterConfig);
    // Act / Assert
    assertThatThrownBy(() -> underTest.escapePattern(pattern))
        .isInstanceOf(IllegalArgumentException.class)
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

  private static Stream<Arguments> escapePattern_invalidInputSource() {
    return Stream.of(
        arguments(null, "Parameter 'escapePattern' cannot be null."),
        arguments("", "Parameter 'escapePattern' cannot be empty or blank."),
        arguments(" ", "Parameter 'escapePattern' cannot be empty or blank."),
        arguments(
            ">><<", "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>0", "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>{0", "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            ">>0}", "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."),
        arguments(
            "{{0}}", "Parameter 'escapePattern' is invalid: can't parse argument number: {0}"),
        arguments(
            "Missing: '{0}'",
            "Parameter 'escapePattern' is invalid: It must contain the string \"{0}\"."));
  }
}
