package Trades;

import DatabaseConnection.CurrencyDatabase;
import Home.HomePage;
import SerializableClasses.TradesList;
import Start.StartPage;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;

import static DatabaseConnection.UsersInformationDatabase.con;
import static Login.LoginPage.existPeron;
import static Person.User.*;

public class Currency_Graphic implements Initializable {
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private ImageView CloseMenu,Menu;
    @FXML
    private ImageView CurrencyImage, transferCurrency, ExchangeCurrency, BankCurrency, swapCurrency;
    @FXML
    private Label CurrencyNameLabel, real_priceCurrency, TotalMarketValue,
            DailyPercentageChange, CirculatingMarketValue, HighestPrice,UserName;
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
    private Button All_Order, Open_Order, Completed_Order ;
    @FXML
    private Line All_Orders;
    @FXML
    private Line Open_Orders;
    @FXML
    private Line Completed_Orders;
    @FXML
    private Pane datePane,headerSlider,slider;
    @FXML
    private MediaView calenderView;
    @FXML
    private ImageView calender,ProfileIcon;
    @FXML
    private Button minute, hour, day, week, month;
    @FXML
    LineChart<String, Number> TimeChart ;
    private XYChart.Series<String,Number> TimeSeries = new XYChart.Series<>();
    private File file;
    private Media media;
    ArrayList<Label> labelList;
    boolean openHeaderSlider = false;
    private MediaPlayer mediaPlayer1;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31, selectedCurrency;
    public static ObservableList<Trades> ObservableExchangeDataList = FXCollections.observableArrayList();
    public static int CurrencyType;
    public static String selecteCurrency;
    public ArrayList<CurrencyTimeChart> CurrencyChart_Minute = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Hour = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Day = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Week = new ArrayList<>();
    public ArrayList<CurrencyTimeChart> CurrencyChart_Month = new ArrayList<>();
    DecimalFormat df =new DecimalFormat("#.######");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        minute.setOnMouseClicked(event -> {
            minute.setStyle("-fx-background-color: #b3e1ff;");
            hour.setStyle("-fx-background-color:  #ffdcf7;");
            day.setStyle("-fx-background-color:  #ffdcf7;");
            week.setStyle("-fx-background-color:  #ffdcf7;");
            month.setStyle("-fx-background-color:  #ffdcf7;");
        });
        hour.setOnMouseClicked(event -> {
            hour.setStyle("-fx-background-color: #b3e1ff;");
            minute.setStyle("-fx-background-color:  #ffdcf7;");
            day.setStyle("-fx-background-color:  #ffdcf7;");
            week.setStyle("-fx-background-color:  #ffdcf7;");
            month.setStyle("-fx-background-color:  #ffdcf7;");
        });
        day.setOnMouseClicked(event -> {
            day.setStyle("-fx-background-color: #b3e1ff;");
            hour.setStyle("-fx-background-color:  #ffdcf7;");
            minute.setStyle("-fx-background-color:  #ffdcf7;");
            week.setStyle("-fx-background-color:  #ffdcf7;");
            month.setStyle("-fx-background-color:  #ffdcf7;");
        });
        week.setOnMouseClicked(event -> {
            week.setStyle("-fx-background-color: #b3e1ff;");
            hour.setStyle("-fx-background-color:  #ffdcf7;");
            day.setStyle("-fx-background-color:  #ffdcf7;");
            minute.setStyle("-fx-background-color:  #ffdcf7;");
            month.setStyle("-fx-background-color:  #ffdcf7;");
        });
        month.setOnMouseClicked(event -> {
            month.setStyle("-fx-background-color: #b3e1ff;");
            hour.setStyle("-fx-background-color:  #ffdcf7;");
            day.setStyle("-fx-background-color:  #ffdcf7;");
            week.setStyle("-fx-background-color:  #ffdcf7;");
            minute.setStyle("-fx-background-color:  #ffdcf7;");
        });
        headerSlider.setTranslateY(-220);
        slider.setTranslateX(-270);
        if(CurrencyType==1){
            selecteCurrency="EUR";
            CurrencyImage.setImage(new Image(getClass().getResourceAsStream("/icons/EUR.png")));
            CurrencyNameLabel.setText("EUR");
            int DataList_index = 0;
            for (int i = 0; i < 5; i++) {
                if (HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals("EUR"))
                    DataList_index = i;
            }
            real_priceCurrency.setText(String.valueOf(df.format(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMaxPrice())));
            TotalMarketValue.setText(TotalMarketCurrency("EUR"));
            CirculatingMarketValue.setText(CirculatingMarketValue("EUR"));
            HighestPrice.setText(HighestPrice("EUR"));
            DailyPercentageChange.setText(changePercentageDaily("EUR"));
        } else if(CurrencyType==3){
            selecteCurrency="GBP";
            CurrencyImage.setImage(new Image(getClass().getResourceAsStream("/icons/GBP.png")));
            CurrencyNameLabel.setText("GBP");
            int DataList_index = 0;
            for (int i = 0; i < 5; i++) {
                if (HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals("GBP"))
                    DataList_index = i;
            }
            real_priceCurrency.setText(String.valueOf(df.format(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMaxPrice())));
            TotalMarketValue.setText(TotalMarketCurrency("GBP"));
            CirculatingMarketValue.setText(CirculatingMarketValue("GBP"));
            HighestPrice.setText(HighestPrice("GBP"));
            DailyPercentageChange.setText(changePercentageDaily("GBP"));
        } else if(CurrencyType==4){
            selecteCurrency="TOMAN";
            CurrencyImage.setImage(new Image(getClass().getResourceAsStream("/icons/TOMAN.png")));
            CurrencyNameLabel.setText("TOMAN");
            int DataList_index = 0;
            for (int i = 0; i < 5; i++) {
                if (HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals("TOMAN"))
                    DataList_index = i;
            }
            real_priceCurrency.setText(String.valueOf(df.format(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMaxPrice())));
            TotalMarketValue.setText(TotalMarketCurrency("TOMAN"));
            CirculatingMarketValue.setText(CirculatingMarketValue("TOMAN"));
            HighestPrice.setText(HighestPrice("TOMAN"));
            DailyPercentageChange.setText(changePercentageDaily("TOMAN"));
        } else if(CurrencyType==5){
            selecteCurrency="YEN";
            CurrencyImage.setImage(new Image(getClass().getResourceAsStream("/icons/YEN.png")));
            CurrencyNameLabel.setText("YEN");
            int DataList_index = 0;
            for (int i = 0; i < 5; i++) {
                if (HomePage.ObservableCurrencyDataList.get(i).CurrencyNameProperty().getValue().equals("YEN"))
                    DataList_index = i;
            }
            real_priceCurrency.setText(df.format(String.valueOf(HomePage.ObservableCurrencyDataList.get(DataList_index).getCurrencyMaxPrice())));
            TotalMarketValue.setText(TotalMarketCurrency("YEN"));
            CirculatingMarketValue.setText(CirculatingMarketValue("YEN"));
            HighestPrice.setText(HighestPrice("YEN"));
            DailyPercentageChange.setText(changePercentageDaily("YEN"));
        }
        //////////////////////////////
        TradeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        OperationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CurrencyNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        rateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        OperationColumn.setCellValueFactory(cellData -> cellData.getValue().Operation());
        CurrencyNameColumn.setCellValueFactory(cellData -> cellData.getValue().CurrencyName());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().count());
        rateColumn.setCellValueFactory(cellData -> cellData.getValue().rate());
        TradeTable.setItems(ObservableExchangeDataList);
        setExchangeTableData();

        ///////////////////////////
        setExchangeTableData();
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
        /////////////////////////////////////////////////
        transferCurrency.setOnMouseClicked(event -> {
            try {
                StartPage.switchPages.ChangePageByClickingImage(transferCurrency,"/Trades/Transfer.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        swapCurrency.setOnMouseClicked(event -> {
            try {
                StartPage.switchPages.ChangePageByClickingImage(swapCurrency,"/Trades/Swap.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        BankCurrency.setOnMouseClicked(event -> {
            try {
                new Transfer().OpenBankPage(BankCurrency);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ExchangeCurrency.setOnMouseClicked(event -> {
            try {
                StartPage.switchPages.ChangePageByClickingImage(swapCurrency,"/Trades/Exchange.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        ///////////////////////////chart
        try {
            fillSeries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TimeChart.getData().add(TimeSeries);

    }

    @FXML
    public void OnMinute() {
        TimeSeries.getData().clear();
        for(CurrencyTimeChart point : CurrencyChart_Minute) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
        }
    }
    @FXML
    public void OnDay(){
        TimeSeries.getData().clear();
        for(CurrencyTimeChart point : CurrencyChart_Day) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
        }
    }

    @FXML
    public void OnHour(){
        TimeSeries.getData().clear();
        for(CurrencyTimeChart point : CurrencyChart_Hour) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
        }
    }

    @FXML
    public void OnWeek(){
        TimeSeries.getData().clear();
        int countDay=1;
        double sum=0.0;
        for(CurrencyTimeChart x : CurrencyChart_Day){
            sum+=x.price;
            if(countDay%7==0){
                sum=sum/7;
                CurrencyChart_Week.add(new CurrencyTimeChart(x.time_Date, sum));
                sum=0;
            }
            countDay++;
        }
        for(CurrencyTimeChart point : CurrencyChart_Week) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
            System.out.print(point.time_Date+ ", "+point.price+"\n");
        }
    }

    @FXML
    public void OnMonth(){
        TimeSeries.getData().clear();
        CurrencyChart_Week.clear();
        int countDay1=1;
        double sum=0.0;
        for(CurrencyTimeChart x : CurrencyChart_Day){
            sum+=x.price;
            if(countDay1%7==0){
                sum=sum/7;
                CurrencyChart_Week.add(new CurrencyTimeChart(x.time_Date, sum));
                sum=0;
            }
            countDay1++;
        }
        int countDay=1;
        sum=0.0;
        for(CurrencyTimeChart x : CurrencyChart_Week){
            sum+=x.price;
            if(countDay%2==0 ){
                sum=sum/4;
                CurrencyChart_Month.add(new CurrencyTimeChart(x.time_Date, sum));
                sum=0;
            }
            countDay++;
        }
        for(CurrencyTimeChart point : CurrencyChart_Month) {
            TimeSeries.getData().add(new XYChart.Data<>(point.time_Date, point.price));
            System.out.print(point.time_Date+ ", "+point.price+"\n");
        }
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
                CurrencyChart_Minute.add(new CurrencyTimeChart((x.getTime("Time")+" | "+x.getDate("Date")).toString(), x.getDouble(selecteCurrency)));
            }
            if(maxPrice<x.getDouble(selecteCurrency)) maxPrice=x.getDouble(selecteCurrency);
            if(!today.equals(instant.atZone(ZoneId.systemDefault()).toLocalDate())){
                CurrencyChart_Day.add(new CurrencyTimeChart(today.toString(), maxPrice));
                today = today.plus(1, ChronoUnit.DAYS);
                maxPrice = Double.MIN_VALUE;
            }
            count++;
            averageHour+=x.getDouble(selecteCurrency);
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



    public static class CurrencyTimeChart{
        String time_Date;
        double price;
        public CurrencyTimeChart(String time, double price){
            this.time_Date=time;
            this.price=price;
        }
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

    public static void setCurrencyType(String CurrencyName){
        if(CurrencyName.equals("USD")) CurrencyType=2;
        if(CurrencyName.equals("EUR")) CurrencyType=1;
        if(CurrencyName.equals("GBP")) CurrencyType=3;
        if(CurrencyName.equals("TOMAN")) CurrencyType=4;
        if(CurrencyName.equals("YEN")) CurrencyType=5;
    }

    public class Trades {
        StringProperty Operation = new SimpleStringProperty(this, "Operation", "");
        StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
        DoubleProperty count = new SimpleDoubleProperty(this, "count", 0.0);
        DoubleProperty rate = new SimpleDoubleProperty(this, "rate", 0.0);
        public Trades(String Operation, String CurrencyName, double count, double rate) {
            this.Operation.set(Operation);
            this.CurrencyName.set(CurrencyName);
            this.count.set(count);
            this.rate.set(rate);
        }
        public StringProperty Operation() {return Operation;}
        public DoubleProperty count() {return count;}
        public DoubleProperty rate() {return rate;}
        public StringProperty CurrencyName() {return CurrencyName;}
    }

    public void setExchangeTableData(){
        ObservableExchangeDataList.clear();
        String operation;
        for(TradesList x: fillCurrencyOrders(selecteCurrency)){
            if(x.operationType==0){
                operation="Completed Order";
            } else if(x.operationType==2){
                operation="Sell";
            } else{
                operation="Buy";
            }
            ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate));
        }
    }

    public void setExchangeTableData_CompletedOrder(){
        ObservableExchangeDataList.clear();
        String operation;
        for(TradesList x: fillCurrencyOrders(selecteCurrency)){
            if(x.percentageOfCompletion==0) {
                operation="Completed order";
                ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate));
            }
        }
    }

    public void setExchangeTableData_OpenOrder(){
        ObservableExchangeDataList.clear();
        String operation;
        for(TradesList x: fillCurrencyOrders(selecteCurrency)){
            if(x.percentageOfCompletion==1) {
                if(x.operationType==2){
                    operation="Sell";
                } else{
                    operation="Buy";
                }
                ObservableExchangeDataList.add(new Trades(operation, x.currencyName, x.Count, x.Rate));
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
        CloseMenu.setOnMouseClicked(event -> {
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
}
