package com.codepulsar.nils.adapter.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.BaseAdapterFactory;

public class JdbcAdapterFactoryTest {

  @Test
  public void create() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = JdbcAdapterConfig.init("test", "sa", "");
    var underTest = new JdbcAdapterFactory();

    // Act
    JdbcAdapter result = underTest.create(config, locale);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void factoryDefault() {
    // Act
    var underTest = new JdbcAdapterFactory();

    // Assert
    assertThat(underTest).isInstanceOf(BaseAdapterFactory.class);
  }
}
