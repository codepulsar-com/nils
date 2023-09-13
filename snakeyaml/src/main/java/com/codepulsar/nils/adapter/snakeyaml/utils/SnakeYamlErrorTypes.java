package com.codepulsar.nils.adapter.snakeyaml.utils;

import com.codepulsar.nils.core.error.ErrorType;
// TODO same in gson
public class SnakeYamlErrorTypes {
  public static final ErrorType CORRUPT_FILE_ERROR = new ErrorType("NILS-100");
  public static final ErrorType IO_ERROR = new ErrorType("NILS-101");
  public static final ErrorType MISSING_FILE_ERROR = new ErrorType("NILS-102");
}
