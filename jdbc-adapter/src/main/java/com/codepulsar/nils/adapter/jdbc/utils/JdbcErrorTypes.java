package com.codepulsar.nils.adapter.jdbc.utils;

import com.codepulsar.nils.api.error.ErrorType;

public class JdbcErrorTypes {

  public static final ErrorType MISSING_JDBC_DRIVER =
      new ErrorType("NILS-250", "The JDBC driver class name is invalid.");
  public static final ErrorType INCOMPLETE_CONNECTION_DATA =
      new ErrorType("NILS-251", "Connection data contains invalid data for 'url' or 'username'.");
  public static final ErrorType SQL_EXCEPTION =
      new ErrorType("NILS-252", "An SQLException occurred: %s");
}
