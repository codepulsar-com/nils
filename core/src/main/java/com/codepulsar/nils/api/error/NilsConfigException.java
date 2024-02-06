package com.codepulsar.nils.api.error;

import com.codepulsar.nils.core.error.ErrorTypes;

/**
 * This exception is thrown by NILS if the configuration of NILS or the used adapter is erroneous.
 */
public class NilsConfigException extends NilsException {
  /** */
  private static final long serialVersionUID = 2L;

  /**
   * Create a new {@link NilsConfigException}
   *
   * @param message The message text
   */
  public NilsConfigException(String message) {
    super(ErrorTypes.CONFIG_ERROR, message);
  }

  /**
   * Create a new {@link NilsConfigException}
   *
   * @param message The message text
   * @param cause The root cause
   */
  public NilsConfigException(String message, Throwable cause) {
    super(ErrorTypes.CONFIG_ERROR, message, cause);
  }
}
