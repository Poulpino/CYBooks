package group.projetcybooks.serveur.model.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super("407"+message);
    }
}
