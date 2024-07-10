package Admin;

import Home.HomePage;
import Person.Person;
import SerializableClasses.SwapList;
import SerializableClasses.TradesList;
import SerializableClasses.TransferLists;
import SerializableClasses.TransferTableData2;
import Start.StartPage;
import TableDataInformation.SwapTableData;
import TableDataInformation.TransferTableData;
import Trades.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import static Login.LoginPage.existPeron;
import static Person.User.*;
import static Trades.Wallet.myCurrency;
import Person.*;

public class UsersHistory implements Initializable {

    @FXML
    private Button All_Order,Completed_Order,Exit,Open_Order,Wallet,Transfer,OkEmbezzlement,Swap;

    @FXML
    private Line All_Orders,Completed_Orders,Completed_Orders1,Open_Orders,Transfers,Swaps;

    @FXML
    private Label EURPrice,Lone,LTwo,LabelThree,LabelFour,LabelFive,TOMANPrice,Total,USDPrice,UserName,UserNameUnderPicture,YENPrice,GBPrice;
    @FXML
    private Label phoneInfo,userNameInfo,walletInfo,emailInfo;
    @FXML
    private Label ToUSD,AmountCurrency,nameCurrency,embezzlementWarning;
    @FXML
    private ImageView CloseMenu,Menu,UserProfile;;
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
    private TableView<Exchange.Trades> TradeTable;
    @FXML
    private TableColumn<Exchange.Trades, String> CurrencyNameColumnC;
    @FXML
    private TableColumn<Exchange.Trades, String> OperationColumn;
    @FXML
    private TableColumn<Exchange.Trades, Number> rateColumn;
    @FXML
    private TableColumn<Exchange.Trades, Number> countColumn;
    @FXML
    private TableColumn<Exchange.Trades, Number> percentageColumn;

    @FXML
    private TableView<SwapTableData> SwapTableView;
    @FXML
    private TableColumn<SwapTableData, Date> DateColumnS;
    @FXML
    private TableColumn<SwapTableData, Time> TimeColumnS;
    @FXML
    private TableColumn<SwapTableData, String> SelectedCurrencyName;
    @FXML
    private TableColumn<SwapTableData, String> ConvertedCurrencyName;
    @FXML
    private TableColumn<SwapTableData, Number> CurrencyAmount;
    @FXML
    private TableColumn<SwapTableData, Number> EquivalentCurrencyAmount;
    ObservableList<SwapTableData> ObservableSwapDataList = FXCollections.observableArrayList();
    @FXML
    private TextField embezzlementAmount;
    @FXML
    private Pane slider;
    PauseTransition transition = new PauseTransition(Duration.seconds(2));
    private Person userSelected = AdminHome.UserHistory;
    private Label[] labels;
    Timeline timeline;
    String embezzlement = "";
    double num = 0;
    public static ArrayList<Currency> userCurrency = new ArrayList<>();
    public static ObservableList<Exchange.Cryptocurrency> ObservableCurrencyDataList = FXCollections.observableArrayList();
    public static ObservableList<Exchange.Trades> ObservableExchangeDataList = FXCollections.observableArrayList();
    ObservableList<TransferTableData> ObservableTransferDataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserName.setText("Admin");
        slider.setTranslateX(-270);
        UserNameUnderPicture.setText(userSelected.name+" "+userSelected.lastName);
        File imagefile = new File(userSelected.profilePass);
        Image image1 = new Image(imagefile.toURI().toString());
        UserProfile.setImage(image1);
        userNameInfo.setText(userSelected.name);
        phoneInfo.setText(userSelected.phoneNumber);
        emailInfo.setText(userSelected.email);
        walletInfo.setText(userSelected.WalletID);
        fillWallet();
        setColorChart();
        UserWallet();



        //////////////////////////////
        TradeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        OperationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumnC.setCellFactory(TextFieldTableCell.forTableColumn());
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        rateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        percentageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        OperationColumn.setCellValueFactory(cellData -> cellData.getValue().Operation());
        CurrencyNameColumnC.setCellValueFactory(cellData -> cellData.getValue().CurrencyName());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().count());
        rateColumn.setCellValueFactory(cellData -> cellData.getValue().rate());
        percentageColumn.setCellValueFactory(cellData -> cellData.getValue().percentage());
        TradeTable.setItems(ObservableExchangeDataList);
        setExchangeTableData();
        //////////////////////////////////
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

        ////////////////////////////////
        SelectedCurrencyName.setCellFactory(TextFieldTableCell.forTableColumn());
        ConvertedCurrencyName.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyAmount.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        EquivalentCurrencyAmount.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        DateColumnS.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        TimeColumnS.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTime()));
        SelectedCurrencyName.setCellValueFactory(cellData -> cellData.getValue().SelectedCurrencyNameProperty());
        ConvertedCurrencyName.setCellValueFactory(cellData -> cellData.getValue().ConvertedCurrencyNameProperty());
        CurrencyAmount.setCellValueFactory(cellData -> cellData.getValue().CurrencyAmountProperty());
        EquivalentCurrencyAmount.setCellValueFactory(cellData -> cellData.getValue().EquivalentCurrencyAmountProperty());

        SwapTableView.setItems(ObservableSwapDataList);
        SwapTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        ////////////////////////////////
        Completed_Order.setOnMouseClicked(event -> {
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData_CompletedOrder();
        });
        Completed_Orders.setOnMouseClicked(event -> {
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData_CompletedOrder();
        });
        Open_Orders.setOnMouseClicked(event -> {
            Open_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData_OpenOrder();
        });
        Open_Order.setOnMouseClicked(event -> {
            Open_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData_OpenOrder();
        });
        All_Orders.setOnMouseClicked(event -> {
            All_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData();
        });
        All_Order.setOnMouseClicked(event -> {
            All_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(false);
            SwapTableView.setVisible(false);
            setExchangeTableData();
        });
        Transfers.setOnMouseClicked(event -> {
            Transfers.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(true);
            SwapTableView.setVisible(false);
            setTransferTableData();
        });
        Transfer.setOnMouseClicked(event -> {
            Transfers.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Swaps.setStroke(Color.rgb(209, 58, 255));
            transferTableView.setVisible(true);
            SwapTableView.setVisible(false);
            setTransferTableData();
        });
        Swaps.setOnMouseClicked(event -> {
            Swaps.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            SwapTableView.setVisible(true);
            transferTableView.setVisible(false);
            setSwapTableData();
        });
        Swap.setOnMouseClicked(event -> {
            Swaps.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Transfers.setStroke(Color.rgb(209, 58, 255));
            SwapTableView.setVisible(true);
            transferTableView.setVisible(false);
            setSwapTableData();
        });

    }
    public void setColorChart(){
        labels = new Label[] {Lone,LTwo,LabelThree,LabelFour,LabelFive};
        Collections.sort(userCurrency, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return Double.compare( o1.getCurrencyAmount(), o2.getCurrencyAmount()) ;
            }
        });
        for(int i = 0; i<5; i++){
            labels[i].setText(userCurrency.get(i).getCurrencyName());
            labels[i].setPrefWidth(45+(userCurrency.get(i).getCurrencyAmount()/1000000)*200);
            if(userCurrency.get(i).getCurrencyName().equals("USD")) labels[i].setStyle("-fx-background-color:#ff923f");
            else if(userCurrency.get(i).getCurrencyName().equals("EUR")) labels[i].setStyle("-fx-background-color:  #fcf885");
            else if(userCurrency.get(i).getCurrencyName().equals("TOMAN")) labels[i].setStyle("-fx-background-color: #28a7eb");
            else if(userCurrency.get(i).getCurrencyName().equals("YEN")) labels[i].setStyle("-fx-background-color: #6e08a4");
            else if(userCurrency.get(i).getCurrencyName().equals("GBP")) labels[i].setStyle("-fx-background-color: #fca3f7");
        }
        nameCurrency.setText(userCurrency.get(4).getCurrencyName());
        AmountCurrency.setText(String.valueOf(userCurrency.get(4).getCurrencyAmount()));

    }
    public void fillWallet(){
        userCurrency = fillArrayList(userSelected.UserName);
    }
    public void UserWallet(){
        for (Currency currency : userCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USDPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("EUR")) {EURPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("TOMAN")) {TOMANPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("YEN")) {YENPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("GBP")) {GBPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
        }
        ConvertCurrenciesToUSD();
    }
    private void ConvertCurrenciesToUSD(){
        double total=0;
        double EURtoUSD=0,TOMANtoUSD=0,YENtoUSD=0,GBPtoUSD=0,USD=0;
        double EURNow=0,TOMANow=0,YENNow=0,GBPNow=0,USDNow=0;
        DecimalFormat df =new DecimalFormat("#.##");
        for(int i = 0; i< HomePage.ObservableCurrencyDataList.size(); i++){
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("USD")) {USDNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("EUR")) {EURNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("TOMAN")) {TOMANow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("YEN")) {YENNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("GBP")) {GBPNow= HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
        }
        for (Currency currency : userCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USD=currency.getCurrencyAmount();total+=USD;}
            else if(currency.getCurrencyName().equals("EUR"))  {EURtoUSD=USDNow/EURNow*currency.getCurrencyAmount();total+=EURtoUSD;}
            else if(currency.getCurrencyName().equals("TOMAN"))  {TOMANtoUSD=USDNow/TOMANow*currency.getCurrencyAmount();total+=TOMANtoUSD;}
            else if(currency.getCurrencyName().equals("YEN"))  {YENtoUSD=USDNow/YENNow*currency.getCurrencyAmount();total+=YENtoUSD;}
            else if(currency.getCurrencyName().equals("GBP"))  {GBPtoUSD=USDNow/GBPNow*currency.getCurrencyAmount();total+=GBPtoUSD;}
        }
        Total.setText(String.valueOf(df.format(total)));
        if(userCurrency.get(4).getCurrencyName().equals("USD")) {ToUSD.setText(String.valueOf(df.format(USD)));}
        else if(userCurrency.get(4).getCurrencyName().equals("EUR"))  {ToUSD.setText(String.valueOf(df.format(EURtoUSD)));}
        else if(userCurrency.get(4).getCurrencyName().equals("TOMAN"))  {ToUSD.setText(String.valueOf(df.format(TOMANtoUSD)));}
        else if(userCurrency.get(4).getCurrencyName().equals("YEN"))   {ToUSD.setText(String.valueOf(df.format(YENtoUSD)));}
        else if(userCurrency.get(4).getCurrencyName().equals("GBP"))  {ToUSD.setText(String.valueOf(df.format(GBPtoUSD)));}
    }

    public void setExchangeTableData(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(userSelected.UserName)){
            String operation;
            if(x.operationType==1)  operation = "Sell";
            else operation = "Buy";
            System.out.print(x.currencyName+" is currencyName.\n");
            ObservableExchangeDataList.add(new Exchange.Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
        }
    }

    public void setExchangeTableData_CompletedOrder(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(userSelected.UserName)){
            if(x.percentageOfCompletion==100) {
                String operation;
                if (x.operationType == 1) operation = "Sell";
                else operation = "Buy";
                System.out.print(x.currencyName + " is currencyName.\n");
                ObservableExchangeDataList.add(new Exchange.Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
            }
        }
    }

    public void setExchangeTableData_OpenOrder(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(userSelected.UserName)){
            if(x.percentageOfCompletion!=100) {
                String operation;
                if (x.operationType == 1) operation = "Sell";
                else operation = "Buy";
                System.out.print(x.currencyName + " is currencyName.\n");
                ObservableExchangeDataList.add(new Exchange.Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
            }
        }
    }
    public void setTransferTableData() {
        ObservableTransferDataList.clear();
        ArrayList<TransferTableData2> ObservableTransferDataList1;
        TransferLists transferLists = sendTransferDataTable(userSelected.UserName,userSelected.WalletID);
        if(transferLists!=null) {
            ObservableTransferDataList1 = transferLists.transfers;

            for (TransferTableData2 transfer : ObservableTransferDataList1) {
                ObservableTransferDataList.add(new TransferTableData(transfer.getDate(), transfer.getTime(),
                        transfer.SourceWalletID1, transfer.DestinationWalletID1, transfer.CurrencyName1,
                        transfer.CurrencyAmount1, transfer.issueTracking1));
            }
        }
    }
    public void setSwapTableData() {
        ObservableSwapDataList.clear();
        ObservableSwapDataList.clear();
        ArrayList<SwapTableData> SwapDataList = new ArrayList<>();
        for(SwapList swapList : SendSwapList(userSelected.UserName)){
            SwapDataList.add(new SwapTableData(swapList.date,swapList.time,swapList.SelectedCurrencyName,swapList.ConvertedCurrencyName,swapList.CurrencyAmount,swapList.EquivalentCurrencyAmount));
        }
        for (SwapTableData swap : SwapDataList) {
            ObservableSwapDataList.add(new SwapTableData(swap.getDate(),swap.getTime(),swap.getSelectedCurrencyName(),swap.getConvertedCurrencyName(),swap.getCurrencyAmount(),swap.getEquivalentCurrencyAmount()));
        }
    }
    @FXML
    private void OnEmbezzlement(){
        embezzlement = embezzlementAmount.getText();
        try {
            num = Double.parseDouble(embezzlement);
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Currency currency : userCurrency){
            if(currency.getCurrencyName().equals("USD")){
                if(num>currency.getCurrencyAmount()){
                    embezzlementWarning.setText(" this user have not enough USD!");
                    transition.setOnFinished(e -> embezzlementWarning.setText(""));
                    transition.play();
                    embezzlementAmount.clear();
                    embezzlement = "";
                }
                if(num >= currency.getCurrencyAmount()/50 ){
                    embezzlementWarning.setText(" Attention ! withdrawal limit");
                    transition.setOnFinished(e -> embezzlementWarning.setText(""));
                    transition.play();
                    embezzlementAmount.clear();
                    embezzlement = "";
                }
            }
        }
    }
    @FXML
    private void OkEmbezzlement(){
        if(!embezzlement.isEmpty()){
            embezzlementUser(userSelected.UserName,num);
            embezzlementWarning.setText(" DONE!");
            transition.setOnFinished(e -> embezzlementWarning.setText(""));
            transition.play();
            embezzlement = "";
            fillWallet();
            UserWallet();
            setColorChart();
        }else {
            embezzlementWarning.setText("Enter Amount!");
            transition.setOnFinished(e -> embezzlementWarning.setText(""));
            transition.play();
        }
    }

    @FXML
    void onExitClicked(ActionEvent event) {
        logOut("Admin");
        try {
            exit();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void exit() throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(Exit, "/Login/LoginPage.fxml");
    }


    @FXML
    void onHomeClicked(ActionEvent event) throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Wallet, "/Admin/AdminHome.fxml");
    }

    @FXML
    void onMessenger(ActionEvent event) {

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

}

