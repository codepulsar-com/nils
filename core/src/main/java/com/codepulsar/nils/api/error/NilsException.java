package com.codepulsar.nils.api.error;

import java.util.Objects;

/** Generic Exception thrown by NILS if a configuration or the usage is erroneous. */
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

  /**
   * Create a new {@link NilsException} based on the parameters. The message and the args will be
   * formatted using <code>String.format()</code>.
   *
   * @param errType The {@link ErrorType}
   * @param message The message text
   * @param args The arguments for the message
   */
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

  /**
   * Create a new {@link NilsException} based on the parameters. The message and the args will be
   * formatted using <code>String.format()</code>.
   *
   * @param errType The {@link ErrorType}
   * @param message The message text
   * @param cause The root cause
   * @param args The arguments for the message
   */
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
