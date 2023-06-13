package com.codepulsar.nils.core.error;

import java.util.Objects;

// TODO public docu
/**
 * A ErrorType configures a type of error.
 *
 * <p>Each ErrorType should have its unique error code in the system. Contains all errors that can
 */
// TODO javadoc
public class ErrorType {
  /** Virtual error: Suppress all errors at runtime. */
  public static final ErrorType ALL = new ErrorType("NILS-ALL", true);
  /** Virtual error: Suppress no error at runtime. */
  public static final ErrorType NONE = new ErrorType("NILS-NONE", true);
  /** Include other keys leads into a loop. */
  public static final ErrorType INCLUDE_LOOP_DETECTED =
      new ErrorType("NILS-002", true); // TODO Implement
  /** The translation source does not provide a transalation for the requested key. */
  public static final ErrorType MISSING_TRANSLATION = new ErrorType("NILS-001", true);
  /** The parameter call at the NLS interface is invalid. */
  public static final ErrorType NLS_PARAMETER_CHECK =
      new ErrorType("NILS-003", true); // TODO Implement
  /** The configuration or a configuration value is invalid. */
  public static final ErrorType CONFIG_ERROR = new ErrorType("NILS-004");
  /** An error using an adapter. */
  public static final ErrorType ADAPTER_ERROR = new ErrorType("NILS-005");

  private final String errCode;

  private final boolean suppressable;

  public ErrorType(String errCode) {
    this(errCode, false);
  }

  public ErrorType(String errCode, boolean suppressable) {
    this.errCode = Objects.requireNonNull(errCode);
    this.suppressable = suppressable;
  }

  public boolean isSuppressable() {
    return suppressable;
  }

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
