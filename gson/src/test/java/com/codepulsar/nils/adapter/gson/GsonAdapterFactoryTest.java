package com.codepulsar.nils.adapter.gson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.AdapterConfig;

public class GsonAdapterFactoryTest {

  @Test
  public void create_configIsNull() {
    // Arrange
    var locale = Locale.ENGLISH;
    AdapterConfig config = null;
    var underTest = new GsonAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void create_localeIsNull() {
    // Arrange
    Locale locale = null;
    AdapterConfig config = GsonAdapterConfig.init(this);
    var underTest = new GsonAdapterFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = GsonAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new GsonAdapterFactory();

    // Act
    var result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }
}
