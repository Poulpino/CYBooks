package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller class for the MostBorrowed scene.
 * This class handles the interaction for displaying the most borrowed books.
 */
public class MostBorrowedController extends SceneController {

    private Stage stage;
    private Scene scene;
    public ListView<Book> bookListView;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the action triggered by the user to display the most borrowed books.
     *
     * @param event the action event triggered by the user interaction
     */
    public void handleMostBorrowed(ActionEvent event) {
        List<Book> books = (List<Book>) new Client().clientAskPopularBook();
        if (books != null) {
            bookListView.getItems().setAll(books);
        } else {
            showError("Error", "Failed to retrieve popular books");
        }
    }
}
