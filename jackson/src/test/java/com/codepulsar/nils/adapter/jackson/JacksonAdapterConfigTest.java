package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.error.NilsConfigException;

public class JacksonAdapterConfigTest {
  @Test
  public void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterConfig.init(nullClass))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterConfig.init(nullObject))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initFromClass() {
    // Act
    var underTest = JacksonAdapterConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
    assertThat(underTest.getFactoryClass()).isEqualTo(JacksonAdapterFactory.class);
  }

  @Test
  public void initFromObject() {
    // Act
    var underTest = JacksonAdapterConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.json");
  }

  @Test
  public void baseFileName_withoutFileExtension() {
    // Assert
    var underTest = JacksonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.json");
  }

  @Test
  public void baseFileName_withFileExtension() {
    // Assert
    var underTest = JacksonAdapterConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.jsn");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.jsn");
  }
}
