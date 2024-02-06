package com.codepulsar.nils.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.api.adapter.AdapterConfig;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class NilsFactoryTest {

  @Test
  public void init_adapterConfigNull() {
    // Arrange
    AdapterConfig adapterConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> NilsFactory.init(adapterConfig))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'adapterConfig' cannot be null.");
  }

  @Test
  public void init_nilsConfigNull() {
    // Arrange
    NilsConfig nilsConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> NilsFactory.init(nilsConfig))
        .isInstanceOf(NilsConfigException.class)
        .hasMessage("NILS-004: Parameter 'config' cannot be null.");
  }

  @Test
  public void init_nilsConfig() {
    // Arrange
    var adapterConfig = new StaticAdapterConfig();
    var nilsConfig = NilsConfig.init(adapterConfig);
    NilsFactory _underTest = NilsFactory.init(nilsConfig);

    // Act
    var nls = _underTest.nls();

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }
}
