package group.projetcybooks.client.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for the Hello scene.
 * This class handles the interaction for displaying a welcome message.
 */
public class HelloController {
    
    @FXML
    private Label welcomeText;

    /**
     * Handles the action triggered by clicking the "Hello" button.
     * Sets the welcome text to display a welcome message.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
