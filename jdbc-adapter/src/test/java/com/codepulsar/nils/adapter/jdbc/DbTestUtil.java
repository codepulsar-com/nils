package com.codepulsar.nils.adapter.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTestUtil {
  private String username = "sa";
  private String baseUrl;

  DbTestUtil() {
    var outputBase = new File("target/test-output");
    if (!outputBase.exists()) {
      outputBase.mkdirs();
    }
    baseUrl = outputBase.getAbsolutePath().replaceAll("\\\\", "/");
  }

  public String initDb(String script) throws SQLException {
    var dbFile = String.format("%s/%s", baseUrl, script.replace(".sql", ""));
    var file = new File(dbFile + ".mv.db");
    if (file.exists()) {
      file.delete();
    }
    var dbUrl = String.format("jdbc:h2:%s", dbFile);
    try (Connection conn =
        DriverManager.getConnection(
            String.format("%s;INIT=RUNSCRIPT FROM 'classpath:%s'", dbUrl, script),
            username,
            ""); ) {}
    return dbUrl;
  }

  public void executeUpdate(String dbUrl, String sql) throws SQLException {
    try (var con = DriverManager.getConnection(dbUrl, username, "");
        var statement = con.prepareStatement(sql)) {
      statement.executeUpdate();
    }
  }
}
