package com.codepulsar.nils.core.impl;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class FormatsImplTestDataProvider {

  public static Stream<Arguments> source_forDate() {
    return Stream.of(
        arguments(LocalDate.of(2023, 7, 25), "Jul 25, 2023", Locale.US, null),
        arguments(LocalDate.of(2023, 7, 25), "25.07.2023", Locale.GERMANY, null),
        arguments(LocalDate.of(2023, 7, 25), "7/25/23", Locale.US, FormatStyle.SHORT),
        arguments(LocalDate.of(2023, 7, 25), "25. Juli 2023", Locale.GERMANY, FormatStyle.LONG),
        arguments(LocalDateTime.of(2023, 7, 25, 12, 15, 00), "Jul 25, 2023", Locale.US, null),
        arguments(LocalDateTime.of(2023, 7, 25, 12, 15, 00), "25.07.2023", Locale.GERMANY, null));
  }

  public static Stream<Arguments> source_forTime() {
    return Stream.of(
        arguments(LocalTime.of(12, 15, 00), "12:15:00 PM", Locale.US, null),
        arguments(LocalTime.of(12, 15, 00), "12:15:00", Locale.GERMANY, null),
        arguments(LocalTime.of(12, 15, 00), "12:15:00 PM", Locale.US, FormatStyle.FULL),
        arguments(LocalTime.of(12, 15, 00), "12:15:00", Locale.GERMANY, FormatStyle.LONG),
        arguments(LocalDateTime.of(2023, 7, 25, 12, 15, 00), "12:15:00 PM", Locale.US, null),
        arguments(LocalDateTime.of(2023, 7, 25, 12, 15, 00), "12:15:00", Locale.GERMANY, null));
  }

  public static Stream<Arguments> source_forDateTime() {
    return Stream.of(
        arguments(
            LocalDateTime.of(2023, 7, 25, 12, 15, 00),
            "Jul 25, 2023, 12:15:00 PM",
            Locale.US,
            null),
        arguments(
            LocalDateTime.of(2023, 7, 25, 12, 15, 00),
            "25.07.2023, 12:15:00",
            Locale.GERMANY,
            null),
        arguments(
            LocalDateTime.of(2023, 7, 25, 12, 15, 00),
            "7/25/23, 12:15 PM",
            Locale.US,
            FormatStyle.SHORT),
        arguments(
            LocalDateTime.of(2023, 7, 25, 12, 15, 00).atZone(ZoneId.of("Z")),
            "25. Juli 2023 um 12:15:00 Z",
            Locale.GERMANY,
            FormatStyle.LONG));
  }

  public static Stream<Arguments> source_forUtilDate() {
    return Stream.of(
        arguments(asDate(2023, 7, 25, 0, 0, 0), "Jul 25, 2023", Locale.US, null),
        arguments(asDate(2023, 7, 25, 0, 0, 0), "25.07.2023", Locale.GERMANY, null),
        arguments(asDate(2023, 7, 25, 0, 0, 0), "7/25/23", Locale.US, FormatStyle.SHORT),
        arguments(asDate(2023, 7, 25, 0, 0, 0), "25.07.2023", Locale.GERMANY, FormatStyle.MEDIUM),
        arguments(asDate(2023, 7, 25, 12, 15, 00), "Jul 25, 2023", Locale.US, null),
        arguments(asDate(2023, 7, 25, 12, 15, 00), "25.07.2023", Locale.GERMANY, null));
  }

  public static Stream<Arguments> source_forUtilTime() {
    return Stream.of(
        arguments(asDate(0, 0, 0, 12, 15, 0), "12:15:00 PM", Locale.US, null),
        arguments(asDate(0, 0, 0, 12, 15, 0), "12:15:00", Locale.GERMANY, null),
        arguments(
            asDate(0, 0, 0, 12, 15, 0),
            "12:15:00 PM Central European Standard Time",
            Locale.US,
            FormatStyle.FULL),
        arguments(asDate(0, 0, 0, 12, 15, 0), "12:15", Locale.GERMANY, FormatStyle.SHORT),
        arguments(asDate(2023, 7, 25, 12, 15, 00), "12:15:00 PM", Locale.US, null),
        arguments(asDate(2023, 7, 25, 12, 15, 00), "12:15:00", Locale.GERMANY, null));
  }

  public static Stream<Arguments> source_forUtilDateTime() {
    return Stream.of(
        arguments(asDate(2023, 7, 25, 12, 15, 00), "Jul 25, 2023, 12:15:00 PM", Locale.US, null),
        arguments(asDate(2023, 7, 25, 12, 15, 00), "25.07.2023, 12:15:00", Locale.GERMANY, null),
        arguments(
            asDate(2023, 7, 25, 12, 15, 00),
            "July 25, 2023 at 12:15:00 PM CEST",
            Locale.US,
            FormatStyle.LONG),
        arguments(
            asDate(2023, 7, 25, 12, 15, 00), "25.07.23, 12:15", Locale.GERMANY, FormatStyle.SHORT));
  }

  public static Stream<Arguments> source_forNumber() {
    return Stream.of(
        arguments(1000, "1,000", Locale.US),
        arguments(1000, "1.000", Locale.GERMANY),
        arguments(1000L, "1,000", Locale.US),
        arguments(1000L, "1.000", Locale.GERMANY),
        arguments(1000.23D, "1,000.23", Locale.US),
        arguments(1000.23D, "1.000,23", Locale.GERMANY),
        arguments(1000.34F, "1,000.34", Locale.US),
        arguments(1000.34F, "1.000,34", Locale.GERMANY),
        arguments(new BigDecimal("1000.15"), "1,000.15", Locale.US),
        arguments(new BigDecimal("1000.15"), "1.000,15", Locale.GERMANY));
  }

  public static Stream<Arguments> source_forCurrency() {
    return Stream.of(
        arguments(1000, "$1,000.00", Locale.US),
        arguments(1000, "1.000,00 €", Locale.GERMANY),
        arguments(1000L, "$1,000.00", Locale.US),
        arguments(1000L, "1.000,00 €", Locale.GERMANY),
        arguments(1000.23D, "$1,000.23", Locale.US),
        arguments(1000.23D, "1.000,23 €", Locale.GERMANY),
        arguments(1000.34F, "$1,000.34", Locale.US),
        arguments(1000.34F, "1.000,34 €", Locale.GERMANY),
        arguments(new BigDecimal("1000.15"), "$1,000.15", Locale.US),
        arguments(new BigDecimal("1000.15"), "1.000,15 €", Locale.GERMANY));
  }

  public static Stream<Arguments> source_forInteger() {
    return Stream.of(
        arguments(1000, "1,000", Locale.US),
        arguments(1000, "1.000", Locale.GERMANY),
        arguments(1000L, "1,000", Locale.US),
        arguments(1000L, "1.000", Locale.GERMANY),
        arguments(1000.23D, "1,000", Locale.US),
        arguments(1000.23D, "1.000", Locale.GERMANY),
        arguments(1000.34F, "1,000", Locale.US),
        arguments(1000.34F, "1.000", Locale.GERMANY),
        arguments(new BigDecimal("1000.15"), "1,000", Locale.US),
        arguments(new BigDecimal("1000.15"), "1.000", Locale.GERMANY));
  }

  public static Stream<Arguments> source_forPercent() {
    return Stream.of(
        arguments(50, "5,000%", Locale.US),
        arguments(50, "5.000 %", Locale.GERMANY),
        arguments(20L, "2,000%", Locale.US),
        arguments(20L, "2.000 %", Locale.GERMANY),
        arguments(0.23D, "23%", Locale.US),
        arguments(0.23D, "23 %", Locale.GERMANY),
        arguments(0.34F, "34%", Locale.US),
        arguments(0.34F, "34 %", Locale.GERMANY),
        arguments(new BigDecimal("0.15"), "15%", Locale.US),
        arguments(new BigDecimal("0.15"), "15 %", Locale.GERMANY));
  }

  public static Stream<Arguments> source_forDecimal() {
    return Stream.of(
        arguments(1000, "1,000", Locale.US),
        arguments(1000, "1.000", Locale.GERMANY),
        arguments(1000L, "1,000", Locale.US),
        arguments(1000L, "1.000", Locale.GERMANY),
        arguments(1000.23D, "1,000.23", Locale.US),
        arguments(1000.23D, "1.000,23", Locale.GERMANY),
        arguments(1000.34F, "1,000.34", Locale.US),
        arguments(1000.34F, "1.000,34", Locale.GERMANY),
        arguments(new BigDecimal("1000.15"), "1,000.15", Locale.US),
        arguments(new BigDecimal("1000.15"), "1.000,15", Locale.GERMANY));
  }

  public static Stream<Arguments> source_decimalFormatSymbols() {
    return Stream.of(
        arguments(DecimalFormatSymbols.getInstance(Locale.US), Locale.US),
        arguments(DecimalFormatSymbols.getInstance(Locale.GERMANY), Locale.GERMANY));
  }

  private static Date asDate(int year, int month, int day, int hour, int min, int sec) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month - 1, day, hour, min, sec);
    return cal.getTime();
  }
}
