package Trades;

import Home.HomePage;
import Login.LoginPage;
import Start.StartPage;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import Start.*;

import static Login.LoginPage.existPeron;
import static Person.User.fillArrayList;
import static Person.User.logOut;

public class Wallet implements Initializable {
    @FXML
    private ImageView Menu,CloseMenu,calender,ProfileIcon;
    @FXML
    private Button Wallet,Home,Transfer,Exchange,History,Swap,Exit;
    @FXML
    private PieChart walletChart;
    @FXML
    private Label TOMANPrice,USDPrice,YENPrice,GBPPrice,EURPrice,Total,TOMANPercent,USDPercent,YENPercent,GBPPercent,EURPercent,UserName;
    @FXML
    private Label LabelFive,LabelFour,LabelThree,Lone,LTwo,AveragePercentage,highest,lowest;
    private MediaPlayer mediaPlayer,mediaPlayer1;

    @FXML
    LineChart<Number,Number> chart ;
    private XYChart.Series<Number,Number> seriesBalance = new XYChart.Series<>();
    @FXML
    private MediaView coinMedia,calenderView;
    private File file;
    private Media media;
    private Label[] labels;
    String myUserName=existPeron.UserName;
    public static ArrayList<Currency> myCurrency = new ArrayList<>();
    public static ArrayList<Double> WalletChanges = new ArrayList<>();
    @FXML
    private Pane datePane,headerSlider,slider;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    boolean openHeaderSlider = false;
    ArrayList<Label> labelList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labels = new Label[] {Lone,LTwo,LabelThree,LabelFour,LabelFive};
        myCurrency=fillArrayList(myUserName);
        Collections.sort(myCurrency, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return Double.compare( o1.getCurrencyAmount(), o2.getCurrencyAmount()) ;
            }
        });
        for(int i = 0; i<5; i++){
            labels[i].setText(myCurrency.get(i).getCurrencyName());
            labels[i].setPrefWidth(45+(myCurrency.get(i).getCurrencyAmount()/10000000)*200);
            if(myCurrency.get(i).getCurrencyName().equals("USD")) labels[i].setStyle("-fx-background-color:#ff923f");
            else if(myCurrency.get(i).getCurrencyName().equals("EUR")) labels[i].setStyle("-fx-background-color:  #fcf885");
            else if(myCurrency.get(i).getCurrencyName().equals("TOMAN")) labels[i].setStyle("-fx-background-color: #28a7eb");
            else if(myCurrency.get(i).getCurrencyName().equals("YEN")) labels[i].setStyle("-fx-background-color: #6e08a4");
            else if(myCurrency.get(i).getCurrencyName().equals("GBP")) labels[i].setStyle("-fx-background-color: #fca3f7");
        }
        //////////////////////////// media player
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
        ////////////////////////////////////////////
        slider.setTranslateX(-270);
        headerSlider.setTranslateY(-200);
        updatePieChartData();
        chart.getData().add(seriesBalance);
        ////////////////////////////
        UserName.setText(LoginPage.existPeron.UserName);

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
        DecimalFormat df =new DecimalFormat("#.##");
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals("USD")) {USDPrice.setText(String.valueOf(df.format(currency.getCurrencyAmount())));}
            else if(currency.getCurrencyName().equals("EUR")) {EURPrice.setText(String.valueOf(df.format(currency.getCurrencyAmount())));}
            else if(currency.getCurrencyName().equals("TOMAN")) {TOMANPrice.setText(String.valueOf(df.format(currency.getCurrencyAmount())));}
            else if(currency.getCurrencyName().equals("YEN")) {YENPrice.setText(String.valueOf(df.format(currency.getCurrencyAmount())));}
            else if(currency.getCurrencyName().equals("GBP")) {GBPPrice.setText(String.valueOf(df.format(currency.getCurrencyAmount())));}
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
        Total.setText(String.valueOf(df.format(total)));
        if(total==0) total=-1;

        USDPercent.setText(String.valueOf(df.format(USD/total*100)+"%"));
        EURPercent.setText(String.valueOf(df.format(EURtoUSD/total*100)+"%"));
        YENPercent.setText(String.valueOf(df.format(YENtoUSD/total*100)+"%"));
        TOMANPercent.setText(String.valueOf(df.format(TOMANtoUSD/total*100)+"%"));
        GBPPercent.setText(String.valueOf(df.format(GBPtoUSD/total*100)+"%"));
        if(total==-1 ) total=0;
        updateBalanceChart();
    }
    public void updateBalanceChart(){
        int transactionsCounter = 0;
        double min = Double.MAX_VALUE ,max=Double.MIN_VALUE ;
        for(Double balance : WalletChanges){
            if(balance>max) max = balance;
            if(balance<min && balance>0) min =balance;
            seriesBalance.getData().add(new XYChart.Data<>(transactionsCounter,balance));
            transactionsCounter++;
        }
        DecimalFormat df =new DecimalFormat("#.##");
        if(max == Double.MIN_VALUE ) max=0;
        if(min == Double.MAX_VALUE ) min=0;
        highest.setText(df.format(max));
        lowest.setText(df.format(min));
        AveragePercentage.setText(df.format((max-min)/min*100)+" %");
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
    @FXML
    protected void onHomeClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(Home,"/Home/HomePage.fxml");
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
