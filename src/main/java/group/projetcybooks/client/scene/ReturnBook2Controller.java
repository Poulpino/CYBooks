package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ReturnBook2Controller extends SceneController {

    private Stage stage;
    private Scene scene;
    private List<Borrow> borrowList = new ArrayList<>(); // Initialisation de borrowList

    @FXML
    Label firstNameLabel;
    @FXML
    Label lastNameLabel;
    @FXML
    Label phoneLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ListView<Borrow> borrowListView;
    public ListView<Book> booksListView;
    public User selectedUser;
    public TextArea ISBN;
    public TextArea isbnTextArea;
    public TextArea authorTextArea;
    public TextArea titleTextArea;

    /**
     * Initializes the view with the specified user's information.
     * @param user the user whose information is used to initialize the view
     */
    public void initializeWithUser(User user) {
        this.selectedUser = user;
        lastNameLabel.setText("Last name : " + selectedUser.getLastName());
        firstNameLabel.setText("First name : " + selectedUser.getFirstName());
        phoneLabel.setText("Phone : " + selectedUser.getPhone());
        loadBorrowList(user);
    }

    /**
     * Loads the borrow list for the specified user.
     * @param user the user for whom to load the borrow list
     */
    public void loadBorrowList(User user) {
        borrowList = new Client().clientAskReturnBookList(user);
        List<Book> books = new ArrayList<>();

        for (Borrow borrow : borrowList) {
            books.add(borrow.getBook());
        }

        if (!books.isEmpty()) {
            booksListView.getItems().addAll(books);
        } else {
            SceneController.showError("Error", "No books found for this user: " + user.getFirstName() + " " + user.getLastName());
        }
    }

    /**
     * Confirms the return of the selected book.
     * @param event the action event triggered by the user
     */
    public void ConfirmReturn(ActionEvent event) {
        Book selectedBook = booksListView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Borrow selectedBorrow = null;
            for (Borrow borrow : borrowList) {
                if (borrow.getBook().equals(selectedBook)) {
                    selectedBorrow = borrow;
                    break;
                }
            }

            if (selectedBorrow != null) {
                int result = new Client().clientReturnBook(selectedBorrow);
                if (result == 1) {
                    SceneController.showError("Success", "The book has been successfully returned.");
                    booksListView.getItems().remove(selectedBook);
                } else {
                    SceneController.showError("Error", "Failed to return the book.");
                }
            } else {
                SceneController.showError("Error", "No matching borrow found.");
            }
        } else {
            SceneController.showError("Error", "No book selected.");
        }
    }
}


