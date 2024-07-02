package Home;

import Start.StartPage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.sql.*;

import java.io.IOException;
import java.net.URL;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseConnection.*;


public class HomePage implements Initializable {

    @FXML
    private ImageView CloseMenu,Menu,centerImageView,leftImageView,rightImageView;
    @FXML
    private TextField SearchField;

    @FXML
    private VBox slider;
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    int index1 = 0,index2 = 0,index3 = 0,index;
    Timeline timelineLeft,timelineRight,timelineCenter,timeline;
    Image image1 = new Image(getClass().getResourceAsStream("/icons/sendDigits.JPG"));
    Image image2 = new Image(getClass().getResourceAsStream("/icons/ForgotPassword.JPG"));
    Image image3 = new Image(getClass().getResourceAsStream("/icons/first.JPG"));

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

    public static ObservableList<CurrencyTableData> ObservableCurrencyDataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setTranslateX(-270);
        setImagesCarousel();
        SearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterCurrencyDataList((String) oldValue,(String) newValue);
            }
        });
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
        updateTable();

//        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), (ActionEvent event )-> {
//                setCurrencyTableData();
//                System.out.println("s");
//        }));


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
          CurrencyDatabaseConnection connectNow = new CurrencyDatabaseConnection();
          Connection connectDB = connectNow.getDBConnection();
          String CurrencyViewQuery = "SELECT Date, Time ,USD,EUR, TOMAN, YEN,GBP FROM BitFUM.currency";
         try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(CurrencyViewQuery);
            Double USD=0.0,maxUSD=-1.0,minUSD=Double.MAX_VALUE,EUR=0.0,maxEUR=-1.0,minEUR=Double.MAX_VALUE,TOMAN=0.0,maxTOMAN=-1.0,minTOMAN=Double.MAX_VALUE,YEN=0.0,maxYEN=-1.0,minYEN=Double.MAX_VALUE,GBP=0.0,maxGBP=-1.0,minGBP=Double.MAX_VALUE;
            Double   USD1Before=0.0,EUR1Before=0.0,TOMAN1Before=0.0,YEN1Before=0.0,GBP1Before=0.0;
            LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
//            LocalDate localDate = LocalDate.now();
             LocalDate localDate = LocalDate.parse("2024-06-05");
             System.out.println("localTime:"+localTime);
             while (queryOutput.next()){
               LocalDate dbDate = queryOutput.getObject("Date",LocalDate.class);
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
                   if(dbTime.equals(localTime.minusMinutes(1))){
                       USD1Before =queryOutput.getDouble("USD");
                       EUR1Before =queryOutput.getDouble("EUR");
                       TOMAN1Before=queryOutput.getDouble("TOMAN");
                       YEN1Before=queryOutput.getDouble("YEN");
                       GBP1Before=queryOutput.getDouble("GBP");
                   }
               }
                 if(dbTime.equals(localTime)){ break;}
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
}
