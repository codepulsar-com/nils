package com.codepulsar.nils.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codepulsar.nils.core.NilsConfig;
import com.codepulsar.nils.core.NilsException;
import com.codepulsar.nils.core.config.ErrorType;
// TODO Add tests
class ErrorHandler {

  private Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);
  private final NilsConfig nilsConfig;

  ErrorHandler(NilsConfig nilsConfig) {
    this.nilsConfig = nilsConfig;
  }

  public void handle(ErrorType suppressType, NilsException ex) throws NilsException {

    if (nilsConfig.getSuppressErrors().contains(ErrorType.ALL)
        || nilsConfig.getSuppressErrors().contains(suppressType)) {
      LOG.error("Suppressed error: {}", ex, ex.getMessage());
      return;
    }
    LOG.error(ex.getMessage(), ex);
    throw ex;
  }

  public void handle(ErrorType suppressType, Exception ex) throws NilsException {
    LOG.error(ex.getMessage(), ex);
    if (nilsConfig.getSuppressErrors().contains(ErrorType.ALL)
        || nilsConfig.getSuppressErrors().contains(suppressType)) {
      LOG.error("Suppressed error: {}", ex, ex.getMessage());
      return;
    }
    LOG.error(ex.getMessage(), ex);
    throw new NilsException(ex.getMessage(), ex);
  }
}
