package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.testdata.ErrorMessageBuilder.IAE_PARAMETER_BLANK;
import static com.codepulsar.nils.core.testdata.ErrorMessageBuilder.IAE_PARAMETER_NULL;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.codepulsar.nils.core.testdata.Dummy;

@SuppressWarnings("exports")
public class NilsFactoryImplTestDataProvider {

  public static Stream<Arguments> nlsFromLang_invalid() {
    return Stream.of(
        arguments(null, IAE_PARAMETER_NULL.apply("lang")),
        arguments("", IAE_PARAMETER_BLANK.apply("lang")),
        arguments(" ", IAE_PARAMETER_BLANK.apply("lang")));
  }

  public static Stream<Arguments> nlsWithContext_string_invalid() {
    return Stream.of(
        arguments(null, IAE_PARAMETER_NULL.apply("context")),
        arguments("", IAE_PARAMETER_BLANK.apply("context")),
        arguments(" ", IAE_PARAMETER_BLANK.apply("context")));
  }

  public static Stream<Arguments> nlsWithContext_fromLang_string_invalid() {
    return Stream.of(
        arguments(null, null, IAE_PARAMETER_NULL.apply("context")),
        arguments("de-DE", null, IAE_PARAMETER_NULL.apply("context")),
        arguments("de-DE", "", IAE_PARAMETER_BLANK.apply("context")),
        arguments("de-DE", " ", IAE_PARAMETER_BLANK.apply("context")),
        arguments(null, "context", IAE_PARAMETER_NULL.apply("lang")),
        arguments("", "context", IAE_PARAMETER_BLANK.apply("lang")),
        arguments(" ", "context", IAE_PARAMETER_BLANK.apply("lang")));
  }

  public static Stream<Arguments> nlsWithContext_fromLang_class_invalid() {
    return Stream.of(
        arguments(null, null, IAE_PARAMETER_NULL.apply("context")),
        arguments("de-DE", null, IAE_PARAMETER_NULL.apply("context")),
        arguments(null, Dummy.class, IAE_PARAMETER_NULL.apply("lang")),
        arguments("", Dummy.class, IAE_PARAMETER_BLANK.apply("lang")),
        arguments(" ", Dummy.class, IAE_PARAMETER_BLANK.apply("lang")));
  }

  public static Stream<Arguments> nlsWithContext_fromLocale_string_invalid() {
    return Stream.of(
        arguments(null, null, IAE_PARAMETER_NULL.apply("context")),
        arguments(Locale.GERMANY, null, IAE_PARAMETER_NULL.apply("context")),
        arguments(Locale.ITALY, "", IAE_PARAMETER_BLANK.apply("context")),
        arguments(Locale.CANADA, " ", IAE_PARAMETER_BLANK.apply("context")),
        arguments(null, "context", IAE_PARAMETER_NULL.apply("locale")));
  }

  public static Stream<Arguments> nlsWithContext_fromLocale_class_invalid() {
    return Stream.of(
        arguments(null, null, IAE_PARAMETER_NULL.apply("context")),
        arguments(Locale.FRANCE, null, IAE_PARAMETER_NULL.apply("context")),
        arguments(null, Dummy.class, IAE_PARAMETER_NULL.apply("locale")));
  }
}
