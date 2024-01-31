package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterConfig;

public class JacksonAdapterFactoryTest {

  @Test
  public void create_configIsNull() {
    // Arrange
    var locale = Locale.ENGLISH;
    AdapterConfig config = null;
    var underTest = new JacksonAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void create_localeIsNull() {
    // Arrange
    Locale locale = null;
    AdapterConfig config = JacksonAdapterConfig.init(this);
    var underTest = new JacksonAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new JacksonAdapterFactory();

    // Act
    var result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }
}
