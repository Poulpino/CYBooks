package group.projetcybooks;

import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import group.projetcybooks.client.Client;

import java.io.IOException;

public class ClientsEditController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextField newFirstNameField;

    public TextField newLastNameField;

    public TextField newPhoneField;

    //Pour finir il faut prendre en paramètre l'user de la recherche
    private void handleEdit(User currentUser) {
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
            //switchToMain(event);
        } else {
            showError("Success", "User updated successfully");
        }
    }
}
