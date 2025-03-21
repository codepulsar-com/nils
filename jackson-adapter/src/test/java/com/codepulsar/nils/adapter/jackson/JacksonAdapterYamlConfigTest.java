package com.codepulsar.nils.adapter.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

public class JacksonAdapterYamlConfigTest {
  @Test
  void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterYamlConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> JacksonAdapterYamlConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initFromClass() {
    // Act
    var underTest = JacksonAdapterYamlConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest).isInstanceOf(BaseLocalizedResourceNilsConfig.class);
    assertThat(underTest.getOwnerModule()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName())
        .isEqualTo("com/codepulsar/nils/adapter/jackson/translation.yaml");
    assertThat(underTest.getFactoryClass()).isEqualTo(JacksonAdapterFactory.class);
    assertThat(underTest.isFallbackActive()).isTrue();
  }

  @Test
  void initFromObject() {
    // Act
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwnerModule()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName())
        .isEqualTo("com/codepulsar/nils/adapter/jackson/translation.yaml");
  }

  @Test
  void baseFileName_withoutFileExtension() {
    // Arrange
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.yaml");
  }

  @Test
  void baseFileName_withFileExtension() {
    // Arrange
    var underTest = JacksonAdapterYamlConfig.init(this);

    // Act
    var returnValue = underTest.baseFileName("TestBundleName.yml");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.yml");
  }
}
