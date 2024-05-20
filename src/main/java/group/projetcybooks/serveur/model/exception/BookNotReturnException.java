package group.projetcybooks.serveur.model.exception;

public class BookNotReturnException extends Exception {
    public BookNotReturnException(String message) {
        super("404"+message);
    }
}
