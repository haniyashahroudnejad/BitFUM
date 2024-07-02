package Start;
import DatabaseConnection.CurrencyDatabaseConnection;
import DatabaseConnection.TransferConnection;
import Home.HomePage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static DatabaseConnection.UsersInformationDatabase.createConnection;
import DatabaseConnection.TransferConnection;

public class
StartPage extends Application {
    public static SwitchPages switchPages = new SwitchPages();
    @Override
    public void start(Stage stage) throws IOException {
        createConnection();
        TransferConnection connectNow = new TransferConnection();
        Connection connectDB = connectNow.getDBConnection();
        try {
            Statement statement = connectDB.createStatement();
            connectNow.createTransferTable(connectDB,statement);
        }catch (SQLException e){
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("/Login/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.
                load(), 1034, 518);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
