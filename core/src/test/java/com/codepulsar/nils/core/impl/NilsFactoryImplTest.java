package com.codepulsar.nils.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.NilsFactory;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;
import com.codepulsar.nils.core.testdata.Dummy;

public class NilsFactoryImplTest {

  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.core.impl.NilsFactoryImplTestDataProvider";

  private NilsFactory underTest;

  @BeforeEach
  public void setup() {
    var adapterConfig = new StaticAdapterConfig();
    var nilsConfig = NilsConfig.init(adapterConfig);
    underTest = new NilsFactoryImpl(nilsConfig);
  }

  @Test
  public void init_nilsConfigNull() {
    // Arrange
    NilsConfig nilsConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> new NilsFactoryImpl(nilsConfig))
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

  @Test
  public void nls() {
    // Act
    var nls = underTest.nls();

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  public void nlsFromLocale() {
    // Act
    var nls = underTest.nls(Locale.GERMAN);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.GERMAN);

    // Act
    var nls2 = underTest.nls(Locale.GERMAN);
    assertThat(nls2).isEqualTo(nls);
  }

  @Test
  public void nlsFromLocaleNull() {
    // Arrange
    Locale locale = null;

    // Act / Assert
    assertThatThrownBy(() -> underTest.nls(locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void nlsFromLang() {
    // Act
    var nls = underTest.nls("de-DE");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @Test
  public void nlsFromLang2() {
    // Act
    var nls = underTest.nls("en_US");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.US);
  }

  @Test
  public void nlsFromLang_notFound() {
    // Act
    var nls = underTest.nls("XYZ");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("xyz"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsFromLang_invalid")
  public void nlsFromLang_invalid(String lang, String errorMsg) {
    // Act / Assert
    assertThatThrownBy(() -> underTest.nls(lang))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  @Test
  public void nlsWithContext_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext(context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  public void nlsWithContext_class() {
    // Arrange
    var context = Dummy.class;
    // Act
    var nls = underTest.nlsWithContext(context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_string_invalid")
  public void nlsWithContext_string_invalid(String context, String errorMsg) {
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  public void nlsWithContext_class_null() {
    // Arrange
    Class<?> context = null;
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("errorMsg");
  }

  @Test
  public void nlsWithContext_fromLang_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext("de-DE", context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @Test
  public void nlsWithContext_fromLang_class() {
    // Arrange
    var context = Dummy.class;
    // Act
    var nls = underTest.nlsWithContext("de-DE", context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLang_string_invalid")
  public void nlsWithContext_fromLang_string_invalid(String lang, String context, String errorMsg) {
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(lang, context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLang_class_invalid")
  public void nlsWithContext_fromLang_class_invalid(
      String lang, Class<?> context, String errorMsg) {
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(lang, context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  @Test
  public void nlsWithContext_fromLocale_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext(Locale.GERMAN, context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.GERMAN);
  }

  @Test
  public void nlsWithContext_fromLocale_class() {
    // Arrange
    var context = Dummy.class;
    // Act
    var nls = underTest.nlsWithContext(Locale.GERMAN, context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.GERMAN);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLocale_string_invalid")
  public void nlsWithContext_fromLocale_string_invalid(
      Locale locale, String context, String errorMsg) {

    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(locale, context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLocale_class_invalid")
  public void nlsWithContext_fromLocale_class_invalid(
      Locale locale, Class<?> context, String errorMsg) {

    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(locale, context))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }
}
