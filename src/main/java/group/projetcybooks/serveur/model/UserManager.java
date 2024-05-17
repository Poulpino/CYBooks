package group.projetcybooks.serveur.model;

import group.projetcybooks.serveur.ConnectDB;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class permits to manage all users
 */
public class UserManager {

    private HashMap<Integer, User> users;

    public UserManager(String requestUser) {

        this.users = new HashMap<>();

        //Split lines and values
        String[] lines = requestUser.split("/");
        for(String line : lines){
            String[] values = line.split(";");
            if(values.length==4){
                int id = Integer.parseInt(values[0]);
                String lastName = values[1];
                String firstName = values[2];
                String phone = values[3];
                User user = new User(id,lastName,firstName,phone);
                users.put(id,user);
            }
            else{
                System.out.println("The lines doesn't have all the values wanted");
            }
        }
    }

    /**
     * This method permits to add a User into the system.
     * It defines a new ID by looking from 0 to +inf if the number is already use for an ID
     * @param lastName;
     * @param firstName;
     * @param phone;
     */
    public void addUser(String lastName, String firstName, String phone) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        int newID=0;
        while(users.containsKey(newID)){
                newID+=1;
            }

        User User = new User(newID, lastName, firstName, phone);
        users.put(newID, User);
        connectDB.requestInsertDB("INSERT into User (id,lastName,firstName,phone) VALUES ('"+User.getId()+"', '"+User.getLastName()+"', '"+User.getFirstName()+"', '"+User.getPhone()+"');");
        System.out.println(users.get(newID).toString() + "added");

    }

    /**
     * This method permits to remove a User from the system.
     * It checks if the User have restored all book he had borrow.
     * @param id;
     * @param borrowManager;
     */
    public void removeUser(int id,BorrowManager borrowManager){
        //TODO : Supprimer la ligne dans la BDD
        //Check if User restored all book he had borrow
        for(Map.Entry<Integer,Borrow> entry : borrowManager.getBorrowing().entrySet()){
            if(entry.getValue().getUser().getId()==id && entry.getValue().getRestore()!=Boolean.TRUE){
                System.out.println("The book" + entry.getValue().getBook().getTitle() + "have not been restored");
                System.out.println("User can't be removed");
                return;
            }
        }
        users.remove(id);
        System.out.println("User removed");
    }

    /**
     * This method permits to change User's information.
     * Information that should not be modified must be written as an empty character string "".
     * @param id;
     * @param lastName;
     * @param firstName;
     * @param phone;
     */
    public void updateUser(int id,String lastName, String firstName, String phone){
        //TODO : Modifier les infos dans la BDD
        //To give the possibility of changing only one element, we assume that if an element must not be changed it is the empty string
        if(!lastName.isEmpty()) {
            users.get(id).setLastName(lastName);
        }
        if(!firstName.isEmpty()) {
            users.get(id).setFirstName(firstName);
        }
        if(!phone.isEmpty()) {
            users.get(id).setPhone(phone);
        }
        System.out.println(users.get(id).toString() + "updated");
    }

    /**
     * This method permits to search the id of a User by using is Last name or is First Name or is Phone number
     * @param lastName;
     * @param firstName;
     * @param phone;
     * @return id of the User or -1 if not found;
     */
    public int searchUser(String lastName, String firstName, String phone){

        //Search the id of the User with is Last/First name or is phone number
        for(Map.Entry<Integer, User> entry : users.entrySet()){
            if (entry.getValue().getLastName().equals(lastName) | entry.getValue().getFirstName().equals(firstName) | entry.getValue().getPhone().equals(phone)){
                return(entry.getKey());
            }
        }
        System.out.println("User not found");
        return(-1);
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
