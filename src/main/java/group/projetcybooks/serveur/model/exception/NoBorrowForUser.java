package group.projetcybooks.serveur.model.exception;

public class NoBorrowForUser extends Exception {
    public NoBorrowForUser(String message) {
        super("405"+message);
    }
}
