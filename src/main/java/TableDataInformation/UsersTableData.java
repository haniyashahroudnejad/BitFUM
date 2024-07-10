package TableDataInformation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsersTableData {
    public UsersTableData (String UserName,String Name,String LastName){
        setUserName(UserName);
        setName(Name);
        setLastName(LastName);
    }
    private StringProperty UserName = new SimpleStringProperty(this, "UserName", "");
    public String getUserName() {
        return UserName.get();
    }
    public StringProperty UserNameProperty() {return UserName;}
    public void setUserName(String UserName) {this.UserName.set(UserName);}
    private StringProperty Name = new SimpleStringProperty(this, "Name", "");
    public String getName() {
        return Name.get();
    }
    public StringProperty NameProperty() {return Name;}
    public void setName(String Name) {this.Name.set(Name);}
    private StringProperty LastName = new SimpleStringProperty(this, "LastName", "");
    public String getLastName() {
        return LastName.get();
    }
    public StringProperty LastNameProperty() {return LastName;}
    public void setLastName(String LastName) {this.LastName.set(LastName);}

}
