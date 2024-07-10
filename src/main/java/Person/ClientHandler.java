package Person;

import SerializableClasses.*;
import DatabaseConnection.BankDatabase;
import DatabaseConnection.SwapDatabase;
import DatabaseConnection.TransferDatabase;
import Home.HomePage;
import TableDataInformation.BankTableData;
import Trades.Currency;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DatabaseConnection.UsersInformationDatabase.*;


public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    DecimalFormat df =new DecimalFormat("#.###");

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    private final static Map<String, String> userStatusMap = new HashMap<>();
    public static boolean openMarket=true;
    @Override
    public void run() {
        System.out.println("Client handler started...");
        boolean existUser = false;
        Object inputObject;

        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
             DataInputStream data = new DataInputStream(clientSocket.getInputStream());
             OutputStream outputString = clientSocket.getOutputStream();
             PrintWriter writer = new PrintWriter(outputString, true);
             InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while (true) {
                int number = data.readInt();
                System.out.print("number is :"+ number+"\n");
                /////////////////////////for signing up
                if (number==1) {
                    inputObject = input.readObject();
                    Person receivedObject = (Person) inputObject;
                    insertRowWithPreparedStatement(receivedObject.UserName, receivedObject.name, receivedObject.lastName, receivedObject.Password, receivedObject.phoneNumber, receivedObject.email, receivedObject.profilePass, receivedObject.WalletID);
                }
                /////////////////for login users
                else if (number==2) {
                    inputObject = input.readObject();
                    String receivedString = (String) inputObject;
                    Statement stmt = con.createStatement();
                    ResultSet users = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (users.next()) {
                        if (receivedString.equals(users.getString("userName").concat(users.getString("password")))) {
                            existUser = true;
                        }
                    }
                    output.writeObject(existUser);
                    System.out.print(existUser);
                    output.flush();
                }
                ////////////////////exist user
                else if(number==3){
                    existUser = true;
                    inputObject = input.readObject();
                    String receivedString = (String) inputObject;
                    Statement stmt = con.createStatement();
                    ResultSet users = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (users.next()) {
                        if (receivedString.equals(users.getString("userName"))) {
                            existUser = false;
                        }
                    }
                    output.writeObject(existUser);
                    System.out.print(existUser);
                    output.flush();
                }
                /////////////////////////send user
                else if (number==5){
                    Person existPerson = null;
                    inputObject = input.readObject();
                    String receivedString = (String) inputObject;
                    Statement stmt = con.createStatement();
                    ResultSet users = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (users.next()) {
                        if (receivedString.equals(users.getString("userName"))) {
                            existPerson = new Person(users.getString("userName"),
                                    users.getString("firstName"), users.getString("LastName"),
                                    users.getString("password"), users.getString("phoneNumber"),
                                    users.getString("email"), users.getString("profileImage"),
                                    users.getString("WalletID"));
                        }
                    }
                    output.writeObject(existPerson);
                    output.flush();
                }
                ////////////////for buying in exchange
                else if(number == 6){
                    boolean possible = false;
                    boolean balance =false;
                    inputObject = input.readObject();
                    String userName = (String) inputObject;
                    inputObject = input.readObject();
                    String CurrencyName = (String) inputObject;
                    Double amount = data.readDouble();
                    Double price = data.readDouble();
                    Double totalAmountBuyer = amount;
                    Random rand = new Random();
                    int BuyerCode = rand.nextInt(100000);
                    Statement st = con.createStatement();
                    ResultSet balances = st.executeQuery("SELECT * FROM BitFUM.usersinfo");
                    while (balances.next() && !balance && openMarket){
                        if(userName.equals(balances.getString("userName"))){
                            if(amount<=balances.getDouble("USD")) {
                                balance = true;
                            }
                        }
                    }
                    double wage=0;
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next() && !possible && balance && openMarket) {
                        if (CurrencyName.equals(exchanges.getString("CurrencyName")) && exchanges.getString("UserBuyer")==null) {
                            if(price >= exchanges.getDouble("CurrencyPrice")){
                                wage=UpdateExchangeInfo(exchanges.getString("UserSeller"), userName, exchanges.getDouble("CurrencyAmount"),
                                        amount, exchanges.getDouble("CurrencyPrice"), price, CurrencyName, 1,
                                        totalAmountBuyer, exchanges.getDouble("TotalAmountSeller"), BuyerCode, exchanges.getInt("SellerCode"));
                                possible=true;
                                Statement stm = con.createStatement();
                                double Wage = wage;
                                double USD = 0.0;
                                String user = "Admin";
                                String UpdateTable;
                                ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                                while (queryOutput.next()) {
                                    if(queryOutput.getString("userName").equals(user)){
                                        USD=queryOutput.getDouble("USD");
                                    }
                                }
                                UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " +
                                        "WHERE userName = ?";
                                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                                    preparedStatement.setDouble(1,Wage+USD);
                                    preparedStatement.setString(2, user);
                                    preparedStatement.executeUpdate();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if(!possible && balance && openMarket){
                        insertRowExchange(CurrencyName, userName, null, price, amount, null, null, 2,
                                totalAmountBuyer, 0, BuyerCode, 0);
                    }
                    System.out.print(balance+" is balance  "+possible+" is possible in buying\n");
                    if(!openMarket) balance=false;
                    output.writeObject(balance);
                }
                /////////////////for selling in exchange
                else if(number==7){
                    boolean possible =false;
                    boolean balance = false;
                    inputObject = input.readObject();
                    String userName = (String) inputObject;
                    inputObject = input.readObject();
                    String CurrencyName = (String) inputObject;
                    Double amount = data.readDouble();
                    Double price = data.readDouble();
                    Double totalAmountSeller = amount;
                    Random rand = new Random();
                    int SellerCode = rand.nextInt(100000);
                    Statement st = con.createStatement();
                    ResultSet balances = st.executeQuery("SELECT * FROM BitFUM.usersinfo");
                    while (balances.next() && !balance &&openMarket){
                        if(userName.equals(balances.getString("userName"))){
                            if(CurrencyName.equals("GBP")){
                                if(amount<=balances.getDouble("GBP"))balance =true;
                            } else if(CurrencyName.equals("YEN")){
                                if(amount<=balances.getDouble("YEN"))balance =true;
                            } else if(CurrencyName.equals("TOMAN")){
                                if(amount<=balances.getDouble("TOMAN"))balance =true;
                            } else if(CurrencyName.equals("EUR")){
                                if(amount<=balances.getDouble("EUR"))balance =true;
                            }
                        }
                    }
                    double wage=0;
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next() && !possible && balance && openMarket) {
                        if (CurrencyName.equals(exchanges.getString("CurrencyName")) && exchanges.getString("UserSeller")==null) {
                            System.out.print("selling saved!\n");
                            if(price <= exchanges.getDouble("CurrencyPrice")){
                                wage = UpdateExchangeInfo(userName, exchanges.getString("UserBuyer"), amount, exchanges.getDouble("CurrencyAmount"), price,
                                        exchanges.getDouble("CurrencyPrice"), CurrencyName, 2,
                                        exchanges.getDouble("TotalAmountBuyer"), totalAmountSeller, exchanges.getInt("BuyerCode"), SellerCode);
                                possible = true;
                                Statement stm = con.createStatement();
                                double Wage = wage;
                                double USD = 0.0;
                                String user = "Admin";
                                String UpdateTable;
                                ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                                while (queryOutput.next()) {
                                    if(queryOutput.getString("userName").equals(user)){
                                        USD=queryOutput.getDouble("USD");
                                    }
                                }
                                UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " +
                                        "WHERE userName = ?";
                                try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                                    preparedStatement.setDouble(1,Wage+USD);
                                    preparedStatement.setString(2, user);
                                    preparedStatement.executeUpdate();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    if(!possible && balance && openMarket){
                        insertRowExchange(CurrencyName, null, userName, price, amount, null, null, 1,
                                0, totalAmountSeller, 0, SellerCode);
                    }
                    System.out.print(balance+" is balance  "+possible+" is possible in selling\n");
                    if(!openMarket) balance=false;
                    output.writeObject(balance);
                }
                //////////////////////most traded currency
                else if (number == 8) {
                    int TOMANNumber = 0;
                    int GBPNumber = 0;
                    int EURNumber = 0;
                    int YENNumber = 0;
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next()){
                        if(exchanges.getString("UserSeller")!=null && exchanges.getString("UserBuyer")!=null){
                            if("YEN".equals(exchanges.getString("CurrencyName"))) YENNumber++;
                            if("GBP".equals(exchanges.getString("CurrencyName"))) GBPNumber++;
                            if("EUR".equals(exchanges.getString("CurrencyName"))) EURNumber++;
                            if("TOMAN".equals(exchanges.getString("CurrencyName"))) TOMANNumber++;
                        }
                    }
                    if(YENNumber==0&&GBPNumber==0&&EURNumber==0&&TOMANNumber==0) writer.println("null");
                    else {
                        int[] numbers = {YENNumber, GBPNumber, EURNumber, TOMANNumber};
                        int largest = numbers[0];
                        int indexOfLargest = 0;
                        for (int i = 1; i < numbers.length; i++) {
                            if (numbers[i] > largest) {
                                largest = numbers[i];
                                indexOfLargest = i;
                            }
                        }
                        if (indexOfLargest == 0) {
                            writer.println("YEN");
                        } else if (indexOfLargest == 1) {
                            writer.println("GBP");
                        } else if (indexOfLargest == 2) {
                            writer.println("EUR");
                        } else if (indexOfLargest == 3) {
                            writer.println("TOMAN");
                        }
                    }
                    writer.flush();
                }
                ///////////////////////////////////////////// find wallet id (for transfer )
                else if(number==10){
                    String WalletID =(String) input.readObject();;
                    String name="" ;
                    String lastName="";
                    boolean existWallet = false;
                    Statement stmt = con.createStatement();
                    ResultSet users = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (users.next() && !existWallet) {
                        if (WalletID.equals(users.getString("WalletID"))) {
                            existWallet = true;
                            name = users.getString("firstName");
                            lastName = users.getString("LastName");
                        }
                    }
                    writer.println(existWallet+" "+name+" "+lastName);
                    writer.flush();
                }
                /////////////////////////////////insert transfer
                else if(number==11){
                    TransferTableData2 transfer = (TransferTableData2) input.readObject();
                    String UpdateTable = null;
                    String currencyName = null;
                    if(transfer.CurrencyName1.equals("USD")){
                        UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ?" +
                                "WHERE WalletID = ?";
                        currencyName = "USD";
                    }
                    if(transfer.CurrencyName1.equals("YEN")){
                        UpdateTable = "UPDATE BitFUM.UsersInfo SET YEN = ?" +
                                "WHERE WalletID = ?";
                        currencyName = "YEN";
                    }
                    if(transfer.CurrencyName1.equals("TOMAN")){
                        UpdateTable = "UPDATE BitFUM.UsersInfo SET TOMAN = ? " +
                                "WHERE WalletID = ?";
                        currencyName = "TOMAN";
                    }
                    if(transfer.CurrencyName1.equals("GBP")){
                        UpdateTable = "UPDATE BitFUM.UsersInfo SET GBP = ? " +
                                "WHERE WalletID = ?";
                        currencyName = "GBP";
                    }
                    if(transfer.CurrencyName1.equals("EUR")){
                        UpdateTable = "UPDATE BitFUM.UsersInfo SET EUR = ?" +
                                "WHERE WalletID = ?";
                        currencyName = "EUR";
                    }

                    Double amount = 0.0;
                    Double amount1 = 0.0;
                    Statement stmt = con.createStatement();
                    ResultSet findAmount = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (findAmount.next()){
                        if (transfer.SourceWalletID1.equals(findAmount.getString("WalletID"))){
                            amount = findAmount.getDouble(currencyName);
                            System.out.println("amount"+amount);
                        }
                    }
                    ResultSet findAmount1 = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (findAmount1.next()){
                        if (transfer.DestinationWalletID1.equals(findAmount1.getString("WalletID"))){
                            amount1 = findAmount1.getDouble(currencyName);
                        }
                    }
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1, amount-transfer.CurrencyAmount1);
                        preparedStatement.setString(2, transfer.SourceWalletID1);
                        int rowsAffected = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1, amount1+transfer.CurrencyAmount1);
                        preparedStatement.setString(2, transfer.DestinationWalletID1);
                        int rowsAffected = preparedStatement.executeUpdate();
                        System.out.println("Rows affected(1): " + rowsAffected);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    TransferDatabase.insertRowWithPreparedStatement(transfer.userName, transfer.getDate(), transfer.getTime(), transfer.SourceWalletID1,
                            transfer.DestinationWalletID1, transfer.CurrencyName1, transfer.CurrencyAmount1, transfer.issueTracking1);
                }
                ////////////////////////////////////////////////// make transfer list
                else if(number==12){
                    String userName = reader.readLine();
                    String WalletID = reader.readLine();
                    ArrayList<TransferTableData2> transfers = new ArrayList<>();
                    ArrayList<String> MyDestinationAddressList = new ArrayList<>();
                    TransferLists transferLists = new TransferLists(transfers, MyDestinationAddressList);
                    String CurrencyViewQuery = "SELECT  UserName,RecordedDate,RecordedTime,SourceWalletID,DestinationWalletID,CurrencyName,CurrencyAmount,IssueTracking FROM BitFUM.TransferInfo";
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet queryOutput = stmt.executeQuery(CurrencyViewQuery);
                        while (queryOutput.next()){
                            if(userName.equals(queryOutput.getObject("UserName",String.class)) || WalletID.equals(queryOutput.getObject("DestinationWalletID",String.class))) {
                                Date date = queryOutput.getDate("RecordedDate");
                                Time time = queryOutput.getTime("RecordedTime");
                                String SourceID = queryOutput.getString("SourceWalletID");
                                String DestinationID = queryOutput.getString("DestinationWalletID");
                                String CurrencyName = queryOutput.getString("CurrencyName");
                                Double CurrencyAmount = queryOutput.getDouble("CurrencyAmount");
                                Integer IssueTracking = queryOutput.getInt("IssueTracking");
                                TransferTableData2 transferData = new TransferTableData2(date,time,SourceID,DestinationID,CurrencyName,CurrencyAmount,IssueTracking, userName);
                                transferLists.transfers.add(transferData);
                                if(!DestinationID.equals(WalletID)) {
                                    boolean find=false;
                                    for(String ID : transferLists.MyDestinationAddressList){
                                        if(DestinationID.equals(ID))  find=true;
                                    }
                                    if(!find) transferLists.MyDestinationAddressList.add(DestinationID);
                                }
                            }
                        }
                        output.writeObject(transferLists);
                        output.flush();
                    } catch (SQLException e) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, e);
                        e.printStackTrace();
                    }
                }
                /////////////////////check Balance In Exchange
                else if(number==13){
                    String userName = (String) input.readObject();
                    String CurrencyName = (String) input.readObject();
                    int value = data.readInt();
                    double TotalAmount = 0;
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next()){
                        if(userName.equals(exchanges.getString("UserSeller")) &&
                                exchanges.getString("UserBuyer")==null && !CurrencyName.equals("USD")) {
                            TotalAmount+=exchanges.getDouble("CurrencyAmount");
                        } else if(userName.equals(exchanges.getString("UserBuyer"))
                                && CurrencyName.equals("USD") && exchanges.getString("UserSeller") == null){
                            TotalAmount+=exchanges.getDouble("CurrencyAmount")*exchanges.getDouble("CurrencyPrice");
                        }
                    }
                    if(TotalAmount>value){
                        output.writeObject(false);
                    }
                    else{
                        output.writeObject(true);
                    }
                    output.flush();
                }
                /////////////////////////////////////fill wallet
                else if(number==14){
                    String userName = reader.readLine();
                    ArrayList<Currency> Currency = new ArrayList<>();
                    Statement stmt = con.createStatement();
                    ResultSet currency = stmt.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (currency.next()){
                        if(userName.equals(currency.getString("userName"))){
                            Currency.add(0, new Currency("USD", currency.getDouble("USD")));
                            Currency.add(1, new Currency("EUR", currency.getDouble("EUR")));
                            Currency.add(2, new Currency("TOMAN", currency.getDouble("TOMAN")));
                            Currency.add(3, new Currency("YEN", currency.getDouble("YEN")));
                            Currency.add(4, new Currency("GBP", currency.getDouble("GBP")));
                        }
                    }
                    output.writeObject(Currency);
                }
                //for filling the table that shows the open trades (selling)
                else if(number == 15){
                    String CurrencyName = reader.readLine();
                    ArrayList<ExchangeList> Exchange = new ArrayList<>();
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next()){
                        if((exchanges.getString("UserSeller") != null) && (exchanges.getString("UserBuyer") == null) &&
                                exchanges.getString("CurrencyName").equals(CurrencyName)){
                            Exchange.add(new ExchangeList(exchanges.getDouble("CurrencyAmount"), exchanges.getDouble("CurrencyPrice")));
                        }
                    }
                    output.writeObject(Exchange);
                }
                //for filling the table that shows the open trades (buying)
                else if(number == 16){
                    String CurrencyName = reader.readLine();
                    ArrayList<ExchangeList> Exchange = new ArrayList<>();
                    Statement stmt = con.createStatement();
                    ResultSet exchanges = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchanges.next()){
                        if((exchanges.getString("UserSeller") == null) && (exchanges.getString("UserBuyer") != null) &&
                                exchanges.getString("CurrencyName").equals(CurrencyName)){
                            Exchange.add(new ExchangeList(exchanges.getDouble("CurrencyAmount"), exchanges.getDouble("CurrencyPrice")));
                        }
                    }
                    output.writeObject(Exchange);
                }
                ////////////////////////////////////////////////////////for the fillExchangeOrders
                else if(number==17){
                    boolean find =true;
                    int code;
                    String userName = reader.readLine();
                    ArrayList<TradesList> Trades = new ArrayList<>();
                    ArrayList<Integer> list = new ArrayList<>();
                    Statement stmt = con.createStatement();
                    ResultSet selling = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (selling.next()){//for selling list
                        find = true;
                        if(userName.equals(selling.getString("UserSeller"))){
                            for (int x : list){
                                if(x == selling.getInt("SellerCode")) find=false;
                            }
                            if(find) {
                                double totalAmount = 0, amount = 0, rate = 0;
                                list.add(selling.getInt("SellerCode"));
                                code = selling.getInt("SellerCode");
                                Statement st = con.createStatement();
                                ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                                while (exchange.next()) {
                                    if (code == exchange.getInt("SellerCode")){
                                        totalAmount = exchange.getDouble("TotalAmountSeller");
                                        rate = exchange.getDouble("CurrencyPrice");
                                        if(exchange.getString("UserBuyer")!=null){
                                            amount+=exchange.getDouble("CurrencyAmount");
                                        }
                                    }
                                }
                                Trades.add(new TradesList(1, selling.getString("CurrencyName"),totalAmount, rate, (float) ((amount/totalAmount)*100)));
                            }
                        }
                    }
                    find = true;
                    Statement stm = con.createStatement();
                    ResultSet buying = stm.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (buying.next()){
                        find = true;
                        if(userName.equals(buying.getString("UserBuyer"))){
                            for (int x : list){
                                if(x == buying.getInt("BuyerCode")) {
                                    find=false;
                                    System.out.print(buying.getString("CurrencyName")+ " IN FIND\n");
                                }
                            }
                            if(find) {
                                double totalAmount = 0, amount = 0, rate = 0;
                                list.add(buying.getInt("BuyerCode"));
                                code = buying.getInt("BuyerCode");
                                System.out.print(code+" is code\n");
                                Statement st = con.createStatement();
                                ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                                while (exchange.next()) {
                                    if (code == exchange.getInt("BuyerCode")){
                                        totalAmount = exchange.getDouble("TotalAmountBuyer");
                                        rate = exchange.getDouble("CurrencyPrice");
                                        if(exchange.getString("UserSeller")!=null){
                                            amount+=exchange.getDouble("CurrencyAmount");
                                            System.out.print(exchange.getString("CurrencyName")+" *******in null\n");
                                        }
                                    }
                                    System.out.print(exchange.getString("CurrencyName")+" in buying\n");
                                }
                                Trades.add(new TradesList(2, buying.getString("CurrencyName"),totalAmount, rate, (float) ((amount/totalAmount)*100)));
                            }
                        }
                    }
                    output.writeObject(Trades);
                }
                ////////////////////////////////////////////////////// insert new walletID
                else if(number == 18){
                    String UserName = (String) input.readObject();
                    String newWalletID = (String) input.readObject();
                    String UpdateTable = null;
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET WalletID = ? " +
                            "WHERE userName = ?";

                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setString(1, newWalletID);
                        preparedStatement.setString(2, UserName);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                ///////////////////////////////////////////// Insert in bank dataBase
                else if(number == 19){
                    BankTableData bankData = (BankTableData) input.readObject();
                    BankDatabase.insertRowWithPreparedStatement(bankData.userName,bankData.firstName,bankData.lastName,bankData.cardID,bankData.cvv2,bankData.expirationDate,bankData.email,bankData.amountUSD);

                }
                //////////////////////////////////////////// cheek Withdrawal for bank
                else if(number == 20){
                    boolean valid = true;
                    int  SwExistCardID=1; //0-> exist
                    int  SwTrueCvv = 2; //3-> exist
                    String CardNum = (String) input.readObject();
                    Double amount = data.readDouble();
                    int cvv = data.readInt();
                    Statement stmt = con.createStatement();
                    ResultSet query = stmt.executeQuery("SELECT * FROM BitFUM.BankInfo");
                    while (query.next()){
                        if(query.getString("cardID").equals(CardNum)){
                            SwExistCardID=0;
                            if(query.getDouble("amountUSD")<amount) valid=false;
                            if(query.getInt("cvv2")==cvv) SwTrueCvv=3;
                        }
                    }
                    writer.println(valid+" "+SwExistCardID+" "+SwTrueCvv);
                    writer.flush();
                }
                /////////////////////////////////////////// Withdrawal from bank
                else if(number == 21){
                    String userName = (String) input.readObject();
                    String CardNum = (String) input.readObject();
                    Double amount = data.readDouble();
                    String UpdateTable = null;
                    Double USDAmount=0.0;
                    UpdateTable = "UPDATE BitFUM.BankInfo SET amountUSD = ? " +
                            "WHERE cardID = ?";
                    Statement stmt = con.createStatement();
                    ResultSet query = stmt.executeQuery("SELECT * FROM BitFUM.BankInfo");
                    while (query.next()){
                        if(query.getString("cardID").equals(CardNum)){
                            USDAmount=query.getDouble("amountUSD");
                        }
                    }
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1,USDAmount-amount);
                        preparedStatement.setString(2, CardNum);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //////////////////////////////////////////////////////////////////////

                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " +
                            "WHERE userName = ?";

                    Statement stmt1 = con.createStatement();
                    ResultSet query1 = stmt1.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (query1.next()){
                        if(query1.getString("userName").equals(userName)){
                            USDAmount=query1.getDouble("USD");
                        }
                    }
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1,USDAmount+amount);
                        preparedStatement.setString(2, userName);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
                ////////////////////////////////////////////////////// return current user bank account
                else if (number == 22) {
                    String userName = (String) input.readObject();
                    String cardID="";
                    Date Enddate=null;
                    int cvv=0;
                    Statement stmt = con.createStatement();
                    ResultSet query = stmt.executeQuery("SELECT * FROM BitFUM.BankInfo");
                    while (query.next()){
                        if(query.getString("userName").equals(userName)){
                            cardID=query.getString("cardID");
                            cvv=query.getInt("cvv2");
                            Enddate=query.getDate("expirationDate");
                        }
                    }
                    writer.println(cardID+"/"+Enddate+"/"+cvv);
                    writer.flush();
                }
                /////////////////////////////////////////////////////////// Insert info edit
                else if(number == 23){
                    String result = (String) input.readObject();
                    inputObject = input.readObject();
                    Person person = (Person) inputObject;
                    String[] tokens = result.split(" ");
                    String UserName= tokens[0];
                    String UpdateTableName = null;

                    UpdateTableName = "UPDATE BitFUM.UsersInfo SET firstName = ? , LastName=? , phoneNumber=? , email=? , profileImage=?" +
                            "WHERE userName = ?";
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTableName)) {
                        if(!tokens[1].equals("null")) preparedStatement.setString(1, tokens[1]);
                        else  preparedStatement.setString(1,person.name);
                        if(!tokens[2].equals("null")) preparedStatement.setString(2, tokens[2]);
                        else  preparedStatement.setString(2, person.lastName);
                        if(!tokens[3].equals("null")) preparedStatement.setString(3, tokens[3]);
                        else  preparedStatement.setString(3, person.phoneNumber);
                        if(!tokens[4].equals("null")) preparedStatement.setString(4, tokens[4]);
                        else  preparedStatement.setString(4, person.email);
                        if(!tokens[5].equals("null")) preparedStatement.setString(5, tokens[5]);
                        else  preparedStatement.setString(5, person.profilePass);
                        preparedStatement.setString(6, UserName);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                //////////////////////////////////////////////////////////////// change card num
                else if (number == 24) {
                    String userName = (String) input.readObject();
                    String newCardNum = (String) input.readObject();
                    String UpdateTable = null;
                    UpdateTable = "UPDATE BitFUM.BankInfo SET cardID = ? " +
                            "WHERE userName = ?";
                    Statement stmt = con.createStatement();
                    ResultSet query = stmt.executeQuery("SELECT * FROM BitFUM.BankInfo");
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setString(1,newCardNum);
                        preparedStatement.setString(2, userName);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                /////////////////////////////////////////////////////////// forget Password
                else if (number==25){
                    String Email = reader.readLine();
                    Statement xs = con.createStatement();
                    boolean email_Valid = false;
                    ResultSet x = xs.executeQuery("select * from UsersInfo");
                    while (x.next()){
                        if(Email.equals(x.getString("email"))) email_Valid = true;
                    }
                    output.writeObject(email_Valid);
                    output.flush();
                }
                ///////////////////////////////////////////////////////////reset Password
                else if(number==26) {
                    String password = reader.readLine();
                    String email = reader.readLine();
                    String updateSQL = "UPDATE BitFUM.UsersInfo SET password = ? WHERE email = ?";
                    try (PreparedStatement preparedStatement = con.prepareStatement(updateSQL)){
                        preparedStatement.setString(1, password);
                        preparedStatement.setString(2, email);
                        int rowsAffected = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                ///////////////////////////////////////////////////////////Valid Password
                else if (number==27){
                    boolean truePass2 = true;
                    String password = reader.readLine();
                    Statement xs = con.createStatement();
                    ResultSet x = xs.executeQuery("select * from BitFUM.UsersInfo");
                    while (x.next()){
                        if(password.equals(x.getString("password"))) truePass2=false;
                    }
                    output.writeObject(truePass2);
                    output.flush();
                }
                ///////////////////////////////////////////////////////////Login Users
                else if (number==28){
                    String userName = reader.readLine();
                    boolean foundUser = true;
                    for (String user : userStatusMap.keySet()) {
                        if (userName.equals(user) && userStatusMap.get(user).equals("online")) {
                            foundUser = false;
                            break;
                        }
                    }
                    if(foundUser) userStatusMap.put(userName,"online");
                    output.writeObject(foundUser);
                    output.flush();
                }
                ///////////////////////////////////////////////////////////LogOut Users
                else if (number==29){
                    String userName = reader.readLine();
                    userStatusMap.put(userName, "offline");
                }
                /////////////////////////////////////////////////////////// make User list
                else if (number==30){
                    ArrayList<UsersList> usersLists = new ArrayList<>();
                    String CurrencyViewQuery = "SELECT  userName,firstName,lastName FROM BitFUM.UsersInfo";
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet queryOutput = stmt.executeQuery(CurrencyViewQuery);
                        while (queryOutput.next()){
                            String userName = queryOutput.getString("userName");
                            String firstName = queryOutput.getString("firstName");
                            String lastName = queryOutput.getString("lastName");
                            if(!userName.equals("Admin") && !firstName.equals("Admin") && !lastName.equals("Admin")) {
                                usersLists.add(new UsersList(userName, firstName, lastName));
                            }
                        }
                        output.writeObject(usersLists);
                        output.flush();
                    } catch (SQLException e) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, e);
                        e.printStackTrace();
                    }
                }
                ////////////////////////////////////////////// Admin wallet
                else if(number==31){
                    Statement stm = con.createStatement();
                    String userName = reader.readLine();
                    double Wage = data.readDouble();
                    double USD = 0.0;
                    String UpdateTable;
                    ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (queryOutput.next()) {
                        if(queryOutput.getString("userName").equals(userName)){
                            USD=queryOutput.getDouble("USD");
                        }
                    }
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " +
                            "WHERE userName = ?";
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1,Wage+USD);
                        preparedStatement.setString(2, userName);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    output.flush();
                }
                //////////////////////////////////////////////////////////total market value
                else if (number==32){
                    String CurrencyName = reader.readLine();
                    Double amount = 0.0;
                    Statement st = con.createStatement();
                    ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchange.next()) {
                        if (exchange.getString("CurrencyName").equals(CurrencyName)){
                            amount+=exchange.getDouble("CurrencyAmount");
                        }
                    }
                    writer.println(String.valueOf(df.format(amount)));
                }
                //////////////////////////////////////////////////////////Circulating market value
                else if (number==33){
                    String CurrencyName = reader.readLine();
                    System.out.print("number is 31*****\n");
                    Double amount = 0.0;
                    Statement st = con.createStatement();
                    ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchange.next()) {
                        if (exchange.getString("CurrencyName").equals(CurrencyName) &&
                                (exchange.getString("UserBuyer")==null || exchange.getString("UserSeller")==null)){
                            amount+=exchange.getDouble("CurrencyAmount");
                        }
                    }
                    writer.println(String.valueOf(df.format(amount)));
                }
                //////////////////////////////////////////////////Highest Price in 24 hours
                else if(number==34) {
                    String CurrencyName = reader.readLine();
                    String sql = "SELECT MAX("+CurrencyName+") AS max_usd FROM BitFUM.currency WHERE Date = ? AND Time BETWEEN ? AND ?";
                    // Date and time parameters
                    Date date = Date.valueOf(LocalDate.now());
                    Time timeStart = new Time(00, 00, 36);
                    Time timeEnd = new Time(23, 59, 36);
                    double maxUsd = 0;
                    try{
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setDate(1, date);
                        pstmt.setTime(2, timeStart);
                        pstmt.setTime(3, timeEnd);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            maxUsd = rs.getDouble("max_usd"); // Getting the value of the max_usd alias
                            System.out.println("The highest USD price between " + timeStart + " and " + timeEnd + " on " + date + " is: " + maxUsd);
                        } else {
                            System.out.println("No records found for the specified time range.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    writer.println(String.valueOf(df.format(maxUsd)));
                }
                /////////////////////////////////////////for the percentage in 24 hours
                else if(number==35){
                    String CurrencyName = reader.readLine();
                    String sql = "SELECT MAX("+CurrencyName+") AS max_usd FROM BitFUM.currency WHERE Date = ? AND Time BETWEEN ? AND ?";
                    Date date = Date.valueOf(LocalDate.now());
                    Time timeStart = new Time(00, 00, 36);
                    Time timeEnd = new Time(23, 59, 36);
                    double maxValue = 0;
                    try{
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setDate(1, date);
                        pstmt.setTime(2, timeStart);
                        pstmt.setTime(3, timeEnd);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            maxValue = rs.getDouble("max_usd"); // Getting the value of the max_usd alias
                            System.out.println("The highest USD price between " + timeStart + " and " + timeEnd + " on " + date + " is: " + maxValue);
                        } else {
                            System.out.println("No records found for the specified time range.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String sqlMin = "SELECT MIN("+CurrencyName+") AS max_usd FROM BitFUM.currency WHERE Date = ? AND Time BETWEEN ? AND ?";
                    double minValue = 0;
                    try{
                        PreparedStatement pstmt = con.prepareStatement(sqlMin);
                        pstmt.setDate(1, date);
                        pstmt.setTime(2, timeStart);
                        pstmt.setTime(3, timeEnd);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            minValue = rs.getDouble("max_usd"); // Getting the value of the max_usd alias
                            System.out.println("The highest USD price between " + timeStart + " and " + timeEnd + " on " + date + " is: " + minValue);
                        } else {
                            System.out.println("No records found for the specified time range.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    writer.println(String.valueOf(df.format((maxValue-minValue)/minValue)));
                }
                /////////////////////////////////////////Currency table
                else if(number==36){
                    String CurrencyName = reader.readLine();
                    ArrayList<TradesList> Trades = new ArrayList<>();
                    Statement st = con.createStatement();
                    ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (exchange.next()){
                        if(exchange.getString("CurrencyName").equals(CurrencyName)){
                            if(exchange.getString("UserBuyer")!=null &&
                                    exchange.getString("UserSeller")!=null){
                                Trades.add(new TradesList(0, CurrencyName, exchange.getDouble("CurrencyAmount"),
                                        exchange.getDouble("CurrencyPrice"), 0));
                            } else if(exchange.getString("UserBuyer")!=null &&
                                    exchange.getString("UserSeller")==null){
                                Trades.add(new TradesList(1, CurrencyName, exchange.getDouble("CurrencyAmount"),
                                        exchange.getDouble("CurrencyPrice"), 1));
                            } else if(exchange.getString("UserBuyer")==null &&
                                    exchange.getString("UserSeller")!=null) {
                                Trades.add(new TradesList(2, CurrencyName, exchange.getDouble("CurrencyAmount"),
                                        exchange.getDouble("CurrencyPrice"), 1));
                            }
                        }
                    }
                    output.writeObject(Trades);
                }
                ///////////////////////////////////// open close market
                else if (number == 37) {
                    String status = reader.readLine();
                    if(status.equals("open")) openMarket=true;
                    else openMarket=false;
                }
                ////////////////////////////////// cheek open close market
                else if (number == 38) {
                    if(openMarket){ output.writeObject(true);}
                    else output.writeObject(false);
                    output.flush();
                }
                /////////////////////////////////// send admin usd
                else if(number == 39){
                    Statement stm = con.createStatement();
                    String userName = reader.readLine();
                    double USD = 0.0;
                    ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (queryOutput.next()) {
                        if(queryOutput.getString("userName").equals(userName)){
                            USD=queryOutput.getDouble("USD");
                        }
                    }
                    writer.println(String.valueOf((USD)));
                }
                ///////////////////////////////////////////////  for swap
                else if (number == 40) {
                    String result = reader.readLine();
                    DecimalFormat df =new DecimalFormat("#.##");
                    String[] tokens = result.split(" ");
                    String userName= tokens[0];
                    String currencyName1=tokens[1];
                    String currencyName2=tokens[2];
                    double currencyAmount1=Double.parseDouble(tokens[3]);
                    double currencyAmount2=Double.parseDouble(tokens[4]);
                    currencyAmount2=Double.parseDouble(df.format(currencyAmount2));

                    double totalAmount1=0,totalAmount2=0;
                    Statement stm = con.createStatement();
                    ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (queryOutput.next()) {
                        if(queryOutput.getString("userName").equals(userName)){
                            if(currencyName1.equals("USD")) totalAmount1=queryOutput.getDouble("USD");
                            else if(currencyName2.equals("USD")) totalAmount2=queryOutput.getDouble("USD");
                            if(currencyName1.equals("EUR")) totalAmount1=queryOutput.getDouble("EUR");
                            else if(currencyName2.equals("EUR")) totalAmount2=queryOutput.getDouble("EUR");
                            if(currencyName1.equals("GBP")) totalAmount1=queryOutput.getDouble("GBP");
                            else if(currencyName2.equals("GBP")) totalAmount2=queryOutput.getDouble("GBP");
                            if(currencyName1.equals("TOMAN")) totalAmount1=queryOutput.getDouble("TOMAN");
                            else if(currencyName2.equals("TOMAN")) totalAmount2=queryOutput.getDouble("TOMAN");
                            if(currencyName1.equals("YEN")) totalAmount1=queryOutput.getDouble("YEN");
                            else if(currencyName2.equals("YEN")) totalAmount2=queryOutput.getDouble("YEN");
                        }
                    }
                    String UpdateTable;

                    UpdateTable = "UPDATE BitFUM.UsersInfo SET "+ currencyName1 + " = ?,"+ currencyName2 + " =? WHERE userName = ?";
                    PreparedStatement pstmt = con.prepareStatement(UpdateTable);
                    pstmt.setDouble(1, totalAmount1 - currencyAmount1);
                    pstmt.setDouble(2, totalAmount2 + currencyAmount2);
                    pstmt.setString(3, userName);
                    pstmt.executeUpdate();
                    /////////////////////////////////// to admin

                    double USD = 0.0;

                    queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (queryOutput.next()) {
                        if(queryOutput.getString("userName").equals("Admin")){
                            USD=queryOutput.getDouble("USD");
                        }
                    }
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " +
                            "WHERE userName = ?";
                    currencyAmount2=Double.parseDouble(df.format(currencyAmount2/100+USD));
                    try (PreparedStatement preparedStatement = con.prepareStatement(UpdateTable)){
                        preparedStatement.setDouble(1,currencyAmount2);
                        preparedStatement.setString(2, "Admin");
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                //////////////////////////////////////// for embezzlementUser
                else if(number==42){
                    String userName = (String) input.readObject();
                    double amount = data.readDouble();
                    double AdminAmount=2*amount,UserAmount=2*amount;
                    Statement stm = con.createStatement();
                    ResultSet queryOutput = stm.executeQuery("SELECT * FROM BitFUM.UsersInfo");
                    while (queryOutput.next()) {
                        if(queryOutput.getString("userName").equals(userName)){
                            UserAmount = queryOutput.getDouble("USD");
                        }
                        if(queryOutput.getString("userName").equals("Admin")){
                            AdminAmount = queryOutput.getDouble("USD");
                        }
                    }
                    String UpdateTable;

                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " + "WHERE userName = ?";
                    PreparedStatement pstmt = con.prepareStatement(UpdateTable);
                    pstmt.setDouble(1,UserAmount-amount );
                    pstmt.setString(2, userName);
                    pstmt.executeUpdate();
                    /////////////////////////////////// to admin
                    UpdateTable = "UPDATE BitFUM.UsersInfo SET USD = ? " + "WHERE userName = ?";
                    pstmt = con.prepareStatement(UpdateTable);
                    pstmt.setDouble(1,AdminAmount+amount );
                    pstmt.setString(2, "Admin");
                    pstmt.executeUpdate();
                }
                /////////////////////////////////////// for swap table
                else if (number==43){
                    ArrayList<SwapList> SwapLists = new ArrayList<>();
                    String userName = (String) input.readObject();
                    String CurrencyViewQuery = "SELECT  UserName,RecordedDate,RecordedTime,SelectedCurrencyName,ConvertedCurrencyName,CurrencyAmount,EquivalentCurrencyAmount FROM  BitFUM.SwapInfo";
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet queryOutput = stmt.executeQuery(CurrencyViewQuery);
                        while (queryOutput.next()){
                            if(userName.equals(queryOutput.getString("UserName"))){
                                Date date = queryOutput.getDate("RecordedDate") ;
                                Time time = queryOutput.getTime("RecordedTime") ;
                                String SelectedCurrencyName = queryOutput.getString("SelectedCurrencyName");
                                String ConvertedCurrencyName = queryOutput.getString("ConvertedCurrencyName");
                                Double CurrencyAmount = queryOutput.getDouble("CurrencyAmount");
                                Double EquivalentCurrencyAmount = queryOutput.getDouble("EquivalentCurrencyAmount");
                                SwapLists.add(new SwapList(date,time,SelectedCurrencyName,ConvertedCurrencyName,CurrencyAmount,EquivalentCurrencyAmount,userName));
                            }
                        }
                        output.writeObject(SwapLists);
                        output.flush();
                    } catch (SQLException e) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, e);
                        e.printStackTrace();
                    }
                }
                ///////////////////////////////////  insert in swap table
                else if(number==44){
                    SwapList swap = (SwapList) input.readObject();
                    SwapDatabase.insertRowWithPreparedStatement(swap.userName,swap.date,swap.time,swap.SelectedCurrencyName,swap.ConvertedCurrencyName,swap.CurrencyAmount,swap.EquivalentCurrencyAmount);
                }
                ///////////////////////////////fill history table
                else if (number==45){
                    boolean find =true;
                    int code;
                    String userName = reader.readLine();
                    ArrayList<HistoryList> Trades = new ArrayList<>();
                    ArrayList<Integer> list = new ArrayList<>();
                    int num =0;
                    Statement stmt = con.createStatement();
                    ResultSet selling = stmt.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (selling.next()){//for selling list
                        find = true;
                        if(userName.equals(selling.getString("UserSeller"))){
                            for (int x : list){
                                if(x == selling.getInt("SellerCode")) find=false;
                            }
                            if(find) {
                                Date date = null;
                                double totalAmount = 0, amount = 0, rate = 0;
                                list.add(selling.getInt("SellerCode"));
                                code = selling.getInt("SellerCode");
                                Statement st = con.createStatement();
                                ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                                while (exchange.next()) {
                                    if (code == exchange.getInt("SellerCode")){
                                        totalAmount = exchange.getDouble("TotalAmountSeller");
                                        rate = exchange.getDouble("CurrencyPrice");
                                        if(exchange.getString("UserBuyer")!=null){
                                            amount+=exchange.getDouble("CurrencyAmount");
                                            num++;
                                            date = exchange.getDate("Date");
                                        }
                                    }
                                }
                                Trades.add(new HistoryList(1, selling.getString("CurrencyName"),totalAmount, rate, (float) ((amount/totalAmount)*100), String.valueOf(date)));
                            }
                        }
                    }
                    find = true;
                    Statement stm = con.createStatement();
                    ResultSet buying = stm.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                    while (buying.next()){
                        find = true;
                        if(userName.equals(buying.getString("UserBuyer"))){
                            for (int x : list){
                                if(x == buying.getInt("BuyerCode")) {
                                    find=false;
                                }
                            }
                            if(find) {
                                double totalAmount = 0, amount = 0, rate = 0;
                                list.add(buying.getInt("BuyerCode"));
                                code = buying.getInt("BuyerCode");
                                java.util.Date date = null;
                                Statement st = con.createStatement();
                                ResultSet exchange = st.executeQuery("SELECT * FROM BitFUM.exchangeinfo");
                                while (exchange.next()) {
                                    if (code == exchange.getInt("BuyerCode")){
                                        totalAmount = exchange.getDouble("TotalAmountBuyer");
                                        rate = exchange.getDouble("CurrencyPrice");
                                        if(exchange.getString("UserSeller")!=null){
                                            amount+=exchange.getDouble("CurrencyAmount");
                                            date = exchange.getDate("Date");
                                        }
                                    }
                                }
                                System.out.print(buying.getString("CurrencyName")+"\n");
                                Trades.add(new HistoryList(2, buying.getString("CurrencyName"),totalAmount, rate, (float) ((amount/totalAmount)*100), String.valueOf(date)));
                            }
                        }
                    }
                    output.writeObject(Trades);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
