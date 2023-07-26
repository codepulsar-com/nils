package com.codepulsar.nils.core.impl;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.codepulsar.nils.core.testdata.Dummy;

public class NLSImplTestDataProvider {

  public static Stream<Arguments> source_string_getByKey_invalid() {
    return Stream.of(
        arguments(null, "NILS-003: Parameter 'key' cannot be null."),
        arguments("", "NILS-003: Parameter 'key' cannot be empty or blank."),
        arguments(" ", "NILS-003: Parameter 'key' cannot be empty or blank."));
  }

  public static Stream<Arguments> source_string_getByKey_invalid_suppressed() {
    return Stream.of(arguments(null, "[null]"), arguments("", "[]"), arguments(" ", "[ ]"));
  }

  public static Stream<Arguments> source_string_getByKeyAndArgs_invalid() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be null."),
        arguments(
            "", new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be empty or blank."),
        arguments(
            " ", new Object[] {"Value"}, "NILS-003: Parameter 'key' cannot be empty or blank."));
  }

  public static Stream<Arguments> source_string_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "[null]"),
        arguments("", new Object[] {"Value"}, "[]"),
        arguments(" ", new Object[] {"Value"}, "[ ]"));
  }

  public static Stream<Arguments> source_class_getByKey_invalid() {
    return Stream.of(
        arguments(null, "attr", "NILS-003: Parameter 'key' cannot be null."),
        arguments(Dummy.class, null, "NILS-003: Parameter 'subKey' cannot be null."),
        arguments(Dummy.class, "", "NILS-003: Parameter 'subKey' cannot be empty or blank."),
        arguments(Dummy.class, " ", "NILS-003: Parameter 'subKey' cannot be empty or blank."));
  }

  public static Stream<Arguments> source_class_getByKey_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", "[null.attr]"),
        arguments(Dummy.class, null, "[Dummy.null]"),
        arguments(Dummy.class, "", "[Dummy.]"),
        arguments(Dummy.class, " ", "[Dummy. ]"));
  }

  public static Stream<Arguments> source_class_getByKeyAndArgs_invalid() {
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

  public static Stream<Arguments> source_class_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", new Object[] {"Value"}, "[null.attr]"),
        arguments(Dummy.class, null, new Object[] {"Value"}, "[Dummy.null]"),
        arguments(Dummy.class, "", new Object[] {"Value"}, "[Dummy.]"),
        arguments(Dummy.class, " ", new Object[] {"Value"}, "[Dummy. ]"));
  }
}
