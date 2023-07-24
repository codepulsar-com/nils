package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.ALL;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.INCLUDE_LOOP_DETECTED;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.MISSING_TRANSLATION;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.Adapter;
import com.codepulsar.nils.core.error.NilsException;
import com.codepulsar.nils.core.handler.ClassPrefixResolver;
import com.codepulsar.nils.core.testadapter.StaticAdapter;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;
import com.codepulsar.nils.core.testdata.Dummy;

public class NLSImplTest {
  @Test
  public void constructor_nullAdapter() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
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
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    Adapter adapter = new StaticAdapter(config.getAdapterConfig(), locale);
    // Act / Assert
    assertThatThrownBy(() -> new NLSImpl(adapter, config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void constructor_nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = null;
    Adapter adapter = new StaticAdapter(new StaticAdapterConfig(), locale);
    // Act / Assert
    assertThatThrownBy(() -> new NLSImpl(adapter, config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource("source_string_getByKey_invalid")
  public void string_getByKey_invalid(String key, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource("source_string_getByKey_invalid_suppressed")
  public void string_getByKey_invalid_suppressed(String key, String expected) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String result = underTest.get(key);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKey_found() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("A simple translation");
  }

  @Test
  public void string_getByKey_notFound_escaping_errorType_all() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(ALL);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKey_notFound_escaping_errorType_MISSING_TRANSLATION() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig())
            .escapePattern(">>{0}<<")
            .suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo(">>not.found<<");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed2() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig())
            .escapePattern("Missing: {0}")
            .suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("Missing: not.found");
  }

  @Test
  public void string_getByKey_notFound_exception_errorType_none() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @Test
  public void string_getByKey_notFound_exception_errorType_Notset() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(INCLUDE_LOOP_DETECTED);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @ParameterizedTest
  @MethodSource("source_string_getByKeyAndArgs_invalid")
  public void string_getByKeyAndArgs_invalid(String key, Object[] args, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource("source_string_getByKeyAndArgs_invalid_suppressed")
  public void string_getByKeyAndArgs_invalid_suppressed(
      String key, Object[] args, String expected) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String result = underTest.get(key, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void string_getByKeyAndArgs_nullArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("with_args", (Object[]) null);

    // Assert
    assertThat(value).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void string_getByKeyAndArgs_emptyArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("with_args", new Object[] {});

    // Assert
    assertThat(value).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void string_getByKeyAndArgs_found() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(ALL);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);
    // Act
    String value = underTest.get("not.found", "with a value");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: Could not find a translation for key 'not.found' and locale 'en'.");
  }

  @ParameterizedTest
  @MethodSource("source_class_getByKey_invalid")
  public void class_getByKey_invalid(Class<?> key, String subKey, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource("source_class_getByKey_invalid_suppressed")
  public void class_getByKey_invalid_suppressed(Class<?> key, String subKey, String expected) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String result = underTest.get(key, subKey);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKey_foundSimpleClassnameResolver() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    assertThat(config.getClassPrefixResolver()).isEqualTo(ClassPrefixResolver.SIMPLE_CLASSNAME);

    // Act
    String value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute");
  }

  @Test
  public void class_getByKey_foundFqnClassnameResolver() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig())
            .classPrefixResolver(ClassPrefixResolver.FQN_CLASSNAME);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute (FQN)");
  }

  @Test
  public void class_getByKey_foundOwnResolver() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).classPrefixResolver(c -> "StaticClassResolver");
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute (StaticResolver)");
  }

  @Test
  public void class_getByKey_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKey_notFound_escaping_changed() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig())
            .escapePattern(">>{0}<<")
            .suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo(">>Dummy.not_found<<");
  }

  @Test
  public void class_getByKey_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(INCLUDE_LOOP_DETECTED);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'Dummy.not_found' and locale 'en'.");
  }

  @ParameterizedTest
  @MethodSource("source_class_getByKeyAndArgs_invalid")
  public void class_getByKeyAndArgs_invalid(
      Class<?> key, String subKey, Object[] args, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey, args))
        .isInstanceOf(NilsException.class)
        .hasMessage(errMsg);
  }

  @ParameterizedTest
  @MethodSource("source_class_getByKeyAndArgs_invalid_suppressed")
  public void class_getByKeyAndArgs_invalid_suppressed(
      Class<?> key, String subKey, Object[] args, String expected) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NLS_PARAMETER_CHECK);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String result = underTest.get(key, subKey, args);

    // Assert
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void class_getByKeyAndArgs_nullArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "with_args", (Object[]) null);

    // Assert
    assertThat(value).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void class_getByKeyAndArgs_emptyArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "with_args", new Object[] {});

    // Assert
    assertThat(value).isEqualTo("A {0} with {1}.");
  }

  @Test
  public void class_getByKeyAndArgs_found() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config =
        NilsConfig.init(new StaticAdapterConfig()).suppressErrors(MISSING_TRANSLATION);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act
    String value = underTest.get(Dummy.class, "not_found", "with a value");

    // Assert
    assertThat(value).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).suppressErrors(NONE);
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage(
            "NILS-001: Could not find a translation for key 'Dummy.not_found' and locale 'en'.");
  }

  @Test
  public void defaults() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    NLSImpl underTest =
        new NLSImpl(new StaticAdapter(config.getAdapterConfig(), locale), config, locale);

    // Act / Assert
    assertThat(underTest.getLocale()).isEqualTo(Locale.ENGLISH);
  }

  private static Stream<Arguments> source_string_getByKey_invalid() {
    return Stream.of(
        arguments(null, "NILS-003: Parameter 'key' cannot be null."),
        arguments("", "NILS-003: Parameter 'key' cannot be empty or blank."),
        arguments(" ", "NILS-003: Parameter 'key' cannot be empty or blank."));
  }

  private static Stream<Arguments> source_string_getByKey_invalid_suppressed() {
    return Stream.of(arguments(null, "[null]"), arguments("", "[]"), arguments(" ", "[ ]"));
  }

  private static Stream<Arguments> source_string_getByKeyAndArgs_invalid() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be null."),
        arguments(
            "", new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be empty or blank."),
        arguments(
            " ", new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be empty or blank."));
  }

  private static Stream<Arguments> source_string_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "[null]"),
        arguments("", new Object[] {"Value"}, "[]"),
        arguments(" ", new Object[] {"Value"}, "[ ]"));
  }

  private static Stream<Arguments> source_class_getByKey_invalid() {
    return Stream.of(
        arguments(null, "attr", "NILS-003: Parameter 'key' cannot be null."),
        arguments(Dummy.class, null, "NILS-003: Parameter 'subKey' cannot be null."),
        arguments(Dummy.class, "", "NILS-003: Parameter 'subKey' cannot be empty or blank."),
        arguments(Dummy.class, " ", "NILS-003: Parameter 'subKey' cannot be empty or blank."));
  }

  private static Stream<Arguments> source_class_getByKey_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", "[null.attr]"),
        arguments(Dummy.class, null, "[Dummy.null]"),
        arguments(Dummy.class, "", "[Dummy.]"),
        arguments(Dummy.class, " ", "[Dummy. ]"));
  }

  private static Stream<Arguments> source_class_getByKeyAndArgs_invalid() {
    return Stream.of(
        arguments(
            null, "attr", new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be null."),
        arguments(
            Dummy.class,
            null,
            new Object[] {"Value"},
            "NILS-003: Parameter 'subKey' cannot be null."),
        arguments(
            Dummy.class,
            "",
            new Object[] {"Value"},
            "NILS-003: Parameter 'subKey' cannot be empty or blank."),
        arguments(
            Dummy.class,
            " ",
            new Object[] {"Value"},
            "NILS-003: Parameter 'subKey' cannot be empty or blank."));
  }

  private static Stream<Arguments> source_class_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", new Object[] {"Value"}, "[null.attr]"),
        arguments(Dummy.class, null, new Object[] {"Value"}, "[Dummy.null]"),
        arguments(Dummy.class, "", new Object[] {"Value"}, "[Dummy.]"),
        arguments(Dummy.class, " ", new Object[] {"Value"}, "[Dummy. ]"));
  }
}
