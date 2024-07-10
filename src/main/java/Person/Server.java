package Person;

import Start.StartPage;
import Start.SwitchPages;
import eu.hansolo.tilesfx.addons.Switch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.Statement;

import static DatabaseConnection.UsersInformationDatabase.*;
import static javafx.application.Application.launch;

public class Server extends Application {


    public static SwitchPages switchPages = new SwitchPages();

    @Override
    public void start(Stage stage) throws IOException {
        createConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(StartPage.class.getResource("/Home/ProfilePage.fxml"));
        Scene scene = new Scene(fxmlLoader.
                load(), 1034
                , 518);
        stage.setTitle("BitFum");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        StartServer.main();
        launch();
    }



    public static class StartServer {
        public static void main() {
            createConnection();
            try (ServerSocket serverSocket = new ServerSocket(9090)) {
                System.out.println("Server listening on port 9090");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                    Thread a = new Thread(new ClientHandler(clientSocket));
                    a.start();
                    System.out.print(a.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

