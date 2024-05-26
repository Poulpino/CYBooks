package group.projetcybooks.client.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the ClientsMain scene.
 * This class handles the main interactions for the client's main view.
 */
public class ClientsMainController extends SceneController {

    private Stage stage;
    private Scene scene;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
