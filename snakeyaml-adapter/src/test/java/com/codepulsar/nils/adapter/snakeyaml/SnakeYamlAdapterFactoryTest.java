package com.codepulsar.nils.adapter.snakeyaml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;

public class SnakeYamlAdapterFactoryTest {

  @Test
  public void create_configIsNull() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    var underTest = new SnakeYamlAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void create_localeIsNull() {
    // Arrange
    Locale locale = null;
    var config = SnakeYamlAdapterConfig.init(this);
    var underTest = new SnakeYamlAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapterFactory();

    // Act
    var result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void invalidAdapterConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this);
    var underTest = new SnakeYamlAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("is not of type");
  }
}
