package group.projetcybooks.serveur.model.exception;

public class NoLateReturnBook extends Exception{

    public NoLateReturnBook(String message){
        super("406" +message);
    }
}
