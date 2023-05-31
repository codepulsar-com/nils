package com.codepulsar.nils.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.NilsConfig;
import com.codepulsar.nils.NilsException;
import com.codepulsar.nils.testadapter.StaticAdapter;
import com.codepulsar.nils.testadapter.StaticAdapterConfig;

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
  @MethodSource("getByKey_invalidSource")
  public void getByKey_invalid(String key, String errMsg) {
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
  public void getByKey_found() {
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
  public void getByKey_notFound_escaping() {
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
  public void getByKey_notFound_escaping_changed() {
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
  public void getByKey_notFound_escaping_changed2() {
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
  public void getByKey_notFound_exception() {
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
  @MethodSource("getByKeyAndArgs_invalidSource")
  public void getByKeyAndArgs_invalid(String key, Object[] args, String errMsg) {
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
  public void getByKeyAndArgs_nullArgs() {
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
  public void getByKeyAndArgs_emptyArgs() {
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
  public void getByKeyAndArgs_found() {
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
  public void getByKeyAndArgs_notFound_escaping() {
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
  public void getByKeyAndArgs_notFound_exception() {
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

  private static Stream<Arguments> getByKey_invalidSource() {
    return Stream.of(
        arguments(null, "Parameter 'key' cannot be null."),
        arguments("", "Parameter 'key' cannot be empty or blank."),
        arguments(" ", "Parameter 'key' cannot be empty or blank."));
  }

  private static Stream<Arguments> getByKeyAndArgs_invalidSource() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "Parameter 'key' cannot be null."),
        arguments("", new Object[] {"Value"}, "Parameter 'key' cannot be empty or blank."),
        arguments(" ", new Object[] {"Value"}, "Parameter 'key' cannot be empty or blank."));
  }
}
