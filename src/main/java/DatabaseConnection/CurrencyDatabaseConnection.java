package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
public class CurrencyDatabaseConnection {
    public Connection databaseLink;

    public Connection getDBConnection() {
        String databaseUser = "root";
        String databasePassword = "h9026666518";
        String url = "jdbc:mysql://localhost:3306/BitFUM" ;
        try {
//            Class.forName("com.mysql.cj-jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}