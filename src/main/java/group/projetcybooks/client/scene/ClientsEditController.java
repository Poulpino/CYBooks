package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

/**
 * Controller class for the ClientsEdit scene.
 * This class handles the user interaction for editing a user's details.
 */
public class ClientsEditController extends SceneController {

    private Stage stage;
    private Scene scene;

    @FXML
    public TextField newFirstNameField;
    @FXML
    public TextField newLastNameField;
    @FXML
    public TextField newPhoneField;
    @FXML
    public Label currentFirstNameLabel;
    @FXML
    public Label currentLastNameLabel;
    @FXML
    public Label currentPhoneLabel;

    private User currentUser;

    /**
     * Sets the stage for this controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller with the specified user.
     * Displays the current details of the user.
     *
     * @param user the user whose details will be edited
     */
    public void initializeWithUser(User user) {
        this.currentUser = user;
        currentFirstNameLabel.setText("Current first name: " + user.getFirstName());
        currentLastNameLabel.setText("Current last name: " + user.getLastName());
        currentPhoneLabel.setText("Current phone: " + user.getPhone());
    }

    /**
     * Handles the edit operation.
     * Validates the input fields and updates the user's details.
     * Displays a success or error message based on the result.
     */
    public void handleEdit() {
        String newFirstName = newFirstNameField.getText();
        String newLastName = newLastNameField.getText();
        String newPhone = newPhoneField.getText();

        if (newFirstName.isEmpty() || newLastName.isEmpty() || newPhone.isEmpty()) {
            showError("Error", "All fields must be filled");
            return;
        }
        
        int result = new Client().clientUpdateUser(currentUser, newLastName, newFirstName, newPhone);
        if (result == 1) {
            showError("Success", "User updated successfully");
        } else {
            showError("Error", "Failed to update user");
        }
    }
}
