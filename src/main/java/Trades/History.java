package Trades;

import Admin.AdminHome;
import Person.Person;
import SerializableClasses.HistoryList;
import SerializableClasses.*;
import SerializableClasses.TransferLists;
import SerializableClasses.TransferTableData2;
import Start.StartPage;
import TableDataInformation.SwapTableData;
import TableDataInformation.TransferTableData;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import Person.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static Login.LoginPage.existPeron;
import static Person.User.*;

public class History implements Initializable {
    @FXML
    private ImageView CloseMenu,Menu,calender,ProfileIcon;
    @FXML
    private Button Wallet1, Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private Pane datePane,headerSlider,slider;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    boolean openHeaderSlider = false;
    ArrayList<Label> labelList;
    String myUserName=existPeron.UserName;
    @FXML
    private MediaView calenderView;
    @FXML
    private Label TOMANTransactions, YENTransactions, GBPTransactions, EURTransactions, ALLTransactions, ALLTransfers, AllSwaps,UserName;
    @FXML
    private Pane TOMANTransactions1, YENTransactions1, GBPTransactions1, EURTransactions1, ALLTransactions1, ALLTransfers1, AllSwaps1;
    @FXML
    private Button export;
    @FXML
    private TextField FileName;
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
    private TableColumn<TransferTableData, String> CurrencyNameColumn1;
    @FXML
    private TableColumn<TransferTableData, Number> CurrencyAmountColumn;
    @FXML
    private TableColumn<TransferTableData, Number> IssueTracking;

    @FXML
    private TableView<Trade> TradeTable;
    @FXML
    private TableColumn<Trade, String> CurrencyNameColumn;
    @FXML
    private TableColumn<Trade, String> OperationColumn;
    @FXML
    private TableColumn<Trade, Number> rateColumn;
    @FXML
    private TableColumn<Trade, Number> countColumn;
    @FXML
    private TableColumn<Trade, String> dateColumn;
    @FXML
    private TableColumn<Trade, Number> percentageColumn;
    @FXML
    private Button All_Order, Open_Order, Completed_Order;
    @FXML
    private Line All_Orders;
    @FXML
    private Line Open_Orders;
    @FXML
    private Line Completed_Orders;
    public static ObservableList<Trade> ObservableExchangeDataList = FXCollections.observableArrayList();
    ObservableList<TransferTableData> ObservableTransferDataList = FXCollections.observableArrayList();
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

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer1;
    public int CurrencyNumber=0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        headerSlider.setTranslateY(-220);
        slider.setTranslateX(-270);
        //////////////////////////////////
        SourceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        DestinationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyAmountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        IssueTracking.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        DateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        TimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTime()));
        SourceColumn.setCellValueFactory(cellData -> cellData.getValue().SourceWalletIDProperty());
        DestinationColumn.setCellValueFactory(cellData -> cellData.getValue().DestinationWalletIDProperty());
        CurrencyNameColumn1.setCellValueFactory(cellData -> cellData.getValue().CurrencyNameProperty());
        CurrencyAmountColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyAmountProperty());
        IssueTracking.setCellValueFactory(cellData -> cellData.getValue().IssueTrackingProperty());

        transferTableView.setItems(ObservableTransferDataList);
        transferTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ///////////////////////////////////////

        //////////////////////////////
        TradeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        OperationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        rateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        percentageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        OperationColumn.setCellValueFactory(cellData -> cellData.getValue().Operation());
        CurrencyNameColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyName());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().count());
        rateColumn.setCellValueFactory(cellData -> cellData.getValue().rate());
        percentageColumn.setCellValueFactory(cellData -> cellData.getValue().percentage());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().date());
        TradeTable.setItems(ObservableExchangeDataList);
        setExchangeTableData();
        /////////////////////////////////////////////////////
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
        //////////////////////////////////////////////////////
        AllSwaps.setOnMouseClicked(event -> {
            All_Orders.setVisible(false);
            All_Order.setVisible(false);
            Completed_Orders.setVisible(false);
            Completed_Order.setVisible(false);
            Open_Orders.setVisible(false);
            Open_Order.setVisible(false);
            TradeTable.setVisible(false);
            transferTableView.setVisible(false);
            SwapTableView.setVisible(true);
            CurrencyNumber=7;
            setSwapTableData();
        });
        ALLTransfers.setOnMouseClicked(event -> {
            All_Orders.setVisible(false);
            SwapTableView.setVisible(false);
            All_Order.setVisible(false);
            Completed_Orders.setVisible(false);
            Completed_Order.setVisible(false);
            Open_Orders.setVisible(false);
            Open_Order.setVisible(false);
            TradeTable.setVisible(false);
            transferTableView.setVisible(true);
            CurrencyNumber=6;
            setTransferTableData();
        });
        ALLTransactions.setOnMouseClicked(event -> {
            All_Orders.setVisible(true);
            SwapTableView.setVisible(false);
            All_Order.setVisible(true);
            Completed_Orders.setVisible(true);
            Completed_Order.setVisible(true);
            Open_Orders.setVisible(true);
            Open_Order.setVisible(true);
            TradeTable.setVisible(true);
            transferTableView.setVisible(false);
            CurrencyNumber=1;
            setExchangeTableData();
        });
        TOMANTransactions.setOnMouseClicked(event -> {
            All_Orders.setVisible(true);
            SwapTableView.setVisible(false);
            All_Order.setVisible(true);
            Completed_Orders.setVisible(true);
            Completed_Order.setVisible(true);
            Open_Orders.setVisible(true);
            Open_Order.setVisible(true);
            TradeTable.setVisible(true);
            transferTableView.setVisible(false);
            CurrencyNumber=2;
            setExchangeTableData();
        });
        YENTransactions.setOnMouseClicked(event -> {
            All_Orders.setVisible(true);
            SwapTableView.setVisible(false);
            All_Order.setVisible(true);
            Completed_Orders.setVisible(true);
            Completed_Order.setVisible(true);
            Open_Orders.setVisible(true);
            Open_Order.setVisible(true);
            TradeTable.setVisible(true);
            transferTableView.setVisible(false);
            CurrencyNumber=3;
            setExchangeTableData();
        });
        EURTransactions.setOnMouseClicked(event -> {
            All_Orders.setVisible(true);
            SwapTableView.setVisible(false);
            All_Order.setVisible(true);
            Completed_Orders.setVisible(true);
            Completed_Order.setVisible(true);
            Open_Orders.setVisible(true);
            Open_Order.setVisible(true);
            TradeTable.setVisible(true);
            transferTableView.setVisible(false);
            CurrencyNumber=4;
            setExchangeTableData();
        });
        GBPTransactions.setOnMouseClicked(event -> {
            All_Orders.setVisible(true);
            SwapTableView.setVisible(false);
            All_Order.setVisible(true);
            Completed_Orders.setVisible(true);
            Completed_Order.setVisible(true);
            Open_Orders.setVisible(true);
            Open_Order.setVisible(true);
            TradeTable.setVisible(true);
            transferTableView.setVisible(false);
            CurrencyNumber = 5;
            setExchangeTableData();
        });
        Completed_Order.setOnMouseClicked(event -> {
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            setExchangeTableData_CompletedOrder();
        });
        Completed_Orders.setOnMouseClicked(event -> {
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            setExchangeTableData_CompletedOrder();
        });
        Open_Orders.setOnMouseClicked(event -> {
            Open_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            setExchangeTableData_OpenOrder();
        });
        Open_Order.setOnMouseClicked(event -> {
            Open_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            All_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            setExchangeTableData_OpenOrder();
        });
        All_Orders.setOnMouseClicked(event -> {
            All_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            setExchangeTableData();
        });
        All_Order.setOnMouseClicked(event -> {
            All_Orders.setStroke(new Color(253 / 255.0, 237 / 255.0, 236 / 255.0, 1.0));
            Open_Orders.setStroke(Color.rgb(209, 58, 255));
            Completed_Orders.setStroke(Color.rgb(209, 58, 255));
            setExchangeTableData();
        });
        //////////////////////////////export file
        export.setOnMouseClicked(event -> {
            if(CurrencyNumber==6){
                exportToFileTransfer(FileName.getScene().getWindow(), FileName.getText(),  sendTransferDataTable(myUserName, existPeron.WalletID).transfers);
            }
            else if(CurrencyNumber==7){
                exportToFileSwap(FileName.getScene().getWindow(), FileName.getText(), SendSwapList(existPeron.UserName));
            }
            else exportToFile(FileName.getScene().getWindow(), FileName.getText(), setExchangeTableData());
        });
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
        UserName.setText(existPeron.UserName);
    }

    public void setSwapTableData() {
        ArrayList<SwapTableData> SwapDataList = new ArrayList<>();
        ObservableSwapDataList.clear();
        for(SwapList swapList : SendSwapList(existPeron.UserName)){
            SwapDataList.add(new SwapTableData(swapList.date,swapList.time,swapList.SelectedCurrencyName,swapList.ConvertedCurrencyName,swapList.CurrencyAmount,swapList.EquivalentCurrencyAmount));
        }
        for (SwapTableData swap : SwapDataList) {
            ObservableSwapDataList.add(new SwapTableData(swap.getDate(),swap.getTime(),swap.getSelectedCurrencyName(),swap.getConvertedCurrencyName(),swap.getCurrencyAmount(),swap.getEquivalentCurrencyAmount()));
        }
    }

    public void setExchangeTableData_CompletedOrder(){
        ObservableExchangeDataList.clear();
        for(HistoryList x: sendHistoryList(myUserName)){
            if(x.percentageOfCompletion==100) {
                if(CurrencyNumber==1) {
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                } else if(CurrencyNumber==2){
                    if(x.currencyName.equals("TOMAN")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==3){
                    if(x.currencyName.equals("YEN")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==4){
                    if(x.currencyName.equals("EUR")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==5){
                    if(x.currencyName.equals("GBP")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                }
            }
        }
    }

    public void setExchangeTableData_OpenOrder(){
        ObservableExchangeDataList.clear();
        for(HistoryList x: sendHistoryList(myUserName)){
            if(x.percentageOfCompletion!=100) {
                if(CurrencyNumber==1) {
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                } else if(CurrencyNumber==2){
                    if(x.currencyName.equals("TOMAN")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==3){
                    if(x.currencyName.equals("YEN")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==4){
                    if(x.currencyName.equals("EUR")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                } else if(CurrencyNumber==5){
                    if(x.currencyName.equals("GBP")){
                        String operation;
                        if (x.operationType == 1) operation = "Sell";
                        else operation = "Buy";
                        ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                    }
                }
            }
        }
    }

    public ArrayList<HistoryList> setExchangeTableData(){
        ObservableExchangeDataList.clear();
        ArrayList<HistoryList> arrayList = new ArrayList<>();
        for(HistoryList x: sendHistoryList(myUserName)){
            if(CurrencyNumber==1) {
                arrayList.add(x);
                String operation;
                if (x.operationType == 1) operation = "Sell";
                else operation = "Buy";
                ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
            } else if(CurrencyNumber==2){
                arrayList.add(x);
                if(x.currencyName.equals("TOMAN")){
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                }
            } else if(CurrencyNumber==3){
                arrayList.add(x);
                if(x.currencyName.equals("YEN")){
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                }
            } else if(CurrencyNumber==4){
                arrayList.add(x);
                if(x.currencyName.equals("EUR")){
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                }
            } else if(CurrencyNumber==5){
                arrayList.add(x);
                if(x.currencyName.equals("GBP")){
                    String operation;
                    if (x.operationType == 1) operation = "Sell";
                    else operation = "Buy";
                    ObservableExchangeDataList.add(new Trade(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion, x.date));
                }
            }
        }
        return arrayList;
    }

    public void setTransferTableData() {
        ArrayList<TransferTableData2> ObservableTransferDataList1;
        TransferLists transferLists = sendTransferDataTable(myUserName, existPeron.WalletID);
        if(transferLists!=null) {
            ObservableTransferDataList1 = transferLists.transfers;
            for (TransferTableData2 transfer : ObservableTransferDataList1) {
                ObservableTransferDataList.add(new TransferTableData(transfer.getDate(), transfer.getTime(),
                        transfer.SourceWalletID1, transfer.DestinationWalletID1, transfer.CurrencyName1,
                        transfer.CurrencyAmount1, transfer.issueTracking1));
            }
        }
    }

    public class Trade {
        StringProperty Operation = new SimpleStringProperty(this, "Operation", "");
        StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
        DoubleProperty count = new SimpleDoubleProperty(this, "count", 0.0);
        DoubleProperty rate = new SimpleDoubleProperty(this, "rate", 0.0);
        FloatProperty percentage = new SimpleFloatProperty(this, "percentage", 0);
        StringProperty date = new SimpleStringProperty(this, "CurrencyName", "");
        public Trade(String Operation, String CurrencyName, double count, double rate, float percentage, String date) {
            this.Operation.set(Operation);
            this.CurrencyName.set(CurrencyName);
            this.count.set(count);
            this.rate.set(rate);
            this.percentage.set(percentage);
            this.date.set(date);
        }
        public StringProperty Operation() {return Operation;}
        public DoubleProperty count() {return count;}
        public DoubleProperty rate() {return rate;}
        public FloatProperty percentage() {return percentage;}
        public StringProperty CurrencyName() {return CurrencyName;}
        public StringProperty date() {return date;}
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
    @FXML
    protected void onHomeClicked() throws IOException {
        Home.setStyle("-fx-background-color:#013b46;");
        StartPage.switchPages.ChangePageByClickingButton(Home,"/Home/HomePage.fxml");
    }
    @FXML
    protected void onWalletClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Wallet1,"/Trades/Wallet.fxml");
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
    protected void onCalender(){
        calender.setOnMouseClicked(event -> {
            if(openHeaderSlider) {
                openHeaderSlider = false;
                TranslateTransition slide = new TranslateTransition();
                slide.setDuration(Duration.seconds(0.4));
                slide.setNode(headerSlider);
                slide.setToY(-220);
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
                headerSlider.setTranslateY(-220);
            }
        });
    }
    @FXML
    protected void onProfile() throws IOException{
        StartPage.switchPages.ChangePageByClickingImage(ProfileIcon,"/Home/ProfilePage.fxml");
    }

    private void exportToFile(Window stage, String fileName, List<HistoryList> dataList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(fileName + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        String operation = null;
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (HistoryList data : dataList) {
                    if(data.operationType==1) operation = "sell";
                    else operation = "buy";
                    writer.write(operation+", "+data.currencyName+", "+data.Count+", "+data.Rate+", "+ data.date+"\n");
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exportToFileTransfer(Window stage, String fileName, List<TransferTableData2> dataList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(fileName + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (TransferTableData2 data : dataList) {
                    writer.write(data.getDate()+", "+data.getTime()+", "+data.SourceWalletID1+", "+
                            data.DestinationWalletID1+ ", "+data.CurrencyName1+", "+data.CurrencyAmount1
                            +", "+data.issueTracking1+"\n");
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void exportToFileSwap(Window stage, String fileName, List<SwapList> dataList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(fileName + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (SwapList data : dataList) {
                    writer.write(data.SelectedCurrencyName+", "+data.ConvertedCurrencyName+", "+data.CurrencyAmount+", "+data.EquivalentCurrencyAmount
                            +", "+data.userName+", "+data.date+", "+data.time+"\n");
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
