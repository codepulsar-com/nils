package com.codepulsar.nils.core.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.Formats;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.util.ParameterCheck;

public class FormatsImpl implements Formats {

  private static final Logger LOG = LoggerFactory.getLogger(FormatsImpl.class);

  private final Locale locale;
  private final FormatStyle formatStyle;
  private final int dateFormatStyle;

  private DateTimeFormatter forDate;
  private DateTimeFormatter forTime;
  private DateTimeFormatter forDateTime;
  private DateFormat forUtilDate;
  private DateFormat forUtilTime;
  private DateFormat forUtilDateTime;
  private NumberFormat forNumber;
  private NumberFormat forCurrency;
  private NumberFormat forInteger;
  private NumberFormat forPercent;
  private DecimalFormat forDecimal;
  private DecimalFormatSymbols decimalFormatSymbols;

  public FormatsImpl(Locale locale, NilsConfig config) {
    this.locale = ParameterCheck.notNull(locale, "locale");
    ParameterCheck.notNull(config, "config");
    this.formatStyle = config.getDateFormatStyle();
    dateFormatStyle = toDateFormatStyle(formatStyle);
  }

  @Override
  public DateTimeFormatter forDate() {
    if (forDate == null) {
      forDate = DateTimeFormatter.ofLocalizedDate(formatStyle).localizedBy(locale);
    }
    return forDate;
  }

  @Override
  public DateTimeFormatter forTime() {
    if (forTime == null) {
      var usedStyle =
          (formatStyle == FormatStyle.MEDIUM || formatStyle == FormatStyle.SHORT)
              ? formatStyle
              : FormatStyle.MEDIUM;
      if (usedStyle != formatStyle) {
        LOG.info(
            "Configured FormatStyle {} cannot be used for time values. Setting FormatStyle to {}",
            formatStyle,
            usedStyle);
      }
      forTime = DateTimeFormatter.ofLocalizedTime(usedStyle).localizedBy(locale);
    }
    return forTime;
  }

  @Override
  public DateTimeFormatter forDateTime() {
    if (forDateTime == null) {
      forDateTime = DateTimeFormatter.ofLocalizedDateTime(formatStyle).localizedBy(locale);
    }
    return forDateTime;
  }

  @Override
  public DateFormat forUtilDate() {
    if (forUtilDate == null) {
      forUtilDate = DateFormat.getDateInstance(dateFormatStyle, locale);
    }
    return forUtilDate;
  }

  @Override
  public DateFormat forUtilTime() {
    if (forUtilTime == null) {
      forUtilTime = DateFormat.getTimeInstance(dateFormatStyle, locale);
    }
    return forUtilTime;
  }

  @Override
  public DateFormat forUtilDateTime() {
    if (forUtilDateTime == null) {
      forUtilDateTime = DateFormat.getDateTimeInstance(dateFormatStyle, dateFormatStyle, locale);
    }
    return forUtilDateTime;
  }

  @Override
  public NumberFormat forNumber() {
    if (forNumber == null) {
      forNumber = NumberFormat.getInstance(locale);
    }
    return forNumber;
  }

  @Override
  public NumberFormat forCurrency() {
    if (forCurrency == null) {
      forCurrency = NumberFormat.getCurrencyInstance(locale);
    }
    return forCurrency;
  }

  @Override
  public NumberFormat forInteger() {
    if (forInteger == null) {
      forInteger = NumberFormat.getIntegerInstance(locale);
    }
    return forInteger;
  }

  @Override
  public NumberFormat forPercent() {
    if (forPercent == null) {
      forPercent = NumberFormat.getPercentInstance(locale);
    }
    return forPercent;
  }

  @Override
  public DecimalFormat forDecimal() {
    if (forDecimal == null) {
      forDecimal = (DecimalFormat) NumberFormat.getNumberInstance(locale);
    }
    return forDecimal;
  }

  @Override
  public DecimalFormatSymbols decimalFormatSymbols() {
    if (decimalFormatSymbols == null) {
      decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
    }
    return decimalFormatSymbols;
  }

  private int toDateFormatStyle(FormatStyle style) {
    switch (style) {
      case FULL:
        return DateFormat.FULL;
      case LONG:
        return DateFormat.LONG;
      case MEDIUM:
        return DateFormat.MEDIUM;
      case SHORT:
        return DateFormat.SHORT;
      default:
        throw new IllegalArgumentException(
            "Could not resolve FormatStyle." + style + " as DateFormat style.");
    }
  }
}
