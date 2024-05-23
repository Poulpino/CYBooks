package group.projetcybooks;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import group.projetcybooks.SceneController;
import group.projetcybooks.client.Client;

import java.io.IOException;

public class ClientsSignUpController extends SceneController{

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
            SceneController.showError("Success", "User Registered");
        } else {
            SceneController.showError("Error", "Failed to register user");
        }
    }
}
