package com.codepulsar.nils.core.adapter.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MapTranslationRetrieverTest {
  @Test
  public void ctor() {
    // Act
    var underTest = new MapTranslationRetriever(Map.of());

    // Assert
    assertThat(underTest).isNotNull();
  }

  @Test
  public void ctor_null() {
    // Act / Assert
    assertThatThrownBy(() -> new MapTranslationRetriever(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Parameter 'translations' cannot be null.");
  }

  @ParameterizedTest
  @MethodSource("source_retrieve")
  public void retrieve(Map<String, Object> source, String key, Optional<String> expected) {
    // Arrange
    var underTest = new MapTranslationRetriever(source);

    // Act
    var value = underTest.retrieve(key);
    // Assert
    assertThat(value).isEqualTo(expected);
  }

  private static Stream<Arguments> source_retrieve() {
    return Stream.of(
        arguments(Map.of(), "test", Optional.empty()),
        arguments(Map.of(), "test.deep.test", Optional.empty()),
        arguments(Map.of("test", "test"), null, Optional.empty()),
        arguments(Map.of("test", "test"), "", Optional.empty()),
        arguments(Map.of("test", "test"), " ", Optional.empty()),
        arguments(Map.of("test", "result"), "test", Optional.of("result")),
        arguments(Map.of("test", Map.of("test2", "result")), "test.test2", Optional.of("result")),
        arguments(
            Map.of("test", Map.of("test2", Map.of("test3", "result"))),
            "test.test2.test3",
            Optional.of("result")),
        arguments(
            Map.of("test", Map.of("test2", Map.of("test3", "result"))),
            "test.test2.test4",
            Optional.empty()));
  }
}
