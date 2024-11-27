package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;

public class JacksonAdapterYamlConfigTest {
  @Test
  public void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterYamlConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterYamlConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initFromClass() {
    // Act
    var underTest = JacksonAdapterYamlConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.yaml");
    assertThat(underTest.getFactoryClass()).isEqualTo(JacksonAdapterFactory.class);
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  public void initFromObject() {
    // Act
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName()).isEqualTo("nls/translation.yaml");
  }

  @Test
  public void baseFileName_withoutFileExtension() {
    // Assert
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.yaml");
  }

  @Test
  public void baseFileName_withFileExtension() {
    // Assert
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.yml");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.yml");
  }
}
