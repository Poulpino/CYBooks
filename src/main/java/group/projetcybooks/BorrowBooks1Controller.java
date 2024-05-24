package group.projetcybooks;

import group.projetcybooks.serveur.model.Book;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    ListView<Book> bookListView;

    Book selectedBook;

    Stage stage;
    Scene scene;

    public void searchBooks(ActionEvent event) throws IOException {

        String isbn = isbnTextField.getText();
        String author = authorTextField.getText();
        String title = titleTextField.getText();

        if (isbn.isBlank()) { isbn = "NULL"; }
        if (author.isBlank()) { author = "NULL"; }
        if (title.isBlank()) { title = "NULL"; }

        ObservableList<Book> listOfBooks = FXCollections.observableList(new Client().clientSearchBook(isbn, author, title));

        bookListView.setItems(listOfBooks);
    }

    public void bookSelected(ActionEvent event) {
        bookListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {

            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book book, Book t1) {

                selectedBook = bookListView.getSelectionModel().getSelectedItem();



            }
        });
    }

    @Override
    public void switchToBorrowBook2 (ActionEvent event) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("BorrowBook2.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
