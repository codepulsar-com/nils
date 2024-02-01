package com.codepulsar.nils.core.error;

import java.util.Objects;

/** Generic RuntimeException thrown by NILS. */
public class NilsException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 2L;

  private final ErrorType errType;

  /**
   * Create a new {@link NilsException}
   *
   * @param errType The {@link ErrorType}.
   * @param message The message text
   */
  public NilsException(ErrorType errType, String message) {
    super(Objects.requireNonNull(errType).getErrCode() + ": " + message);
    this.errType = errType;
  }

  public NilsException(ErrorType errType, String message, Object... args) {
    this(errType, String.format(message, args));
  }

  /**
   * Create a new {@link NilsException}
   *
   * @param errType The {@link ErrorType}.
   * @param message The message text
   * @param cause The root cause
   */
  public NilsException(ErrorType errType, String message, Throwable cause) {
    super(Objects.requireNonNull(errType).getErrCode() + ": " + message, cause);
    this.errType = errType;
  }

  public NilsException(ErrorType errType, String message, Throwable cause, Object... args) {
    super(
        Objects.requireNonNull(errType).getErrCode() + ": " + String.format(message, args), cause);
    this.errType = errType;
  }

  /**
   * Gets the {@link ErrorType}.
   *
   * @return A {@link ErrorType}
   */
  public ErrorType getErrorType() {
    return errType;
  }
}
