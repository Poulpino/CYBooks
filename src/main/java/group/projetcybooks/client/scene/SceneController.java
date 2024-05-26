package group.projetcybooks.client.scene;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for managing scene transitions and displaying error messages.
 */
public class SceneController {

    private Stage stage;
    private Scene scene;

    /**
     * Sets the stage for the controller.
     *
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Methods for switching to different scenes

    // Switches to the Main scene
    public void switchToMain(ActionEvent event) throws IOException {
        // Code for loading Main scene
    }

    // Switches to the ClientsMain scene
    public void switchToClientsMain(ActionEvent event) throws IOException {
        // Code for loading ClientsMain scene
    }

    // Switches to the BorrowMain scene
    public void switchToBorrowMain(ActionEvent event) throws IOException {
        // Code for loading BorrowMain scene
    }

    // Switches to the BooksMain scene
    public void switchToBooksMain(ActionEvent event) throws IOException {
        // Code for loading BooksMain scene
    }

    // Switches to the BorrowBook1 scene
    public void switchToBorrowBook1(ActionEvent event) throws IOException {
        // Code for loading BorrowBook1 scene
    }

    // Switches to the BorrowBook2 scene
    public void switchToBorrowBook2(ActionEvent event) throws IOException {
        // Code for loading BorrowBook2 scene
    }

    // Switches to the ReturnBook1 scene
    public void switchToReturnBook1(ActionEvent event) throws IOException {
        // Code for loading ReturnBook1 scene
    }

    // Switches to the ClientsResearch scene
    public void switchToClientsResearch(ActionEvent event) throws IOException {
        // Code for loading ClientsResearch scene
    }

    // Switches to the ClientsSignUp scene
    public void switchToClientsSignUp(ActionEvent event) throws IOException {
        // Code for loading ClientsSignUp scene
    }

    // Switches to the LateReturn scene
    public void switchToLateReturn(ActionEvent event) throws IOException {
        // Code for loading LateReturn scene
    }

    // Switches to the ClientsBorrowHistory scene
    public void switchToClientsBorrowHistory(ActionEvent event) throws IOException {
        // Code for loading ClientsBorrowHistory scene
    }

    // Switches to the ClientsEdit scene
    public void switchToClientsEdit(ActionEvent event) throws IOException {
        // Code for loading ClientsEdit scene
    }

    // Switches to the MostBorrowed scene
    public void switchToMostBorrowed(ActionEvent event) throws IOException {
        // Code for loading MostBorrowed scene
    }

    /**
     * Displays an error message.
     *
     * @param title   the title of the error message
     * @param message the content of the error message
     */
    public static void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
