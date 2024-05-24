package group.projetcybooks;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainSceneController extends SceneController{

    private Scene scene;

    private Stage stage;

    public void setStage(Stage stage) { this.stage = stage; }
}
