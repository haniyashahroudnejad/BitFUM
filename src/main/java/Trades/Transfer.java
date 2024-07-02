package Trades;

import DatabaseConnection.CurrencyDatabaseConnection;
import Home.CurrencyTableData;
import Home.HomePage;
import Start.StartPage;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseConnection.TransferConnection;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class Transfer  implements Initializable {
    @FXML
    private VBox slider;
    @FXML
    private ImageView CloseMenu,Menu;
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private ChoiceBox<String> ChoiceCurrency,DestinationAddress;
    @FXML
    private Button Confirmed,button100,button25,button50,button75,transferButton,RegisterCardButton,ConfirmedBank,BankWithdrawalButton,newWalletIDButton,ConfirmedID;
    @FXML
    private TextField CurrencyValueField,SearchWalletIDField,CurrencyValueFieldBank,EnterDestinationAddress;
    @FXML
    private Label currencyAmount,currencyName,lastname,name,today,total,walletID,warningField,totalBank,newID,newIDOnCard;
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
    private Pane transferPane,BankWithdrawaPane,RegisterCardPane,IDPane,ReceiptPane,bankPane1,bankPane2;

    PauseTransition transition = new PauseTransition(Duration.seconds(2));
    String SelectedCurrency;
    double value;
    String SelectedDestination;
    String myWalletID="1234";
    String myUserName="haniya";
    ObservableList<TransferTableData> ObservableTransferDataList = FXCollections.observableArrayList();
    private ArrayList<String> MyDestinationAddressList = new ArrayList<>();
    private String[] Currency = {"USD","YEN","TOMAN","EUR","GBP"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setTranslateX(-270);
        ChoiceCurrency.getItems().addAll(Currency);
        ChoiceCurrency.setOnAction(this::getCurrency);
        DestinationAddress.setOnAction(this::getWalletID);
        Trades.Wallet.myCurrency.add(new Currency("YEN",200.0)); ////////////////////////////////////////////  delete
        CurrencyValueField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){
                    total.setText("0");
                }
                else if(validValue()) {total.setText(newValue);currencyAmount.setText(newValue);}
            }
        });
        today.setText(String.valueOf(LocalDate.now()));
        //////////////////////////////////////////////
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

        //////////////////////////////////////////////
        DestinationAddress.getItems().addAll(MyDestinationAddressList);
    }
    public void setTransferTableData() {
        TransferConnection connectNow = new TransferConnection();
        Connection connectDB = connectNow.getDBConnection();
        String CurrencyViewQuery = "SELECT  UserName,RecordedDate,RecordedTime,SourceWalletID,DestinationWalletID,CurrencyName,CurrencyAmount,IssueTracking FROM BitFUM.TransferInfo";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(CurrencyViewQuery);
            while (queryOutput.next()){
                if(myUserName.equals(queryOutput.getObject("UserName",String.class)) || myWalletID.equals(queryOutput.getObject("DestinationWalletID",String.class))) {
                    Date date = queryOutput.getDate("RecordedDate");
                    Time time = queryOutput.getTime("RecordedTime");
                    String SourceID = queryOutput.getString("SourceWalletID");
                    String DestinationID = queryOutput.getString("DestinationWalletID");
                    String CurrencyName = queryOutput.getString("CurrencyName");
                    Double CurrencyAmount = queryOutput.getDouble("CurrencyAmount");
                    Integer IssueTracking = queryOutput.getInt("IssueTracking");
                    TransferTableData transferData = new TransferTableData(date,time,SourceID,DestinationID,CurrencyName,CurrencyAmount,IssueTracking);
                    ObservableTransferDataList.add(transferData);
                    if(!DestinationID.equals(myWalletID)) {
                        boolean find=false;
                        for(String ID : MyDestinationAddressList){
                            if(DestinationID.equals(ID))  find=true;
                        }
                       if(!find) MyDestinationAddressList.add(DestinationID);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
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

    @FXML
    protected void onOK(){
        DestinationAddress.getItems().add(EnterDestinationAddress.getText());
        EnterDestinationAddress.clear();
    }

    public void getCurrency(ActionEvent event){
        SelectedCurrency = ChoiceCurrency.getValue();
        if(validChoiceCurrency()) currencyName.setText(SelectedCurrency);
    }
    public void getWalletID(ActionEvent event){
        SelectedDestination = DestinationAddress.getValue();
        walletID.setText(SelectedDestination);
    }
    private boolean validChoiceCurrency(){
        int SwFind = 1,SwValid=1;
        for (Trades.Currency currency : Trades.Wallet.myCurrency){
            if(currency.getCurrencyName().equals(SelectedCurrency)) SwFind=0;
        }
        if(SwFind==1){
            warningField.setText(SelectedCurrency+" is not available in your wallet");
            transition.setOnFinished(e -> warningField.setText(""));
            transition.play();
            SwValid=0;
        }else {
            for (Trades.Currency currency : Trades.Wallet.myCurrency){
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
        if (valid) return true;
        else return false;
    }
    private boolean validDestinationAddress(){
        if(walletID.getText().isEmpty()) {
            warningField.setText("Select DestinationAddress ");
            transition.setOnFinished(event -> warningField.setText(""));
            transition.play();
            return false;
        }
        return true;
    }
    @FXML
    protected void onButton25Clicked(){
        if(validChoiceCurrency()){
            for (Trades.Currency currency : Trades.Wallet.myCurrency){
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
            for (Trades.Currency currency : Trades.Wallet.myCurrency){
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
            for (Trades.Currency currency : Trades.Wallet.myCurrency){
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
            for (Trades.Currency currency : Trades.Wallet.myCurrency){
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
    @FXML
    protected void onConfirmedClicked(){
        if(validChoiceCurrency() && validValue() && validDestinationAddress()) {
            TransferConnection.insertRowWithPreparedStatement(myUserName,Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()),myWalletID,SelectedDestination,SelectedCurrency,value,1233);
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
    }

    @FXML
    protected void onHomeClicked() throws IOException {
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
        StartPage.switchPages.ChangePageByClickingButton(History,"/Trades/Transfer.fxml");
    }
    @FXML
    protected void onSwapClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Swap,"/Trades/Transfer.fxml");
    }
    @FXML
    protected void onExitClicked() {
        System.out.println("EXIT");
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
        RegisterCardButton.setStyle("-fx-background-color:#53AAD0;");
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
        BankWithdrawalButton.setStyle("-fx-background-color:#53AAD0;");
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
