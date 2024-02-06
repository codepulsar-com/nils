package com.codepulsar.nils.adapter.jackson.utils;

import com.codepulsar.nils.api.error.ErrorType;

public class JacksonErrorTypes {
  public static final ErrorType MISSING_DEPENDENCY = new ErrorType("NILS-200");
  public static final ErrorType CORRUPT_FILE_ERROR = new ErrorType("NILS-201");
}
