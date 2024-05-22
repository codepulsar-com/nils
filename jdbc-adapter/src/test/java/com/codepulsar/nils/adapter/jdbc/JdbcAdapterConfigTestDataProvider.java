package com.codepulsar.nils.adapter.jdbc;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class JdbcAdapterConfigTestDataProvider {

  public static Stream<Arguments> string_invalid() {
    return Stream.of(
        arguments(null, "NILS-004: Parameter '%s' cannot be null."),
        arguments("", "NILS-004: Parameter '%s' cannot be empty or blank."),
        arguments(" ", "NILS-004: Parameter '%s' cannot be empty or blank."));
  }

  public static Stream<Arguments> string_valid() {
    return Stream.of(arguments("aaa", "aaa"), arguments(" a d b ", "a d b"));
  }
}
