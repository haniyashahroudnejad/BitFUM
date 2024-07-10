package Login;


import Home.HomePage;
import Person.Person;
import TableDataInformation.BankTableData;
import Trades.Currency;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;
import java.util.ResourceBundle;
import Start.*;

import static DatabaseConnection.UsersInformationDatabase.con;
import static DatabaseConnection.UsersInformationDatabase.createConnection;
import static Person.User.*;
import static Trades.Wallet.myCurrency;

public class LoginPage implements Initializable {
    @FXML
    private MediaView mediaView;

    @FXML
    private Button LoginButton,ForgotPasswordButton, SignUpButton;

    @FXML
    private TextField UserNameField,ChaptchaField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label Label1,Label2,Label3,Label4,Label5,Label6;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Random random = new Random();
    private String SecurityCode;
    public static Person existPeron = null;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        file = new File("src/LoginVideo.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        setRandomLabels();

    }
    @FXML
    protected void setRandomLabels(){
        SecurityCode="";
        Label1.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label1.getText();
        Label2.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label2.getText();
        Label3.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label3.getText();
        Label4.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label4.getText();
        Label5.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label5.getText();
        Label6.setText(makeRandomNumberOrLetter());
        SecurityCode+=Label6.getText();
    }
    @FXML
    protected void onLoginButtonClicked() throws SQLException, IOException {
        boolean existUser = true;
        boolean userFound = true;
        if(ChaptchaField.getText().equals(SecurityCode)) {
            existUser = sendString(UserNameField.getText()+""+PasswordField.getText(), 2);
            if(existUser){
                existPeron = sendPerson(UserNameField.getText(), 5);
                if (existPeron==null)
                    System.out.print(existPeron.name+"^^");
                userFound = LoginUsers(UserNameField.getText());
                if(!userFound){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("You have previously logged in with this username!");
                    alert.showAndWait();
                }
            }
            if(!existUser){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("No such user found!");
                alert.showAndWait();
            }
            else{
                try {
                    if(existPeron.UserName.equals("Admin"))  EnterAdminHomePage();
                    else  EnterHomePage();

                } catch (IOException exception) {
                    System.out.println(exception);
                }
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Security code is wrong");
            alert.showAndWait();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////

    @FXML
    protected void onForgotPasswordButtonClicked(){
        try {
            EnterForgotPasswordPage();
        } catch (IOException exception) {
            System.out.println("Warning");
        }
    }

    public void EnterHomePage() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(LoginButton,"/Home/HomePage.fxml");
    }
    public void EnterAdminHomePage() throws IOException {
        try {
            StartPage.switchPages.ChangePageByClickingButton(LoginButton, "/Admin/AdminHome.fxml");
        }catch (Exception e){
            System.out.println("cant open admin page ");
        }

    }
    public void EnterForgotPasswordPage() throws IOException{
        StartPage.switchPages.ChangePageByClickingButton(LoginButton,"/Login/ForgotPasswordPage.fxml");
    }
    public String makeRandomNumberOrLetter(){
        int randomNumber;
        char randomChar=' ';
        boolean isNumber = random.nextBoolean();
        if(isNumber) {
            randomNumber = random.nextInt(8) + 0;
            return String.valueOf(randomNumber);
        }else{
            randomChar = (char) (random.nextInt(26)+'A');
            return String.valueOf(randomChar);
        }
    }
    @FXML
    protected void onSignUpButton() throws IOException {
        StartPage.switchPages.ChangePageByClickingButton(SignUpButton,"/SignUp/SignUpPage.fxml");
    }

}
