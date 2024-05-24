package group.projetcybooks;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static group.projetcybooks.SceneController.showError;

public class MostBorrowedController extends SceneController {

    private Stage stage;
    private Scene scene;
    public ListView<Book> bookListView;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleMostBorrowed(ActionEvent event) {
        List<Book> books = (List<Book>) new Client().clientAskPopularBook();
        if (books != null) {
            bookListView.getItems().setAll(books);
        } else {
            showError("Error", "Failed to retrieve popular books");
        }
    }
}
