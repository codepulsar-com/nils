package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;

public class ResourceBundleAdapterTest {

  private Locale current;

  @BeforeEach
  void defineDefault() {
    current = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
  }

  @AfterEach
  void resetLocale() {
    Locale.setDefault(current);
  }

  @Test
  void resourceBundleAdapter_nullLocale() {
    // Arrange
    Locale locale = null;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  void resourceBundleAdapter_nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  void resourceBundleAdapter_defautltConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  void invalidResourceBundle() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this).baseFileName("test/non_existing");
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(MissingResourceException.class);
  }

  @Test
  void missingResourceBundle() {
    // Arrange
    var locale = Locale.ITALIAN;
    var config = ResourceBundleAdapterConfig.init(this).baseFileName("test/existing");
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.getTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm am translated!");
  }

  @Test
  void translateKeyFound1() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.getTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm am translated!");
  }

  @Test
  void translateKeyFound2() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.getTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Ich bin übersetzt!");
  }

  @Test
  void translateKeyFallback() {
    // Arrange
    var locale = Locale.GERMAN;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.getTranslation("translate.fallback");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm a fallback!");
  }

  @Test
  void translateKeyNotFound() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this);
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.getTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }
}
