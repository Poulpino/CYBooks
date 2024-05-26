package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

/**
 * Controller class for the ClientsResearch scene.
 * This class handles the user interaction for searching and managing users.
 */
public class ClientsResearchController extends SceneController{

    private Stage stage;
    private Scene scene;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextArea lastNameField;
    public TextArea firstNameField;
    public TextArea phoneField;
    public ListView<User> userListView;

    /**
     * Initializes the controller.
     * Sets up double-click listener on the userListView to handle edit action.
     */
    public void initialize() {
        userListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                switchToClientsEdit(event);
            }
        });
    }

    /**
     * Handles the search action triggered by the user.
     * Searches for users based on the input fields and displays the results in the userListView.
     *
     * @param event the action event triggered by the user interaction
     */
    public void handleSearch(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();

        if (firstName.isBlank()) {firstName = null; }
        if (lastName.isBlank()) {lastName = null; }
        if (phone.isBlank()) { phone = null; }

        List<User> users = new Client().clientSearchUser(lastName, firstName, phone);
        if (users != null) {
            userListView.getItems().setAll(users);
        } else {
            showError("Error", "Failed to search users");
        }
    }

    /**
     * Handles the delete action triggered by the user.
     * Deletes the selected user from the userListView and the database.
     *
     * @param event the action event triggered by the user interaction
     */
    public void handleDelete(ActionEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            int result = new Client().clientRemoveUser(selectedUser);
            if (result == 1) {
                showError("Success", "User deleted successfully.");
                userListView.getItems().remove(selectedUser);
            } else {
                showError("Error", "Failed to delete user.");
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the ClientsEdit scene for editing the selected user.
     * 
     * @param event the mouse event triggered by the user interaction
     */
    public void switchToClientsEdit(MouseEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsEdit.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());

                ClientsEditController controller = fxmlLoader.getController();
                controller.setStage(stage);
                controller.initializeWithUser(selectedUser);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load edit user scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the ClientsEdit scene for editing the selected user.
     *
     * @param event the action event triggered by the user interaction
     */
    public void switchToClientsEdit(ActionEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsEdit.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());

                ClientsEditController controller = fxmlLoader.getController();
                controller.setStage(stage);
                controller.initializeWithUser(selectedUser);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load edit user scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the ClientsBorrowHistory scene to display the borrow history of the selected user.
     *
     * @param event the action event triggered by the user interaction
     * @throws IOException if an I/O error occurs during loading the FXML file
     */
    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsBorrowHistory.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            ClientsBorrowHistoryController controller = fxmlLoader.getController();
            controller.setStage(stage);
            controller.initializeWithUser(selectedUser);

            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            showError("Error", "No user selected.");
        }
    }
}
