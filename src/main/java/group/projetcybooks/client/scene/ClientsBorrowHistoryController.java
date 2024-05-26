package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Controller class for the ClientsBorrowHistory scene.
 * This class handles the interaction for displaying a user's borrow history.
 */
public class ClientsBorrowHistoryController extends SceneController {

    private Stage stage;
    private Scene scene;
    
    @FXML
    public ListView<Borrow> borrowHistoryListView;
    
    private User user;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller with the specified user.
     *
     * @param user the user whose borrow history will be displayed
     */
    public void initializeWithUser(User user) {
        this.user = user;
    }

    /**
     * Loads and displays the borrow history of the initialized user.
     * If the borrow history is null or empty, an error message is displayed.
     */
    public void loadBorrowHistory() {
        List<Borrow> borrowHistory = new Client().clientAskHistoryBookList(user);
        if (borrowHistory != null) {
            borrowHistoryListView.getItems().setAll(borrowHistory);
        } else {
            showError("History doesn't exist", "No books in borrow history.");
        }
    }
}
