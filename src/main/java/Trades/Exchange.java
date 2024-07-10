package Trades;

import TableDataInformation.CurrencyTableData;
import Home.HomePage;
import Login.LoginPage;
import SerializableClasses.ExchangeList;
import SerializableClasses.TradesList;
import Start.StartPage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
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
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static Login.LoginPage.existPeron;
import static Person.User.*;

public class Exchange implements Initializable {

    @FXML
    private ImageView CloseMenu,Menu,calender,ProfileIcon;
    @FXML
    private Button Wallet1, Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private Label x;
    @FXML
    private TableView<Cryptocurrency> currencyPrice;
    @FXML
    private TableColumn<Cryptocurrency, String> symbolColumn;
    @FXML
    private TableColumn<Cryptocurrency, Number> priceColumn;
    @FXML
    private TableColumn<Cryptocurrency, String> changesColumn;
    @FXML
    private TableView<Trades> TradeTable;
    @FXML
    private TableColumn<Trades, String> OperationColumn;
    @FXML
    private TableColumn<Trades, String> CurrencyNameColumn;
    @FXML
    private TableColumn<Trades, Number> rateColumn;
    @FXML
    private TableColumn<Trades, Number> countColumn;
    @FXML
    private TableColumn<Trades, Number> percentageColumn;
    @FXML
    private Button buy;
    @FXML
    private Button sell;
    @FXML
    private Button buyButton;
    @FXML
    private Button selLButton;
    @FXML
    private CubicCurve buying;
    @FXML
    private CubicCurve selling;
    @FXML
    private TextField amount;
    @FXML
    private Label TotalSum;
    @FXML
    private Label received_commission;
    @FXML
    private Line All_Orders;
    @FXML
    private Line Open_Orders;
    @FXML
    private Line Completed_Orders;
    @FXML
    private Label USD_amount;
    @FXML
    private ImageView plus;
    @FXML
    private ImageView minus;
    @FXML
    private Label NoBuying;
    @FXML
    private Label NoSelling;
    @FXML
    private Label nameCurrency;
    @FXML
    private Label highestPrice;
    @FXML
    private Label lowestPrice;
    @FXML
    private ImageView CurrencyPicture;
    @FXML
    private Label buy1, buy2, buy3, buy4, buy5, buy6, buy7, buy8, buy9;
    @FXML
    private Label buy11, buy12, buy13, buy14, buy15, buy16, buy17, buy18, buy19;
    @FXML
    private Label sell1, sell2, sell3, sell4, sell5, sell6, sell7, sell8, sell9;
    @FXML
    private Label sell11, sell12, sell13, sell14, sell15, sell16, sell17, sell18, sell19;
    @FXML
    private Label price , balance, validAmount, recordExchange, percent25, percent50, percent75, percent100
            , percent251, percent501, percent751, percent101, closeStore;
    @FXML
    private Label percentBalance,UserName;
    @FXML
    private ChoiceBox<String> SelectCurrency;
    @FXML
    private Button All_Order, Open_Order, Completed_Order ;
    private double priceNow;
    String SelectedCurrency;
    private Label[] buyLabels;
    private Label[] buyLabels1;
    private Label[] sellLabels;
    private Label[] sellLabels1;
    Timeline timeline;
    String myUserName=existPeron.UserName;

    @FXML
    private Pane datePane,headerSlider,slider;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31,
            selectedCurrency;
    boolean openHeaderSlider = false;
    ArrayList<Label> labelList;
    public static ObservableList<Cryptocurrency> ObservableCurrencyDataList = FXCollections.observableArrayList();
    public static ObservableList<Trades> ObservableExchangeDataList = FXCollections.observableArrayList();
    @FXML
    private MediaView calenderView;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer1;
    private static int FromCurrency=0;
    private String currencyName;
    DecimalFormat df =new DecimalFormat("#.###");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCurrency.setVisible(true);
        headerSlider.setTranslateY(-200);
        slider.setTranslateX(-270);
        recordExchange.setVisible(false);
        balance.setVisible(false);
        closeStore.setVisible(false);
        percentBalance.setVisible(false);
        buyLabels = new Label[]{buy1, buy2, buy3, buy4, buy5, buy6, buy7, buy8, buy9};
        buyLabels1 = new Label[]{buy11, buy12, buy13, buy14, buy15, buy16, buy17, buy18, buy19};
        sellLabels = new Label[]{sell1, sell2, sell3, sell4, sell5, sell6, sell7, sell8, sell9};
        sellLabels1 = new Label[]{sell11, sell12, sell13, sell14, sell15, sell16, sell17, sell18, sell19};
        USD_amount.setText("                       USD");
        NoSelling.setVisible(true);
        NoBuying.setVisible(true);
        for (int i=0; i<9; i++){
            buyLabels[i].setVisible(false);
            buyLabels1[i].setVisible(false);
            sellLabels[i].setVisible(false);
            sellLabels1[i].setVisible(false);
        }
        SelectCurrency.getItems().addAll("TOMAN", "YEN", "EUR", "GBP");
        SelectCurrency.setOnAction(event -> {
            SelectedCurrency = SelectCurrency.getValue();
            recordExchange.setVisible(false);
            USD_amount.setText("                       USD");
            onSellORBuyButton();
            NoSelling.setVisible(true);
            NoBuying.setVisible(true);
            for (int i=0; i<9; i++){
                buyLabels[i].setVisible(false);
                buyLabels1[i].setVisible(false);
                sellLabels[i].setVisible(false);
                sellLabels1[i].setVisible(false);
            }
            amount.setText(null);
            TotalSum.setText("         USD");
            price.setText(String.valueOf(df.format(priceNow)));
            ArrayList<ExchangeList> sellsList = fillExchangeTable(SelectedCurrency, 15);
            ArrayList<ExchangeList> buysList = fillExchangeTable(SelectedCurrency, 16);
            int buyAmount = buysList.size();
            int sellAmount = sellsList.size();
            for(int i=0; i<buyAmount; i++){
                NoBuying.setVisible(false);
                for(int j =i+1; j<buyAmount; j++){
                    if(buysList.get(j).price>buysList.get(i).price){
                        ExchangeList x = buysList.get(j);
                        buysList.set(j, buysList.get(i));
                        buysList.set(i, x);
                    }
                }
            }
            for(int i=0; i<sellAmount; i++){
                NoSelling.setVisible(false);
                for(int j =i+1; j<sellAmount; j++){
                    if(sellsList.get(j).price>sellsList.get(i).price){
                        ExchangeList x = sellsList.get(j);
                        sellsList.set(j, buysList.get(i));
                        sellsList.set(i, x);
                    }
                }
            }
            for(int i = 0; i<sellAmount; i++){
                sellLabels[8-i].setText(String.valueOf(sellsList.get(i).amount));
                sellLabels[8-i].setPrefWidth((sellsList.get(i).amount/200)*500);
                sellLabels[8-i].setVisible(true);
                sellLabels1[8-i].setText(String.valueOf(sellsList.get(i).price));
                sellLabels1[8-i].setVisible(true);
            }
            for(int i = 0; i<buyAmount; i++){
                buyLabels[i].setText(String.valueOf(buysList.get(i).amount));
                buyLabels[i].setPrefWidth((buysList.get(i).amount/200)*500);
                buyLabels[i].setVisible(true);
                buyLabels1[i].setText(String.valueOf(buysList.get(i).price));
                buyLabels1[i].setVisible(true);
            }
        });
        plus.setOnMouseClicked(event -> {
            DecimalFormat df =new DecimalFormat("#.###");
            USD_amount.setText("                "+df.format(Double.parseDouble(USD_amount.getText())+0.001));
        });
        minus.setOnMouseClicked(event -> {
            DecimalFormat df =new DecimalFormat("#.###");
            USD_amount.setText("                "+df.format(Double.parseDouble(USD_amount.getText())-0.001));
        });
        sell.setOnMouseClicked(event -> {
            sell.setStyle("-fx-background-color:#c991d8;");
            buy.setStyle("-fx-background-color: #FDEDEC;");
        });
        buy.setOnMouseClicked(event -> {
            sell.setStyle("-fx-background-color: #FDEDEC;");
            buy.setStyle("-fx-background-color:#c991d8;");
        });

        /////////////////////////////
        String tradedCurrency = sendTradedCurrency();
        if(!tradedCurrency.equals("null")) {
            int DataList_index = 0;
            for (int i = 0; i < 5; i++) {
                if (HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals(tradedCurrency))
                    DataList_index = i;
            }
            if(tradedCurrency.equals("GBP")) CurrencyPicture.setImage(new Image(getClass().getResourceAsStream("/icons/GBP.png")));
            if(tradedCurrency.equals("YEN")) CurrencyPicture.setImage(new Image(getClass().getResourceAsStream("/icons/YEN.png")));
            if(tradedCurrency.equals("EUR")) CurrencyPicture.setImage(new Image(getClass().getResourceAsStream("/icons/EUR.png")));
            if(tradedCurrency.equals("TOMAN")) CurrencyPicture.setImage(new Image(getClass().getResourceAsStream("/icons/TOMAN.png")));
            nameCurrency.setText(tradedCurrency);
            highestPrice.setText(String.valueOf(df.format(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMaxPrice())));
            lowestPrice.setText(String.valueOf(df.format(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMinPrice())));
        }

        /////////////////////////////////
        currencyPrice.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        symbolColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        changesColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        symbolColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyNameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyPriceProperty());
        changesColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyChangesProperty());
        currencyPrice.setItems(ObservableCurrencyDataList);
        currencyPrice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCurrencyTableData();
        updateTable();

        //////////////////////////////
        TradeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        OperationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        rateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        percentageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        OperationColumn.setCellValueFactory(cellData -> cellData.getValue().Operation());
        CurrencyNameColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyName());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().count());
        rateColumn.setCellValueFactory(cellData -> cellData.getValue().rate());
        percentageColumn.setCellValueFactory(cellData -> cellData.getValue().percentage());
        TradeTable.setItems(ObservableExchangeDataList);
        setExchangeTableData();

        ///////////////////////////
        percent25.setOnMouseClicked(event -> {
            onButton25Clicked();
        });
        percent50.setOnMouseClicked(event -> {
            onButton50Clicked();
        });
        percent75.setOnMouseClicked(event -> {
            onButton75Clicked();
        });
        percent100.setOnMouseClicked(event -> {
            onButton100Clicked();
        });
        percent101.setOnMouseClicked(event -> {
            onButton100Clicked();
        });
        percent501.setOnMouseClicked(event -> {
            onButton50Clicked();
        });
        percent751.setOnMouseClicked(event -> {
            onButton75Clicked();
        });
        percent251.setOnMouseClicked(event -> {
            onButton25Clicked();
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
        UserName.setText(myUserName);
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

    public static class Cryptocurrency {
        public Cryptocurrency(String symbol, double price, String changes) {
            setCurrencyName(symbol);
            setCurrencyChanges(changes);
            setCurrencyPrice(price);
        }
        private StringProperty symbol = new SimpleStringProperty(this, "symbol", "");
        public StringProperty CurrencyNameProperty() {return symbol;}
        public void setCurrencyName(String currencyName) {this.symbol.set(currencyName);}
        private DoubleProperty price = new SimpleDoubleProperty(this, "price", 0.0);
        public DoubleProperty CurrencyPriceProperty() {return price;}
        public void setCurrencyPrice(double CurrencyPrice) {this.price.set(CurrencyPrice);}
        private StringProperty changes = new SimpleStringProperty(this, "changes", "");
        public StringProperty CurrencyChangesProperty() {return changes;}
        public void setCurrencyChanges(String CurrencyChanges) {this.changes.set(CurrencyChanges);}
    }

    public static class Trades {
        StringProperty Operation = new SimpleStringProperty(this, "Operation", "");
        StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
        DoubleProperty count = new SimpleDoubleProperty(this, "count", 0.0);
        DoubleProperty rate = new SimpleDoubleProperty(this, "rate", 0.0);
        FloatProperty percentage = new SimpleFloatProperty(this, "percentage", 0);
        public Trades(String Operation, String CurrencyName, double count, double rate, float percentage) {
            this.Operation.set(Operation);
            this.CurrencyName.set(CurrencyName);
            this.count.set(count);
            this.rate.set(rate);
            this.percentage.set(percentage);
        }
        public StringProperty Operation() {return Operation;}
        public DoubleProperty count() {return count;}
        public DoubleProperty rate() {return rate;}
        public FloatProperty percentage() {return percentage;}
        public StringProperty CurrencyName() {return CurrencyName;}
    }

    public void updateTable(){
        timeline = new Timeline(60000, new KeyFrame(Duration.millis(60000), event -> {
            ObservableCurrencyDataList.clear();
            setCurrencyTableData();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void setCurrencyTableData(){
        for(int i =0; i<5; i++){
            ObservableCurrencyDataList.add(new Cryptocurrency(HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue(),
                    HomePage.ObservableCurrencyDataList.get(i).CurrencyPriceProperty().getValue(), HomePage.ObservableCurrencyDataList.get(i).CurrencyChangesProperty().getValue()));
        }
    }

    public void setExchangeTableData(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(myUserName)){
            String operation;
            if(x.operationType==1)  operation = "Sell";
            else operation = "Buy";
            System.out.print(x.currencyName+" is currencyName.\n");
            ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
        }
    }

    public void setExchangeTableData_CompletedOrder(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(myUserName)){
            if(x.percentageOfCompletion==100) {
                String operation;
                if (x.operationType == 1) operation = "Sell";
                else operation = "Buy";
                System.out.print(x.currencyName + " is currencyName.\n");
                ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
            }
        }
    }

    public void setExchangeTableData_OpenOrder(){
        ObservableExchangeDataList.clear();
        for(TradesList x: fillExchangeOrders(myUserName)){
            if(x.percentageOfCompletion!=100) {
                String operation;
                if (x.operationType == 1) operation = "Sell";
                else operation = "Buy";
                System.out.print(x.currencyName + " is currencyName.\n");
                ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate, x.percentageOfCompletion));
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
    private void onBuyClicked(){
        buyButton.setVisible(true);
        selLButton.setVisible(false);
    }
    @FXML
    private void onSellClicked(){
        selLButton.setVisible(true);
        buyButton.setVisible(false);
    }
    Double USD_price;
    @FXML
    private void onSellORBuyButton(){
        int DataList_index=0;
        int USD_INDEX = 0;
        for(int i =0; i<5; i++){
            if(HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals(SelectedCurrency)) DataList_index=i;
            if(HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals("USD"))USD_INDEX=i;
        }
        System.out.print(SelectedCurrency);
        DecimalFormat df =new DecimalFormat("#.######");
        priceNow = HomePage.ObservableCurrencyDataList.get(DataList_index).CurrencyPriceProperty().get();
        USD_price = HomePage.ObservableCurrencyDataList.get(USD_INDEX).CurrencyPriceProperty().get()/HomePage.ObservableCurrencyDataList.get(DataList_index).CurrencyPriceProperty().get();
        USD_amount.setText("                "+df.format(USD_price));
    }

    double Amount = 0;
    boolean ok;
    @FXML
    private void onAmount(){
        DecimalFormat df =new DecimalFormat("#.###");
        String amountText = amount.getText();
        ok = true;
        for(char x : amountText.toCharArray()){
            if(x<48 && x>57 ){
                ok = false;
            }
        }
        if(!ok) validAmount.setVisible(true);
        else {
            Amount = Double.parseDouble(amountText);
            TotalSum.setText(String.valueOf(df.format(Amount * USD_price)));
            received_commission.setText(df.format(Amount * USD_price / 100));
        }
    }

    @FXML
    private void onSellButton(){
        boolean possible = false;
        if(ok)  {possible = sendExchange(7, LoginPage.existPeron.UserName, SelectedCurrency, Double.parseDouble(amount.getText()), Double.valueOf(USD_amount.getText()));}
        if(!possible) {
            if(!cheekOpenMarket()) closeStore.setVisible(true);
            else balance.setVisible(true);
        }
        else {
            ObservableExchangeDataList.clear();
            setExchangeTableData();
            balance.setVisible(false);
            closeStore.setVisible(false);
            amount.setText(null);
            TotalSum.setText("         USD");
            received_commission.setText("          0.00");
            USD_amount.setText("                      USD");
            for (int i=0; i<9; i++){
                buyLabels[i].setVisible(false);
                buyLabels1[i].setVisible(false);
                sellLabels[i].setVisible(false);
                sellLabels1[i].setVisible(false);
            }
            TotalSum.setText("         USD");
            price.setText(String.valueOf(df.format(priceNow)));
            ArrayList<ExchangeList> sellsList = fillExchangeTable(SelectedCurrency, 15);
            ArrayList<ExchangeList> buysList = fillExchangeTable(SelectedCurrency, 16);
            int buyAmount = buysList.size();
            int sellAmount = sellsList.size();
            for(int i=0; i<buyAmount; i++){
                NoBuying.setVisible(false);
                for(int j =i+1; j<buyAmount; j++){
                    if(buysList.get(j).price>buysList.get(i).price){
                        ExchangeList x = buysList.get(j);
                        buysList.set(j, buysList.get(i));
                        buysList.set(i, x);
                    }
                }
            }
            for(int i=0; i<sellAmount; i++){
                NoSelling.setVisible(false);
                for(int j =i+1; j<sellAmount; j++){
                    if(sellsList.get(j).price>sellsList.get(i).price){
                        ExchangeList x = sellsList.get(j);
                        sellsList.set(j, buysList.get(i));
                        sellsList.set(i, x);
                    }
                }
            }
            for(int i = 0; i<sellAmount; i++){
                sellLabels[8-i].setText(String.valueOf(sellsList.get(i).amount));
                sellLabels[8-i].setPrefWidth((sellsList.get(i).amount/200)*500);
                sellLabels[8-i].setVisible(true);
                sellLabels1[8-i].setText(String.valueOf(sellsList.get(i).price));
                sellLabels1[8-i].setVisible(true);
            }
            for(int i = 0; i<buyAmount; i++){
                buyLabels[i].setText(String.valueOf(buysList.get(i).amount));
                buyLabels[i].setPrefWidth((buysList.get(i).amount/200)*500);
                buyLabels[i].setVisible(true);
                buyLabels1[i].setText(String.valueOf(buysList.get(i).price));
                buyLabels1[i].setVisible(true);
            }
            recordExchange.setVisible(true);
        }
    }

    @FXML
    private void onBuyButton(){
        boolean possible = false;
        if(ok) possible = sendExchange(6, LoginPage.existPeron.UserName, SelectedCurrency, Double.parseDouble(amount.getText()), Double.valueOf(USD_amount.getText()));
        if(!possible) {
            if(!cheekOpenMarket()) closeStore.setVisible(true);
            else balance.setVisible(true);
        }
        else {
            ObservableExchangeDataList.clear();
            setExchangeTableData();
            balance.setVisible(false);
            closeStore.setVisible(false);
            amount.setText(null);
            TotalSum.setText("         USD");
            received_commission.setText("          0.00");
            USD_amount.setText("                      USD");
            for (int i=0; i<9; i++){
                buyLabels[i].setVisible(false);
                buyLabels1[i].setVisible(false);
                sellLabels[i].setVisible(false);
                sellLabels1[i].setVisible(false);
            }
            SelectedCurrency = SelectCurrency.getValue();
            amount.setText(null);
            TotalSum.setText("         USD");
            price.setText(String.valueOf(df.format(priceNow)));
            ArrayList<ExchangeList> sellsList = fillExchangeTable(SelectedCurrency, 15);
            ArrayList<ExchangeList> buysList = fillExchangeTable(SelectedCurrency, 16);
            int buyAmount = buysList.size();
            int sellAmount = sellsList.size();
            for(int i=0; i<buyAmount; i++){
                NoBuying.setVisible(false);
                for(int j =i+1; j<buyAmount; j++){
                    if(buysList.get(j).price>buysList.get(i).price){
                        ExchangeList x = buysList.get(j);
                        buysList.set(j, buysList.get(i));
                        buysList.set(i, x);
                    }
                }
            }
            for(int i=0; i<sellAmount; i++){
                NoSelling.setVisible(false);
                for(int j =i+1; j<sellAmount; j++){
                    if(sellsList.get(j).price>sellsList.get(i).price){
                        ExchangeList x = sellsList.get(j);
                        sellsList.set(j, buysList.get(i));
                        sellsList.set(i, x);
                    }
                }
            }
            for(int i = 0; i<sellAmount; i++){
                sellLabels[8-i].setText(String.valueOf(sellsList.get(i).amount));
                sellLabels[8-i].setPrefWidth((sellsList.get(i).amount/200)*500);
                sellLabels[8-i].setVisible(true);
                sellLabels1[8-i].setText(String.valueOf(sellsList.get(i).price));
                sellLabels1[8-i].setVisible(true);
            }
            for(int i = 0; i<buyAmount; i++){
                buyLabels[i].setText(String.valueOf(buysList.get(i).amount));
                buyLabels[i].setPrefWidth((buysList.get(i).amount/200)*500);
                buyLabels[i].setVisible(true);
                buyLabels1[i].setText(String.valueOf(buysList.get(i).price));
                buyLabels1[i].setVisible(true);
            }
            recordExchange.setVisible(true);
        }
    }

    @FXML
    protected void onButton25Clicked(){
        boolean possible = false;
        percentBalance.setVisible(false);
        Wallet.myCurrency = fillArrayList(myUserName);
        for(int i=0; i<5; i++){
            if(SelectedCurrency.equals(Wallet.myCurrency.get(i).CurrencyName)){
                TotalSum.setText(String.valueOf(0.25*Wallet.myCurrency.get(i).CurrencyAmount));
                possible = true;
            }
        }
        if (!possible) percentBalance.setVisible(true);
    }
    @FXML
    protected void onButton50Clicked(){
        boolean possible = false;
        percentBalance.setVisible(false);
        Wallet.myCurrency = fillArrayList(myUserName);
        for(int i=0; i<5; i++){
            if(SelectedCurrency.equals(Wallet.myCurrency.get(i).CurrencyName)){
                TotalSum.setText(String.valueOf(0.50*Wallet.myCurrency.get(i).CurrencyAmount));
                possible = true;
            }
        }
        if (!possible) percentBalance.setVisible(true);
    }
    @FXML
    protected void onButton75Clicked(){
        boolean possible = false;
        percentBalance.setVisible(false);
        Wallet.myCurrency = fillArrayList(myUserName);
        for(int i=0; i<5; i++){
            if(SelectedCurrency.equals(Wallet.myCurrency.get(i).CurrencyName)){
                TotalSum.setText(String.valueOf(0.75*Wallet.myCurrency.get(i).CurrencyAmount));
                possible = true;
            }
        }
        if (!possible) percentBalance.setVisible(true);
    }
    @FXML
    protected void onButton100Clicked(){
        boolean possible = false;
        percentBalance.setVisible(false);
        Wallet.myCurrency = fillArrayList(myUserName);
        for(int i=0; i<5; i++){
            if(SelectedCurrency.equals(Wallet.myCurrency.get(i).CurrencyName)){
                TotalSum.setText(String.valueOf(Wallet.myCurrency.get(i).CurrencyAmount));
                possible = true;
            }
        }
        if (!possible) percentBalance.setVisible(true);
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
}