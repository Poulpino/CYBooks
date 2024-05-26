package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ReturnBook1Controller extends SceneController{

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
     * Switches to the return book view when triggered by the user.
     * @param event the action event triggered by the user
     */

    public void switchToReturnBook2(ActionEvent event) {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("ReturnBook2.fxml"));

                Parent root = fxmlLoader.load();
                ReturnBook2Controller controller = fxmlLoader.getController();
                controller.initializeWithUser(selectedUser);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to load ReturnBook2 scene: " + e.getMessage());
            }
        } else {
            showError("Error", "No user selected.");
        }
    }}
