package com.codepulsar.nils.core;
/** Generic RuntimeException thrown by NILS. */
public class NilsException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;
  /**
   * Create a new {@link NilsException}
   *
   * @param message The message text
   */
  public NilsException(String message) {
    super(message);
  }
  /**
   * Create a new {@link NilsException}
   *
   * @param message The message text
   * @param cause The root cause
   */
  public NilsException(String message, Throwable cause) {
    super(message, cause);
  }
}
