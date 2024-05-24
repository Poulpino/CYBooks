package group.projetcybooks.client.scene;

import group.projetcybooks.client.Client;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.util.List;

public class ClientsBorrowHistoryController extends SceneController{

    private Stage stage;
    private Scene scene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ListView<Borrow> borrowHistoryListView;
    private User user;
    public void initializeWithUser(User user) {
        this.user = user;
        loadBorrowHistory();
    }

    public void loadBorrowHistory() {
        List<Borrow> borrowHistory = new Client().clientAskHistoryBookList(user);
        if (borrowHistory != null) {
            borrowHistoryListView.getItems().setAll(borrowHistory);
        } else {
            showError("History don't Exist", "No Book in borrow history");
        }
    }
}
