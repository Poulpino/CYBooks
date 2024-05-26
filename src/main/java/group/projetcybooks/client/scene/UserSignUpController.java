package group.projetcybooks.client.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

import java.io.IOException;

public class UserSignUpController extends SceneController{

    public TextField lastNameField;
    public TextField firstNameField;

    public TextField phoneField;
    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles the sign-up action initiated by the user.
     */
    public void handleSignUp(ActionEvent event) throws IOException {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String phone = phoneField.getText();

        int result = new Client().clientSendNewUser(lastName,firstName,phone);

        if (result == 1) {
            showError("Success", "User Registered");
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            showError("Error", "Failed to register user");
        }
    }
}
