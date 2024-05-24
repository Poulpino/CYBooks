package group.projetcybooks;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksMainController extends SceneController {

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchToMostBorrowed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("MostBorrowed.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
