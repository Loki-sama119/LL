package com.codegym.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Database {
  private Database() {}
  public static Connection getConnection() throws SQLException {
    String url = config("DB_URL", "jdbc:mysql://localhost:3306/product_management?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh");
    return DriverManager.getConnection(url, config("DB_USER", "root"), config("DB_PASSWORD", ""));
  }
  private static String config(String name, String fallback) {
    String v=System.getenv(name);return v==null ? fallback : v;
  }
}
