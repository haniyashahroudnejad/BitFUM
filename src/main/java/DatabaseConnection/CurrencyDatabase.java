package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
public class CurrencyDatabase {
    public Connection databaseLink;

    public Connection getDBConnection() {
        String databaseUser = "root";
        String databasePassword = "h9026666518";
        String url = "jdbc:mysql://localhost:3306/BitFUM" ;
        try {
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}