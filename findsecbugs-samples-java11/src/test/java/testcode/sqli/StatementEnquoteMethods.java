package testcode.sqli;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Test cases for java.sql.Statement enquote methods (Java 9+)
 * Issue #721: These methods should be marked as SQL_INJECTION_SAFE
 */
public class StatementEnquoteMethods {

    Connection con;

    /**
     * Safe: enquoteLiteral properly escapes the literal value
     * No SQL_INJECTION bug should be reported
     */
    public void safeLiteralUsage(String userInput) throws SQLException {
        Statement stmt = con.createStatement();
        String safeLiteral = stmt.enquoteLiteral(userInput);
        String query = "SELECT * FROM users WHERE name = " + safeLiteral;
        ResultSet rs = stmt.executeQuery(query);
    }

    /**
     * Safe: enquoteIdentifier properly escapes the identifier
     * No SQL_INJECTION bug should be reported
     */
    public void safeIdentifierUsage(String columnName) throws SQLException {
        Statement stmt = con.createStatement();
        String safeColumn = stmt.enquoteIdentifier(columnName, true);
        String query = "SELECT " + safeColumn + " FROM users";
        ResultSet rs = stmt.executeQuery(query);
    }

    /**
     * Safe: enquoteIdentifier without always quote
     * No SQL_INJECTION bug should be reported
     */
    public void safeIdentifierUsageNoQuote(String tableName) throws SQLException {
        Statement stmt = con.createStatement();
        String safeTable = stmt.enquoteIdentifier(tableName, false);
        String query = "SELECT * FROM " + safeTable;
        ResultSet rs = stmt.executeQuery(query);
    }

    /**
     * Safe: enquoteNCharLiteral properly escapes the NChar literal
     * No SQL_INJECTION bug should be reported
     */
    public void safeNCharLiteralUsage(String userInput) throws SQLException {
        Statement stmt = con.createStatement();
        String safeLiteral = stmt.enquoteNCharLiteral(userInput);
        String query = "SELECT * FROM users WHERE description = " + safeLiteral;
        ResultSet rs = stmt.executeQuery(query);
    }

    /**
     * Safe: Multiple enquote methods used together
     * No SQL_INJECTION bug should be reported
     */
    public void multipleEnquoteUsage(String table, String column, String value) throws SQLException {
        Statement stmt = con.createStatement();
        String safeTable = stmt.enquoteIdentifier(table, true);
        String safeColumn = stmt.enquoteIdentifier(column, true);
        String safeValue = stmt.enquoteLiteral(value);
        String query = "SELECT * FROM " + safeTable + " WHERE " + safeColumn + " = " + safeValue;
        ResultSet rs = stmt.executeQuery(query);
    }

    /**
     * Unsafe: Direct concatenation without enquote
     * This SHOULD still be detected as SQL_INJECTION
     */
    public void unsafeDirectConcatenation(String userInput) throws SQLException {
        Statement stmt = con.createStatement();
        String query = "SELECT * FROM users WHERE name = '" + userInput + "'";
        ResultSet rs = stmt.executeQuery(query); // Bug SHOULD be reported
    }

    /**
     * Unsafe: Using execute with direct concatenation
     * This SHOULD still be detected as SQL_INJECTION
     */
    public void unsafeExecute(String userInput) throws SQLException {
        Statement stmt = con.createStatement();
        String query = "DELETE FROM users WHERE name = '" + userInput + "'";
        stmt.execute(query); // Bug SHOULD be reported
    }

    /**
     * Unsafe: Using executeUpdate with direct concatenation
     * This SHOULD still be detected as SQL_INJECTION
     */
    public void unsafeExecuteUpdate(String userInput) throws SQLException {
        Statement stmt = con.createStatement();
        String query = "UPDATE users SET active = 1 WHERE name = '" + userInput + "'";
        stmt.executeUpdate(query); // Bug SHOULD be reported
    }
}