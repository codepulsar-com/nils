package com.codepulsar.nils.core.config;

import com.codepulsar.nils.core.error.ErrorType;

/**
 * Contains all errors that can be suppressed when running NILS.
 *
 * <p>Special types are
 *
 * <ul>
 *   <li>{@link #ALL} - All are suppressed
 *   <li>{@link #NONE} - None are suppressed
 */
public class SuppressableErrorTypes {
  /** Suppress all errors at runtime. */
  public static final ErrorType ALL = ErrorType.ALL;
  /** Suppress no error at runtime. */
  public static final ErrorType NONE = ErrorType.NONE;
  /** Include other keys leads into a loop. */
  public static final ErrorType INCLUDE_LOOP_DETECTED = ErrorType.INCLUDE_LOOP_DETECTED;
  /** The translation source does not provide a translation for the requested key. */
  public static final ErrorType MISSING_TRANSLATION = ErrorType.MISSING_TRANSLATION;
  /** The parameter call at the NLS interface is invalid. */
  public static final ErrorType NLS_PARAMETER_CHECK = ErrorType.NLS_PARAMETER_CHECK;
}
