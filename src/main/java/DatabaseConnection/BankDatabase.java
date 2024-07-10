package DatabaseConnection;

import java.sql.*;

import static DatabaseConnection.UsersInformationDatabase.con;

public class BankDatabase {
    public static void createBankTable(Connection con, Statement stmt) {
        try {
            stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS BankInfo " +
                    "(userName VARCHAR(20) not NULL , " +
                    "firstName VARCHAR(40) not null,"+
                    "lastName VARCHAR(40) not null,"+
                    "cardID VARCHAR(30) not null,"+
                    "cvv2 INT not null,"+
                    "expirationDate date not null,"+
                    "email VARCHAR(80) not null,"+
                    "amountUSD double not null)";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public static void insertRowWithPreparedStatement(String userName, String firstName, String lastName,String cardID,int cvv2,Date expirationDate,String email, Double amountUSD) {
        String sql = "INSERT INTO BankInfo (userName,firstName,lastName,cardID,cvv2,expirationDate,email,amountUSD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, cardID);
            pstmt.setInt(5, cvv2);
            pstmt.setDate(6, expirationDate);
            pstmt.setString(7, email);
            pstmt.setDouble(8, amountUSD);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting row", e);
        }
    }
}
