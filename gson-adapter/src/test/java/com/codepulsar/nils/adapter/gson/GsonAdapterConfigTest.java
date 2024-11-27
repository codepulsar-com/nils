package com.codepulsar.nils.adapter.gson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;

public class GsonAdapterConfigTest {
  @Test
  public void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> GsonAdapterConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> GsonAdapterConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initFromClass() {
    // Act
    var underTest = GsonAdapterConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
    assertThat(underTest.getFactoryClass()).isEqualTo(GsonAdapterFactory.class);
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  public void initFromObject() {
    // Act
    var underTest = GsonAdapterConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
  }

  @Test
  public void baseFileName_withoutFileExtension() {
    // Assert
    var underTest = GsonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.json");
  }

  @Test
  public void baseFileName_withFileExtension() {
    // Assert
    var underTest = GsonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.jsn");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.jsn");
  }
}
