package DatabaseConnection;

import java.sql.*;

public class TransferConnection {
    public static Connection con;
    public static void createConnection(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "h9026666518");
            System.out.print("DataBas connection success");
            Statement query = con.createStatement();
            query.executeUpdate("CREATE SCHEMA IF NOT EXISTS BitFUM");
            System.out.println("Schema created successfully!");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BitFUM", "root", "h9026666518");
            createTable(con, query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void createTable(Connection con, Statement stmt) {
        try {
            stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS TransferInfo " +
                    "(userName VARCHAR(20) not NULL primary key, " +
                    "Date date not null,"+
                    "Time time(6) not null,"+
                    "DestinationWalletID VARCHAR(20) not null,"+
                    "currencyName VARCHAR(10),"+
                    "currencyAmount double DEFAULT 0,"+
                    "IssueTracking INT DEFAULT 0,";

            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public static void insertRowWithPreparedStatement(String userName, Date date, Time time, String DestinationWalletID, String currencyName, Double currencyAmount,int IssueTracking) {
        String sql = "INSERT INTO UsersInfo (userName, date, time, DestinationWalletID, currencyName, currencyAmount,IssueTracking) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setDate(2, date);
            pstmt.setTime(3, time);
            pstmt.setString(4, DestinationWalletID);
            pstmt.setString(5, currencyName);
            pstmt.setDouble(6, currencyAmount);
            pstmt.setInt(7, IssueTracking);
            System.out.println("Row inserted successfully using PreparedStatement");
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting row", e);
        }
    }
}

