package com.codepulsar.nils.adapter.gson.utils;

import com.codepulsar.nils.core.error.ErrorType;

public class GsonErrorTypes {
  public static final ErrorType CORRUPT_JSON_FILE_ERROR = new ErrorType("NILS-100");
  public static final ErrorType IO_ERROR = new ErrorType("NILS-101");
  public static final ErrorType MISSING_FILE_ERROR = new ErrorType("NILS-102");
}
