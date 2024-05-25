package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReturnBook2Controller extends SceneController{

    private Stage stage;
    private Scene scene;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextArea lastNameField;
    public TextArea firstNameField;
    public TextArea phoneField;
    private User currentUser;

    public void initializeWithUser(User user) {
        this.currentUser = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        phoneField.setText(user.getPhone());
    }
}
