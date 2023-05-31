package com.codepulsar.nils.core.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class ResourceBundleAdapterTest {

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
  public void resourceBundleAdapter_nullLocale() {
    // Arrange
    Locale locale = null;
    NilsConfig config = NilsConfig.init(ResourceBundleAdapterConfig.init(this));
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void resourceBundleAdapter_nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = null;
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void resourceBundleAdapter_invalidAdapterConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("is not of type");
  }

  @Test
  public void resourceBundleAdapter_defautltConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(ResourceBundleAdapterConfig.init(this));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  public void invalidResourceBundle() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/non_existing"));
    // Act / Assert
    assertThatThrownBy(() -> new ResourceBundleAdapter(config, locale))
        .isInstanceOf(MissingResourceException.class);
  }

  @Test
  public void missingResourceBundle() {
    // Arrange
    Locale locale = Locale.ITALIAN;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing"));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.resolveTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm am translated!");
  }

  @Test
  public void translateKeyFound1() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing"));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.resolveTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("I'm am translated!");
  }

  @Test
  public void translateKeyFound2() {
    // Arrange
    Locale locale = Locale.GERMAN;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing"));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.resolveTranslation("translate.me");
    // Assert
    assertThat(value).isNotEmpty();
    assertThat(value.get()).isEqualTo("Ich bin übersetzt!");
  }

  @Test
  public void translateKeyNotFound() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing"));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> value = underTest.resolveTranslation("translate.me.butImNotThere");
    // Assert
    assertThat(value).isEmpty();
  }

  @Test
  public void get() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(
            ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing"));
    // Act
    ResourceBundleAdapter underTest = new ResourceBundleAdapter(config, locale);
    Optional<String> resolvedValue = underTest.resolveTranslation("translate.get");
    String getValue = underTest.get("translate.get");
    String getWithArgsValue = underTest.get("translate.get", "A", 400L);

    // Assert
    assertThat(resolvedValue).isNotEmpty();
    assertThat(resolvedValue.get()).isEqualTo("Get from {0} to {1} from file.");
    assertThat(getValue).isEqualTo("Get from {0} to {1} from file.");
    assertThat(getWithArgsValue).isEqualTo("Get from A to 400 from file.");
  }
}
