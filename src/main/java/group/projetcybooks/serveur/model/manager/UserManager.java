package group.projetcybooks.serveur.model.manager;

import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.User;
import group.projetcybooks.serveur.model.exception.BookNotReturnException;
import group.projetcybooks.serveur.model.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages user-related operations.
 * Attributes :
 *  HashMap<Integer, User> <b> users</b> List of users; Key : id of a user;
 *                                                     Values : users
 */
public class UserManager {

    private HashMap<Integer, User> users;

    /**
     * Constructs a UserManager and populates the users map based on the input request string.
     *
     * @param requestUser               A string containing user data
     *                                  Example: "id;lastName;firstName;phone".
     * @throws IllegalArgumentException If a line does not contain exactly 4 values.
     */
    public UserManager(String requestUser) {

        this.users = new HashMap<>();
        if (!requestUser.isEmpty()) {
            //Split lines and values
            String[] lines = requestUser.split("/");
            for (String line : lines) {
                String[] values = line.split(";");
                if (values.length == 4) {
                    int id = Integer.parseInt(values[0]);
                    String lastName = values[1];
                    String firstName = values[2];
                    String phone = values[3];
                    User user = new User(id, lastName, firstName, phone);
                    users.put(id, user);
                } else {
                    throw new IllegalArgumentException("The line doesn't have all the values wanted: " + line);
                }
            }
        }
    }

    /**
     * This method permits to add a User into the system.
     * It defines a new ID by looking from 0 to +inf if the number is already use for an ID.
     *
     * @param lastName   The last name of the user.
     * @param firstName  The first name of the user.
     * @param phone      The phone number of the user.
     * @throws Exception If an error occurs during the user addition process.
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
    }

    /**
     * This method permits to remove a User from the system.
     * It checks if the User has restored all books he had borrowed.
     *
     * @param id            The ID of the user to be removed.
     * @param borrowManager An instance of BorrowManager for accessing borrow data.
     * @throws Exception    If an error occurs during the user removal process.
     */
    public void removeUser(int id,BorrowManager borrowManager) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        //Check if User restored all book he had borrow
        for(Map.Entry<Integer, Borrow> entry : borrowManager.getBorrowing().entrySet()){
            if(entry.getValue().getUser().getId()==id && entry.getValue().getRestore()!=Boolean.TRUE){
                throw new BookNotReturnException("The book "+entry.getValue().getBook().getTitle()+" have not been restored \n User can't be removed");
            }
        }
        connectDB.requestInsertDB("DELETE FROM user WHERE id = '"+id+"'");
        connectDB.requestInsertDB("DELETE FROM history WHERE userId = '"+id+"'");
        users.remove(id);
    }

    /**
     * This method permits to change a User's information.
     * Information that should not be modified must be written as an empty character string "".
     *
     * @param id         The ID of the user whose information is to be updated.
     * @param lastName   The new last name of the user (or an empty string if not to be changed).
     * @param firstName  The new first name of the user (or an empty string if not to be changed).
     * @param phone      The new phone number of the user (or an empty string if not to be changed).
     * @throws Exception If an error occurs during the user information update process.
     */
    public void updateUser(int id,String lastName, String firstName, String phone) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        //To give the possibility of changing only one element, we assume that if an element must not be changed it is null
        if (!(users.get(id) == null)) {
            if (!(lastName == null)) {
                connectDB.requestInsertDB("UPDATE user SET lastName='"+lastName+"' WHERE id ='"+id+"'");
                users.get(id).setLastName(lastName);
            }
            if (!(firstName == null)) {
                connectDB.requestInsertDB("UPDATE user SET firstName='"+firstName+"' WHERE id ='"+id+"'");
                users.get(id).setFirstName(firstName);
            }
            if (!(phone == null)) {
                connectDB.requestInsertDB("UPDATE user SET phone='"+phone+"' WHERE id ='"+id+"'");
                users.get(id).setPhone(phone);
            }
        }
        else{
            throw new UserNotFoundException("User not found so can't update information");
        }
    }


    /**
     * This method permits to search for a User's ID using their last name, first name, or phone number.
     *
     * @param lastName  The last name of the user.
     * @param firstName The first name of the user.
     * @param phone     The phone number of the user.
     * @param bool      If true all parameter need to be exact to select one user,
     *                  otherwise if false only one parameter needed to find several user
     * @return The ID of the User if found, otherwise -1.
     */
    public List<User> searchUser(String lastName, String firstName, String phone,Boolean bool) throws UserNotFoundException {

        List<User> userId = new ArrayList<>();
        if(bool.equals(Boolean.TRUE)) {
            for(Map.Entry<Integer, User> entry : users.entrySet()){
                if (entry.getValue().getLastName().contains(lastName) && entry.getValue().getFirstName().contains(firstName) && entry.getValue().getPhone().contains(phone)){
                    userId.add(entry.getValue());
                }
            }

        } else if (bool.equals(Boolean.FALSE)) {
            for (Map.Entry<Integer, User> entry : users.entrySet()){
                if (entry.getValue().getLastName().contains(lastName) | entry.getValue().getFirstName().contains(firstName) | entry.getValue().getPhone().contains(phone)){
                    userId.add(entry.getValue());
                }
            }

        }
        if (!userId.isEmpty()){
            return userId;
        }
        else{
            throw new UserNotFoundException("No user found");
        }
    }

    /**
     * Checks if a user with the specified ID exists in the collection of users.
     *
     * @param id the ID of the user to check
     * @return boolean if a user with the specified ID exists
     */
    public boolean userExiste(int id){
        List<User> userId = new ArrayList<>();
        for (Map.Entry<Integer, User> entry : users.entrySet()){
            if (entry.getValue().getId()==id){
                return true;
            }
        }
        return false;
    }


    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
