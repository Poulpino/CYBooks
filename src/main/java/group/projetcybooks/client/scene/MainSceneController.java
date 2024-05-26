package group.projetcybooks.client.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the MainScene.
 * This class handles the main interactions for the main scene of the application.
 */
public class MainSceneController extends SceneController {

    private Scene scene;
    private Stage stage;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
