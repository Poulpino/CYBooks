package group.projetcybooks.client.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

public class UserSignUpController extends SceneController{

    public TextField lastNameField;
    public TextField firstNameField;

    public TextField phoneField;
    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
