package group.projetcybooks.client.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainSceneController extends SceneController{

    private Scene scene;

    private Stage stage;

    public void switchToLateReturn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("LateReturn.fxml"));

        Parent root = fxmlLoader.load();
        LateReturnController controller = fxmlLoader.getController();
        controller.initializeLateReturns();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public void setStage(Stage stage) { this.stage = stage; }
}
