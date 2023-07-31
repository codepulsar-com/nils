package com.codepulsar.nils.core.testdata;

import java.util.function.Function;

public class ErrorMessageBuilder {

  private ErrorMessageBuilder() {
    super();
  }

  public static final Function<String, String> IAE_PARAMETER_NULL =
      (s) -> String.format("Parameter '%s' cannot be null.", s);

  public static final Function<String, String> IAE_PARAMETER_BLANK =
      (s) -> String.format("Parameter '%s' cannot be empty or blank.", s);
}
