package com.codepulsar.nils.core.impl;

import static com.codepulsar.nils.core.config.SuppressableErrorTypes.ALL;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.MISSING_TRANSLATION;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NLS_PARAMETER_CHECK;
import static com.codepulsar.nils.core.config.SuppressableErrorTypes.NONE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.adapter.rb.ResourceBundleAdapterConfig;
import com.codepulsar.nils.core.error.ErrorType;
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
  public void handle_with_errorMessage_all() {
    // Arrange
    nilsConfig.suppressErrors(ALL);

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, "MissingTranslation");
  }

  @Test
  public void handle_with_errorMessage_some() {
    // Arrange
    nilsConfig.suppressErrors(MISSING_TRANSLATION);

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, "MissingTranslation");
  }

  @Test
  public void handle_with_errorMessage_none() {
    // Arrange
    nilsConfig.suppressErrors(NONE);

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, "MissingTranslation"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_errorMessage_someNotIncluded() {
    // Arrange
    nilsConfig.suppressErrors(NLS_PARAMETER_CHECK);

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, "MissingTranslation"))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_nilsException_all() {
    // Arrange
    nilsConfig.suppressErrors(ALL);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, exception);
  }

  @Test
  public void handle_with_nilsException_some() {
    // Arrange
    nilsConfig.suppressErrors(MISSING_TRANSLATION);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, exception);
  }

  @Test
  public void handle_with_nilsException_someOther() {
    // Arrange
    nilsConfig.suppressErrors(MISSING_TRANSLATION);
    var exception = new NilsException(NLS_PARAMETER_CHECK, "MissingTranslation");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(ErrorType.MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-003: MissingTranslation");
  }

  @Test
  public void handle_with_nilsException_none() {
    // Arrange
    nilsConfig.suppressErrors(NONE);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_nilsException_someNotIncluded() {
    // Arrange
    nilsConfig.suppressErrors(NLS_PARAMETER_CHECK);
    var exception = new NilsException(MISSING_TRANSLATION, "MissingTranslation");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: MissingTranslation");
  }

  @Test
  public void handle_with_exception_all() {
    // Arrange
    nilsConfig.suppressErrors(ALL);
    var exception = new Exception("An error occured.");

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, exception);
  }

  @Test
  public void handle_with_exception_some() {
    // Arrange
    nilsConfig.suppressErrors(MISSING_TRANSLATION);
    var exception = new Exception("An error occured.");

    // Act / Assert
    underTest.handle(MISSING_TRANSLATION, exception);
  }

  @Test
  public void handle_with_exception_none() {
    // Arrange
    nilsConfig.suppressErrors(NONE);
    var exception = new Exception("An error occured.");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(ErrorType.MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: An error occured.");
  }

  @Test
  public void handle_with_exception_someNotIncluded() {
    // Arrange
    var exception = new Exception("An error occured.");

    // Act / Assert
    assertThatThrownBy(() -> underTest.handle(MISSING_TRANSLATION, exception))
        .isInstanceOf(NilsException.class)
        .hasMessage("NILS-001: An error occured.");
  }
}
