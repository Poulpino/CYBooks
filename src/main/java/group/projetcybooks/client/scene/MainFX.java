package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the JavaFX client application.
 * This class starts the JavaFX application and initializes the main scene.
 */
public class MainFX extends Application {

    /**
     * Starts the JavaFX application by loading the main scene.
     *
     * @param stage the primary stage for the JavaFX application
     * @throws IOException if an I/O error occurs during loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1010);
        stage.setTitle("CY Books");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     * Starts the server in a separate thread and launches the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Start the server in a separate thread
        new Thread(() -> {
            try {
                Server.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Launch the JavaFX application
        launch();
    }
}
