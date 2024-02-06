package com.codepulsar.nils.core.util;

import java.util.function.Function;

import com.codepulsar.nils.api.error.ErrorType;
import com.codepulsar.nils.api.error.NilsConfigException;
import com.codepulsar.nils.api.error.NilsException;

public class ParameterCheck {

  public static final Function<String, ? extends RuntimeException> ILLEGAL_ARGUMENT =
      s -> new IllegalArgumentException(s);

  public static final Function<String, ? extends RuntimeException> NILS_CONFIG =
      s -> new NilsConfigException(s);

  private ParameterCheck() {
    super();
  }

  public static <O> O notNull(O value, String parameterName) {
    return notNull(value, parameterName, ILLEGAL_ARGUMENT);
  }

  public static <O> O notNull(
      O value,
      String parameterName,
      Function<String, ? extends RuntimeException> exceptionBuilder) {
    if (value == null) {
      throw exceptionBuilder.apply(String.format("Parameter '%s' cannot be null.", parameterName));
    }
    return value;
  }

  public static String notNullEmptyOrBlank(String value, String parameterName) {
    return notNullEmptyOrBlank(value, parameterName, ILLEGAL_ARGUMENT);
  }

  public static String notNullEmptyOrBlank(
      String value,
      String parameterName,
      Function<String, ? extends RuntimeException> exceptionBuilder) {
    notNull(value, parameterName, exceptionBuilder);
    if (value.isEmpty() || value.isBlank()) {
      throw exceptionBuilder.apply(
          String.format("Parameter '%s' cannot be empty or blank.", parameterName));
    }
    return value;
  }

  public static Function<String, ? extends RuntimeException> nilsException(ErrorType type) {
    return s -> new NilsException(type, s);
  }
}
