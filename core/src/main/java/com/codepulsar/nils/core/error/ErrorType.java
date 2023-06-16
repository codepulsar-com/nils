package com.codepulsar.nils.core.error;

import java.util.Objects;

/**
 * A <strong>ErrorType</strong> configures a type of error.
 *
 * <p>Each ErrorType should have its unique error code in the system. Contains all errors that can
 */
public class ErrorType {
  private static final boolean SUPPRESSABLE = true;
  /** Virtual error: Suppress all errors at runtime. */
  public static final ErrorType ALL = new ErrorType("NILS-ALL", SUPPRESSABLE);
  /** Virtual error: Suppress no error at runtime. */
  public static final ErrorType NONE = new ErrorType("NILS-NONE", SUPPRESSABLE);
  /** Include other keys leads into a loop. */
  public static final ErrorType INCLUDE_LOOP_DETECTED = new ErrorType("NILS-002", SUPPRESSABLE);
  /** The translation source does not provide a translation for the requested key. */
  public static final ErrorType MISSING_TRANSLATION = new ErrorType("NILS-001", SUPPRESSABLE);
  /** The parameter call at the NLS interface is invalid. */
  public static final ErrorType NLS_PARAMETER_CHECK = new ErrorType("NILS-003", SUPPRESSABLE);
  /** The configuration or a configuration value is invalid. */
  public static final ErrorType CONFIG_ERROR = new ErrorType("NILS-004");
  /** An error using an adapter. */
  public static final ErrorType ADAPTER_ERROR = new ErrorType("NILS-005");

  private final String errCode;

  private final boolean suppressable;
  /**
   * Create a new {@link ErrorType} with suppressable = <code>false</code>.
   *
   * @param errCode A error code.
   */
  public ErrorType(String errCode) {
    this(errCode, false);
  }
  /**
   * Create a new {@link ErrorType}.
   *
   * @param errCode A error code.
   * @param suppressable Flag, if it is possible to suppress the error type during runtime or not.
   */
  public ErrorType(String errCode, boolean suppressable) {
    this.errCode = Objects.requireNonNull(errCode);
    this.suppressable = suppressable;
  }
  /**
   * Gets the flag, if it is possible to suppress the error type during runtime.
   *
   * @return <code>true</code> if suppressable.
   */
  public boolean isSuppressable() {
    return suppressable;
  }

  /**
   * Gets the error code for these error type.
   *
   * @return A String
   */
  public String getErrCode() {
    return errCode;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "[errCode="
        + errCode
        + ", suppressable="
        + suppressable
        + "]";
  }
}
