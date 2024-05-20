package group.projetcybooks.serveur.model.exception;

public class BookNotFoundException extends Exception{

    public BookNotFoundException(String message) {
        super("402"+message);
    }
}
