package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for the BorrowBooks2 scene.
 * This class handles the user interaction for selecting a user to borrow a book.
 */
public class BorrowBooks2Controller extends SceneController {

    private Stage stage;
    private Scene scene;

    @FXML
    TextArea firstNameTextField;
    @FXML
    TextArea lastNameTextField;
    @FXML
    TextArea phoneTextField;
    @FXML
    ListView<User> usersListView;

    private Book bookToBorrow;

    User userBorrowing;

    /**
     * Sets the book to be borrowed.
     *
     * @param bookToBorrow the book to be borrowed
     */
    public void setBookToBorrow(Book bookToBorrow) {
        this.bookToBorrow = bookToBorrow;
    }

    /**
     * Handles the search for users based on the input fields for first name, last name, and phone.
     * The search results are displayed in the ListView.
     *
     * @param event the action event triggered by the user interaction
     */
    public void handleSearch(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String phone = phoneTextField.getText();

        if (firstName.isBlank()) { firstName = null; }
        if (lastName.isBlank()) { lastName = null; }
        if (phone.isBlank()) { phone = null; }

        List<User> users = new Client().clientSearchUser(lastName, firstName, phone);

        if (users != null){
            usersListView.getItems().setAll(users);
        }
        else{
            showError("Error", "Failed to search users.");
        }
    }

    /**
     * Handles the selection of a user from the ListView.
     * The selected user is stored in the userBorrowing field.
     */
    public void userSelected() {
        userBorrowing = usersListView.getSelectionModel().getSelectedItem();
        System.out.println(userBorrowing);
    }

    /**
     * Handles the borrowing of the book by the selected user.
     * If successful, switches back to the main scene.
     *
     * @param event the action event triggered by the user interaction
     * @throws IOException if an I/O error occurs during loading the FXML file
     */
    public void borrowBook(ActionEvent event) throws IOException {
        int borrow = new Client().clientBorrowBook(bookToBorrow, userBorrowing);
        if (borrow == 1){
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        }
        else{
            showError("Error","There was an error while trying to borrow the book.");
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
}
