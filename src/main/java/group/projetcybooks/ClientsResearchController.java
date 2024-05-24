package group.projetcybooks;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;

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

    public void initialize() {
        userListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleEditUser(event);
            }
        });
    }

    public void handleSearch(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();

        if (firstName.isBlank()) {firstName = "NULL"; }
        if (lastName.isBlank()) {lastName = "NULL"; }
        if (phone.isBlank()) { phone = "NULL"; }

        List<User> users = new Client().clientSearchUser(lastName, firstName, phone);
        if (users != null) {
            userListView.getItems().setAll(users);
        }else {
            showError("Error", "Failed to search users");
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

    private void handleEditUser(MouseEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsEdit.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());

                ClientsEditController controller = fxmlLoader.getController();
                controller.setStage(stage);
                controller.initializeWithUser(selectedUser);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load edit user scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ClientsBorrowHistory.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            ClientsBorrowHistoryController controller = fxmlLoader.getController();
            controller.setStage(stage);
            controller.initializeWithUser(selectedUser);

            stage.setScene(scene);
            stage.show();
        } else {
            showError("Error", "No user selected.");
        }
    }
}
