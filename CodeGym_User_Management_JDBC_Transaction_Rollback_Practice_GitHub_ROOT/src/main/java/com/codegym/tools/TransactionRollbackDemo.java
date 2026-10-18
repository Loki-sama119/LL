package com.codegym.tools;

import com.codegym.dao.UserDAO;
import com.codegym.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Classroom-only executable demonstrating rollback within addUserTransaction().
 * It intentionally requests a nonexistent permission (foreign-key violation).
 * Run ONLY after creating the demo database and permission tables.
 */
public final class TransactionRollbackDemo {
    private TransactionRollbackDemo() { }

    public static void main(String[] args) throws Exception {
        String url = setting("JDBC_URL", "jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=UTF-8");
        String username = setting("JDBC_USERNAME", "root");
        String password = setting("JDBC_PASSWORD", "");
        UserDAO dao = new UserDAO(url, username, password);
        String email = "rollback-demo-" + UUID.randomUUID() + "@example.test";
        User sample = new User("Rollback Demo", email, "Viet Nam");

        long before = countByEmail(url, username, password, email);
        if (before != 0) throw new IllegalStateException("Demo email already present");

        // 1 is a valid permission, 2147483647 is deliberately missing.
        // The first assignment may succeed; the second throws a FOREIGN KEY error.
        // addUserTransaction must roll back both user and permission assignments.
        boolean expectedSqlError = false;
        try {
            dao.addUserTransaction(sample, new int[] { 1, Integer.MAX_VALUE });
        } catch (SQLException expected) {
            expectedSqlError = true;
            System.out.println("EXPECTED SQLException: " + expected.getMessage());
        }
        if (!expectedSqlError) {
            throw new AssertionError("Expected a foreign-key error, but none occurred. Verify table constraints and seed data.");
        }

        long after = countByEmail(url, username, password, email);
        System.out.printf("User rows before: %d, after: %d%n", before, after);
        if (after != 0) throw new AssertionError("FAILED: User remains in database after rollback");
        System.out.println("PASS: addUserTransaction() rolled back the full operation.");
        System.out.println("Normal UI create flow remains unchanged.");
    }

    private static long countByEmail(String url, String username, String password, String email) throws SQLException {
        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private static String setting(String key, String fallback) {
        String value = System.getenv(key);
        return value == null ? fallback : value;
    }
}
