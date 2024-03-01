package com.codepulsar.nils.adapter.snakeyaml;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class SnakeYamlAdapterFactoryTest {

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = SnakeYamlAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new SnakeYamlAdapterFactory();

    // Act
    SnakeYamlAdapter result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void factoryDefault() {
    // Act
    var underTest = new SnakeYamlAdapterFactory();

    // Assert
    assertThat(underTest).isInstanceOf(BaseAdapterFactory.class);
  }
}
