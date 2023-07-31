package com.codepulsar.nils.core.impl;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.codepulsar.nils.core.testdata.Dummy;

public class ContextNLSImplTestDataProvider {

  public static Stream<Arguments> source_string_getByKey_invalid() {
    return NLSImplTestDataProvider.source_string_getByKey_invalid();
  }

  public static Stream<Arguments> source_string_getByKey_invalid_suppressed() {
    return Stream.of(
        arguments(null, "[context.null]"),
        arguments("", "[context.]"),
        arguments(" ", "[context. ]"));
  }

  public static Stream<Arguments> source_string_getByKeyAndArgs_invalid() {
    return NLSImplTestDataProvider.source_string_getByKeyAndArgs_invalid();
  }

  public static Stream<Arguments> source_string_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, new Object[] {"Value"}, "[context.null]"),
        arguments("", new Object[] {"Value"}, "[context.]"),
        arguments(" ", new Object[] {"Value"}, "[context. ]"));
  }

  public static Stream<Arguments> source_class_getByKey_invalid() {
    return NLSImplTestDataProvider.source_class_getByKey_invalid();
  }

  public static Stream<Arguments> source_class_getByKey_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", "[context.null.attr]"),
        arguments(Dummy.class, null, "[context.Dummy.null]"),
        arguments(Dummy.class, "", "[context.Dummy.]"),
        arguments(Dummy.class, " ", "[context.Dummy. ]"));
  }

  public static Stream<Arguments> source_class_getByKeyAndArgs_invalid() {
    return NLSImplTestDataProvider.source_class_getByKeyAndArgs_invalid();
  }

  public static Stream<Arguments> source_class_getByKeyAndArgs_invalid_suppressed() {
    return Stream.of(
        arguments(null, "attr", new Object[] {"Value"}, "[context.null.attr]"),
        arguments(Dummy.class, null, new Object[] {"Value"}, "[context.Dummy.null]"),
        arguments(Dummy.class, "", new Object[] {"Value"}, "[context.Dummy.]"),
        arguments(Dummy.class, " ", new Object[] {"Value"}, "[context.Dummy. ]"));
  }

  public static Stream<Arguments> source_context_string_invalid() {
    return NLSImplTestDataProvider.source_context_string_invalid();
  }
}
