package Trades;

import Home.CurrencyTableData;
import Home.HomePage;
import Start.StartPage;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Start.*;
import javafx.util.converter.NumberStringConverter;

public class Wallet implements Initializable {
    @FXML
    private ImageView Menu,CloseMenu;
    @FXML
    private VBox slider;
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private PieChart walletChart;
    @FXML
    private Label TOMANPrice,USDPrice,YENPrice,GBPPrice,EURPrice,Total,TOMANPercent,USDPercent,YENPercent,GBPPercent,EURPercent;
    private MediaPlayer mediaPlayer;

    @FXML
    LineChart<Number,Number> chart ;
    public int transactionsCounter=0;
    private XYChart.Series<Number,Number> seriesBalance = new XYChart.Series<>();
    @FXML
    private MediaView coinMedia;
    private File file;
    private Media media;
    public static ArrayList<Currency> myCurrency = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        file = new File("src/rotateCoin.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        coinMedia.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        slider.setTranslateX(-270);
        updatePieChartData();
        chart.getData().add(seriesBalance);

    }
    public  synchronized void addCurrencyToWallet(String name,Double amount){
        int SwFind=1;
        for(Currency currency : myCurrency){
            if(currency.getCurrencyName().equals(name)) {SwFind=0;currency.CurrencyAmount+=amount;}
        }
        if(SwFind==1){
            myCurrency.add(new Currency(name,amount));
        }
        updatePieChartData();

    }
    public synchronized void  WithdrawalCurrencyFromWallet(String name,Double amount){
        for(Currency currency : myCurrency){
            if(currency.getCurrencyName().equals(name)) {currency.CurrencyAmount-=amount;}
        }
        updatePieChartData();

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
        updateWallet();
    }
    public void updateWallet(){
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USDPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("EUR")) {EURPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("TOMAN")) {TOMANPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("YEN")) {YENPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
            else if(currency.getCurrencyName().equals("GBP")) {GBPPrice.setText(String.valueOf(currency.getCurrencyAmount()));}
        }
        ConvertCurrenciesToUSD();

    }
    private void ConvertCurrenciesToUSD(){
        double total=0;
        double EURtoUSD=0,TOMANtoUSD=0,YENtoUSD=0,GBPtoUSD=0,USD=0;
        double EURNow=0,TOMANow=0,YENNow=0,GBPNow=0,USDNow=0;
        for(int i=0;i<HomePage.ObservableCurrencyDataList.size();i++){
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("USD")) {USDNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("EUR")) {EURNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("TOMAN")) {TOMANow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("YEN")) {YENNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("GBP")) {GBPNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
        }
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USD=currency.getCurrencyAmount();total+=USD;}
            else if(currency.getCurrencyName().equals("EUR"))  {EURtoUSD=USDNow/EURNow*currency.getCurrencyAmount();total+=EURtoUSD;}
            else if(currency.getCurrencyName().equals("TOMAN"))  {TOMANtoUSD=USDNow/TOMANow*currency.getCurrencyAmount();total+=TOMANtoUSD;}
            else if(currency.getCurrencyName().equals("YEN"))  {YENtoUSD=USDNow/YENNow*currency.getCurrencyAmount();total+=YENtoUSD;}
            else if(currency.getCurrencyName().equals("GBP"))  {GBPtoUSD=USDNow/GBPNow*currency.getCurrencyAmount();total+=GBPtoUSD;}
        }
        Total.setText(String.valueOf(total));
        if(total==0) total=-1;
        DecimalFormat df =new DecimalFormat("#.##");
        USDPercent.setText(String.valueOf(df.format(USD/total*100)+"%"));
        EURPercent.setText(String.valueOf(df.format(EURtoUSD/total*100)+"%"));
        YENPercent.setText(String.valueOf(df.format(YENtoUSD/total*100)+"%"));
        TOMANPercent.setText(String.valueOf(df.format(TOMANtoUSD/total*100)+"%"));
        GBPPercent.setText(String.valueOf(df.format(GBPtoUSD/total*100)+"%"));
        if(total==-1 ) total=0;
        updateBalanceChart(total);
    }
    public void updateBalanceChart(Double totalUSD){
        seriesBalance.getData().add(new XYChart.Data<>(transactionsCounter,totalUSD));
        transactionsCounter++;
    }

    @FXML
    protected void onMenuIconClicked(){
        Menu.setOnMouseClicked(event -> {
//            addCurrencyToWallet("GBP",10000.0);
//            addCurrencyToWallet("YEN",20000.0);
//            addCurrencyToWallet("USD",10000.0);
//            addCurrencyToWallet("TOMAN",20000.0);
//            addCurrencyToWallet("EUR",30000.0);
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
           // WithdrawalCurrencyFromWallet("USD",10000);
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

}
