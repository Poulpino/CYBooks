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

    public void setBookToBorrow(Book bookToBorrow) {
        this.bookToBorrow = bookToBorrow;
    }

    /**
     * Handles the search action triggered by the user.
     * Updates the users list view with the retrieved users.
     * @param event the action event triggered by the user
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
     * Retrieves the selected user from the users list view.
     * @return void
     */
    public void userSelected() {
        userBorrowing = usersListView.getSelectionModel().getSelectedItem();
        System.out.println(userBorrowing);
    };

    /**
     * Borrows the selected book for the selected user.
     * @param event the action event triggered by the user
     * @throws IOException if there is an error loading the MainScene.fxml file
     */
    public void borrowBook(ActionEvent event) throws IOException {

        int borrow = new Client().clientBorrowBook(bookToBorrow, userBorrowing);
        if (borrow == 1){
            showError("Success", "Book borrowed");
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



    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
