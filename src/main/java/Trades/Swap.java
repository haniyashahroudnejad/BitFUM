package Trades;

import Home.HomePage;
import Start.StartPage;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

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
import static Trades.Wallet.myCurrency;

public class Swap  implements Initializable {

    @FXML
    private ImageView CloseMenu,Menu,ProfileIcon,calender,swap;

    @FXML
    private TextField CurrencyAmountField;

    @FXML
    private Button Exchange,Exit,History,Home,Swap,Transfer,Wallet;

    @FXML
    private Label ToCurrencyLabel,UserName,currencyName1,currencyName2,toAmount,warning,one,equal;

    @FXML
    private MediaView calenderView;

    @FXML
    private ChoiceBox<String> choice1;

    @FXML
    private ChoiceBox<String> choice2;
    @FXML
    private Pane datePane,headerSlider,slider;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    boolean openHeaderSlider = false;
    PauseTransition transition = new PauseTransition(Duration.seconds(2));
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer1;
    ArrayList<Label> labelList;
    String selectedCurrency1,selectedCurrency2;
    double currencyAmount ;
    double amount1=1,amount2=1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.setTranslateX(-270);
        headerSlider.setTranslateY(-200);
        fillWallet();
        clear();
        //////////////////////////////////
        choice1.getItems().addAll("USD","TOMAN", "YEN", "EUR", "GBP");
        choice2.getItems().addAll("USD","TOMAN", "YEN", "EUR", "GBP");
        choice1.setOnAction(event -> {
            selectedCurrency1 = choice1.getValue();
            if(!selectedCurrency2.isEmpty() && selectedCurrency2.equals(selectedCurrency1) ){
                warning.setText(" cant selected same currency !");
                transition.setOnFinished(e -> warning.setText(""));
                transition.play();
            }

        });
        choice2.setOnAction(event -> {
            selectedCurrency2 = choice2.getValue();
            if(!selectedCurrency1.isEmpty() && selectedCurrency2.equals(selectedCurrency1) ){
                warning.setText(" cant selected same currency !");
                transition.setOnFinished(e -> warning.setText(""));
                transition.play();
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
    private void swap(){
        double EURNow=0,TOMANow=0,YENNow=0,GBPNow=0,USDNow=0;
        for(int i = 0; i< HomePage.ObservableCurrencyDataList.size(); i++){
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("USD")) {USDNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("EUR")) {EURNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("TOMAN")) {TOMANow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("YEN")) {YENNow=HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
            if(HomePage.ObservableCurrencyDataList.get(i).getCurrencyName().equals("GBP")) {GBPNow= HomePage.ObservableCurrencyDataList.get(i).getCurrencyPrice();}
        }

        if(selectedCurrency1.equals("USD")) amount1 = USDNow;
        else if(selectedCurrency2.equals("USD")) amount2 = USDNow;
        if(selectedCurrency1.equals("EUR")) amount1 = EURNow;
        else if(selectedCurrency2.equals("EUR")) amount2 = EURNow;
        if(selectedCurrency1.equals("TOMAN")) amount1 = TOMANow;
        else if(selectedCurrency2.equals("TOMAN")) amount2 = TOMANow;
        if(selectedCurrency1.equals("YEN")) amount1 = YENNow;
        else if(selectedCurrency2.equals("YEN")) amount2 = YENNow;
        if(selectedCurrency1.equals("GBP")) amount1 = GBPNow;
        else if(selectedCurrency2.equals("GBP")) amount2 = GBPNow;

        System.out.println("amount1"+amount1);
        System.out.println("amount2"+amount2);

        currencyName1.setText(selectedCurrency1);
        currencyName2.setText(selectedCurrency2);
        one.setText("1");
        equal.setText("=");
        DecimalFormat df =new DecimalFormat("#.####");
        toAmount.setText(String.valueOf(df.format(amount2/amount1)));
        ToCurrencyLabel.setText("   "+(df.format(amount2/amount1*currencyAmount)));

    }
    private boolean valid(){
        if(!CurrencyAmountField.getText().isEmpty()){
            if(!selectedCurrency1.isEmpty() && !selectedCurrency2.isEmpty()){
                swap();
            }else {
                warning.setText("select  currency ");
                transition.setOnFinished(event -> warning.setText(""));
                transition.play();
                return false;
            }
        }else{
            warning.setText("Enter Amount ");
            transition.setOnFinished(event -> warning.setText(""));
            transition.play();
            return false;
        }
        return false;
    }
    @FXML
    protected void onSwap(){
        swap.setOnMouseClicked(event -> {
            valid();
        });
    }
    @FXML
    protected void onCurrencyField(){
        System.out.println("on amount field");
        double number=0;
        try {
            number=Double.parseDouble(CurrencyAmountField.getText());
            currencyAmount=number;
        }catch (NumberFormatException e){
            warning.setText("The value is invalid Enter correct number");
            transition.setOnFinished(event -> warning.setText(""));
            transition.play();

        }

    }
    @FXML
    protected void onSwapButton(){
        for (Currency currency : myCurrency) {
            if(currency.getCurrencyName().equals(selectedCurrency1)) {
                if(currency.getCurrencyAmount()>=currencyAmount){
                    System.out.println("amountttttt="+currency.getCurrencyAmount());
                    double toCurrency = amount2/amount1*currencyAmount;
                    SwapInWallet(existPeron.UserName,selectedCurrency1,selectedCurrency2,currencyAmount,toCurrency);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Done !");
                    alert.setContentText(" Swap done ! cheek your wallet ");
                    alert.showAndWait();
                    clear();
                }else {
                    System.out.println("not enough");
                    warning.setText("you have not enough money !");
                    transition.setOnFinished(event -> warning.setText(""));
                    transition.play();
                    clear();
                }
            }
        }
    }
    public void fillWallet(){
        myCurrency = fillArrayList(existPeron.UserName);
    }
    private void clear(){
        CurrencyAmountField.setText("");
        selectedCurrency1="";
        selectedCurrency2="";
        one.setText("");
        equal.setText("");
        toAmount.setText("");
        ToCurrencyLabel.setText("");
        currencyName1.setText("");
        currencyName2.setText("");

    }

    ///////////////////////////////////////////////////////////////////////////////
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
