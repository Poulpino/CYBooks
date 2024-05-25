package group.projetcybooks.client.scene;

import group.projetcybooks.serveur.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1010);
        stage.setTitle("CY Books");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                Server.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Démarrez l'application JavaFX
        launch();
    }
}
