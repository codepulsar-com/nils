package com.codepulsar.nils.core.handler;

import java.text.MessageFormat;
import java.util.Locale;

import com.codepulsar.nils.api.NLS;
/**
 * Interface for format a value for a translation based on the implementation.
 *
 * <p>The interface is used on the {@link NLS}<code>.get(...)</code> methods, replacing the
 * arguments.
 */
public interface TranslationFormatter {
  /**
   * Default implementation using <code>MessageFormat.format(String, Object ...)</code> to format a
   * text.
   */
  public static final TranslationFormatter MESSAGE_FORMAT = (l, v, a) -> MessageFormat.format(v, a);
  /**
   * Default implementation using <code> String.format(Locale, String, Object...)</code> to format a
   * text.
   */
  public static final TranslationFormatter STRING_FORMAT = (l, v, a) -> String.format(l, v, a);
  /**
   * Format the value for a given Locale and the arguments.
   *
   * @param locale The Locale for the formatting.
   * @param value The base value (including the formatting marks for the formatting).
   * @param args The arguments for the formatting.
   * @return The formatted String.
   */
  String format(Locale locale, String value, Object... args);
}
