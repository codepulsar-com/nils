package com.codepulsar.nils.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.text.DecimalFormatSymbols;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class FormatsImplTest {
  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.core.impl.FormatsImplTestDataProvider";

  @Test
  public void constructor_nullLocale() {
    // Arrange
    Locale locale = null;
    var config = createTestConfig(null);

    // Act / Assert
    assertThatThrownBy(() -> new FormatsImpl(locale, config))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void constructor_nullConfig() {
    // Arrange
    var locale = Locale.US;
    NilsConfig config = null;

    // Act / Assert
    assertThatThrownBy(() -> new FormatsImpl(locale, config))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forDate")
  public void forDate(Temporal checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forDate();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forTime")
  public void forTime(Temporal checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forTime();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forDateTime")
  public void forDateTime(Temporal checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forDateTime();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forUtilDate")
  public void forUtilDate(Date checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forUtilDate();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forUtilTime")
  public void forUtilTime(Date checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forUtilTime();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forUtilDateTime")
  public void forUtilDateTime(Date checkValue, String expected, Locale locale, FormatStyle style) {
    // Arrange
    var config = createTestConfig(style);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forUtilDateTime();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forNumber")
  public void forNumber(Number checkValue, String expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forNumber();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forCurrency")
  public void forCurrency(Number checkValue, String expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forCurrency();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forInteger")
  public void forInteger(Number checkValue, String expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forInteger();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forPercent")
  public void forPercent(Number checkValue, String expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forPercent();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_forDecimal")
  public void forDecimal(Number checkValue, String expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.forDecimal();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.format(checkValue)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_decimalFormatSymbols")
  public void decimalFormatSymbols(DecimalFormatSymbols expected, Locale locale) {
    // Arrange
    var config = createTestConfig(null);
    var underTest = new FormatsImpl(locale, config);

    // Act
    var result = underTest.decimalFormatSymbols();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(expected);
  }

  private NilsConfig createTestConfig(FormatStyle style) {
    var config = NilsConfig.init(new StaticAdapterConfig());
    if (style != null) {
      config.dateFormatStyle(style);
    }
    return config;
  }
}
