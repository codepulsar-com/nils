package com.codepulsar.nils.adapter.snakeyaml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.core.error.NilsConfigException;
import com.codepulsar.nils.core.error.NilsException;

public class SnakeYamlAdapterTest {

  private Locale current;

  @BeforeEach
  public void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
  }

  @AfterEach
  public void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  public void nullLocale() {
    // Arrange
    Locale locale = null;
    AdapterConfig config = SnakeYamlAdapterConfig.init(this);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    AdapterConfig config = null;

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void invalidAdapterConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    AdapterConfig config = ResourceBundleAdapterConfig.init(this);

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("is not of type");
  }

  @Test
  public void resourceBundleAdapter_defautltConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this);

    // Act
    var underTest = new SnakeYamlAdapter(config, locale);

    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  public void invalidBaseFileName() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/non_existing");

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-008: Could not find a resource for baseFilename 'test/non_existing.yaml'.");
  }

  @Test
  public void fallbackFile() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

    // Act
    var value = underTest.getTranslation("translate.me");

    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm translated!");
  }

  @Test
  public void translateKeyFound_nestedKeys() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this);
    var underTest = new SnakeYamlAdapter(config, locale);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

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
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

    // Act
    var value = underTest.getTranslation("translate.fallback_notfound");

    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void translateKeyNotFound() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapter(config, locale);

    // Act
    var value = underTest.getTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void corruptJsonFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/corrupt");

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-150: Error reading YAML file '/test/corrupt.yaml'.");
  }

  @Test
  public void nonYamlFile() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/non_yaml");

    // Act / Assert
    assertThatThrownBy(() -> new SnakeYamlAdapter(config, locale))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-150: Error reading YAML file '/test/non_yaml.yaml'.");
  }
}
