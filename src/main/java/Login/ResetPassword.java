package Login;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Start.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static DatabaseConnection.UsersInformationDatabase.con;
import static Login.ForgotPasswordPage.email;
import static Person.User.ValidPassword;
import static Person.User.resetPassword;

public class ResetPassword {
    @FXML
    private PasswordField pass;
    @FXML
    protected PasswordField repeatPass;
    @FXML
    private Label passL;
    @FXML
    protected Label repeatPassL;
    @FXML
    private Button Submit;

    @FXML
    protected void onContinueClicked() throws IOException, SQLException {
        boolean password = isPasswordValid(pass.getText());
        boolean repeatedPass = false;
        if(pass.getText().equals(repeatPass.getText())) repeatedPass = true;
        if(!password) passL.setVisible(true);
        if (!repeatedPass) repeatPassL.setVisible(true);
        if(repeatedPass && password){
            resetPassword(pass.getText(), email);
            StartPage.switchPages.ChangePageByClickingButton(Submit,"/Login/LoginPage.fxml");
        }
    }

    protected boolean isPasswordValid(String pass) throws SQLException {
        String regex = "^[a-zA-Z0-9]{5,10}$";
        Pattern patten = Pattern.compile(regex);
        Matcher matcher = patten.matcher(pass);
        boolean matchFound = matcher.find();
        boolean truePass1 = false;
        boolean truePass2 = true;
        if (matchFound) {
            truePass1=true;
        }
        truePass2=ValidPassword(pass);
        if(truePass2 && truePass1)return true;
        else return false;
    }
}
