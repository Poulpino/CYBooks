package group.projetcybooks;

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

public class BorrowBooks1Controller extends SceneController {

    @FXML
    TextArea isbnTextField;
    @FXML
    TextArea authorTextField;
    @FXML
    TextArea titleTextField;
    @FXML
    ListView<Book> booksListView;

    Book selectedBook = null;

    Stage stage;
    Scene scene;
    Parent root;

    public void searchBooks() {

        String isbn = isbnTextField.getText();
        String author = authorTextField.getText();
        String title = titleTextField.getText();

        if (isbn.isBlank()) { isbn = null; }
        if (author.isBlank()) { author = null; }
        if (title.isBlank()) { title = null; }

        List<Book> books = new Client().clientAskListBook(isbn, author, title);

        if (books == null){
            showError("Error", "Failed to search books.");
        }
        else{
            booksListView.getItems().setAll(books);
        }
    }

    public void bookSelected() {
        selectedBook = booksListView.getSelectionModel().getSelectedItem();
        System.out.println(selectedBook);
    }


    @Override
    public void switchToBorrowBook2 (ActionEvent event) throws IOException {

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
        }
        else{
            showError("Error", "No book selected.");
        }



    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
