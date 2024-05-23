package group.projetcybooks.serveur.model.exception;

public class NoArgumentException extends Exception {
    public NoArgumentException(String message) {
        super("410 "+message);
    }
}
