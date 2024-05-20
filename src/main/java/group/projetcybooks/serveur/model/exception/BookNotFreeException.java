package group.projetcybooks.serveur.model.exception;

public class BookNotFreeException extends Exception {
    public BookNotFreeException(String message) {
        super("403"+message);
    }
}
