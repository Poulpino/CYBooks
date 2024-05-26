package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

import java.io.IOException;

public class UserEditController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
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
     * Initializes the interface with the provided user's information.
     * @param user
     */
    public void initializeWithUser(User user) {
        this.currentUser = user;
        currentFirstNameLabel.setText("Current first name : " + user.getFirstName());
        currentLastNameLabel.setText("Current last name : " + user.getLastName());
        currentPhoneLabel.setText("Current phone : :" + user.getPhone());
    }

    /**
     * Modifies the information of the current user.
     */
    public void handleEdit(ActionEvent event) throws IOException {
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
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            showError("Error", "Failed to update user");
        }
    }
}
