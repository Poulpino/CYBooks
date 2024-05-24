package group.projetcybooks;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import group.projetcybooks.client.Client;

import java.io.IOException;
import java.util.List;

public class ClientsResearchController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextArea lastNameField;
    public TextArea firstNameField;
    public TextArea phoneField;
    public ListView<User> userListView;

    public void handleSearch(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();

        List<User> users = new Client().clientSearchUser(lastName, firstName, phone);
        if (users != null) {
            userListView.getItems().setAll(users);
        } else {
            showError("Error", "Failed to search users.");
        }
    }

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

    public void switchToClientsEdit(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsEdit.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsBorrowHistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setScene(scene);
        stage.show();
    }

}
