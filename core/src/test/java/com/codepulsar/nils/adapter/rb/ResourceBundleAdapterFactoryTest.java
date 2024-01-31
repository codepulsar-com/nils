package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterConfig;

public class ResourceBundleAdapterFactoryTest {

  @Test
  public void create_configIsNull() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    AdapterConfig config = null;
    ResourceBundleAdapterFactory underTest = new ResourceBundleAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void create_localeIsNull() {
    // Arrange
    Locale locale = null;
    AdapterConfig config = ResourceBundleAdapterConfig.init(this);
    ResourceBundleAdapterFactory underTest = new ResourceBundleAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void create() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    AdapterConfig config =
        ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing");
    ResourceBundleAdapterFactory underTest = new ResourceBundleAdapterFactory();
    // Act
    ResourceBundleAdapter result = underTest.create(config, locale);
    // Assert
    assertThat(result).isNotNull();
  }
}
