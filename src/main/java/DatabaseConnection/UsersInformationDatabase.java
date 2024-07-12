package DatabaseConnection;


import Home.HomePage;
import kotlin.random.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static DatabaseConnection.UsersInformationDatabase.con;
import static Person.User.*;

public class UsersInformationDatabase {
    public static Connection con;
    public static void createConnection(){
        System.out.print("create connect");
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "h9026666518");
            System.out.print("DataBas connection success");
            Statement query = con.createStatement();
            query.executeUpdate("CREATE SCHEMA IF NOT EXISTS BitFUM");
            System.out.println("Schema created successfully!");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BitFUM", "root", "h9026666518");
            createTable(con, query);
            TransferDatabase.createTransferTable(con, query);
            SwapDatabase.createSwapTable(con, query);
            BankDatabase.createBankTable(con,query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void createTable(Connection con, Statement stmt) {
        try {
            stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS UsersInfo " +
                    "(userName VARCHAR(20) not NULL primary key, " +
                    "firstName VARCHAR(40) not null,"+
                    "LastName VARCHAR(40) not null,"+
                    "password VARCHAR(30) not null,"+
                    "phoneNumber VARCHAR(11) not null,"+
                    "email VARCHAR(80) not null,"+
                    "profileImage VARCHAR(500),"+
                    "USD DOUBLE DEFAULT 0,"+
                    "EUR DOUBLE DEFAULT 0,"+
                    "TOMAN DOUBLE DEFAULT 0,"+
                    "YEN DOUBLE DEFAULT 0,"+
                    "GBP DOUBLE DEFAULT 0,"+
                    "WalletID VARCHAR(40) not null)";
            stmt.executeUpdate(sql);
            String exchangeTable = "CREATE TABLE IF NOT EXISTS ExchangeInfo"+
                    "(UserSeller VARCHAR(20), UserBuyer VARCHAR(40), " +
                    "CurrencyName VARCHAR(40) not null, CurrencyPrice DOUBLE," +
                    " CurrencyAmount DOUBLE, Date DATE , Time TIME(6), TotalAmountSeller DOUBLE," +
                    " TotalAmountBuyer Double, SellerCode INT, BuyerCode INT)";
            stmt.executeUpdate(exchangeTable);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public static void createMoney(){
        String UpdateTable = "UPDATE UsersInfo SET USD = ?, EUR = ?, TOMAN = ?, GBP = ?, YEN = ?, WalletID = ? " +
                "WHERE  userName = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
            preparedStatement.setDouble(1, 2000);
            preparedStatement.setDouble(2, 1000);
            preparedStatement.setDouble(3, 200);
            preparedStatement.setDouble(4, 500);
            preparedStatement.setDouble(5, 350);
            preparedStatement.setString(6, "123 123 123");
            preparedStatement.setString(7, "hanyia");
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected(2): " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertRowWithPreparedStatement(String userName, String firstName, String lastName,
                                                      String password, String phoneNumber, String email, String profileImage, String WalletID) {
        String sql = "INSERT INTO UsersInfo (userName, firstName, LastName, password, phoneNumber, email, profileImage, WalletID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, password);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, email);
            pstmt.setString(8, WalletID);
            if (profileImage != null) {
                pstmt.setString(7, profileImage);
                pstmt.executeUpdate();
            } else {
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting row", e);
        }
    }

    public static void insertRowExchange(String CurrencyName, String UserBuyer, String UserSeller,
                                         double CurrencyPrice, double Amount, Date date, Time time, int number,
                                         double totalAmountBuyer, double totalAmountSeller, int BuyerCode, int SellerCode){
        if(number==1) {//for sel
            String sql = "INSERT INTO ExchangeInfo (UserSeller, CurrencyName, CurrencyPrice, CurrencyAmount, TotalAmountSeller, SellerCode)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, UserSeller);
                pstmt.setString(2, CurrencyName);
                pstmt.setDouble(3, CurrencyPrice);
                pstmt.setDouble(4, Amount);
                pstmt.setDouble(5, totalAmountSeller);
                pstmt.setInt(6, SellerCode);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting row", e);
            }
        } else{//for buy
            String sql = "INSERT INTO ExchangeInfo (UserBuyer, CurrencyName, CurrencyPrice, CurrencyAmount, TotalAmountBuyer, BuyerCode)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, UserBuyer);
                pstmt.setString(2, CurrencyName);
                pstmt.setDouble(3, CurrencyPrice);
                pstmt.setDouble(4, Amount);
                pstmt.setDouble(5, totalAmountBuyer);
                pstmt.setDouble(6, BuyerCode);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting row", e);
            }
        }

    }

    public static double UpdateExchangeInfo(String userSeller, String userBuyer, double sellerAmount,
                                            double buyerAmount, double sellerPrice,
                                            double buyerPrice, String CurrencyName, int Number, double totalAmountBuyer,
                                            double totalAmountSeller, int BuyerCode, int SellerCode) throws SQLException {
        String currencyName = null;
        Double usd = 0.0;
        double  DataList_index = 0;
        double DataList_currency=0;
        CurrencyDatabase connectNow = new CurrencyDatabase();
        Connection connectDB = connectNow.getDBConnection();
        String LastSql = "SELECT * FROM BitFUM.currency ORDER BY STR_TO_DATE(CONCAT(Date, ' ', Time), '%Y-%m-%d %H:%i:%s') DESC LIMIT 1";
        Statement stLast = connectDB.createStatement();
        ResultSet Last = stLast.executeQuery(LastSql);
        while (Last.next()){
            DataList_index = Last.getDouble("USD");
            DataList_currency = Last.getDouble(CurrencyName);
        }
        double price;
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        double totalAdminBalance = 0.0;
        if(buyerAmount>sellerAmount){
            if(Number==1) {//buying now
                String UpdateTable = "UPDATE ExchangeInfo SET UserBuyer = ?, CurrencyPrice = ?, CurrencyAmount = ?, Date = ?, Time = ?, BuyerCode = ?, TotalAmountBuyer = ? " +
                        "WHERE UserSeller = ? AND CurrencyName = ? AND CurrencyPrice = ? AND SellerCode = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                System.out.print(userSeller+" is user seller."+userBuyer+" is user buyer in 1\n");
                // Set the parameters for the update statement
                totalAdminBalance+=price*sellerAmount/100;
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userBuyer);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDouble(3, sellerAmount);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, Time.valueOf(localTime));
                    preparedStatement.setInt(6, BuyerCode);
                    preparedStatement.setDouble(7, totalAmountBuyer);
                    preparedStatement.setString(8, userSeller);
                    preparedStatement.setString(9, CurrencyName);
                    preparedStatement.setDouble(10, sellerPrice);
                    preparedStatement.setInt(11, SellerCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(1): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else{//selling now
                String UpdateTable = "UPDATE ExchangeInfo SET UserSeller = ?, CurrencyPrice = ?, CurrencyAmount = ?, Date = ?, Time = ?, SellerCode = ?, TotalAmountSeller = ? " +
                        "WHERE UserBuyer = ? AND CurrencyName = ? AND CurrencyPrice = ? AND BuyerCode = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                System.out.print(userSeller+" is user seller."+userBuyer+" is user buyer in 2\n");
                totalAdminBalance+=price*sellerAmount/100;
                // Set the parameters for the update statement
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userSeller);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDouble(3, sellerAmount);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, Time.valueOf(localTime));
                    preparedStatement.setInt(6, SellerCode);
                    preparedStatement.setDouble(7, totalAmountSeller);
                    preparedStatement.setString(8, userBuyer);
                    preparedStatement.setString(9, CurrencyName);
                    preparedStatement.setDouble(10, buyerPrice);
                    preparedStatement.setInt(11, BuyerCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(2): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            String UpdateTable = null;
            if(CurrencyName.equals("USD")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "USD";
            }
            if(CurrencyName.equals("YEN")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET YEN = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "YEN";
            }
            if(CurrencyName.equals("TOMAN")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET TOMAN = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "TOMAN";
            }
            if(CurrencyName.equals("GBP")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET GBP = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "GBP";
            }
            if(CurrencyName.equals("EUR")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET EUR = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "EUR";
            }
            Double amount = 0.0;
            Statement stmt = con.createStatement();
            ResultSet findAmount = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
            while (findAmount.next()){
                if (userSeller.equals(findAmount.getString("userName"))){
                    amount = findAmount.getDouble(currencyName);
                    usd = findAmount.getDouble("USD");
                }
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                preparedStatement.setDouble(1, amount-sellerAmount);
                preparedStatement.setDouble(2, usd + sellerAmount*(DataList_index/
                        DataList_currency));
                preparedStatement.setString(3, userSeller);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected(1):** " + rowsAffected);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Statement stm = con.createStatement();
            ResultSet findAmount1 = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
            while (findAmount1.next()){
                if (userBuyer.equals(findAmount1.getString("userName"))){
                    amount = findAmount1.getDouble(currencyName);
                    usd = findAmount1.getDouble("USD");
                }
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                preparedStatement.setDouble(1, amount+sellerAmount);
                preparedStatement.setDouble(2, usd - sellerAmount*(DataList_index/
                        DataList_currency));
                preparedStatement.setString(3, userBuyer);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected(2):** " + rowsAffected);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.print(BuyerCode+" is buyer code");
            insertRowExchange(CurrencyName, userBuyer, null, buyerPrice, buyerAmount - sellerAmount,
                    null, null, 2, totalAmountBuyer, 0, BuyerCode, 0);
        } else if(buyerAmount<sellerAmount){
            if(Number==1) {//buying now
                String UpdateTable = "UPDATE ExchangeInfo SET UserBuyer = ?, CurrencyPrice = ?, CurrencyAmount = ?, Date = ?, Time = ?, BuyerCode = ?, TotalAmountBuyer = ? " +
                        "WHERE UserSeller = ? AND CurrencyName = ? AND CurrencyPrice = ? AND SellerCode = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                System.out.print(userSeller+" is user seller."+userBuyer+" is user buyer in 3\n");
                // Set the parameters for the update statement
                totalAdminBalance+=price*buyerAmount/100;
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userBuyer);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDouble(3, buyerAmount);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, Time.valueOf(localTime));
                    preparedStatement.setInt(6, BuyerCode);
                    preparedStatement.setDouble(7, totalAmountBuyer);
                    preparedStatement.setString(8, userSeller);
                    preparedStatement.setString(9, CurrencyName);
                    preparedStatement.setDouble(10, sellerPrice);
                    preparedStatement.setDouble(11, SellerCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(3): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else{//selling now
                String UpdateTable = "UPDATE ExchangeInfo SET UserSeller = ?, CurrencyPrice = ?, CurrencyAmount = ?, Date = ?, Time = ?, SellerCode = ?, TotalAmountSeller = ? " +
                        "WHERE UserBuyer = ? AND CurrencyName = ? AND CurrencyPrice = ? AND BuyerCode = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                System.out.print(userSeller+" is user seller."+userBuyer+" is user buyer in 4\n");
                totalAdminBalance+=price*buyerAmount/100;
                // Set the parameters for the update statement
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userSeller);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDouble(3, buyerAmount);
                    preparedStatement.setDate(4, date);
                    preparedStatement.setTime(5, Time.valueOf(localTime));
                    preparedStatement.setInt(6, SellerCode);
                    preparedStatement.setDouble(7, totalAmountSeller);
                    preparedStatement.setString(8, userBuyer);
                    preparedStatement.setString(9, CurrencyName);
                    preparedStatement.setDouble(10, buyerPrice);
                    preparedStatement.setInt(11, BuyerCode);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(4): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            String UpdateTable=null;
            if(CurrencyName.equals("USD")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "USD";
            }
            if(CurrencyName.equals("YEN")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET YEN = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "YEN";
            }
            if(CurrencyName.equals("TOMAN")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET TOMAN = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "TOMAN";
            }
            if(CurrencyName.equals("GBP")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET GBP = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "GBP";
            }
            if(CurrencyName.equals("EUR")){
                UpdateTable = "UPDATE BitFUM.UsersInfo SET EUR = ? , USD = ?" +
                        "WHERE userName = ?";
                currencyName = "EUR";
            }
            Double amount = 0.0;
            Statement stmt = con.createStatement();
            ResultSet findAmount = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
            while (findAmount.next()){
                if (userSeller.equals(findAmount.getString("userName"))){
                    amount = findAmount.getDouble(currencyName);
                    usd = findAmount.getDouble("USD");
                }
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                preparedStatement.setDouble(1, amount-buyerAmount);
                preparedStatement.setDouble(2, usd + buyerAmount*(DataList_index/
                        DataList_currency));
                preparedStatement.setString(3, userSeller);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected(3):** " + rowsAffected);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Statement stm = con.createStatement();
            ResultSet findAmount1 = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
            while (findAmount1.next()){
                if (userBuyer.equals(findAmount1.getString("userName"))){
                    amount = findAmount1.getDouble(currencyName);
                    usd = findAmount1.getDouble("USD");
                }
            }
            try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                preparedStatement.setDouble(1, amount+buyerAmount);
                preparedStatement.setDouble(2, usd - buyerAmount*(DataList_index/
                        DataList_currency));
                preparedStatement.setString(3, userBuyer);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected(4):** " + rowsAffected);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.print(SellerCode+" is seller code");
            insertRowExchange(CurrencyName, null, userSeller, sellerPrice, sellerAmount-buyerAmount,
                    null, null, 1, 0, totalAmountSeller, 0, SellerCode);
        } else if(buyerAmount==sellerAmount){
            if(Number==1) {//buying now
                String UpdateTable = "UPDATE ExchangeInfo SET UserBuyer = ?, CurrencyPrice = ?, Date = ?, Time = ?, BuyerCode = ?, TotalAmountBuyer = ? " +
                        "WHERE UserSeller = ? AND CurrencyName = ? AND CurrencyPrice = ? AND SellerCode = ? AND TotalAmountSeller = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                totalAdminBalance+=price*buyerAmount/100;

                // Set the parameters for the update statement
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userBuyer);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDate(3, date);
                    preparedStatement.setTime(4, Time.valueOf(localTime));
                    preparedStatement.setInt(5, BuyerCode);
                    preparedStatement.setDouble(6, totalAmountBuyer);
                    preparedStatement.setString(7, userSeller);
                    preparedStatement.setString(8, CurrencyName);
                    preparedStatement.setDouble(9, sellerPrice);
                    preparedStatement.setInt(10, SellerCode);
                    preparedStatement.setDouble(11, totalAmountSeller);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(5): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(CurrencyName.equals("USD")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "USD";
                }
                if(CurrencyName.equals("YEN")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET YEN = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "YEN";
                }
                if(CurrencyName.equals("TOMAN")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET TOMAN = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "TOMAN";
                }
                if(CurrencyName.equals("GBP")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET GBP = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "GBP";
                }
                if(CurrencyName.equals("EUR")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET EUR = ?, USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "EUR";
                }
                Double amount = 0.0;
                Statement stmt = con.createStatement();
                ResultSet findAmount = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                while (findAmount.next()){
                    if (userSeller.equals(findAmount.getString("userName"))){
                        amount = findAmount.getDouble(currencyName);
                        usd = findAmount.getDouble("USD");
                    }
                }
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                    preparedStatement.setDouble(1, amount-sellerAmount);
                    preparedStatement.setDouble(2, usd + sellerAmount*(DataList_index/
                            DataList_currency));
                    preparedStatement.setString(3, userSeller);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(5):** " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Statement stm = con.createStatement();
                ResultSet findAmount1 = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                while (findAmount1.next()){
                    if (userBuyer.equals(findAmount1.getString("userName"))){
                        amount = findAmount1.getDouble(currencyName);
                        usd = findAmount1.getDouble("USD");
                    }
                }
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                    preparedStatement.setDouble(1, amount+buyerAmount);
                    preparedStatement.setDouble(2, usd - buyerAmount*(DataList_index/
                            DataList_currency));
                    preparedStatement.setString(3, userBuyer);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(6):** " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else{//selling now
                String UpdateTable = "UPDATE ExchangeInfo SET UserSeller = ?, CurrencyPrice = ?, Date = ?, Time = ?, TotalAmountSeller = ?, SellerCode = ? " +
                        "WHERE UserBuyer = ? AND CurrencyName = ? AND CurrencyPrice = ? AND BuyerCode = ? AND TotalAmountBuyer = ?";
                if(buyerPrice>sellerPrice) price=sellerPrice;
                else price=buyerPrice;
                totalAdminBalance+=price*buyerAmount/100;
                // Set the parameters for the update statement
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable);){
                    preparedStatement.setString(1, userSeller);
                    preparedStatement.setDouble(2, price);
                    preparedStatement.setDate(3, date);
                    preparedStatement.setTime(4, Time.valueOf(localTime));
                    preparedStatement.setDouble(5, sellerAmount);
                    preparedStatement.setInt(6, SellerCode);
                    preparedStatement.setString(7, userBuyer);
                    preparedStatement.setString(8, CurrencyName);
                    preparedStatement.setDouble(9, buyerPrice);
                    preparedStatement.setInt(10, BuyerCode);
                    preparedStatement.setDouble(11, totalAmountBuyer);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(6): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(CurrencyName.equals("USD")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "USD";
                }
                if(CurrencyName.equals("YEN")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET YEN = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "YEN";
                }
                if(CurrencyName.equals("TOMAN")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET TOMAN = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "TOMAN";
                }
                if(CurrencyName.equals("GBP")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET GBP = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "GBP";
                }
                if(CurrencyName.equals("EUR")){
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET EUR = ? , USD = ?" +
                            "WHERE userName = ?";
                    currencyName = "EUR";
                }
                Double amount = 0.0;
                Statement stmt = con.createStatement();
                ResultSet findAmount = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                while (findAmount.next()){
                    if (userSeller.equals(findAmount.getString("userName"))){
                        amount = findAmount.getDouble(currencyName);
                        usd = findAmount.getDouble("USD");
                    }
                }
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                    preparedStatement.setDouble(1, amount-sellerAmount);
                    preparedStatement.setDouble(2, usd + sellerAmount*(DataList_index/
                            DataList_currency));
                    preparedStatement.setString(3, userSeller);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(1): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Statement stm = con.createStatement();
                ResultSet findAmount1 = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                while (findAmount1.next()){
                    if (userBuyer.equals(findAmount1.getString("userName"))){
                        amount = findAmount1.getDouble(currencyName);
                        usd = findAmount1.getDouble("USD");
                    }
                }
                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                    preparedStatement.setDouble(1, amount+buyerAmount);
                    preparedStatement.setDouble(2, usd - buyerAmount*(DataList_index/
                            DataList_currency));
                    preparedStatement.setString(3, userBuyer);
                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println("Rows affected(2): " + rowsAffected);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return totalAdminBalance;
    }
}

