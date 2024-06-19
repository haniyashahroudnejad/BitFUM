package DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransferConnection {
    public static Connection databaseLink;
    public Connection getDBConnection(){
        try {
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "h9026666518");
            Statement query = databaseLink.createStatement();
            query.executeUpdate("CREATE SCHEMA IF NOT EXISTS BitFUM");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306/BitFUM", "root", "h9026666518");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return databaseLink;
    }
    public void createTransferTable(Connection con, Statement stmt) {
        try {
            stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS TransferInfo " +
                    "(UserName VARCHAR(20) not NULL , " +
                    "RecordedDate date not null,"+
                    "RecordedTime time(6) not null,"+
                    "SourceWalletID VARCHAR(20) not null,"+
                    "DestinationWalletID VARCHAR(20) not null,"+
                    "CurrencyName VARCHAR(10) not null,"+
                    "CurrencyAmount double not null,"+
                    "IssueTracking INT DEFAULT 0 not null)";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public static void insertRowWithPreparedStatement(String UserName, Date RecordedDate, Time RecordedTime,String SourceWalletID, String DestinationWalletID, String CurrencyName, Double CurrencyAmount, int IssueTracking) {
        String sql = "INSERT INTO TransferInfo (UserName,RecordedDate,RecordedTime,SourceWalletID,DestinationWalletID,CurrencyName,CurrencyAmount,IssueTracking) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseLink.prepareStatement(sql)) {
            pstmt.setString(1, UserName);
            pstmt.setDate(2, RecordedDate);
            pstmt.setTime(3, RecordedTime);
            pstmt.setString(4, SourceWalletID);
            pstmt.setString(5, DestinationWalletID);
            pstmt.setString(6, CurrencyName);
            pstmt.setDouble(7, CurrencyAmount);
            pstmt.setInt(8, IssueTracking);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting row", e);
        }
    }
}

