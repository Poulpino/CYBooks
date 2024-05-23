package group.projetcybooks.serveur.model.exception;

public class NoHistoryForUser extends Exception {
    public NoHistoryForUser(String message) {
        super("408" + message);
    }
}