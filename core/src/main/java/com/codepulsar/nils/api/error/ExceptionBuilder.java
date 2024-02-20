package com.codepulsar.nils.api.error;

import java.util.Objects;
/**
 * Helper class building a {@link NilsException} for an {@link ErrorType}.
 *
 * <pre>
 * TYPE.asException().args("Argument").go();
 * </pre>
 */
public class ExceptionBuilder {

  private final ErrorType errorType;
  private String msg = null;
  private Object[] args = null;
  private Throwable cause;

  ExceptionBuilder(ErrorType errorType) {
    this.errorType = Objects.requireNonNull(errorType);
  }
  /**
   * Set the message. If not set the default message of the {@link ErrorType} is used.
   *
   * <p>For the message <code>String.format()</code> is used if arguments are passed in.
   *
   * @param message The message
   * @return The {@linkplain ExceptionBuilder}
   */
  public ExceptionBuilder message(String message) {
    this.msg = Objects.requireNonNull(message);
    return this;
  }

  /**
   * Set the arguments for the message.
   *
   * <p>For the message <code>String.format()</code> is used if arguments are passed in.
   *
   * @param args The arguments.
   * @return The {@linkplain ExceptionBuilder}
   */
  public ExceptionBuilder args(Object... args) {
    this.args = args;
    return this;
  }
  /**
   * Set a {@link Throwable} for the exception.
   *
   * @param cause A {@link Throwable}
   * @return The {@linkplain ExceptionBuilder}
   */
  public ExceptionBuilder cause(Throwable cause) {
    this.cause = cause;
    return this;
  }
  /**
   * Create a {@link NilsException} based on the given values.
   *
   * @return A {@link NilsException}
   */
  public NilsException go() {
    if ((msg == null || msg.isBlank())
        && (errorType.getDefaultMessage() == null || errorType.getDefaultMessage().isBlank())) {
      throw new IllegalArgumentException(
          "Not both 'msg' and errorType.defaultMessage can be null or empty.");
    }

    var message = msg != null ? msg : errorType.getDefaultMessage();
    if (cause == null) {
      if (args == null || args.length == 0) {
        return new NilsException(errorType, message, cause);
      }
      return new NilsException(errorType, message, cause, args);
    }
    if (args == null || args.length == 0) {
      return new NilsException(errorType, message);
    }
    return new NilsException(errorType, message, args);
  }
}
