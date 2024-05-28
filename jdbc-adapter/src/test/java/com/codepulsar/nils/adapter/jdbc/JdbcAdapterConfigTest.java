package com.codepulsar.nils.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.error.NilsException;

public class JdbcAdapterConfigTest {

  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.adapter.jdbc.JdbcAdapterConfigTestDataProvider";

  @Test
  public void init() {
    // Act
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getFactoryClass()).isEqualTo(JdbcAdapterFactory.class);
    assertThat(underTest.isFallbackActive()).isTrue();
    assertThat(underTest.getUrl()).isEqualTo("URL");
    assertThat(underTest.getUsername()).isEqualTo("user");
    assertThat(underTest.getPassword()).isEqualTo("password");
    assertThat(underTest.getRootLocale()).isEqualTo(new Locale(""));
    assertThat(underTest.getDriverClass()).isEqualTo(null);
    assertThat(underTest.getSchema()).isEqualTo(null);
    assertThat(underTest.getTableName()).isEqualTo("NILS_TRANSLATION");
    assertThat(underTest.getLocaleField()).isEqualTo("NLS_LOCALE");
    assertThat(underTest.getKeyField()).isEqualTo("NLS_KEY");
    assertThat(underTest.getValueField()).isEqualTo("NLS_VALUE");
    assertThat(underTest.getCacheTimeout()).isEqualTo(-1L);
  }

  @Test
  public void fallback_disabled() {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.fallbackActive(false);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.isFallbackActive()).isFalse();
  }

  @Test
  public void cacheTimeout() {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.cacheTimeout(1000L);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getCacheTimeout()).isEqualTo(1000L);
  }

  @Test
  public void rootLocale() {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.rootLocale(Locale.FRANCE);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getRootLocale()).isEqualTo(Locale.FRANCE);
  }

  @Test
  public void rootLocale_invalid() {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");
    Locale locale = null;
    // Act / Assert
    assertThatThrownBy(() -> underTest.rootLocale(locale))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'rootLocale' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void url_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.url(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getUrl()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void url_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.url(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "url"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void username_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.username(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getUsername()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void username_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.username(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "username"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void password_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.password(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getPassword()).isEqualTo(expected);
  }

  @Test
  public void password_invalid() {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");
    String str = null;
    var errMsg = "NILS-004: Parameter 'password' cannot be null.";
    // Act / Assert
    assertThatThrownBy(() -> underTest.password(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void driverClass_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.driverClass(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getDriverClass()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void driverClass_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.driverClass(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "driverClass"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void schema_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.schema(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getSchema()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void schema_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.schema(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "schema"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void tableName_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.tableName(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getTableName()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void tableName_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.tableName(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "tableName"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void localeField_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.localeField(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getLocaleField()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void localeField_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.localeField(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "localeField"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void keyField_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.keyField(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getKeyField()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void keyField_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act / Assert
    assertThatThrownBy(() -> underTest.keyField(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "keyField"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_valid")
  public void valueField_valid(String str, String expected) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");

    // Act
    var next = underTest.valueField(str);

    // Assert
    assertThat(next).isNotNull();
    assertThat(next.getValueField()).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#string_invalid")
  public void valueField_invalid(String str, String errMsg) {
    // Arrange
    var underTest = JdbcAdapterConfig.init("URL", "user", "password");
    // Act / Assert
    assertThatThrownBy(() -> underTest.valueField(str))
        .isInstanceOf(NilsException.class)
        .hasMessage(String.format(errMsg, "valueField"));
  }
}
