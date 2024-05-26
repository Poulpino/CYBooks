package group.projetcybooks.serveur.model.exception;

public class UserAlreadyExist extends Exception {

    public UserAlreadyExist(String message){
        super("408" +message);
    }
}
