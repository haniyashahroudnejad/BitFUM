package Login;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;

import Start.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static DatabaseConnection.UsersInformationDatabase.con;


public class ForgotPasswordPage {
    @FXML
    private Label BackToLogin;
    @FXML
    private Button Continue;
    @FXML
    private TextField Email;
    @FXML
    private Label InvalidEmail;
    public static String code;
    public static String email;

    @FXML
    protected void onBackToLoginClicked() throws IOException {
        InvalidEmail.setVisible(false);
       StartPage.switchPages.ChangePageByClickingLabel(BackToLogin,"/Login/LoginPage.fxml");
    }
    @FXML
    protected void onContinueClicked() throws IOException, SQLException {
        boolean email_Valid = false;
        Statement xs = con.createStatement();
        ResultSet x = xs.executeQuery("select * from UsersInfo");
        while (x.next()){
            if(Email.getText().equals(x.getString("email"))) email_Valid = true;
        }
        if(email_Valid) {
            email = Email.getText();
            sendEmail(email);
            StartPage.switchPages.ChangePageByClickingButton(Continue, "/Login/SendDigitsCode.fxml");
        }
        else{
            InvalidEmail.setVisible(true);
        }
    }


    public void sendEmail(String emailAddress){
        // Sender's email address
        String senderEmail = "tshtrynftmh@gmail.com";
        String senderPassword = "evce offf zdpw nlao"; // Your Gmail password
        String receiverEmail = emailAddress;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(receiverEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject("JavaMail API Test");
            Random random = new Random();
            int randomNumber;
            char randomChar=' ';
            code="";
            for(int i=0; i<4; i++) {
                boolean isNumber = random.nextBoolean();
                if (isNumber) {
                    randomNumber = random.nextInt(8) + 0;
                    code += String.valueOf(randomNumber);
                } else {
                    randomChar = (char) (random.nextInt(26) + 'A');
                    code += String.valueOf(randomChar);
                }
            }
            message.setText(code);
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            InvalidEmail.setVisible(true);
        }
    }
}
