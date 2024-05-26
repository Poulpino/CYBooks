package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import group.projetcybooks.client.Client;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for the main books scene in the application.
 * Handles the user interface interactions and connects to the client to retrieve book data.
 */
public class BooksMainController extends SceneController {

    @FXML
    TextArea isbnTextArea;
    @FXML
    TextArea authorTextArea;
    @FXML
    TextArea titleTextArea;
    @FXML
    ListView<Book> booksListView;

    Book selectedBook = null;

    Stage stage;
    Scene scene;
    Parent root;

    /**
     * Searches for books based on the criteria provided in the text fields.
     * Retrieves a list of books from the server and displays them in the ListView.
     */
    public void searchBooks() {
        String isbn = isbnTextArea.getText();
        String author = authorTextArea.getText();
        String title = titleTextArea.getText();

        if (isbn.isBlank()) {
            isbn = null;
        }
        if (author.isBlank()) {
            author = null;
        }
        if (title.isBlank()) {
            title = null;
        }

        List<Book> books = new Client().clientAskListBook(isbn, author, title);

        if (books == null) {
            showError("Error", "Failed to search books.");
        } else {
            booksListView.getItems().setAll(books);
        }
    }

    /**
     * Handles the selection of a book from the ListView.
     * Sets the selectedBook to the currently selected item.
     */
    public void bookSelected() {
        selectedBook = booksListView.getSelectionModel().getSelectedItem();
        System.out.println(selectedBook);
    }

    /**
     * Switches the scene to the BorrowBook2 scene.
     * Passes the selected book to the BorrowBooks2Controller.
     *
     * @param event the event that triggered this method
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void switchToBorrowBook2(ActionEvent event) throws IOException {
        if (selectedBook != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("BorrowBook2.fxml"));
            root = fxmlLoader.load();
            BorrowBooks2Controller controller = fxmlLoader.getController();
            controller.setBookToBorrow(selectedBook);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            showError("Error", "No book selected.");
        }
    }

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Switches the scene to the MostBorrowed scene.
     *
     * @param event the event that triggered this method
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToMostBorrowed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MostBorrowed.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());

        MostBorrowedController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.handleMostBorrowed();

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
