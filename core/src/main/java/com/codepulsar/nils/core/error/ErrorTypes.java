package com.codepulsar.nils.core.error;

/**
 * A <strong>ErrorType</strong> configures a type of error.
 *
 * <p>Each ErrorType should have its unique error code in the system. Contains all errors that can
 */
public class ErrorTypes {
  /** Include other keys leads into a loop. */
  public static final ErrorType INCLUDE_LOOP_DETECTED = new ErrorType("NILS-002");
  /** The translation source does not provide a translation for the requested key. */
  public static final ErrorType MISSING_TRANSLATION =
      new ErrorType("NILS-001", "Could not find a translation for key '%s' and locale '%s'.");
  /** The parameter call at the NLS interface is invalid. */
  public static final ErrorType NLS_PARAMETER_CHECK = new ErrorType("NILS-003");
  /** The configuration or a configuration value is invalid. */
  public static final ErrorType CONFIG_ERROR = new ErrorType("NILS-004");
  /** An error using an adapter. */
  public static final ErrorType ADAPTER_ERROR = new ErrorType("NILS-005");
  /** The value of a translation could not be formatted with the used TranslationFormatter. */
  public static final ErrorType TRANSLATION_FORMAT_ERROR =
      new ErrorType("NILS-006", "Error in key '%s': %s");
  /** An error on IO level occurred (Maybe file access, network access etc.) */
  public static final ErrorType IO_ERROR = new ErrorType("NILS-007");
  /** An requested resource file is missing. */
  public static final ErrorType MISSING_RESOURCE_FILE_ERROR = new ErrorType("NILS-008");
  /** A mandatory file extension is not found on a file name. */
  public static final ErrorType MISSING_FILE_EXTENSION_ERROR = new ErrorType("NILS-009");
}
