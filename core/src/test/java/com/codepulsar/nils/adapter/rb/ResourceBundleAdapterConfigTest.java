package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.adapter.config.BaseLocalizedResourceNilsConfig;

public class ResourceBundleAdapterConfigTest {
  @Test
  void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> ResourceBundleAdapterConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> ResourceBundleAdapterConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  void initFromClass() {
    // Act
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest).isInstanceOf(BaseLocalizedResourceNilsConfig.class);
    assertThat(underTest.getOwnerModule()).isEqualTo(getClass().getModule());
    assertThat(underTest.getBaseFileName())
        .isEqualTo("com/codepulsar/nils/adapter/rb/translation.properties");
    assertThat(underTest.getFactoryClass()).isEqualTo(ResourceBundleAdapterFactory.class);
  }

  @Test
  void initFromObject() {
    // Act
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwnerModule()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getBaseFileName())
        .isEqualTo("com/codepulsar/nils/adapter/rb/translation.properties");
  }

  @Test
  void resourceBundleName() {
    // Arrange
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(this);

    // Act
    ResourceBundleAdapterConfig returnValue = underTest.baseFileName("TestBundleName");

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getBaseFileName()).isEqualTo("TestBundleName.properties");
  }

  @Test
  void fallresourceBundleName() {
    // Arrange
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(this);
    assertThat(underTest.isFallbackActive()).isTrue();

    // Act
    ResourceBundleAdapterConfig returnValue = underTest.fallbackActive(false);

    // Assert
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(returnValue.isFallbackActive()).isTrue();
  }
}
