package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class ResourceBundleAdapterFactoryTest {

  @Test
  public void create_configIsNull() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
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
    var config = ResourceBundleAdapterConfig.init(this);
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
    var config = ResourceBundleAdapterConfig.init(this).resourcesBundleName("test/existing");
    ResourceBundleAdapterFactory underTest = new ResourceBundleAdapterFactory();
    // Act
    ResourceBundleAdapter result = underTest.create(config, locale);
    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void invalidAdapterConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = new StaticAdapterConfig();
    var underTest = new ResourceBundleAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("is not of type");
  }
}
