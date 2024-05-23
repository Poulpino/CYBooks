package group.projetcybooks;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientsResearchController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchToClientsEdit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsEdit.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsBorrowHistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

}
