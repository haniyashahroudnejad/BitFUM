package Login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import Start.*;
import javafx.scene.control.TextField;

public class SendDigitsCode {
    @FXML
    private Label BackToLogin;
    @FXML
    private Button Continue;
    @FXML
    private TextField one;
    @FXML
    private TextField two;
    @FXML
    private TextField three;
    @FXML
    private TextField four;
    @FXML
    private Label falseCode;

    @FXML
    protected void onBackToLoginClicked() throws IOException {
        StartPage.switchPages.ChangePageByClickingLabel(BackToLogin,"/Login/LoginPage.fxml");
    }
    @FXML
    protected void onContinueClicked() throws IOException {
        System.out.print(one.getText()+two.getText()+three.getText()+four.getText()+"*****");
        if(ForgotPasswordPage.code.equals(one.getText()+two.getText()+three.getText()+four.getText())) {
            StartPage.switchPages.ChangePageByClickingButton(Continue, "/Login/ResetPassword.fxml");
        }
        else falseCode.setVisible(true);
    }
}
