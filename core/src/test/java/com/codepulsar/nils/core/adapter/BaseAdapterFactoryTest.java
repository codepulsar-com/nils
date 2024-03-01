package com.codepulsar.nils.core.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.BaseAdapterFactoryTesteeConfig;
import com.codepulsar.nils.core.testadapter.BaseAdapterFactoryTesteeFactory;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class BaseAdapterFactoryTest {
  @Test
  public void create_configIsNull() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig<?> config = null;
    var underTest = new BaseAdapterFactoryTesteeFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @Test
  public void create_localeIsNull() {
    // Arrange
    Locale locale = null;
    var config = new BaseAdapterFactoryTesteeConfig();
    var underTest = new BaseAdapterFactoryTesteeFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void invalidAdapterConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = new StaticAdapterConfig();
    var underTest = new BaseAdapterFactoryTesteeFactory();

    // Act / Assert
    assertThatThrownBy(() -> underTest.create(config, locale))
        .isInstanceOf(NilsConfigException.class)
        .hasMessageContaining("The provided AdapterConfig")
        .hasMessageContaining("is not of the expected type");
  }

  @Test
  public void checkCaching() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = new BaseAdapterFactoryTesteeConfig();
    var underTest = new BaseAdapterFactoryTesteeFactory();
    // Act
    var result = underTest.create(config, locale);
    // Assert
    assertThat(result).isNotNull();

    // Act
    var result2 = underTest.create(config, locale);
    // Assert
    assertThat(result2).isEqualTo(result);

    // Arrange
    locale = Locale.GERMAN;
    // Act
    var result3 = underTest.create(config, locale);
    // Assert
    assertThat(result3).isNotEqualTo(result);
  }
}
