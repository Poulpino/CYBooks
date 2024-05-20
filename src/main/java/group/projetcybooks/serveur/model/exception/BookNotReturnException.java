package group.projetcybooks.serveur.model.exception;

public class BookNotReturnException extends Exception {
    public BookNotReturnException(String message) {
        super(STR."404\{message}");
    }
}
