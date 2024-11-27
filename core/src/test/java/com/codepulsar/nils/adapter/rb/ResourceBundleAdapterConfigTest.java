package com.codepulsar.nils.adapter.rb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.error.NilsException;

public class ResourceBundleAdapterConfigTest {
  @Test
  public void initClassNull() {
    // Arrange
    Class<?> nullClass = null;
    // Act / Assert
    assertThatThrownBy(() -> ResourceBundleAdapterConfig.init(nullClass))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initObjectNull() {
    // Arrange
    Object nullObject = null;
    // Act / Assert
    assertThatThrownBy(() -> ResourceBundleAdapterConfig.init(nullObject))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'owner' cannot be null.");
  }

  @Test
  public void initFromClass() {
    // Act
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(getClass());

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(getClass().getModule());
    assertThat(underTest.getResourcesBundleName()).isEqualTo("nls/translation");
    assertThat(underTest.getFactoryClass()).isEqualTo(ResourceBundleAdapterFactory.class);
  }

  @Test
  public void initFromObject() {
    // Act
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(this);

    // Assert
    assertThat(underTest).isNotNull();
    assertThat(underTest.getOwner()).isEqualTo(this.getClass().getModule());
    assertThat(underTest.getResourcesBundleName()).isEqualTo("nls/translation");
  }

  @Test
  public void resourceBundleName() {
    // Assert
    ResourceBundleAdapterConfig underTest = ResourceBundleAdapterConfig.init(this);

    // Act
    ResourceBundleAdapterConfig returnValue = underTest.resourcesBundleName("TestBundleName");
    assertThat(returnValue).isNotNull();
    assertThat(returnValue).isEqualTo(underTest);
    assertThat(underTest.getResourcesBundleName()).isEqualTo("TestBundleName");
  }
}
