package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class JacksonAdapterFactoryTest {

  @Test
  public void createForJson() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterJsonConfig.init(this).baseFileName("test/existing");
    var underTest = new JacksonAdapterFactory();

    // Act
    var result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void createForYaml() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JacksonAdapterYamlConfig.init(this).baseFileName("test/existing");
    var underTest = new JacksonAdapterFactory();

    // Act
    var result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void factoryDefault() {
    // Act
    var underTest = new JacksonAdapterFactory();

    // Assert
    assertThat(underTest).isInstanceOf(BaseAdapterFactory.class);
  }
}
