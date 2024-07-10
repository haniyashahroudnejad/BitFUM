package Trades;

import Home.HomePage;
import Login.LoginPage;
import SerializableClasses.TransferLists;
import SerializableClasses.TransferTableData2;
import Start.StartPage;
import TableDataInformation.TransferTableData;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseConnection.TransferDatabase;
import javafx.util.converter.NumberStringConverter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static Login.LoginPage.existPeron;
import static Person.User.*;
import static Trades.Wallet.myCurrency;

public class Transfer  implements Initializable {

    @FXML
    private ImageView CloseMenu,Menu,calender,ProfileIcon;
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private ChoiceBox<String> ChoiceCurrency,DestinationAddress;
    @FXML
    private Button transferButton,RegisterCardButton,ConfirmedBank,BankWithdrawalButton,newWalletIDButton,ConfirmedID,CheekRobot,SendPassBank;
    @FXML
    private TextField CurrencyValueField,SearchWalletIDField,EnterDestinationAddress,EnterAmountBank,YearBankField,MonthBankField,CardNumberBank,CVVField,EnterPassBank;
    @FXML
    private Label currencyAmount,currencyName,lastname,name,today,total,walletID,warningField,totalBank,newID,newIDOnCard,UserName;
    @FXML
    private TableView<TransferTableData> transferTableView;
    @FXML
    private TableColumn<TransferTableData, Date> DateColumn;
    @FXML
    private TableColumn<TransferTableData, Time> TimeColumn;
    @FXML
    private TableColumn<TransferTableData, String> SourceColumn;
    @FXML
    private TableColumn<TransferTableData, String> DestinationColumn;
    @FXML
    private TableColumn<TransferTableData, String> CurrencyNameColumn;
    @FXML
    private TableColumn<TransferTableData, Number> CurrencyAmountColumn;
    @FXML
    private TableColumn<TransferTableData, Number> IssueTracking;
    @FXML
    private Pane transferPane,BankWithdrawaPane,RegisterCardPane,IDPane,ReceiptPane,bankPane1,bankPane2,slider;
    @FXML
    private MediaView NotRobotView,calenderView;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer,mediaPlayer1;
    private boolean sendPass = false,EnterCorrectPass=false;
    String code = null;

    PauseTransition transition = new PauseTransition(Duration.seconds(2));
    String SelectedCurrency;
    double value;
    String SelectedDestination="";
    String myWalletID=existPeron.WalletID;
    String myUserName=existPeron.UserName;
    ObservableList<TransferTableData> ObservableTransferDataList = FXCollections.observableArrayList();
    private ArrayList<String> MyDestinationAddressList = new ArrayList<>();
    private String[] Currency = {"USD","YEN","TOMAN","EUR","GBP"};
    @FXML
    private Pane datePane,headerSlider;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    boolean openHeaderSlider = false;
    ArrayList<Label> labelList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setTranslateX(-270);
        headerSlider.setTranslateY(-200);
        ////////////////////////////////////////////
        ChoiceCurrency.getItems().addAll(Currency);
        ChoiceCurrency.setOnAction(this::getCurrency);
        DestinationAddress.setOnAction(this::getWalletID);
        ////////////////////////////////////////////
        CurrencyValueField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    total.setText("0");
                    currencyAmount.setText("0");
                }
                else if(validValue()) {total.setText(newValue);currencyAmount.setText(newValue);}
            }
        });
        ////////////////////////////////////////////// table
        SearchWalletIDField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterWalletDataList((String) oldValue,(String) newValue);
            }
        });

        SourceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        DestinationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        IssueTracking.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        DateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        TimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTime()));
        SourceColumn.setCellValueFactory(cellData -> cellData.getValue().SourceWalletIDProperty());
        DestinationColumn.setCellValueFactory(cellData -> cellData.getValue().DestinationWalletIDProperty());
        CurrencyNameColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyNameProperty());
        CurrencyAmountColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyAmountProperty());
        IssueTracking.setCellValueFactory(cellData -> cellData.getValue().IssueTrackingProperty());

        transferTableView.setItems(ObservableTransferDataList);
        transferTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setTransferTableData();
        DestinationAddress.getItems().addAll(MyDestinationAddressList);
        //////////////////////////////////////////////

        newIDOnCard.setText(myWalletID);
        fillWallet();

        //////////////////////////// set labels name
        today.setText(String.valueOf(LocalDate.now()));
        UserName.setText(myUserName);
        clear();
        //////////////////////////// media player
        file = new File("src/calender.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer1 = new MediaPlayer(media);
        calenderView.setMediaPlayer(mediaPlayer1);
        mediaPlayer1.setAutoPlay(true);
        mediaPlayer1.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer1.setOnEndOfMedia(() -> {
            mediaPlayer1.seek(Duration.ZERO);
            mediaPlayer1.play();
        });
        //////////////////////// make list for date
        ObservableList<Node> gridPaneChildren = datePane.getChildren();
        labelList = new ArrayList<>();
        for (Node node : gridPaneChildren) {
            if (node instanceof Label) {
                Label label = (Label) node;
                labelList.add(label);
            }
        }
        ////////////////////////////
        setCalender();
        File imagefile = new File(existPeron.profilePass);
        Image image1 = new Image(imagefile.toURI().toString());
        ProfileIcon.setImage(image1);
    }
    public void setCalender(){
        LocalDateTime localDateTime = LocalDateTime.now();
        String dayName = localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);
        int day = localDateTime.getDayOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String dateTime = localDateTime.format(formatter)+"   "+dayName;
        currentDate.setText(dateTime);
        for(int i=0 ;i<31;i++){
            if(i==day) {
                for(Label s : labelList){
                    if(s.getText().equals(String.valueOf(i))){
                        s.setStyle("-fx-background-color: #67c953;-fx-background-radius :50;");
                    }
                }
            }
        }
    }
    public void setTransferTableData() {
        ArrayList<TransferTableData2> ObservableTransferDataList1;
        TransferLists transferLists = sendTransferDataTable(myUserName,myWalletID);
        if(transferLists!=null) {
            ObservableTransferDataList1 = transferLists.transfers;
            for(String st : transferLists.MyDestinationAddressList ){
                if(!st.equals(myWalletID)) MyDestinationAddressList.add(st);
            }

            for (TransferTableData2 transfer : ObservableTransferDataList1) {
                ObservableTransferDataList.add(new TransferTableData(transfer.getDate(), transfer.getTime(),
                        transfer.SourceWalletID1, transfer.DestinationWalletID1, transfer.CurrencyName1,
                        transfer.CurrencyAmount1, transfer.issueTracking1));
            }
        }
    }
    public void filterWalletDataList(String oldValue,String newValue){
        ObservableList<TransferTableData> filteredList = FXCollections.observableArrayList();
        if(SearchWalletIDField==null || newValue.length()<oldValue.length() || newValue==null){
            transferTableView.setItems(ObservableTransferDataList);
        }else {
            newValue = newValue.toUpperCase();
            for (TransferTableData transferData : transferTableView.getItems()){
                String filterWalletID1 = transferData.getSourceWalletID();
                String filterWalletID2 = transferData.getDestinationWalletID();
                if(filterWalletID1.toUpperCase().contains(newValue) || filterWalletID2.toUpperCase().contains(newValue) ){
                    filteredList.add(transferData);
                }
            }
            transferTableView.setItems(filteredList);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////  for  Destination Address

    @FXML
    protected void onOK(){
        if(EnterDestinationAddress.getText().isEmpty()) {
            warningField.setText(" You have to enter Destination Address !");
            transition.setOnFinished(e -> warningField.setText(""));
            transition.play();
        }else {
            SelectedDestination = EnterDestinationAddress.getText();
            if(SelectedDestination.equals(myWalletID)){
                warningField.setText("cant select yourself !");
                transition.setOnFinished(e -> warningField.setText(""));
                transition.play();
            }
            else if(validDestinationAddress()){
                walletID.setText(SelectedDestination);
                boolean find = false;
                for(String s :MyDestinationAddressList){
                    if(s.equals(EnterDestinationAddress.getText())) find=true;
                }
                if(!find)DestinationAddress.getItems().add(EnterDestinationAddress.getText());
            }
            else {
                warningField.setText(" cant find this Destination Address !");
                transition.setOnFinished(e -> warningField.setText(""));
                transition.play();
                SelectedDestination="";
            }
        }

    }
    public void getWalletID(ActionEvent event){
        SelectedDestination = DestinationAddress.getValue();
        if(validDestinationAddress()){
            walletID.setText(SelectedDestination);
        }
    }
    private boolean validDestinationAddress(){
        if(SelectedDestination.isEmpty()) {
            warningField.setText("Select DestinationAddress ");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
            return false;
        }
//        String walletID = EnterDestinationAddress.getText();
        else {
            boolean findWalletID;
            String result = sendValidWalletID(SelectedDestination);
            if(result.contains("false")) findWalletID=false;
            else{
                findWalletID=true;
                String[] tokens = result.split(" ");
                name.setText(tokens[1]);
                lastname.setText(tokens[2]);
            }
            if (!findWalletID) return false;
        }
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////////  get Currency
    public void getCurrency(ActionEvent event){
        SelectedCurrency = ChoiceCurrency.getValue();
        if(validChoiceCurrency()) currencyName.setText(SelectedCurrency);
    }
    private boolean validChoiceCurrency(){
        int SwFind = 1,SwValid=1;
        for (Trades.Currency currency : myCurrency){
            if(currency.getCurrencyName().equals(SelectedCurrency)) SwFind=0;
        }
        if(SwFind==1){
            if(!SelectedCurrency.isEmpty()) warningField.setText(SelectedCurrency+" is not available in your wallet !");
            else warningField.setText(SelectedCurrency+"select Currency ! ");
            transition.setOnFinished(e -> warningField.setText(""));
            transition.play();
            SwValid=0;
        }else {
            for (Trades.Currency currency : myCurrency){
                if(currency.getCurrencyName().equals(SelectedCurrency)) {
                    if(currency.getCurrencyAmount()==0.0){
                        warningField.setText("The amount of "+SelectedCurrency+" in your wallet is 0");
                        transition.setOnFinished(e -> warningField.setText(""));
                        transition.play();
                        SwValid=0;
                    }
                }
            }
        }
        if(SwValid==0) return false;
        else return true;
    }
    ///////////////////////////////////////////////////////////////////////////////  valid value
    private boolean validValue(){
        boolean valid=true;
        String input =  CurrencyValueField.getText();
        double number=0;
        try {
            number=Double.parseDouble(input);
        }catch (NumberFormatException e){
            warningField.setText("The value is invalid");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
            valid=false;
        }
        if(number<0 || number>500){
            warningField.setText("The Amount is invalid");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
            valid=false;
        }
        for (Trades.Currency currency : myCurrency) {
            if (currency.getCurrencyName().equals(SelectedCurrency)) {
                if (currency.getCurrencyAmount() < value) {
                    warningField.setText("you have not enough currency !");
                    transition.setOnFinished(e -> warningField.setText(""));
                    transition.play();
                    valid = false;
                }
            }
        }
        if (valid) return true;
        else return false;
    }
    ////////////////////////////////////////////////////////////////// for value
    @FXML
    protected void onCurrencyValue(){
        value =Double.parseDouble(CurrencyValueField.getText());
        System.out.println("value ="+value);
        validValue();
    }
    @FXML
    protected void onButton25Clicked(){
        if(validChoiceCurrency()){
            for (Trades.Currency currency : myCurrency){
                if(currency.getCurrencyName().equals(SelectedCurrency)) {
                    value=currency.getCurrencyAmount()*25/100;
                    CurrencyValueField.clear();
                    CurrencyValueField.setText(String.valueOf(value));
                    total.setText(String.valueOf(value));
                }
            }
        }else {
            warningField.setText("Select correct Currency!");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }
    }
    @FXML
    protected void onButton50Clicked(){
        if(validChoiceCurrency()){
            for (Trades.Currency currency : myCurrency){
                if(currency.getCurrencyName().equals(SelectedCurrency)){
                    value=currency.getCurrencyAmount()*50/100;
                    CurrencyValueField.clear();
                    CurrencyValueField.setText(String.valueOf(value));
                    total.setText(String.valueOf(value));
                }
            }
        }else {
            warningField.setText("Select correct Currency!");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }
    }
    @FXML
    protected void onButton75Clicked(){
        if(validChoiceCurrency()){
            for (Trades.Currency currency : myCurrency){
                if(currency.getCurrencyName().equals(SelectedCurrency)) {
                    value=currency.getCurrencyAmount()*75/100;
                    CurrencyValueField.clear();
                    CurrencyValueField.setText(String.valueOf(value));
                    total.setText(String.valueOf(value));
                }
            }
        }else {
            warningField.setText("Select correct Currency!");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }
    }
    @FXML
    protected void onButton100Clicked(){
        if(validChoiceCurrency()){
            for (Trades.Currency currency : myCurrency){
                if(currency.getCurrencyName().equals(SelectedCurrency)){
                    value=currency.getCurrencyAmount();
                    CurrencyValueField.clear();
                    CurrencyValueField.setText(String.valueOf(value));
                    total.setText(String.valueOf(value));
                }
            }
        }else {
            warningField.setText("Select correct Currency!");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////// confirmed
    @FXML
    protected void onConfirmedClicked(){
        if(validChoiceCurrency() && validValue() && validDestinationAddress()) {
            Random random = new Random();
            int IssueTracking = random.nextInt((int) Math.pow(10,6))+111111;
            System.out.print("VALUE IS :"+CurrencyValueField.getText());
            if(checkBalanceInExchange(myUserName, SelectedCurrency, value)) {
                sendInsertTransfer(new TransferTableData2(Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), myWalletID, SelectedDestination, SelectedCurrency, Double.valueOf(CurrencyValueField.getText()), IssueTracking, myUserName));
                fillWalletAndChange();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("You have order in Exchage.");
                alert.showAndWait();
            }
            ObservableTransferDataList.clear();
            setTransferTableData();
            clear();
        }
    }
    private void clear(){
        currencyAmount.setText("");
        currencyName.setText("");
        lastname.setText("");
        name.setText("");
        total.setText("0");
        walletID.setText("");
        SelectedCurrency="";
        value=0;
        SelectedDestination="";
        CurrencyValueField.clear();
        EnterDestinationAddress.clear();
        ////////////////
        EnterAmountBank.setText("");
        CardNumberBank.setText("");
        YearBankField.setText("");
        MonthBankField.setText("");
        CVVField.setText("");
        EnterCorrectPass = false;
        sendPass = false;
        NotRobotView.setVisible(false);

    }
    public void fillWallet(){
        myCurrency = fillArrayList(myUserName);
    }
    public void fillWalletAndChange(){
        myCurrency = fillArrayList(myUserName);
        double total = 0.0;
        double EURNow=0,TOMANow=0,YENNow=0,GBPNow=0,USDNow=0;
        for(int i=0;i<HomePage.ObservableCurrencyDataList.size();i++){
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("USD")) {USDNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("EUR")) {EURNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("TOMAN")) {TOMANow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("YEN")) {YENNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("GBP")) {GBPNow= HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
        }
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals("USD")) {total+=currency.getCurrencyAmount();}
            else if(currency.getCurrencyName().equals("EUR"))  {total+=USDNow/EURNow*currency.getCurrencyAmount();}
            else if(currency.getCurrencyName().equals("TOMAN"))  {total+=USDNow/TOMANow*currency.getCurrencyAmount();}
            else if(currency.getCurrencyName().equals("YEN"))  {total+=USDNow/YENNow*currency.getCurrencyAmount();}
            else if(currency.getCurrencyName().equals("GBP"))  {total+=USDNow/GBPNow*currency.getCurrencyAmount();}
        }
        Trades.Wallet.WalletChanges.add(total);
    }
    /////////////////////////////////////////////////////////////////////////////// generate wallet id
    @FXML
    public void generateWalletId(){
        int YEAR_LENGTH = 4;
        int MONTH_LENGTH = 2;
        int DAY_LENGTH = 2;
        int RANDOM_NUMBER_LENGTH = 4;
        StringBuilder sb = new StringBuilder();

        String yearString = String.format("%04d", LocalDate.now().getYear());
        sb.append(yearString.substring(0, YEAR_LENGTH));
        sb.append(" ");

        String monthString = String.format("%02d", YearMonth.now().getMonthValue());
        sb.append(monthString.substring(0, MONTH_LENGTH));

        String dayString = String.format("%02d",LocalDate.now().getDayOfMonth());
        sb.append(dayString.substring(0, DAY_LENGTH));
        sb.append(" ");

        Random random = new Random();
        int randomNumber = random.nextInt((int) Math.pow(10,RANDOM_NUMBER_LENGTH));
        String randomNumberString = String.valueOf(randomNumber);
        sb.append(randomNumberString);
        sb.append(" ");
        int randomNumber2 = random.nextInt((int) Math.pow(10, RANDOM_NUMBER_LENGTH));
        String randomNumberString2 = String.valueOf(randomNumber2);
        sb.append(randomNumberString2);

        newID.setText(sb.toString());

    }
    ///////////////////////////////////////////////////////////////////////////////
    @FXML
    protected void onConfirmedID(){
        if(!newID.getText().isEmpty()){
            newIDOnCard.setText(newID.getText());
            myWalletID = newID.getText();
            InsertNewWalletID(myUserName,myWalletID);
        }
        newID.setText("");
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////// handel bank page
    ////////////////////////////////////////////////////////////////////////// value
    private double withdrawalAmount=0;
    private String cardID="";
    private int cvv=0,year=0,month=0;
    @FXML
    public void onEnterAmountBank(){
        withdrawalAmount=Double.parseDouble(EnterAmountBank.getText());
    }
    @FXML
    public void onCardNumberBank(){
        cardID=(CardNumberBank.getText());
    }
    @FXML
    public void onYearBank(){year=Integer.parseInt(YearBankField.getText());
    }
    @FXML
    public void onMonthBank(){month=Integer.parseInt(MonthBankField.getText());
    }
    @FXML
    public void onCVVField(){
        if(!CVVField.getText().isEmpty() )  { cvv=Integer.parseInt(CVVField.getText());}
    }
    private boolean ValidWithdrawalAmount(){
        if(withdrawalAmount==0){
            warningField.setText("Invalid Amount ");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else if(cardID.isEmpty()){
            warningField.setText("Invalid Card Number ");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else if(cvv==0 || (cvv>10000 || cvv<999)){
            warningField.setText("Invalid CVV");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else if(year<=0 || year<LocalDate.now().getYear()){
            warningField.setText("Invalid Year");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else if(month<=0 ){
            warningField.setText("Invalid month");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else {
            String result = ValidWithdrawal(cardID,withdrawalAmount,cvv);
            String[] tokens = result.split(" ");
            if(tokens[1].equals("1")){
                warningField.setText("Invalid Card Number");
                transition.setOnFinished(event -> warningField.setText(""));
                transition.play();
            }
            else if(tokens[2].equals("2")){
                warningField.setText("Invalid CVV ");
                transition.setOnFinished(event -> warningField.setText(""));
                transition.play();
            }
            else if(result.contains("false")){
                warningField.setText("you have not enough money");
                transition.setOnFinished(event -> warningField.setText(""));
                transition.play();
            }else {
                return true;
            }
        }
        return false;
    }
    @FXML
    private void onSendPassBank(){
        sendPass = true;
        sendEmail(existPeron.email);
    }
    @FXML
    private void OnEnterPassBank(){
        if(!EnterPassBank.getText().isEmpty()){
            if(EnterPassBank.getText().equals(code)) EnterCorrectPass=true;
            else {
                warningField.setText("pass code is not correct !");
                transition.setOnFinished(event -> warningField.setText(""));
                transition.play();
            }
        }else if(sendPass) {
            warningField.setText("Enter pass code that sent you !");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }else {
            warningField.setText("Peres Send email button");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
        }
    }
    //////////////////////////////////////////////////////////////////////////
    public void sendEmail(String emailAddress){
        // Sender's email address
        String senderEmail = "tshtrynftmh@gmail.com";
        String senderPassword = "xqiz jaax rzke wtvw"; // Your Gmail password

        // Receiver's email address
        String receiverEmail = emailAddress;

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(receiverEmail));

            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));

            // Set Subject: header field
            message.setSubject("JavaMail API Test");
            Random random = new Random();
            int randomNumber;
            char randomChar=' ';
            code="";
            for(int i=0; i<5; i++) {
                boolean isNumber = random.nextBoolean();
                if (isNumber) {
                    randomNumber = random.nextInt(8) + 0;
                    code += String.valueOf(randomNumber);
                } else {
                    randomChar = (char) (random.nextInt(26) + 'A');
                    code += String.valueOf(randomChar);
                }
            }
            // Now set the actual message
            message.setText(code);
            System.out.println("send to email : ="+code);

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
    }
    @FXML
    public void onConfirmedBank(){
        if(ValidWithdrawalAmount()){
            if(sendPass){
                if(EnterCorrectPass){
                    WithdrawalFromBank(myUserName,cardID,withdrawalAmount);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Done !");
                    alert.setContentText("The withdrawal was successful !");
                    alert.showAndWait();
                    fillWalletAndChange();
                    clear();
                }else{
                    warningField.setText("Enter pass code that sent you !");
                    transition.setOnFinished(event -> warningField.setText(""));
                    transition.play();
                }
            }else {
                warningField.setText("Peres Send email button");
                transition.setOnFinished(event -> warningField.setText(""));
                transition.play();
            }
        }
    }
    @FXML
    protected  void onCheekRobot(){
        NotRobotView.setVisible(true);
        //////////////////////////// media player
        file = new File("src/cheakRobot.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        NotRobotView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
    }
    ///////////////////////////////////////////////////////////////////////////////
    @FXML
    protected void onHomeClicked() throws IOException {
        Home.setStyle("-fx-background-color:#013b46;");
        StartPage.switchPages.ChangePageByClickingButton(Home,"/Home/HomePage.fxml");
    }
    @FXML
    protected void onWalletClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Wallet,"/Trades/Wallet.fxml");
    }
    @FXML
    protected void onTransferClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Transfer,"/Trades/Transfer.fxml");
    }
    @FXML
    protected void onExchangeClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Exchange,"/Trades/Exchange.fxml");
    }
    @FXML
    protected void onHistoryClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(History,"/Trades/HistoryGraphic.fxml");
    }
    @FXML
    protected void onSwapClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Swap,"/Trades/Swap.fxml");
    }
    @FXML
    protected void onExitClicked() throws IOException{
        logOut(existPeron.UserName);
        StartPage.switchPages.ChangePageByClickingButton(Exit, "/Login/LoginPage.fxml");
    }
    @FXML
    protected void onCloseMenuClicked(){
        CloseMenu. setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-270);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e) -> {
            });
        });
    }
    @FXML
    protected void onMenuIconClicked(){
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-270);

        });
    }
    @FXML
    protected void onTransferButtonClicked(){
        RegisterCardButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        BankWithdrawalButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferPane.setVisible(true);
        ReceiptPane.setVisible(true);
        BankWithdrawaPane.setVisible(false);
        bankPane1.setVisible(false);
        bankPane2.setVisible(false);
        RegisterCardPane.setVisible(false);
        IDPane.setVisible(false);

    }
    @FXML
    protected void onRegisterCardButtonClicked(){
        RegisterCardButton.setStyle("-fx-background-color:#53AAD0;-fx-background-radius :20;");
        transferButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        BankWithdrawalButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferPane.setVisible(false);
        ReceiptPane.setVisible(false);
        BankWithdrawaPane.setVisible(false);
        bankPane1.setVisible(false);
        bankPane2.setVisible(false);
        RegisterCardPane.setVisible(true);
        IDPane.setVisible(true);
    }
    @FXML
    protected void onBankWithdrawalButtonClicked(){
        BankWithdrawalButton.setStyle("-fx-background-color:#53AAD0;-fx-background-radius :20;");
        RegisterCardButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferPane.setVisible(false);
        ReceiptPane.setVisible(false);
        BankWithdrawaPane.setVisible(true);
        bankPane1.setVisible(true);
        bankPane2.setVisible(true);
        RegisterCardPane.setVisible(false);
        IDPane.setVisible(false);
    }
    @FXML
    protected void onCalender(){
        calender.setOnMouseClicked(event -> {
            if(openHeaderSlider) {
                openHeaderSlider = false;
                TranslateTransition slide = new TranslateTransition();
                slide.setDuration(Duration.seconds(0.4));
                slide.setNode(headerSlider);
                slide.setToY(-200);
                slide.play();
                headerSlider.setTranslateY(0);
                slide.setOnFinished((ActionEvent e) -> {
                });
            }else {
                openHeaderSlider = true;
                TranslateTransition slide = new TranslateTransition();
                slide.setDuration(Duration.seconds(0.4));
                slide.setNode(headerSlider);
                slide.setToY(0);
                slide.play();
                headerSlider.setTranslateY(-200);
            }
        });
    }
    @FXML
    protected void onProfile() throws IOException{
        StartPage.switchPages.ChangePageByClickingImage(ProfileIcon,"/Home/ProfilePage.fxml");
    }
    public void OpenBankPage(ImageView image) throws IOException {
        StartPage.switchPages.ChangePageByClickingImage(image,"/Trades/Transfer.fxml");
        BankWithdrawalButton.setStyle("-fx-background-color:#53AAD0;-fx-background-radius :20;");
        RegisterCardButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferButton.setBackground(new Background(new BackgroundFill(Color.rgb(178,231,255), CornerRadii.EMPTY, Insets.EMPTY)));
        transferPane.setVisible(false);
        ReceiptPane.setVisible(false);
        BankWithdrawaPane.setVisible(true);
        bankPane1.setVisible(true);
        bankPane2.setVisible(true);
        RegisterCardPane.setVisible(false);
        IDPane.setVisible(false);

    }

}
