package DatabaseConnection;

import java.sql.*;

import static DatabaseConnection.UsersInformationDatabase.con;

public class SwapDatabase {
    public static void createSwapTable(Connection con, Statement stmt) {
        try {
            stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SwapInfo " +
                    "(UserName VARCHAR(20) not NULL , " +
                    "RecordedDate date not null,"+
                    "RecordedTime time(6) not null,"+
                    "SelectedCurrencyName VARCHAR(20) not null,"+
                    "ConvertedCurrencyName VARCHAR(20) not null,"+
                    "CurrencyAmount double not null,"+
                    "EquivalentCurrencyAmount double not null)";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public static void insertRowWithPreparedStatement(String UserName, Date RecordedDate, Time RecordedTime, String SelectedCurrencyName, String ConvertedCurrencyName, Double CurrencyAmount, double EquivalentCurrencyAmount) {
        String sql = "INSERT INTO SwapInfo (UserName,RecordedDate,RecordedTime,SelectedCurrencyName,ConvertedCurrencyName,CurrencyAmount,EquivalentCurrencyAmount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, UserName);
            pstmt.setDate(2, RecordedDate);
            pstmt.setTime(3, RecordedTime);
            pstmt.setString(4, SelectedCurrencyName);
            pstmt.setString(5, ConvertedCurrencyName);
            pstmt.setDouble(6, CurrencyAmount);
            pstmt.setDouble(7, EquivalentCurrencyAmount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting row", e);
        }
    }
}
