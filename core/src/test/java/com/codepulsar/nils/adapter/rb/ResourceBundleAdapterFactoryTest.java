package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class ResourceBundleAdapterFactoryTest {

  @Test
  void factoryDefault() {
    // Act
    var underTest = new ResourceBundleAdapterFactory();

    // Assert
    assertThat(underTest).isInstanceOf(BaseAdapterFactory.class);
  }

  @Test
  void create() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    var config = ResourceBundleAdapterConfig.init(this).baseFileName("test/existing");
    ResourceBundleAdapterFactory underTest = new ResourceBundleAdapterFactory();
    // Act
    ResourceBundleAdapter result = underTest.create(config, locale);
    // Assert
    assertThat(result).isNotNull();
  }
}
