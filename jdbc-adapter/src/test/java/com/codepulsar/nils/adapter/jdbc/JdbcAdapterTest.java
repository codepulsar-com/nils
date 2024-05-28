package com.codepulsar.nils.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.AdapterContext;

public class JdbcAdapterTest {

  private Locale current;
  private AdapterContext<JdbcAdapter> context;

  private String url;
  private String username = "sa";
  private String password = "";
  private DbTestUtil dbUtil = new DbTestUtil();

  @BeforeEach
  public void defineDefault() throws SQLException {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);

    url = dbUtil.initDb("adapter_test.sql");
    context = new AdapterContext<JdbcAdapter>().factory(new JdbcAdapterFactory());
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void defaultConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);

    // Act
    var underTest = new JdbcAdapter(context);

    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  public void nullContext() {
    // Act / Assert
    assertThatThrownBy(() -> new JdbcAdapter(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context' cannot be null.");
  }

  @Test
  public void nullLocale() {
    // Arrange
    Locale locale = null;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new JdbcAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.locale' cannot be null.");
  }

  @Test
  public void nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new JdbcAdapter(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'context.config' cannot be null.");
  }

  @Test
  public void invalidConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig<?> config = ResourceBundleAdapterConfig.init(this);
    context.locale(locale).config(config);

    // Act / Assert
    assertThatThrownBy(() -> new JdbcAdapter(context))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("NILS-004")
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("does not implement");
  }

  @Test
  public void fallback() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void fallback_alternativeRootLocale() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = JdbcAdapterConfig.init(url, username, password).rootLocale(Locale.GERMAN);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Ich bin übersetzt!");
  }

  @Test
  public void translateKeyFound_nestedKeys() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("deep");
    // Assert
    assertThat(value).isEmpty();

    // Act
    value = underTest.getTranslation("deep.deepkey3");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Key 3");

    // Act
    value = underTest.getTranslation("deep.deeper.deeperkey4");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Key 4");

    // Act
    value = underTest.getTranslation("deep.non_existing");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyFound1() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void translateKeyFound2() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Ich bin übersetzt!");
  }

  @Test
  public void translateKeyFallback() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm a fallback!");
  }

  @Test
  public void translateKeyFallbackNotFound() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback_notfound");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyFallback_fallbackInactive() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = JdbcAdapterConfig.init(url, username, password).fallbackActive(false);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.fallback");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyNotFound() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void url_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init("abc:" + url, username, "");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(
            "NILS-252: An SQLException occurred: No suitable driver found for abc:jdbc:h2");
  }

  @Test
  public void username_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, "username", "");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Wrong user name or password");
  }

  @Test
  public void password_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).password("superSecure");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Wrong user name or password");
  }

  @Test
  public void schema_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).schema("ABC");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Schema \"ABC\" not found");
  }

  @Test
  public void tableName_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).tableName("ABC");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Table \"ABC\" not found");
  }

  @Test
  public void localeField_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).localeField("ABC");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Column \"ABC\" not found");
  }

  @Test
  public void keyField_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).keyField("ABC");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Column \"ABC\" not found");
  }

  @Test
  public void valueField_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).valueField("ABC");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("NILS-252")
        .hasMessageContaining("Column \"ABC\" not found");
  }

  @Test
  public void driverNameDefined() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).driverClass("org.h2.Driver");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void schemaDefined() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).schema("NILS");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void alternativeTableDefinition() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        JdbcAdapterConfig.init(url, username, "")
            .tableName("TRANSLATIONS")
            .localeField("THE_LOCALE")
            .keyField("THE_KEY")
            .valueField("THE_VALUE");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void driverName_invalid() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).driverClass("h2.JDBCDriver");
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act / Assert
    assertThatThrownBy(() -> underTest.getTranslation("translate.me"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-250: The JDBC driver class name is invalid.");
  }

  @Test
  public void cacheTimeoutDefined() throws SQLException, InterruptedException {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).cacheTimeout(1);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");

    // Arrange
    dbUtil.executeUpdate(
        url, "UPDATE NILS_TRANSLATION SET NLS_VALUE = 'Translate me now!' WHERE ID = 1;");
    Thread.sleep(2000);
    // Act
    var value2 = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value2).isNotEmpty();
    assertThat(value2.get()).isEqualTo("Translate me now!");
  }

  @Test
  public void cacheTimeoutDisabled() throws SQLException, InterruptedException {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init(url, username, password).cacheTimeout(-1);
    context.locale(locale).config(config);
    var underTest = new JdbcAdapter(context);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");

    // Arrange
    dbUtil.executeUpdate(
        url, "UPDATE NILS_TRANSLATION SET NLS_VALUE = 'Translate me now!' WHERE ID = 1;");
    Thread.sleep(2000);
    // Act
    var value2 = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value2).isNotEmpty();
    assertThat(value2.get()).isEqualTo("I'm translated!");
  }
}
