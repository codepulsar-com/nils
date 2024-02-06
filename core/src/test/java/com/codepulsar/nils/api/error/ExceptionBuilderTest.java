package com.codepulsar.nils.api.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codepulsar.nils.api.error.ErrorType;
import com.codepulsar.nils.api.error.ExceptionBuilder;

public class ExceptionBuilderTest {

  private static final ErrorType NO_MSG = new ErrorType("NILS-0");
  private static final ErrorType WITH_MSG = new ErrorType("NILS-1", "A message %s");

  @ParameterizedTest
  @MethodSource("checkValidCase_provider")
  public void checkValidCase(ExceptionBuilder underTest, String errorMsg) {
    // Act
    var result = underTest.go();

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getMessage()).isEqualTo(errorMsg);
  }

  @ParameterizedTest
  @MethodSource("checkInvalidCase_provider")
  public void checkInvalidCase(ExceptionBuilder underTest, String errorMsg) {
    // Act / Assert
    assertThatThrownBy(() -> underTest.go())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(errorMsg);
  }

  public static Stream<Arguments> checkValidCase_provider() {
    return Stream.of(
        arguments(
            new ExceptionBuilder(NO_MSG).message("This is a message."),
            "NILS-0: This is a message."),
        arguments(
            new ExceptionBuilder(NO_MSG).message("This is %s and %s.").args("arg1", "arg2"),
            "NILS-0: This is arg1 and arg2."),
        arguments(
            new ExceptionBuilder(NO_MSG)
                .message("This is %s and %s.")
                .args("arg1", "arg2")
                .cause(new Exception("test")),
            "NILS-0: This is arg1 and arg2."),
        arguments(
            new ExceptionBuilder(NO_MSG).message("This is a message.").cause(new Exception("test")),
            "NILS-0: This is a message."),
        arguments(new ExceptionBuilder(WITH_MSG), "NILS-1: A message %s"),
        arguments(new ExceptionBuilder(WITH_MSG).args("arg1", "arg2"), "NILS-1: A message arg1"),
        arguments(
            new ExceptionBuilder(WITH_MSG).message("This is a message."),
            "NILS-1: This is a message."),
        arguments(
            new ExceptionBuilder(WITH_MSG).message("This is %s and %s.").args("arg1", "arg2"),
            "NILS-1: This is arg1 and arg2."),
        arguments(
            new ExceptionBuilder(WITH_MSG).cause(new Exception("test")), "NILS-1: A message %s"),
        arguments(
            new ExceptionBuilder(WITH_MSG)
                .message("This is %s and %s.")
                .args("arg1", "arg2")
                .cause(new Exception("test")),
            "NILS-1: This is arg1 and arg2."),
        arguments(
            new ExceptionBuilder(WITH_MSG)
                .message("This is a message.")
                .cause(new Exception("test")),
            "NILS-1: This is a message."));
  }

  public static Stream<Arguments> checkInvalidCase_provider() {
    return Stream.of(
        arguments(
            new ExceptionBuilder(NO_MSG),
            "Not both 'msg' and errorType.defaultMessage can be null or empty."),
        arguments(
            new ExceptionBuilder(NO_MSG).args("arg1", "arg2"),
            "Not both 'msg' and errorType.defaultMessage can be null or empty."),
        arguments(
            new ExceptionBuilder(NO_MSG).cause(new Exception("test")),
            "Not both 'msg' and errorType.defaultMessage can be null or empty."));
  }
}
