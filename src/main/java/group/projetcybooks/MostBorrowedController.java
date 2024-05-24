package group.projetcybooks;

import com.sun.glass.ui.Menu;
import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static group.projetcybooks.SceneController.showError;

public class MostBorrowedController extends SceneController{

    private Stage stage;
    private Scene scene;
    public ListView<Book> bookListView;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
public  void handleMostBorrowed(ActionEvent event) {
    List<Book> book = (List<Book>) new Client().clientAskPopularBook();
    if (book != null) {
        bookListView.getItems().setAll(book);
    }else {
        showError("Error", "Failed to search users");
    }
   
}
