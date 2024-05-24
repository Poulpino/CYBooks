package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

public class ClientsEditController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextField newFirstNameField;

    public TextField newLastNameField;

    public TextField newPhoneField;
    private User currentUser;

    public void initializeWithUser(User user) {
        this.currentUser = user;
        newFirstNameField.setText(user.getFirstName());
        newLastNameField.setText(user.getLastName());
        newPhoneField.setText(user.getPhone());
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
