package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MostBorrowedController extends SceneController {

    private Stage stage;
    private Scene scene;
    public ListView<Book> bookListView;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the action to display the most borrowed books, called in BooksMainController.java
     */
    public void handleMostBorrowed() {
        List<Book> books = (List<Book>) new Client().clientAskPopularBook();
        if (books != null) {
            bookListView.getItems().setAll(books);
        } else {
            showError("Error", "Failed to retrieve popular books");
        }
    }
}
