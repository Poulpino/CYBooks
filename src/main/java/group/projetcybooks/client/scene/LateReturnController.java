package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Borrow;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class LateReturnController extends SceneController{

    private Stage stage;
    private Scene scene;

    @FXML
    ListView<Borrow> lateReturnListView;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the late returns view with the list of late return records.
     */
    public void initializeLateReturns(){
        List<Borrow>lateReturnList = new Client().clientAskLateReturn();
        lateReturnListView.getItems().addAll(lateReturnList);
    }
}
