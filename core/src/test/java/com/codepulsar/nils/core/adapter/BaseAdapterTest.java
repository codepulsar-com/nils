package com.codepulsar.nils.core.adapter;

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
import com.codepulsar.nils.core.NilsException;
import com.codepulsar.nils.core.testadapter.StaticAdapter;
import com.codepulsar.nils.core.testadapter.StaticAdapterConfig;

public class BaseAdapterTest {

  @Test
  public void resourceBundleAdapter_nullLocale() {
    // Arrange
    Locale locale = null;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    // Act / Assert
    assertThatThrownBy(() -> new StaticAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'locale' cannot be null.");
  }

  @Test
  public void resourceBundleAdapter_nullConfig() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = null;
    // Act / Assert
    assertThatThrownBy(() -> new StaticAdapter(config, locale))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'config' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource("string_getByKey_invalidSource")
  public void string_getByKey_invalid(String key, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void string_getByKey_found() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act
    String value = underTest.get("simple");

    // Assert
    assertThat(value).isEqualTo("A simple translation");
  }

  @Test
  public void string_getByKey_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapePattern(">>{0}<<");
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo(">>not.found<<");
  }

  @Test
  public void string_getByKey_notFound_escaping_changed2() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapePattern("Missing: {0}");
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get("not.found");

    // Assert
    assertThat(value).isEqualTo("Missing: not.found");
  }

  @Test
  public void string_getByKey_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapeIfMissing(false);
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isFalse();
    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("Could not find translation for key 'not.found'.");
  }

  @ParameterizedTest
  @MethodSource("string_getByKeyAndArgs_invalidSource")
  public void string_getByKeyAndArgs_invalid(String key, Object[] args, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, args))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void string_getByKeyAndArgs_nullArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
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
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
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
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get("with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get("not.found", "with a value");

    // Assert
    assertThat(value).isEqualTo("[not.found]");
  }

  @Test
  public void string_getByKeyAndArgs_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapeIfMissing(false);
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isFalse();
    // Act / Assert
    assertThatThrownBy(() -> underTest.get("not.found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("Could not find translation for key 'not.found'.");
  }

  @ParameterizedTest
  @MethodSource("class_getByKey_invalidSource")
  public void class_getByKey_invalid(Class<?> key, String subKey, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void class_getByKey_found() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act
    String value = underTest.get(Dummy.class, "attribute");

    // Assert
    assertThat(value).isEqualTo("Attribute");
  }

  @Test
  public void class_getByKey_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKey_notFound_escaping_changed() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapePattern(">>{0}<<");
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get(Dummy.class, "not_found");

    // Assert
    assertThat(value).isEqualTo(">>Dummy.not_found<<");
  }

  @Test
  public void class_getByKey_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapeIfMissing(false);
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isFalse();
    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found"))
        .isInstanceOf(NilsException.class)
        .hasMessage("Could not find translation for key 'Dummy.not_found'.");
  }

  @ParameterizedTest
  @MethodSource("class_getByKeyAndArgs_invalidSource")
  public void class_getByKeyAndArgs_invalid(
      Class<?> key, String subKey, Object[] args, String errMsg) {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act / Assert
    assertThatThrownBy(() -> underTest.get(key, subKey, args))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errMsg);
  }

  @Test
  public void class_getByKeyAndArgs_nullArgs() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
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
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
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
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get(Dummy.class, "with_args", "First", 200L);

    // Assert
    assertThat(value).isEqualTo("A First with 200.");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_escaping() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isTrue();
    // Act
    String value = underTest.get(Dummy.class, "not_found", "with a value");

    // Assert
    assertThat(value).isEqualTo("[Dummy.not_found]");
  }

  @Test
  public void class_getByKeyAndArgs_notFound_exception() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig()).escapeIfMissing(false);
    BaseAdapter underTest = new StaticAdapter(config, locale);
    assertThat(config.isEscapeIfMissing()).isFalse();
    // Act / Assert
    assertThatThrownBy(() -> underTest.get(Dummy.class, "not_found", "with a value"))
        .isInstanceOf(NilsException.class)
        .hasMessage("Could not find translation for key 'Dummy.not_found'.");
  }

  @Test
  public void defaults() {
    // Arrange
    Locale locale = Locale.ENGLISH;
    NilsConfig config = NilsConfig.init(new StaticAdapterConfig());
    BaseAdapter underTest = new StaticAdapter(config, locale);

    // Act / Assert
    assertThat(underTest.getLocale()).isEqualTo(Locale.ENGLISH);
    assertThat(underTest.getConfig()).isEqualTo(config);
  }

  private static Stream<Arguments> string_getByKey_invalidSource() {
    return Stream.of(
        arguments(null, "Parameter 'key' cannot be null."),
        arguments("", "Parameter 'key' cannot be empty or blank."),
        arguments(" ", "Parameter 'key' cannot be empty or blank."));
  }

  private static Stream<Arguments> string_getByKeyAndArgs_invalidSource() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "Parameter 'key' cannot be null."),
        arguments("", new Object[] {"Value"}, "Parameter 'key' cannot be empty or blank."),
        arguments(" ", new Object[] {"Value"}, "Parameter 'key' cannot be empty or blank."));
  }

  private static Stream<Arguments> class_getByKey_invalidSource() {
    return Stream.of(
        arguments(null, "attr", "Parameter 'key' cannot be null."),
        arguments(Dummy.class, null, "Parameter 'subKey' cannot be null."),
        arguments(Dummy.class, "", "Parameter 'subKey' cannot be empty or blank."),
        arguments(Dummy.class, " ", "Parameter 'subKey' cannot be empty or blank."));
  }

  private static Stream<Arguments> class_getByKeyAndArgs_invalidSource() {
    return Stream.of(
        arguments(null, "attr", new Object[] {"Value"}, "Parameter 'key' cannot be null."),
        arguments(Dummy.class, null, new Object[] {"Value"}, "Parameter 'subKey' cannot be null."),
        arguments(
            Dummy.class,
            "",
            new Object[] {"Value"},
            "Parameter 'subKey' cannot be empty or blank."),
        arguments(
            Dummy.class,
            " ",
            new Object[] {"Value"},
            "Parameter 'subKey' cannot be empty or blank."));
  }

  private static class Dummy {
    // Dummy class
  }
}
