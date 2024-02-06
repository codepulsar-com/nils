package com.codepulsar.nils.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.api.NilsConfig;
import com.codepulsar.nils.api.error.ErrorType;
import com.codepulsar.nils.api.error.NilsException;

class ErrorHandler {

  private Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);
  private final NilsConfig nilsConfig;

  ErrorHandler(NilsConfig nilsConfig) {
    this.nilsConfig = nilsConfig;
  }

  void handle(ErrorType suppressType, String errorMessage) throws NilsException {
    NilsException ex = new NilsException(suppressType, errorMessage);
    handle(ex);
  }

  void handle(NilsException ex) throws NilsException {
    if (nilsConfig.isSuppressErrors()) {
      LOG.error("Suppressed error: {}", ex, ex.getMessage());
      return;
    }
    LOG.error(ex.getMessage(), ex);
    throw ex;
  }

  void handle(ErrorType suppressType, Exception ex) throws NilsException {
    if (nilsConfig.isSuppressErrors()) {
      LOG.error("Suppressed error: {}", ex, ex.getMessage());
      return;
    }
    LOG.error(ex.getMessage(), ex);
    throw new NilsException(suppressType, ex.getMessage(), ex);
  }
}
