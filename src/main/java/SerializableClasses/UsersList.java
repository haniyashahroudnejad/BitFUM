package SerializableClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class UsersList implements Serializable {
   public String UserName;
   public String Name;
   public String LastName;

    public UsersList(String UserName, String Name ,String LastName){
        this.UserName = UserName;
        this.Name = Name;
        this.LastName = LastName;
    }
}
