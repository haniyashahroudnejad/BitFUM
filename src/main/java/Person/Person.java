package Person;



import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class Person implements Serializable {
    public String name, lastName, UserName, Password, phoneNumber, email, profilePass, WalletID;
    public static int personCounter;
    static Person currentUser;

    public Person(String userName, String First_Name, String Last_Name, String Password, String Phone_Number, String Email, String profilePath, String WalletID) {
        this.name = First_Name;
        this.lastName = Last_Name;
        this.phoneNumber = Phone_Number;
        this.email = Email;
        this.UserName = userName;
        this.Password = Password;
        this.profilePass = profilePath;
        this.WalletID =WalletID;
    }

}