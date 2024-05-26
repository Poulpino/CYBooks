package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    /**
     * Initializes the main application window.
     * @param stage the primary stage for the application
     * @throws IOException if there is an error loading the MainScene.fxml file
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
     * The main entry point for the application.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        new Thread(() -> {
            try {
                Server.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        launch();
    }
}

