package group.projetcybooks.client.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

/**
 * Controller class for the ClientsSignUp scene.
 * This class handles the user interaction for signing up new users.
 */
public class ClientsSignUpController extends SceneController{

    public TextField lastNameField;
    public TextField firstNameField;
    public TextField phoneField;
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

    /**
     * Handles the sign-up action triggered by the user.
     * Sends the user's details to the server for registration.
     * Displays a success or error message based on the result.
     */
    public void handleSignUp() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String phone = phoneField.getText();

        int result = new Client().clientSendNewUser(lastName,firstName,phone);

        if (result == 1) {
            showError("Success", "User Registered");
        } else {
            showError("Error", "Failed to register user");
        }
    }
}
