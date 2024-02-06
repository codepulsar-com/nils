package com.codepulsar.nils.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.adapter.Adapter;
import com.codepulsar.nils.api.error.NilsException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.handler.TranslationFormatter;
import com.codepulsar.nils.core.testadapter.StaticAdapter;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;
import com.codepulsar.nils.core.testdata.Dummy;

public class NLSImplTest {

  private static final String DATA_PROVIDER =
      "com.codepulsar.nils.core.impl.NLSImplTestDataProvider";

  @Test
  public void constructor_nullAdapter() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    Adapter adapter = null;

    // Act / Assert
    assertThatThrownBy(() -> new NLSImpl(adapter, config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'adapter' cannot be null.");
  }

  @Test
  public void constructor_nullLocale() {
    // Arrange
    Locale locale = null;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var adapter = new StaticAdapter();

    // Act / Assert
    assertThatThrownBy(() -> new NLSImpl(adapter, config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void constructor_nullConfig() {
    // Arrange
    var locale = Locale.ENGLISH;
    NilsConfig config = null;
    var adapter = new StaticAdapter();

    // Act / Assert
    assertThatThrownBy(() -> new NLSImpl(adapter, config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKey_invalid")
  public void string_getByKey_invalid(String key, String errMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKey_invalid_suppressed")
  public void string_getByKey_invalid_suppressed(String key, String expected) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(key);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKey_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("simple");

    // Assert
    assertThat(result).isEqualTo("A simple translation");
  }

  @Test
  public void string_getByKey_notFound_escaping_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("not.found");

    // Assert
    assertThat(result).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig()).escapePattern(">>{0}<<").suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);
    // Act
    var result = underTest.get("not.found");

    // Assert
    assertThat(result).isEqualTo(">>not.found<<");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed2() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .escapePattern("Missing: {0}")
            .suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("not.found");

    // Assert
    assertThat(result).isEqualTo("Missing: not.found");
  }

  @Test
  public void string_getByKey_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKeyAndArgs_invalid")
  public void string_getByKeyAndArgs_invalid(String key, Object[] args, String errMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_string_getByKeyAndArgs_invalid_suppressed")
  public void string_getByKeyAndArgs_invalid_suppressed(
      String key, Object[] args, String expected) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(key, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKeyAndArgs_nullArgs() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("with_args", (Object[]) null);

    // Assert
    assertThat(result).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void string_getByKeyAndArgs_emptyArgs() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("with_args", new Object[] {});

    // Assert
    assertThat(result).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void string_getByKeyAndArgs_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200.");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_escaping_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("not.found", "with a value");

    // Assert
    assertThat(result).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @Test
  public void string_getByKeyAndArgs_emptyArgs_stringformat_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .translationFormatter(TranslationFormatter.STRING_FORMAT)
            .suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("with_args_string_format", new Object[] {}))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-006: Error in key 'with_args_string_format': Format specifier '%1$s'");
  }

  @Test
  public void string_getByKeyAndArgs_emptyArgs_stringformat_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .translationFormatter(TranslationFormatter.STRING_FORMAT)
            .suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("with_args_string_format", new Object[] {});

    // Assert
    assertThat(result).isEqualTo("[with_args_string_format]");
  }

  @Test
  public void string_getByKeyAndArgs_found_stringformat() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .translationFormatter(TranslationFormatter.STRING_FORMAT);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get("with_args_string_format", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKey_invalid")
  public void class_getByKey_invalid(Class<?> key, String subKey, String errMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKey_invalid_suppressed")
  public void class_getByKey_invalid_suppressed(Class<?> key, String subKey, String expected) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(key, subKey);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKey_foundSimpleClassnameResolver() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    assertThat(config.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.SIMPLE_CLASSNAME);

    // Act
    var result = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(result).isEqualTo("Attribute");
  }

  @Test
  public void class_getByKey_foundFqnClassnameResolver() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig())
            .classPrefixResolver(ClassPrefixResolver.FQN_CLASSNAME);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(result).isEqualTo("Attribute (FQN)");
  }

  @Test
  public void class_getByKey_foundOwnResolver() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig()).classPrefixResolver(c -> "StaticClassResolver");
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(result).isEqualTo("Attribute (StaticResolver)");
  }

  @Test
  public void class_getByKey_notFound_escaping_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(result).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKey_notFound_escaping_changed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config =
        NilsConfig.init(new StaticAdapterConfig()).escapePattern(">>{0}<<").suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(result).isEqualTo(">>Dummy.not_found<<");
  }

  @Test
  public void class_getByKey_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'Dummy.not_found' and locale 'en'.");
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKeyAndArgs_invalid")
  public void class_getByKeyAndArgs_invalid(
      Class<?> key, String subKey, Object[] args, String errMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_class_getByKeyAndArgs_invalid_suppressed")
  public void class_getByKeyAndArgs_invalid_suppressed(
      Class<?> key, String subKey, Object[] args, String expected) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(key, subKey, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKeyAndArgs_nullArgs() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "with_args", (Object[]) null);

    // Assert
    assertThat(result).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void class_getByKeyAndArgs_emptyArgs() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "with_args", new Object[] {});

    // Assert
    assertThat(result).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void class_getByKeyAndArgs_found() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "with_args", "First", 200L);

    // Assert
    assertThat(result).isEqualTo("A First with 200.");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.get(Dummy.class, "not_found", "with a value");

    // Assert
    assertThat(result).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'Dummy.not_found' and locale 'en'.");
  }

  @Test
  public void context_string() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.context("Dummy");

    // Assert
    assertThat(result).isNotNull();

    // Act
    var call = result.get("attribute");

    // Assert
    assertThat(call).isEqualTo("Attribute");
  }

  @Test
  public void context_class() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.context(Dummy.class);

    // Assert
    assertThat(result).isNotNull();

    // Act
    var call = result.get("attribute");

    // Assert
    assertThat(call).isEqualTo("Attribute");
  }

  @Test
  public void defaults() {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThat(underTest.getLocale()).isEqualTo(Locale.ENGLISH);
  }

  @Test
  public void getFormats() {
    // Arrange
    var locale = Locale.US;
    var config = NilsConfig.init(new StaticAdapterConfig());
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.getFormats();
    // Assert
    assertThat(result).isNotNull();

    // Act
    var result2 = underTest.getFormats();

    // Assert
    assertThat(result2).isEqualTo(result2);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_context_string_invalid")
  public void context_string_invalid_exception(String context, String errorMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.context(context))
        .isInstanceOf(NilsException.class)
        .hasMessage(errorMsg);
  }

  @ParameterizedTest
  @MethodSource(DATA_PROVIDER + "#source_context_string_invalid")
  public void context_string_invalid_suppressed(String context, String errorMsg) {
    // Arrange
    var locale = Locale.ENGLISH;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.context(context);

    // Assert
    assertThat(result).isEqualTo(underTest);
  }

  @Test
  public void context_class_invalid_exception() {
    // Arrange
    var locale = Locale.ENGLISH;
    Class<?> context = null;
    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(false);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.context(context))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-003: Parameter 'context' cannot be null.");
  }

  @Test
  public void context_class_invalid_suppressed() {
    // Arrange
    var locale = Locale.ENGLISH;
    Class<?> context = null;

    var config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(true);
    var underTest = new NLSImpl(new StaticAdapter(), config, locale);

    // Act
    var result = underTest.context(context);

    // Assert
    assertThat(result).isEqualTo(underTest);
  }
}
