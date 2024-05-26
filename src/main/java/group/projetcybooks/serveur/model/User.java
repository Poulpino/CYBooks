package group.projetcybooks.serveur.model;
/**
 * Represents a user instance in the system.
 * Attributes :
 *   int <b>id</b>               The unique identifier of the user.
 *   String <b>lastName</b>      The last name of the user.
 *   String <b>firstName</b>     The first name of the user.
 *   Sting <b>phone</b>          The phone number of the user.
 */
public class User {
    private final int id;
    private String lastName;
    private String firstName;
    private String phone;

    /**
     * Constructs a new User object with the specified parameters.
     *
     * @param id        The unique identifier of the user.
     * @param lastName  The last name of the user.
     * @param firstName The first name of the user.
     * @param phone     The phone number of the user.
     */
    public User(int id, String lastName, String firstName, String phone) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
    }
    /**
     * Constructs a new User object by parsing a string.
     *
     * @param str A string representing user data in the format "id;lastName;firstName;phone".
     */
    public User(String str){
        String [] strSplit = str.split(";");
        this.id = Integer.parseInt(strSplit[0]);
        this.lastName = strSplit[1];
        this.firstName = strSplit[2];
        this.phone = strSplit[3];
    }

    //Getter
    public int getId() {
        return id;
    }
    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getPhone() {
        return phone;
    }

    //Setter
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return id + ";" + lastName + ";" + firstName + ";" + phone;
    }
}
