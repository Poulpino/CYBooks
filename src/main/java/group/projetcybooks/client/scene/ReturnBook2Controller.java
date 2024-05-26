package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the ReturnBook2 scene.
 * This class handles the interaction for returning borrowed books.
 */
public class ReturnBook2Controller extends SceneController {

    private Stage stage;
    private Scene scene;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public ListView<Borrow> borrowListView;

    public ListView<Object> booksListView;
    public User selectedUser;
    public TextArea ISBN;
    public TextArea isbnTextArea;
    public TextArea authorTextArea;
    public TextArea titleTextArea;

    /**
     * Initializes the controller with the selected user and loads the borrow list.
     *
     * @param user the selected user
     */
    public void initializeWithUser(User user) {
        this.selectedUser = user;
        loadBorrowList(user);
    }

    /**
     * Loads the borrow list for the selected user.
     *
     * @param user the selected user
     */
    public void loadBorrowList(User user) {
        List<Borrow> borrows = new Client().clientAskReturnBookList(user);
        List<Book> books = new ArrayList<>();

        for (Borrow borrow : borrows) {
            books.add(borrow.getBook());
        }

        if (!books.isEmpty()) {
            booksListView.getItems().addAll(books);
        } else {
            SceneController.showError("Error", "No books found for this user: " + user.getFirstName() + " " + user.getLastName());
        }
    }

    /**
     * Confirms the return of the selected borrowed book.
     * Removes the book from the borrow list if returned successfully.
     */
    public void ConfirmReturn() {
        Borrow selectedBorrow = borrowListView.getSelectionModel().getSelectedItem();
        if (selectedBorrow != null) {
            Client client = new Client();
            int result = client.clientReturnBook(selectedBorrow);
            if (result == 1) {
                showError("Success", "The book has been successfully returned.");
                borrowListView.getItems().remove(selectedBorrow);
            } else {
                showError("Error", "Failed to return the book.");
            }
        } else {
            showError("Error", "No borrow selected.");
        }
    }
}
