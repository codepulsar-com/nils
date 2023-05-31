package com.codepulsar.nils.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.adapter.AdapterConfig;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class NilsFactoryTest {
  @Test
  public void init_adapterConfigNull() {
    // Arrange
    AdapterConfig adapterConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> NilsFactory.init(adapterConfig))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'adapterConfig' cannot be null.");
  }

  @Test
  public void nls() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsFactory underTest = NilsFactory.init(adapterConfig);
    // Act
    NLS nls = underTest.nls();
    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  public void nlsFromLocale() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsFactory underTest = NilsFactory.init(adapterConfig);
    // Act
    NLS nls = underTest.nls(Locale.GERMAN);
    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.GERMAN);

    // Act
    NLS nls2 = underTest.nls(Locale.GERMAN);
    assertThat(nls2).isEqualTo(nls);
  }

  @Test
  public void nlsFromLang() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsFactory underTest = NilsFactory.init(adapterConfig);
    // Act
    NLS nls = underTest.nls("de-DE");
    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @Test
  public void nlsFromLang2() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsFactory underTest = NilsFactory.init(adapterConfig);
    // Act
    NLS nls = underTest.nls("en_US");
    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.US);
  }

  @Test
  public void nlsFromLang_notFound() {
    // Arrange
    AdapterConfig adapterConfig = new StaticAdapterConfig();
    NilsFactory underTest = NilsFactory.init(adapterConfig);
    // Act
    NLS nls = underTest.nls("XYZ");
    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("xyz"));
  }
}
