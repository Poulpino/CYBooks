package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

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

    public void initializeWithUser(User user) {
        this.currentUser = user;
        currentFirstNameLabel.setText("Current first name : " + user.getFirstName());
        currentLastNameLabel.setText("Current last name : " + user.getLastName());
        currentPhoneLabel.setText("Current phone : :" + user.getPhone());
    }
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
