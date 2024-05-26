package group.projetcybooks.client.scene;

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

public class UserResearchController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextArea lastNameField;
    public TextArea firstNameField;
    public TextArea phoneField;
    public ListView<User> userListView;

    /**
     * Initializes the interface and sets up event handling for the user list view.
     * When a user double-clicks on an item in the list, it triggers the switch to the client's edit view.
     */

    public void initialize() {
        userListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                switchToClientsEdit(event);
            }
        });
    }

    /**
     * Handles the search action triggered by the user.
     * @param event the action event triggered by the user
     */
    public void handleSearch(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();

        if (firstName.isBlank()) {firstName = null; }
        if (lastName.isBlank()) {lastName = null; }
        if (phone.isBlank()) { phone = null; }

        List<User> users = new Client().clientSearchUser(lastName, firstName, phone);
        if (users != null) {
            userListView.getItems().setAll(users);
        }else {
            showError("Error", "Failed to search users");
        }
    }

    /**
     * Handles the delete action triggered by the user.
     * @param event the action event triggered by the user
     */
    public void handleDelete(ActionEvent event) throws IOException {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            int result = new Client().clientRemoveUser(selectedUser);
            if (result == 1) {
                showError("Success", "User deleted successfully.");
                userListView.getItems().remove(selectedUser);
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } else {
                showError("Error", "Failed to delete user.");
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the client edit view when triggered by the user.
     * @param event the action event triggered by the user
     */
    public void switchToClientsEdit(ActionEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("UsersEdit.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());

                UserEditController controller = fxmlLoader.getController();
                controller.setStage(stage);
                controller.initializeWithUser(selectedUser);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load edit user scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the client edit view when triggered by a mouse event.
     * @param event the mouse event triggered by the user
     */
    public void switchToClientsEdit(MouseEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("UsersEdit.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());

                UserEditController controller = fxmlLoader.getController();
                controller.setStage(stage);
                controller.initializeWithUser(selectedUser);

                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load edit user scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }

    /**
     * Switches to the client's borrow history view when triggered by the user.
     * @param event the action event triggered by the user
     */
    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("UsersBorrowHistory.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            UserBorrowHistoryController controller = fxmlLoader.getController();
            controller.setStage(stage);
            controller.initializeWithUser(selectedUser);

            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            showError("Error", "No user selected.");
        }
    }
}
