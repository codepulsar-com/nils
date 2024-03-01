package com.codepulsar.nils.core.adapter.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterFactory;
import com.codepulsar.nils.core.testadapter.BaseLocalizedResourceAdapterTesteeFactory;

public class BaseLocalizedResourceNilsConfigTest {
  @Test
  public void defaultTest() {
    // Assert
    var underTest = new Testee(this);

    // Act
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.test");
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  public void baseFileName_withoutFileExtension() {
    // Assert
    var underTest = new Testee(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.test");
  }

  @Test
  public void baseFileName_withFileExtension() {
    // Assert
    var underTest = new Testee(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.jsn");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.jsn");
  }

  @Test
  public void fallbackActive_false() {
    // Assert
    var underTest = new Testee(this);

    // Act
    var returnValue = underTest.fallbackActive(false);
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.isFallbackActive()).isFalse();
  }

  private class Testee extends BaseLocalizedResourceNilsConfig<Testee> {

    protected Testee(Object o) {
      super(o.getClass().getModule(), ".test");
    }

    @Override
    public Class<? extends AdapterFactory<?>> getFactoryClass() {
      return BaseLocalizedResourceAdapterTesteeFactory.class;
    }
  }
}
