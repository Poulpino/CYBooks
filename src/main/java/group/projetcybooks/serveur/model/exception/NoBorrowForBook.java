package group.projetcybooks.serveur.model.exception;

public class NoBorrowForBook extends Exception {
    public NoBorrowForBook(String message) {
        super("408"+message);
    }
}
