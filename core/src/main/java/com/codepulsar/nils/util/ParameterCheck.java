package com.codepulsar.nils.util;

public class ParameterCheck {
  private ParameterCheck() {
    super();
  }

  public static <O> O notNull(O value, String parameterName) {
    if (value == null) {
      throw new IllegalArgumentException(
          String.format("Parameter '%s' cannot be null.", parameterName));
    }
    return value;
  }

  public static String notNullEmptyOrBlank(String value, String parameterName) {
    notNull(value, parameterName);
    if (value.isEmpty() || value.isBlank()) {
      throw new IllegalArgumentException(
          String.format("Parameter '%s' cannot be empty or blank.", parameterName));
    }
    return value;
  }
}
