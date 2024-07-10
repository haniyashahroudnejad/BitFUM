package Start;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchPages {
    public void ChangePageByClickingButton(Button buttonName, String destinationPage) throws IOException {
        //  System.out.println("hereee="+getClass().getResource(destinationPage));
        Parent backParent = FXMLLoader.load(getClass().getResource(destinationPage));
        Scene backScene;
        if(destinationPage.equals("/Login/LoginPage.fxml"))  backScene = new Scene(backParent,1034,518);
        else backScene = new Scene(backParent);
        Stage window = (Stage) buttonName.getScene().getWindow();
        window.setScene(backScene);
        window.show();
    }
    public void ChangePageByClickingLabel(Label LabelName, String destinationPage) throws IOException {
        Parent backParent = FXMLLoader.load(getClass().getResource(destinationPage));
        Scene backScene = new Scene(backParent);
        Stage window = (Stage) LabelName.getScene().getWindow();
        window.setScene(backScene);
        window.show();
    }
    public void ChangePageByClickingImage(ImageView imageView, String destinationPage) throws IOException {
        Parent backParent = FXMLLoader.load(getClass().getResource(destinationPage));
        Scene backScene = new Scene(backParent);
        Stage window = (Stage) imageView.getScene().getWindow();
        window.setScene(backScene);
        window.show();
    }
    public void ChangePageByClickingTable(TableView x, String destinationPage) throws IOException{
        Parent backParent = FXMLLoader.load(getClass().getResource(destinationPage));
        Scene backScene = new Scene(backParent);
        Stage window = (Stage) x.getScene().getWindow();
        window.setScene(backScene);
        window.show();
    }

}
