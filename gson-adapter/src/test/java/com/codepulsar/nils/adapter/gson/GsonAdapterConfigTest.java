package com.codepulsar.nils.adapter.gson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

public class GsonAdapterConfigTest {
  @Test
  void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> GsonAdapterConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> GsonAdapterConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initFromClass() {
    // Act
    var underTest = GsonAdapterConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest).isInstanceOf(BaseLocalizedResourceNilsConfig.class);
    assertThat(underTest.getOwner()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
    assertThat(underTest.getFactoryClass()).isEqualTo(GsonAdapterFactory.class);
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  void initFromObject() {
    // Act
    var underTest = GsonAdapterConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
  }

  @Test
  void baseFileName_withoutFileExtension() {
    // Arrange
    var underTest = GsonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.json");
  }

  @Test
  void baseFileName_withFileExtension() {
    // Arrange
    var underTest = GsonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.jsn");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.jsn");
  }
}
