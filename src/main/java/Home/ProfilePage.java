package Home;

import Start.StartPage;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Login.LoginPage.existPeron;
import static Person.User.*;

public class ProfilePage implements Initializable {

    @FXML
    private Label CVVOnCard,CVVOnInfo,UserName,cardNumOnCard,cardNumOnInfo,newCardNumLable,untilEndOnCard,validFromOnCard,untilEndOnInfo;

    @FXML
    private ImageView CloseMenu,Menu,ProfileIcon;
    @FXML
    private Button CreatCardNumber,EditButton,Exchange,Exit,History,Home,Swap,Transfer,Wallet,UploadImage;
    @FXML
    private TextField EditEmailField,EditLastNameField,EditNameField,EditPhoneNumberField;
    @FXML
    private Label UserNameUnderPicture,emailWarning,lastNameWarning,nameWarning,phoneNumberWarning;
    @FXML
    private ImageView UserProfile,calender;
    @FXML
    private MediaView profileView,calenderView;

    @FXML
    private Pane headerSlider,datePane,slider;
    @FXML
    private Label emailCircle,phoneCircle,profileCircle,nameCircle,lastNameCircle;
    private MediaPlayer mediaPlayer,mediaPlayer2;
    private File file;
    private Media media;
    boolean openHeaderSlider = false;
    @FXML
    public Label currentDate, da1, da2, da3, da4, da5, da6, da7, da8, da9, da10, da11, da12, da13, da14, da15, da16, da17, da18, da19, da20, da21, da22, da23, da24, da25, da26, da27, da28, da29, da30, da31;
    ArrayList<Label> labelList;

    String myUserName=existPeron.UserName;
    String profilePath="",newName="",newLastName="",newEmail="",newPhone="";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Node> gridPaneChildren = datePane.getChildren();
        labelList = new ArrayList<>();
        for (Node node : gridPaneChildren) {
            if (node instanceof Label) {
                Label label = (Label) node;
                labelList.add(label);
            }
        }
        //////////////////////////// media player for calender
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
        //////////////////////////// media player
        file = new File("src/profile.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer2 = new MediaPlayer(media);
        profileView.setMediaPlayer(mediaPlayer2);
        mediaPlayer2.setAutoPlay(true);
        mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer2.setOnEndOfMedia(() -> {
           mediaPlayer2.seek(Duration.ZERO);
            mediaPlayer2.play();
        });
        slider.setTranslateX(-270);
        headerSlider.setTranslateY(-200);
        //////////////////////////////
        String result = CurrentUserBankAccount(myUserName);
        String[] tokens = result.split("/");
        cardNumOnCard.setText(tokens[0]);
        cardNumOnInfo.setText(tokens[0]);
        untilEndOnCard.setText(tokens[1].substring(0,7).replace("-","/"));
        untilEndOnInfo.setText(tokens[1].substring(0,7).replace("-","/"));
        CVVOnCard.setText(tokens[2]);
        CVVOnInfo.setText(tokens[2]);
        UserNameUnderPicture.setText(existPeron.name +" "+existPeron.lastName);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM");
        String formattedDate = today.format(formatter);
        validFromOnCard.setText(formattedDate);
        setCalender();
        clear();
        ////////////////////////////////////////////
        EditNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()) {nameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;"); nameWarning.setText(""); newName = "";}
            }
        });
        EditLastNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){ lastNameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;"); lastNameWarning.setText("");newLastName = "";}
            }
        });
        EditPhoneNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()) {phoneCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;"); phoneNumberWarning.setText("");newPhone = "";}
            }
        });
        EditEmailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.isEmpty()){ emailCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");emailWarning.setText("");newEmail = "";}
            }
        });
        ////////////////////////////////////////////// set photo
        if(!profilePath.isEmpty()){
            File imagefile = new File(existPeron.profilePass);
            Image image1 = new Image(imagefile.toURI().toString());
            UserProfile.setImage(image1);
        }
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
    public void generateBankAccount() {
        int YEAR_LENGTH = 4;
        int RANDOM_NUMBER_LENGTH = 4;
        StringBuilder cardID = new StringBuilder();

        String yearString = String.format("%04d", LocalDate.now().getYear());
        cardID.append(yearString.substring(0, YEAR_LENGTH));
        cardID.append(" ");
        Random random = new Random();
        int randomNumber = random.nextInt((int) Math.pow(10, RANDOM_NUMBER_LENGTH));
        String randomNumberString = String.valueOf(randomNumber);
        cardID.append(randomNumberString);
        cardID.append(" ");
        int randomNumber2 = random.nextInt((int) Math.pow(10, RANDOM_NUMBER_LENGTH));
        String randomNumberString2 = String.valueOf(randomNumber2);
        cardID.append(randomNumberString2);
        cardID.append(" ");
        int randomNumber3 = random.nextInt((int) Math.pow(10, RANDOM_NUMBER_LENGTH));
        String randomNumberString3 = String.valueOf(randomNumber3);
        cardID.append(randomNumberString3);
        newCardNumLable.setText("  "+cardID.toString());
    }

    @FXML
    void onEditNameField(ActionEvent event) {
        nameWarning.setText("");
        newName = EditNameField.getText();
        if(!isInputValid(newName)) { nameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
        nameWarning.setText("Invalid Name !");
        }else   nameCircle.setStyle("-fx-background-color: #67c953;-fx-background-radius :100;");
    }
    @FXML
    void onEditLastNameField(ActionEvent event) {
        lastNameWarning.setText("");
        newLastName = EditLastNameField.getText();
        if(!isInputValid(newLastName)) { lastNameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
            lastNameWarning.setText("Invalid LastName !");
        }else   lastNameCircle.setStyle("-fx-background-color: #67c953;-fx-background-radius :100;");

    }
    @FXML
    void onEditEmailField(ActionEvent event) {
        emailWarning.setText("");
        newEmail = EditEmailField.getText();
        if(!isEmailValid(newEmail)) { emailCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
            emailWarning.setText("Invalid email !");
        }else    emailCircle.setStyle("-fx-background-color: #67c953;-fx-background-radius :100;");

    }
    @FXML
    void onEditPhoneNumberField(ActionEvent event) {
        phoneNumberWarning.setText("");
        newPhone = EditPhoneNumberField.getText();
        if(!isPhoneNumberValid(newPhone)) { phoneCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
            phoneNumberWarning.setText("Invalid phone Number !");
        }else     phoneCircle.setStyle("-fx-background-color: #67c953;-fx-background-radius :100;");

    }
    public void EditsValid(){
//        if(EditNameField.getText().isEmpty()) newName="";
//        if(!newName.isEmpty() && !isInputValid(newName)){
//            nameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
//            nameWarning.setText("Invalid Name !");
//        }else if( (newName.isEmpty() && nameWarning.getText().isEmpty()) || (!newName.isEmpty() && nameWarning.getText().isEmpty()) ){
//            if(EditLastNameField.getText().isEmpty()) {
//                newLastName = "";
//            }if(!newLastName.isEmpty() && !isInputValid(newLastName)) {
//                lastNameWarning.setText("Invalid LastName !");
//                lastNameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
//            }else if( (newLastName.isEmpty() && lastNameWarning.getText().isEmpty()) || (!newLastName.isEmpty() && lastNameWarning.getText().isEmpty()) ){
//                if(EditPhoneNumberField.getText().isEmpty()) {
//                    newPhone = "";
//                }if(!newPhone.isEmpty() && !isPhoneNumberValid(newPhone)) {
//                    phoneNumberWarning.setText("Invalid phone number !");
//                    newPhone=null;phoneCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
//                } else if( (newPhone.isEmpty() && phoneNumberWarning.getText().isEmpty()) || (!newPhone.isEmpty() && phoneNumberWarning.getText().isEmpty()) ){
//                    if(EditEmailField.getText().isEmpty()) {
//                        newEmail = "";
//                    }if(!newEmail.isEmpty() && !isEmailValid(newEmail)) {
//                        newEmail=null; emailCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
//                        emailWarning.setText("Invalid email !");
//                    } else if( (newEmail.isEmpty() && EditEmailField.getText().isEmpty()) || (!newEmail.isEmpty() && EditEmailField.getText().isEmpty()) ){
//                        nameWarning.setText("");
//                        lastNameWarning.setText("");
//                        phoneNumberWarning.setText("");
                      //  emailWarning.setText("");
        StringBuilder sb = new StringBuilder();
        if(newName.isEmpty()) {newName=null;}
        else sb.append("Name  ");
        if(newLastName.isEmpty()) {newLastName=null;}
        else  sb.append("LastName  ");
        if(newPhone.isEmpty()) {newPhone=null;}
        else sb.append("Phone number  ");
        if(newEmail.isEmpty()){ newEmail=null;}
        else sb.append("Email  ");
        if(profilePath.isEmpty()) {profilePath=null;profileCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");}
        else sb.append("Profile image  ");
        if(newName==null && newLastName==null  && newPhone==null && newEmail==null && profilePath==null ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("You have to press Enter after typing or enter something");
            alert.showAndWait();
        }else {
            InsertEditInfoDatabase(myUserName, newName, newLastName, newPhone, newEmail, profilePath, existPeron);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Done !");
            alert.setContentText("The " + sb + "were changed !");
            alert.showAndWait();
            existPeron = sendPerson(existPeron.UserName,5);
            UserNameUnderPicture.setText(existPeron.name +" "+existPeron.lastName);
            if(profilePath!=null){
                File imagefile = new File(existPeron.profilePass);
                Image image1 = new Image(imagefile.toURI().toString());
                UserProfile.setImage(image1);
            }
        }
        clear();
//                    }
//                }
//            }
//        }
    }
    private void clear(){
        newName="";
        newLastName = "";
        newPhone = "";
        newEmail = "";
        profilePath = "";
        EditNameField.setText("");
        EditLastNameField.setText("");
        EditPhoneNumberField.setText("");
        EditEmailField.setText("");
        nameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
        lastNameCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
        emailCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
        phoneCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
        profileCircle.setStyle("-fx-background-color: #e34040;-fx-background-radius :100;");
    }
    protected boolean isInputValid(String string) {
        System.out.println(string);
        String regex = "^[a-zA-Z]{3,20}$";
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(string);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return true;
        }
        return false;
    }
    protected boolean isPhoneNumberValid(String phone) {
        String regex = "^09[0-9]{9}$";
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(phone);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return true;
        }
        return false;
    }
    protected boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9.]{1,30}@[a-z0-9.]{1,8}(.)[a-z]{1,4}$";
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(email);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return true;
        }
        return false;
    }
    @FXML
    void onProfileImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Get the Stage from the button to show the file chooser
        Stage stage = (Stage) UploadImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            profilePath = selectedFile.getPath();
            profileCircle.setStyle("-fx-background-color: #67c953;-fx-background-radius :100;");
        }
    }
    @FXML
    void OnCreatCardNumber(ActionEvent event) {
        generateBankAccount();
    }
    @FXML void OnOK(){
        if(!newCardNumLable.getText().isEmpty()){
            changeNumCard(existPeron.UserName,newCardNumLable.getText());
            cardNumOnInfo.setText(newCardNumLable.getText());
            cardNumOnCard.setText(newCardNumLable.getText());
            newCardNumLable.setText("");
        }
    }
    @FXML
    void onEdit(ActionEvent event) {
        EditsValid();
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
}
