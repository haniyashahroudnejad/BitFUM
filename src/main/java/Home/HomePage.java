package Home;


import Start.StartPage;
import TableDataInformation.CurrencyTableData;
import Trades.Currency;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.sql.*;

import java.io.IOException;
import java.net.URL;

import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseConnection.*;

import static Login.LoginPage.existPeron;
import static Person.User.fillArrayList;
import static Person.User.logOut;
import static Trades.Currency_Graphic.setCurrencyType;
import static Trades.Wallet.myCurrency;


public class HomePage implements Initializable {

    @FXML
    private ImageView CloseMenu,Menu,centerImageView,leftImageView,rightImageView,calender,ProfileIcon,bell;
    @FXML
    private TextField SearchField;

    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    int index1 = 0,index2 = 0,index3 = 0,index;
    Timeline timelineLeft,timelineRight,timelineCenter,timeline;
    Image image1 = new Image(getClass().getResourceAsStream("/icons/second.jpg"));
    Image image2 = new Image(getClass().getResourceAsStream("/icons/third.jpg"));
    Image image3 = new Image(getClass().getResourceAsStream("/icons/first.jpg"));

    @FXML
    private TableView<CurrencyTableData> CurrenciesTableView;
    @FXML
    private TableColumn<CurrencyTableData, String> NameColumn;
    @FXML
    private TableColumn<CurrencyTableData, Number> PriceColumn;
    @FXML
    private TableColumn<CurrencyTableData, String> ChangesColumn;
    @FXML
    private TableColumn<CurrencyTableData, Number> MaxPriceColumn;
    @FXML
    private TableColumn<CurrencyTableData, Number> MinPriceColumn;
    @FXML
    private MediaView calenderView,bellView;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer,mediaPlayer1;
    boolean openHeaderSlider = false;
    @FXML
    private Pane headerSlider,datePane,slider,notifPane;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    ArrayList<Label> labelList;
    @FXML
    private Label UserName;
    @FXML
    private PieChart walletChart;
    @FXML
    private Label TOMANPercent,USDPercent,YENPercent,GBPPercent,EURPercent;
    @FXML
    private Label NameOnNotif,notif;
    public ArrayList<CurrencyTimeChart> CurrencyChart_Minute = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Hour = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Day = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Week = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Month = new ArrayList<>();

    public static ObservableList<CurrencyTableData> ObservableCurrencyDataList = FXCollections.observableArrayList();
    @FXML
    LineChart<String, Number> TimeChart ;
    private XYChart.Series<String,Number> TimeSeries = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableCurrencyDataList.clear();
        try {
            fillSeries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(CurrencyTimeChart point : CurrencyChart_Hour) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
        }
        TimeChart.getData().add(TimeSeries);
        try {
            new Regression();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        slider.setTranslateX(-270);
        headerSlider.setTranslateY(-200);
        setImagesCarousel();
        setCalender();
        //////////////////////////// search
        SearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterCurrencyDataList((String) oldValue,(String) newValue);
            }
        });
        //////////////////////////// media player
        file = new File("src/calender.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        calenderView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        file = new File("src/bell.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer1 = new MediaPlayer(media);
        bellView.setMediaPlayer(mediaPlayer1);
        mediaPlayer1.setAutoPlay(true);
        mediaPlayer1.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer1.setOnEndOfMedia(() -> {
            mediaPlayer1.seek(Duration.ZERO);
            mediaPlayer1.play();
        });
        //////////////////////////// home table
        CurrenciesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        ChangesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        MaxPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        MinPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        NameColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyNameProperty());
        PriceColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyPriceProperty());
        ChangesColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyChangesProperty());
        MaxPriceColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyMaxPriceProperty());
        MinPriceColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyMinPriceProperty());

        CurrenciesTableView.setItems(ObservableCurrencyDataList);
        CurrenciesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCurrencyTableData();
        try {
            updateTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CurrenciesTableView.setOnMouseClicked(event -> {
            CurrencyTableData selectedCurrency = CurrenciesTableView.getSelectionModel().getSelectedItem();
            if(selectedCurrency != null && !selectedCurrency.getCurrencyName().equals("USD")){
                if(selectedCurrency.getCurrencyName().equals("EUR")) setCurrencyType("EUR");
                if(selectedCurrency.getCurrencyName().equals("YEN")) setCurrencyType("YEN");
                if(selectedCurrency.getCurrencyName().equals("GBP")) setCurrencyType("GBP");
                if(selectedCurrency.getCurrencyName().equals("TOMAN")) setCurrencyType("TOMAN");
                try {
                    StartPage.switchPages.ChangePageByClickingTable(CurrenciesTableView,"/Trades/Currency.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        ////////////////////////////
        UserName.setText(existPeron.UserName);
        fillWalletAndChange();
        updatePieChartData();
        File imagefile = new File(existPeron.profilePass);
        Image image1 = new Image(imagefile.toURI().toString());
        ProfileIcon.setImage(image1);
        NameOnNotif.setText(existPeron.UserName);

    }
    public void fillSeries() throws SQLException {
        CurrencyDatabase connectNow = new CurrencyDatabase();
        Connection connectDB = connectNow.getDBConnection();
        Date date = null;
        double maxPrice=Double.MIN_VALUE;
        double averageHour = 0.0;
        int count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");//TODO
        LocalDate today = LocalDate.parse("2024-06-24");//TODO
        LocalDate todayMain = LocalDate.now();//TODO
        LocalTime time = new Time(00, 35, 36).toLocalTime();
        String fillSeries = "SELECT*FROM BitFUM.currency";
        Statement xs = connectDB.createStatement();
        ResultSet x = xs.executeQuery(fillSeries);
        Calendar calendar = Calendar.getInstance();
        while (x.next()){
            date = x.getDate("Date");
            java.util.Date utilDate = new java.util.Date(date.getTime());
            Instant instant = utilDate.toInstant();
            if(todayMain.equals(instant.atZone(ZoneId.systemDefault()).toLocalDate())){
                CurrencyChart_Minute.add(new CurrencyTimeChart((x.getTime("Time")+" | "+x.getDate("Date")).toString(), x.getDouble("USD")));
            }
            if(maxPrice<x.getDouble("USD")) maxPrice=x.getDouble("USD");
            if(!today.equals(instant.atZone(ZoneId.systemDefault()).toLocalDate())){
                CurrencyChart_Day.add(new CurrencyTimeChart(today.toString(), maxPrice));
                today = today.plus(1, ChronoUnit.DAYS);
                maxPrice = Double.MIN_VALUE;
            }
            count++;
            averageHour+=x.getDouble("USD");
            if(time.getHour()!=x.getTime("Time").toLocalTime().getHour()) {
                CurrencyChart_Hour.add(new CurrencyTimeChart((Time.valueOf(time)+" | "+x.getDate("Date")).toString(), averageHour/count));
                count=0;
                averageHour=0.0;
                calendar.setTime(Time.valueOf(time));
                calendar.add(Calendar.HOUR, 1);/////////////////////////////add time
                time = new Time(calendar.getTimeInMillis()).toLocalTime();
            }

        }
        CurrencyChart_Day.add(new CurrencyTimeChart(today.toString(), maxPrice));
    }
    public class CurrencyTimeChart{
        String time_Date;
        double price;
        public CurrencyTimeChart(String time, double price){
            this.time_Date=time;
            this.price=price;
        }
    }
    public void fillWalletAndChange(){
        myCurrency = fillArrayList(existPeron.UserName);
        double total = 0.0;
        double EURNow=0,TOMANow=0,YENNow=0,GBPNow=0,USDNow=0;
        double EURtoUSD=0,TOMANtoUSD=0,YENtoUSD=0,GBPtoUSD=0,USD=0;
        for(int i = 0; i< HomePage.ObservableCurrencyDataList.size(); i++){
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("USD")) {USDNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("EUR")) {EURNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("TOMAN")) {TOMANow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("YEN")) {YENNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("GBP")) {GBPNow= HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
        }
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USD=currency.getCurrencyAmount();total+=USD;}
            else if(currency.getCurrencyName().equals("EUR"))  {EURtoUSD=USDNow/EURNow*currency.getCurrencyAmount();total+=EURtoUSD;}
            else if(currency.getCurrencyName().equals("TOMAN"))  {TOMANtoUSD=USDNow/TOMANow*currency.getCurrencyAmount();total+=TOMANtoUSD;}
            else if(currency.getCurrencyName().equals("YEN"))  {YENtoUSD=USDNow/YENNow*currency.getCurrencyAmount();total+=YENtoUSD;}
            else if(currency.getCurrencyName().equals("GBP"))  {GBPtoUSD=USDNow/GBPNow*currency.getCurrencyAmount();total+=GBPtoUSD;}
        }
        DecimalFormat df =new DecimalFormat("#.##");
        USDPercent.setText(String.valueOf(df.format(USD/total*100)+"%"));
        EURPercent.setText(String.valueOf(df.format(EURtoUSD/total*100)+"%"));
        YENPercent.setText(String.valueOf(df.format(YENtoUSD/total*100)+"%"));
        TOMANPercent.setText(String.valueOf(df.format(TOMANtoUSD/total*100)+"%"));
        GBPPercent.setText(String.valueOf(df.format(GBPtoUSD/total*100)+"%"));

        Trades.Wallet.WalletChanges.add(total);
    }
    public void updatePieChartData() {
        int SwEmpty = 1;
        walletChart.getData().clear();
        for (Currency currency : myCurrency) {
            if ( currency.getCurrencyAmount() > 0) {
                double currentCurrencyAmount = currency.getCurrencyAmount();
                while (currentCurrencyAmount>2){
                    currentCurrencyAmount/=10;
                }
                PieChart.Data pieData = new PieChart.Data(currency.getCurrencyName(), currentCurrencyAmount);
                walletChart.getData().add(pieData);

                if(currency.getCurrencyName().equals("USD")) {pieData.getNode().setStyle("-fx-pie-color:#ff923f");}
                else if(currency.getCurrencyName().equals("EUR")) {pieData.getNode().setStyle("-fx-pie-color:#fffb00");}
                else if(currency.getCurrencyName().equals("TOMAN")) {pieData.getNode().setStyle("-fx-pie-color:#28a7eb");}
                else if(currency.getCurrencyName().equals("YEN")) {pieData.getNode().setStyle("-fx-pie-color:#6e08a4");}
                else if(currency.getCurrencyName().equals("GBP")) {pieData.getNode().setStyle("-fx-pie-color:#fca3f7");}
                SwEmpty=0;
            }
        }
        if(SwEmpty==1){
            PieChart.Data pieData = new PieChart.Data("", 1);
            walletChart.getData().add(pieData);
            pieData.getNode().setStyle("-fx-pie-color:white;-fx-border-color:black");
        }
    }
    /////////////////////////////////////////////////////////////////////
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
    public void updateTable() throws SQLException {
        timeline = new Timeline(60000, new KeyFrame(Duration.millis(60000), event -> {
            ObservableCurrencyDataList.clear();
            setCurrencyTableData();
            try {
                new Regression();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void setCurrencyTableData(){
        CurrencyDatabase connectNow = new CurrencyDatabase();
        Connection connectDB = connectNow.getDBConnection();
        String CurrencyViewQuery = "SELECT*FROM BitFUM.currency";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(CurrencyViewQuery);
            Double USD=0.0,maxUSD=-1.0,minUSD=Double.MAX_VALUE,EUR=0.0,maxEUR=-1.0,minEUR=Double.MAX_VALUE,TOMAN=0.0,maxTOMAN=-1.0,
                    minTOMAN=Double.MAX_VALUE,YEN=0.0,maxYEN=-1.0,minYEN=Double.MAX_VALUE,
                    GBP=0.0,maxGBP=-1.0,minGBP=Double.MAX_VALUE;
            Double   USD1Before=0.0,EUR1Before=0.0,TOMAN1Before=0.0,YEN1Before=0.0,GBP1Before=0.0;
            LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            LocalDate localDate = LocalDate.now();
            //  LocalDate localDate = LocalDate.parse("2024-06-28");
            while (queryOutput.next()){
                LocalDate dbDate = queryOutput.getDate("Date").toLocalDate();
                LocalTime dbTime = LocalTime.parse(queryOutput.getString("Time")).truncatedTo(ChronoUnit.MINUTES);
                if(localDate.equals(dbDate)) {
                    USD = queryOutput.getDouble("USD");
                    if(USD>=maxUSD) {maxUSD=USD;}
                    if(USD<=minUSD) {minUSD=USD;}
                    EUR  = queryOutput.getDouble("EUR");
                    if(EUR>=maxEUR) maxEUR=EUR;
                    if(EUR<=minEUR) minEUR=EUR;
                    TOMAN = queryOutput.getDouble("TOMAN");
                    if(TOMAN>=maxTOMAN) maxTOMAN=TOMAN;
                    if(TOMAN<=minTOMAN) minTOMAN=TOMAN;
                    YEN = queryOutput.getDouble("YEN");
                    if(YEN>=maxYEN) maxYEN=YEN;
                    if(YEN<=minYEN) minYEN=YEN;
                    GBP = queryOutput.getDouble("GBP");
                    if(GBP>=maxGBP) maxGBP=GBP;
                    if(GBP<=minGBP) minGBP=GBP;
                    System.out.print(dbTime+" is dbTime\n");
                    System.out.print(localTime.minusMinutes(1)+" is lokalTime\n");
                    if(dbTime.equals(localTime.minusMinutes(1))){
                        System.out.print(dbTime+" is db");
                        USD1Before =queryOutput.getDouble("USD");
                        EUR1Before =queryOutput.getDouble("EUR");
                        TOMAN1Before=queryOutput.getDouble("TOMAN");
                        YEN1Before=queryOutput.getDouble("YEN");
                        GBP1Before=queryOutput.getDouble("GBP");
                    }
                }
                //   if(dbTime.equals(localTime)){ break;}
            }
            DecimalFormat df =new DecimalFormat("#.##");
            Double price=0.0,min=0.0,max=0.0;
            String name="",change="";
            for(int i=0;i<5;i++){
                if(i==0) {name="USD";price=USD;change=df.format((USD-USD1Before)/USD1Before*100)+" %";max=maxUSD;min=minUSD;}
                if(i==1) {name="EUR";price=EUR;change= df.format((EUR - EUR1Before)/EUR1Before*100)+" %";max=maxEUR;min=minEUR;}
                if(i==2) {name="TOMAN";price=TOMAN;change= df.format((TOMAN - TOMAN1Before)/TOMAN1Before*100)+" %";max=maxTOMAN;min=minTOMAN;}
                if(i==3) {name="YEN";price=YEN;change= df.format((YEN - YEN1Before)/YEN1Before*100)+" %";max=maxYEN;min=minYEN;}
                if(i==4) {name="GBP";price=GBP;change= df.format((GBP - GBP1Before)/GBP1Before*100)+" %";max=maxGBP;min=minGBP;}
                CurrencyTableData currencyData = new CurrencyTableData(name,price,change,max,min);
                ObservableCurrencyDataList.add(currencyData);
            }
            String[] number ;
            Double maxChange = Double.MIN_VALUE;
            String maxName="USD";
            for(int i = 0; i< 5; i++){
                number = (ObservableCurrencyDataList.get(i).getCurrencyChanges()).split(" %");
//                double d = Double.parseDouble(number[0]);
//                if(d<0){
//                    if(d>-0.4) System.out.println("more");
//                }
                if(maxChange.compareTo(Double.parseDouble(number[0]))<0 ){
                    maxChange=Double.parseDouble(number[0]);
                    maxName = ObservableCurrencyDataList.get(i).getCurrencyName();
                }
            }

            notif.setText(maxName);
        }catch (SQLException e){
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }

    }
    public void filterCurrencyDataList(String oldValue,String newValue){
        ObservableList<CurrencyTableData> filteredList = FXCollections.observableArrayList();
        if(SearchField==null || newValue.length()<oldValue.length() || newValue==null){
            CurrenciesTableView.setItems(ObservableCurrencyDataList);
        }else {
            newValue = newValue.toUpperCase();
            for (CurrencyTableData currencyData : CurrenciesTableView.getItems()){
                String filterCurrencyName = currencyData.getCurrencyName();
                if(filterCurrencyName.toUpperCase().contains(newValue)){
                    filteredList.add(currencyData);
                }
            }
            CurrenciesTableView.setItems(filteredList);
        }
    }
    private ArrayList<Image> loadImages(int location) {
        ArrayList<Image> images = new ArrayList<>();
        if(location==1){
            images.add(image1);
            images.add(image3);
            images.add(image2);
        }else if(location==2){
            images.add(image2);
            images.add(image1);
            images.add(image3);
        }else if(location==3) {
            images.add(image3);
            images.add(image2);
            images.add(image1);
        }
        return images;
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
    private void setImagesCarousel(){
        ArrayList<Image> leftImages = loadImages(1);
        ArrayList<Image> centerImages = loadImages(2);
        ArrayList<Image> rightImages = loadImages(3);

        timelineLeft = new Timeline(3000, new KeyFrame(Duration.millis(3000), event -> {
            leftImageView.setImage(leftImages.get(index1));
            index1 = (index1 + 1) % leftImages.size();

            if (index2 == 0) {
                timelineLeft.stop();
                timelineLeft.playFromStart();
            }
        }));
        timelineLeft.setCycleCount(Timeline.INDEFINITE);

        timelineCenter = new Timeline(3000, new KeyFrame(Duration.millis(3000), event -> {
            centerImageView.setImage(centerImages.get(index2));
            index2 = (index2 + 1) % centerImages.size();

            if (index2 == 0) {
                timelineCenter.stop();
                timelineCenter.playFromStart();
            }
        }));
        timelineCenter.setCycleCount(Timeline.INDEFINITE);

        timelineRight = new Timeline(3000, new KeyFrame(Duration.millis(3000), event -> {
            rightImageView.setImage(rightImages.get(index3));
            index3 = (index3 + 1) % rightImages.size();

            if (index3 == 0) {
                timelineRight.stop();
                timelineRight.playFromStart();
            }
        }));
        timelineRight.setCycleCount(Timeline.INDEFINITE);
        timelineLeft.play();
        timelineRight.play();
        timelineCenter.play();
    }
    @FXML
    protected void walletOnpage()throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(Wallet,"/Trades/Wallet.fxml");
    }
    @FXML
    protected void transferOnpage()throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(Transfer,"/Trades/Transfer.fxml");
    }
    @FXML
    protected void exchangeOnpage()throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(Exchange,"/Trades/Exchange.fxml");
    }
    @FXML
    protected void swapOnpage()throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(Swap,"/Trades/Swap.fxml");
    }
    @FXML
    protected void historyOnpage()throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(History,"/Trades/HistoryGraphic.fxml");
    }
    @FXML
    protected void onCalender(){
        calender.setOnMouseClicked(event -> {
            notifPane.setVisible(false);
            datePane.setVisible(true);
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
    @FXML
    protected void onBell(){
        bell.setOnMouseClicked(event -> {
            datePane.setVisible(false);
            notifPane.setVisible(true);
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
                notifPane.setVisible(true);
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
}
