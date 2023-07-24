package com.codepulsar.nils.core.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.MissingFormatArgumentException;

import org.junit.jupiter.api.Test;

public class TranslationFormatterTest {

  private Locale locale = Locale.ENGLISH;

  @Test
  public void messageFormat() {
    // Arrange
    var underTest = TranslationFormatter.MESSAGE_FORMAT;

    // Act
    var value = underTest.format(locale, "A {0} and a {1}", "word", 400L);

    // Assert
    assertThat(value).isEqualTo("A word and a 400");
  }

  @Test
  public void messageFormat_nullArgs() {
    // Arrange
    var underTest = TranslationFormatter.MESSAGE_FORMAT;
    Object[] args = null;

    // Act
    var value = underTest.format(locale, "A {0} and a {1}", args);

    // Assert
    assertThat(value).isEqualTo("A {0} and a {1}");
  }

  @Test
  public void messageFormat_emptyArgs() {
    // Arrange
    var underTest = TranslationFormatter.MESSAGE_FORMAT;
    var args = new Object[] {};

    // Act
    var value = underTest.format(locale, "A {0} and a {1}", args);

    // Assert
    assertThat(value).isEqualTo("A {0} and a {1}");
  }

  @Test
  public void messageFormat_wrongFormat() {
    // Arrange
    var underTest = TranslationFormatter.MESSAGE_FORMAT;

    // Act
    var value = underTest.format(locale, "A %s and a %d", "word", 400L);

    // Assert
    assertThat(value).isEqualTo("A %s and a %d");
  }

  @Test
  public void stringFormat() {
    // Arrange
    var underTest = TranslationFormatter.STRING_FORMAT;

    // Act
    var value = underTest.format(locale, "A %s and a %d", "word", 400L);

    // Assert
    assertThat(value).isEqualTo("A word and a 400");
  }

  @Test
  public void stringFormat_nullArgs() {
    // Arrange
    var underTest = TranslationFormatter.STRING_FORMAT;
    Object[] args = null;

    // Act
    var value = underTest.format(locale, "A %s and a %d", args);

    // Assert
    assertThat(value).isEqualTo("A null and a null");
  }

  @Test
  public void stringFormat_emptyArgs() {
    // Arrange
    var underTest = TranslationFormatter.STRING_FORMAT;
    var args = new Object[] {};

    // Act / Assert
    assertThatThrownBy(() -> underTest.format(locale, "A %s and a %d", args))
        .isInstanceOf(MissingFormatArgumentException.class);
  }

  @Test
  public void stringFormat_wrongFormat() {
    // Arrange
    var underTest = TranslationFormatter.STRING_FORMAT;

    // Act
    var value = underTest.format(locale, "A {0} and a {1}", "word", 400L);

    // Assert
    assertThat(value).isEqualTo("A {0} and a {1}");
  }
}
