package Person;
import SerializableClasses.*;
//import Home.Regression;

import Home.Regression;
import Start.StartPage;
import TableDataInformation.BankTableData;
import Trades.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import static DatabaseConnection.UsersInformationDatabase.createConnection;
import static Login.LoginPage.existPeron;
import static javafx.application.Application.launch;

public  class User extends Application {
    public static ObjectInputStream ois = null;
    public static ObjectOutputStream oos = null;
    public static DataOutputStream dos = null;
    public static Socket socket;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException, SQLException {
//        new Regression();
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("/Login/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.
                load(), 1034
                , 518);
        stage.setTitle("BitFum");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            if(existPeron!=null) {
                logOut(existPeron.UserName);
                System.out.println("Window is closing");
            }
        });
        stage.show();
    }
    public static void main(String[] args) throws IOException {
        socket = new Socket("localhost", 9090);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        launch();
    }

    public static void sendObject(Object obj){//for signing up and add new user in the userInfo table in dataBase
        try  {
            dos.writeInt(1);
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean sendString(String st, int number){
        try  {
            dos.writeInt(number);
            oos.writeObject(st);
            boolean s = (boolean) ois.readObject();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Person sendPerson(String st, int number){
        try  {
            dos.writeInt(number);
            oos.writeObject(st);
            Person s = (Person) ois.readObject();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean sendExchange(int number, String userName, String CurrencyName, Double amount, Double price){//when the users create new trade
        try  {
            dos.writeInt(number);
            oos.writeObject(userName);
            oos.writeObject(CurrencyName);
            dos.writeDouble(amount);
            dos.writeDouble(price);
            return (boolean) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String sendTradedCurrency(){
        try  {
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(8);
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendValidWalletID(String WalletID){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(10);
            oos.writeObject(WalletID);
            return reader.readLine();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendInsertTransfer(TransferTableData2 transfer){
        try{
            dos.writeInt(11);
            oos.writeObject(transfer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TransferLists sendTransferDataTable(String userName, String WalletID){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(12);
            writer.println(userName);
            writer.println(WalletID);
            TransferLists list = (TransferLists) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkBalanceInExchange(String userName, String CurrencyName, double amount){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(13);
            oos.writeObject(userName);
            oos.writeObject(CurrencyName);
            dos.writeDouble(amount);
            boolean s = (boolean) ois.readObject();
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Currency> fillArrayList(String userName){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(14);
            writer.println(userName);
            ArrayList<Currency> list = (ArrayList<Currency>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<ExchangeList> fillExchangeTable(String CurrencyName, int number){//for filling the table that shows the open trades  15/16
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(number);
            writer.println(CurrencyName);
            ArrayList<ExchangeList> list = (ArrayList<ExchangeList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<TradesList> fillExchangeOrders(String userName){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(17);
            writer.println(userName);
            ArrayList<TradesList> list = (ArrayList<TradesList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void InsertNewWalletID(String userName,String WalletID){
        try{
            dos.writeInt(18);
            oos.writeObject(userName);
            oos.writeObject(WalletID);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void sendInsertBankDataBase(BankTableData bankData){
        try{
            dos.writeInt(19);
            oos.writeObject(bankData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String ValidWithdrawal(String CardNum, double amount ,int cvv){ //for bank
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(20);
            oos.writeObject(CardNum);
            dos.writeDouble(amount);
            dos.writeInt(cvv);
            return reader.readLine();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void WithdrawalFromBank(String userName ,String CardNum,Double amount){
        try{
            dos.writeInt(21);
            oos.writeObject(userName);
            oos.writeObject(CardNum);
            dos.writeDouble(amount);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static String CurrentUserBankAccount(String Username){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(22);
            oos.writeObject(Username);
            String r = reader.readLine();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void InsertEditInfoDatabase(String userName , String newName , String newLastName , String newPhone , String newEmail,String newPath,Person person){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(23);
            oos.writeObject(userName+" "+newName+" "+newLastName+" "+newPhone+" "+newEmail+" "+newPath);
            oos.writeObject(person);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void changeNumCard(String username,String NewCardNum){
        try{
            dos.writeInt(24);
            oos.writeObject(username);
            oos.writeObject(NewCardNum);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean forgetPassword(String email){
        try{
            dos.writeInt(25);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(email);
            boolean s = (boolean) ois.readObject();
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void resetPassword(String password, String email){
        try{
            dos.writeInt(26);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(password);
            writer.println(email);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean ValidPassword(String password){
        try{
            dos.writeInt(27);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(password);
            boolean s = (boolean) ois.readObject();
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean LoginUsers(String userName){
        try{
            dos.writeInt(28);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(userName);
            boolean s = (boolean) ois.readObject();
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void logOut(String username){//TODO
        try{
            dos.writeInt(29);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(username);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<UsersList> SendUsersList(){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(30);
            ArrayList<UsersList> list = (ArrayList<UsersList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void CheekAdminUSD(String username,double Wage){//userInformation
        try{
            dos.writeInt(31);
            oos.writeObject(username);
            dos.writeDouble(Wage);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static String TotalMarketCurrency(String CurrencyName){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(32);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(CurrencyName);
            String r = reader.readLine();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String CirculatingMarketValue(String CurrencyName){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(33);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(CurrencyName);
            String r = reader.readLine();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String HighestPrice(String CurrencyName){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(34);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(CurrencyName);
            String r = reader.readLine();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String changePercentageDaily(String CurrencyName){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(35);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(CurrencyName);
            String r = reader.readLine();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<TradesList> fillCurrencyOrders(String CurrencyName){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(36);
            writer.println(CurrencyName);
            ArrayList<TradesList> list = (ArrayList<TradesList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void OpenCloseMarket(String status){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(37);
            writer.println(status);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static boolean  cheekOpenMarket(){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(38);
            boolean s = (boolean) ois.readObject();
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String SendAdminUSD(String username){
        try{
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);
            dos.writeInt(39);
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            writer.println(username);
            String r = reader.readLine();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void SwapInWallet(String username,String currency1,String currency2, double amount1,double amount2){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(40);
            writer.println(username+" "+currency1+" "+currency2+" "+amount1+" "+amount2);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void embezzlementUser(String userName,Double amount ){
        try{
            dos.writeInt(42);
            oos.writeObject(userName);
            dos.writeDouble(amount);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static ArrayList<SwapList> SendSwapList(String userName){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(43);
            oos.writeObject(userName);
            ArrayList<SwapList> list = (ArrayList<SwapList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void sendInsertSwap(SwapList swap){
        try{
            dos.writeInt(44);
            oos.writeObject(swap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HistoryList> sendHistoryList(String username){
        try{
            OutputStream outputString = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputString, true);
            dos.writeInt(45);
            writer.println(username);
            ArrayList<HistoryList> list = (ArrayList<HistoryList>) ois.readObject();
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
