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
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.testadapter.InvalidAdapterFactory;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;
import com.codepulsar.nils.core.testadapter.TestFactoryAdapterConfig;
import com.codepulsar.nils.core.testdata.Dummy;

public class NilsFactoryImplTest {

  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.core.impl.NilsFactoryImplTestDataProvider";

  private NilsFactory underTest;
  private NilsConfig<?> config;

  @BeforeEach
  void setup() {
    config = new StaticAdapterConfig();
    underTest = new NilsFactoryImpl(config);
  }

  @Test
  void init_nilsConfigNull() {
    // Arrange
    NilsConfig<?> nilsConfig = null;
    // Act / Assert
    assertThatThrownBy(() -> new NilsFactoryImpl(nilsConfig))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-004: Parameter 'config' cannot be null.");
  }

  @Test
  void init_nilsConfig() {
    // Arrange
    var adapterConfig = new StaticAdapterConfig();
    NilsFactory _underTest = NilsFactory.init(adapterConfig);

    // Act
    var nls = _underTest.nls();

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  void nls() {
    // Act
    var nls = underTest.nls();

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  void nlsFromLocale() {
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
  void nlsFromLocaleNull() {
    // Arrange
    Locale locale = null;
    config.suppressErrors(false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.nls(locale))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("Parameter 'locale' cannot be null.");
  }

  @Test
  void nlsFromLocaleNull_suppressErrors() {
    // Arrange
    Locale locale = null;
    config.suppressErrors(true);

    // Act
    var nls = underTest.nls(locale);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nlsFromLang() {
    // Act
    var nls = underTest.nls("de-DE");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @Test
  void nlsFromLang2() {
    // Act
    var nls = underTest.nls("en_US");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.US);
  }

  @Test
  void nlsFromLang_notFound() {
    // Act
    var nls = underTest.nls("XYZ");

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("xyz"));
  }

  @Test
  void reset() {
    // Arrange
    var nlsDe = underTest.nls(Locale.GERMAN);
    var nlsDefault = underTest.nls();
    // Act
    underTest.reset();
    var nlsDe2 = underTest.nls(Locale.GERMAN);
    var nlsDefault2 = underTest.nls();

    // Assert
    assertThat(nlsDe2).isNotEqualTo(nlsDe);
    assertThat(nlsDefault2).isNotEqualTo(nlsDefault);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsFromLang_invalid")
  void nlsFromLang_invalid(String lang, String errorMsg) {
    // Arrange
    config.suppressErrors(false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.nls(lang))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsFromLang_invalid")
  void nlsFromLang_invalid_suppressErrors(String lang, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nls(lang);

    // Assert

    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nlsWithContext_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext(context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.getDefault());
  }

  @Test
  void nlsWithContext_class() {
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
  void nlsWithContext_string_invalid(String context, String errorMsg) {
    // Arrange
    config.suppressErrors(false);
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_string_invalid")
  void nlsWithContext_string_invalid_suppressErrors(String context, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nlsWithContext(context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nlsWithContext_class_null() {
    // Arrange
    config.suppressErrors(false);
    Class<?> context = null;

    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("Parameter 'context' cannot be null.");
  }

  @Test
  void nlsWithContext_class_null_suppressErrors() {
    // Arrange
    config.suppressErrors(true);
    Class<?> context = null;

    // Act
    var nls = underTest.nlsWithContext(context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nlsWithContext_fromLang_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext("de-DE", context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(new Locale("de", "DE"));
  }

  @Test
  void nlsWithContext_fromLang_class() {
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
  void nlsWithContext_fromLang_string_invalid(String lang, String context, String errorMsg) {
    // Arrange
    config.suppressErrors(false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(lang, context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLang_string_invalid")
  void nlsWithContext_fromLang_string_invalid_suppressErrors(
      String lang, String context, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nlsWithContext(lang, context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLang_class_invalid")
  void nlsWithContext_fromLang_class_invalid(String lang, Class<?> context, String errorMsg) {
    // Arrange
    config.suppressErrors(false);
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(lang, context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLang_class_invalid")
  void nlsWithContext_fromLang_class_invalid_suppressErrors(
      String lang, Class<?> context, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nlsWithContext(lang, context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nlsWithContext_fromLocale_string() {
    // Arrange
    var context = "context";
    // Act
    var nls = underTest.nlsWithContext(Locale.GERMAN, context);

    // Assert
    assertThat(nls).isNotNull();
    assertThat(nls.getLocale()).isEqualTo(Locale.GERMAN);
  }

  @Test
  void nlsWithContext_fromLocale_class() {
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
  void nlsWithContext_fromLocale_string_invalid(Locale locale, String context, String errorMsg) {
    // Arrange
    config.suppressErrors(false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(locale, context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLocale_string_invalid")
  void nlsWithContext_fromLocale_string_invalid_suppressErrors(
      Locale locale, String context, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nlsWithContext(locale, context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLocale_class_invalid")
  void nlsWithContext_fromLocale_class_invalid(Locale locale, Class<?> context, String errorMsg) {
    // Arrange
    config.suppressErrors(false);
    // Act / Assert
    assertThatThrownBy(() -> underTest.nlsWithContext(locale, context))
        .isInstanceOf(NilsException.class)
        .hasMessageContaining(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#nlsWithContext_fromLocale_class_invalid")
  void nlsWithContext_fromLocale_class_invalid_suppressErrors(
      Locale locale, Class<?> context, String errorMsg) {
    // Arrange
    config.suppressErrors(true);

    // Act
    var nls = underTest.nlsWithContext(locale, context);

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void nullAdapterFactoryClass() {
    var _config = new TestFactoryAdapterConfig(null);
    _config.suppressErrors(false);

    NilsFactory _underTest = NilsFactory.init(_config);

    // Act / Assert
    assertThatThrownBy(() -> _underTest.nls())
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("Could not create AdapterFactory");
  }

  @Test
  void nullAdapterFactoryClass_suppressErrors() {
    var _config = new TestFactoryAdapterConfig(null);
    _config.suppressErrors(true);

    NilsFactory _underTest = NilsFactory.init(_config);

    // Act
    var nls = _underTest.nls();

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }

  @Test
  void adapterFactoryWithNonEmptyCtor() {
    var _config = new TestFactoryAdapterConfig(InvalidAdapterFactory.class);
    _config.suppressErrors(false);

    NilsFactory _underTest = NilsFactory.init(_config);

    // Act / Assert
    assertThatThrownBy(() -> _underTest.nls())
        .isInstanceOf(NilsException.class)
        .hasMessageContaining("Could not create AdapterFactory");
  }

  @Test
  void adapterFactoryWithNonEmptyCtor_suppressErrors() {
    var _config = new TestFactoryAdapterConfig(InvalidAdapterFactory.class);
    _config.suppressErrors(true);

    NilsFactory _underTest = NilsFactory.init(_config);

    // Act
    var nls = _underTest.nls();

    // Assert
    assertThat(nls).isInstanceOf(FallsaveNLSImpl.class);
  }
}
