package com.codepulsar.nils.adapter.gson;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class GsonAdapterFactoryTest {

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = GsonAdapterConfig.init(this).baseFileName("test/existing");
    var underTest = new GsonAdapterFactory();

    // Act
    GsonAdapter result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void factoryDefault() {
    // Act
    var underTest = new GsonAdapterFactory();

    // Assert
    assertThat(underTest).isInstanceOf(BaseAdapterFactory.class);
  }
}
