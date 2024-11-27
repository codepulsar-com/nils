package com.codepulsar.nils.core.adapter.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.testadapter.BaseLocalizedResourceAdapterTesteeFactory;

public class BaseLocalizedResourceNilsConfigTest {
  @Test
  void defaultTest() {
    // Act
    var underTest = new Testee(this);

    // Assert
    assertThat(underTest.getBaseFileName())
        .isEqualTo("com/codepulsar/nils/core/adapter/config/translation.test");
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  void baseFileName_withoutFileExtension() {
    // Act
    var underTest = new Testee(this);

    // Assert
    var returnValue = underTest.baseFileName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.test");
  }

  @Test
  void baseFileName_withFileExtension() {
    // Arrange
    var underTest = new Testee(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.jsn");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.jsn");
  }

  @Test
  void fallbackActive_false() {
    // Arrange
    var underTest = new Testee(this);

    // Act
    var returnValue = underTest.fallbackActive(false);

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.isFallbackActive()).isFalse();
  }

  private class Testee extends BaseLocalizedResourceNilsConfig<Testee> {

    protected Testee(Object o) {
      super(o.getClass(), ".test");
    }

    @Override
    public Class<? extends AdapterFactory<?>> getFactoryClass() {
      return BaseLocalizedResourceAdapterTesteeFactory.class;
    }
  }
}
