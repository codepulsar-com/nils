package com.codepulsar.nils.api;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * A <strong>Formats</strong> provides access to divers Java related formatter object.
 *
 * <p>All are related to the <code>Locale</code> used in a {@link NLS} object.
 *
 * <p>A <strong>Formats</strong> can be retrieved by calling {@link NLS#getFormats()}.
 */
public interface Formats {
  /**
   * Get a <code>DateTimeFormatter</code> object for "temporal" dates like <code>LocalDate</code> in
   * the FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * @return A <code>DateTimeFormatter</code> object for date.
   */
  DateTimeFormatter forDate();
  /**
   * Get a <code>DateTimeFormatter</code> object for "temporal" times like <code>LocalTime</code> in
   * the FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * <p><em>Note: Only <code>FormatStyle.MEDIUM</code> and <code>FormatStyle.SHORT</code> can be
   * used for LocalTime object. Any other configuration of {@link NilsConfig#getDateFormatStyle()}
   * will result in <code>FormatStyle.MEDIUM</code>. </em>
   *
   * @return A <code>DateTimeFormatter</code> object for time.
   */
  DateTimeFormatter forTime();
  /**
   * Get a <code>DateTimeFormatter</code> object for "temporal" dates with time like <code>
   * LocalDateTime</code> in the FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * @return A <code>DateTimeFormatter</code> object for date with time.
   */
  DateTimeFormatter forDateTime();
  /**
   * Get a <code>DateFormat</code> object for "util" <code>Date</code>s with date information in the
   * FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * @return A <code>DateFormat</code> object for date.
   */
  DateFormat forUtilDate();
  /**
   * Get a <code>DateFormat</code> object for "util" <code>Date</code>s with time information in the
   * FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * @return A <code>DateFormat</code> object for time.
   */
  DateFormat forUtilTime();
  /**
   * Get a <code>DateFormat</code> object for "util" <code>Date</code> with date and time
   * information in the FormatStyle defined in {@link NilsConfig#getDateFormatStyle()}.
   *
   * @return A <code>DateFormat</code> object for date with time.
   */
  DateFormat forUtilDateTime();
  /**
   * Get a <code>NumberFormat</code> object for <code>Number</code>s.
   *
   * @return A <code>NumberFormat</code> object.
   */
  NumberFormat forNumber();
  /**
   * Get a <code>NumberFormat</code> object for formatting <code>Number</code>s as currencies.
   *
   * @return A <code>NumberFormat</code> object.
   */
  NumberFormat forCurrency();
  /**
   * Get a <code>NumberFormat</code> object for formatting <code>Number</code>s as integer.
   *
   * @return A <code>NumberFormat</code> object.
   */
  NumberFormat forInteger();
  /**
   * Get a <code>NumberFormat</code> object for formatting <code>Number</code>s as percent values.
   *
   * @return A <code>NumberFormat</code> object.
   */
  NumberFormat forPercent();
  /**
   * Get a <code>DecimalFormat</code> object for formatting <code>Number</code>s as decimal values.
   *
   * @return A <code>DecimalFormat</code> object.
   */
  DecimalFormat forDecimal();

  /**
   * Get a <code>DecimalFormatSymbols</code> object.
   *
   * @return A <code>DecimalFormatSymbols</code> object.
   */
  DecimalFormatSymbols decimalFormatSymbols();
}
