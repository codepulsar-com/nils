package com.codepulsar.nils.api.error;

import java.util.Objects;

/**
 * A <strong>ErrorType</strong> configures a type of error.
 *
 * <p>Each ErrorType should have its unique error code in the system. Contains all errors that can
 */
public class ErrorType {

  private final String errCode;

  private String defaultMessage;

  /**
   * Create a new {@link ErrorType}.
   *
   * @param errCode A error code.
   */
  public ErrorType(String errCode) {
    this.errCode = Objects.requireNonNull(errCode);
  }

  public ErrorType(String errCode, String defaultMessage) {
    this.errCode = Objects.requireNonNull(errCode);
    this.defaultMessage = defaultMessage;
  }

  /**
   * Gets the error code of these error type.
   *
   * @return A String
   */
  public String getErrCode() {
    return errCode;
  }
  /**
   * Gets the default message of these error type.
   *
   * @return A String or <code>null</code>.
   */
  public String getDefaultMessage() {
    return defaultMessage;
  }
  /**
   * Transfer these error type into an exception.
   *
   * @return An {@link ExceptionBuilder} object.
   */
  public ExceptionBuilder asException() {
    return new ExceptionBuilder(this);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "[errCode="
        + errCode
        + ", defaultMessage="
        + defaultMessage
        + "]";
  }
}
