package com.codepulsar.nils.core.config;
// TODO public docu
/**
 * The enumeration contains all errors or exceptions that can be suppressed running NILS.
 *
 * <p>Special types are
 *
 * <ul>
 *   <li>{@link #ALL} - All are suppressed
 *   <li>{@link #NONE} - None are suppressed
 */
public enum ErrorType {
  /** Suppress all errors at runtime. */
  ALL, // TODO Check at the end
  /** Suppress no error at runtime. */
  NONE, // TODO Check at the end
  /** Include other keys leads into a loop. */
  INCLUDE_LOOP, // TODO Implement
  /** The translation source does not provide a transalation for the requested key. */
  MISSING_TRANSLATION,
  /** The parameter callind the NLS interface is invalid. */
  NLS_PARAMETER_CHECK; // TODO Implement
}
