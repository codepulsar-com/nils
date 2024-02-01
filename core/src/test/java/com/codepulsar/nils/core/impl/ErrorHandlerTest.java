package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.error.ErrorTypes.MISSING_TRANSLATION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.core.error.NilsException;

public class ErrorHandlerTest {

  private NilsConfig nilsConfig;
  private ErrorHandler underTest;

  @BeforeEach
  public void arrange() {
    // Arrange
    var adapterConfig = ResourceBundleAdapterConfig.init(this);
    nilsConfig = NilsConfig.init(adapterConfig);
    underTest = new ErrorHandler(nilsConfig);
  }

  @Test
  public void handle_with_errorMessage_suppressErrors_true() {
    // Arrange
    nilsConfig.suppressErrors(true);

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, "MissingTranslation");
  }

  @Test
  public void handle_with_errorMessage_suppressErrors_false() {
    // Arrange
    nilsConfig.suppressErrors(false);

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, "MissingTranslation"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_nilsException_suppressErrors_true() {
    // Arrange
    nilsConfig.suppressErrors(true);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    underTest.handle(exception);
  }

  @Test
  public void handle_with_nilsException_suppressErrors_false() {
    // Arrange
    nilsConfig.suppressErrors(false);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_exception_suppressError_true() {
    // Arrange
    nilsConfig.suppressErrors(true);
    var exception = new Exception("An error occured.");

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, exception);
  }

  @Test
  public void handle_with_exception_none_suppressError_false() {
    // Arrange
    nilsConfig.suppressErrors(false);
    var exception = new Exception("An error occured.");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: An error occured.");
  }
}
