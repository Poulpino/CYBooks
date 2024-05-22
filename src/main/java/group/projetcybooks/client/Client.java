package group.projetcybooks.client;

import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.*;
import group.projetcybooks.SceneController;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    private static final String host = "localhost";
    private static final int port = 5543;

    public Client(){}


    /**
     * Client provides information about the book they want, including ISBN, title, and author.
     * If you don't want to fill in a parameter, enter "null".
     *
     * @param isbn the ISBN of the book
     * @param title the title of the book
     * @param author the author of the book
     * @return The list of books that match the criteria
     */
    //TODO : A finir quand API ok
    public List<Book> clientSearchBook(String isbn, String title, String author){
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "104 " + isbn + " " + title + " " + author;
            out.println(clientInput);
            //book = new Book(in.readLine());
            //System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return null;

    }

    /**
     * The client provides an instance of a book and a user. Create a new book and borrow in the DataBase
     *
     * @param book the book to be borrowed
     * @param user the user who wants to borrow the book
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientBorrowBook(Book book,User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "105 "+book.toString() + " " + user.toString();
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * The client provides the details of a new user and the method creates a user on the server.
     *
     * @param lastName the last name of the user
     * @param firstName the first name of the user
     * @param phone the phone number of the user
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientSendNewUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "106 " + "0;" +lastName + ";" + firstName + ";" + phone;
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * The client provides an instance of the user they want to modify and the changes they want to make.
     * If you don't want to fill in a parameter, enter "null".
     *
     * @param user the user to be updated
     * @param lastName the new last name of the user
     * @param firstName the new first name of the user
     * @param phone the new phone number of the user
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientUpdateUser(User user, String lastName, String firstName, String phone){
        try (Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "107 "+user.toString()+"/"+lastName+ ";"+firstName+";"+phone+";";
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * The client provides search criteria. If you don't want to fill in a parameter, enter "null".
     *
     * @param lastName the last name of the user
     * @param firstName the first name of the user
     * @param phone the phone number of the user
     * @return List of users matching the criteria
     */
    public List<User> clientSearchUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "108 " + lastName + " " + firstName + " " + phone;
            out.println(clientInput);

            List<User> users = new ArrayList<>();
            String result = in.readLine();
            System.out.println(result);
            String[] resultSplit = result.split(" ");
            System.out.println(resultSplit[0]);
            for (int i = 1; i < resultSplit.length; i++) {
                String line = resultSplit[i];
                users.add(new User(line));
            }

            return users;

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The client provides a user instance to be removed.
     *
     * @param user the user to be removed
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientRemoveUser(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "109 "+user.toString();
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * The client provides a user instance return the list of books to be returned by a user.
     *
     * @param user the user who borrowed the books
     * @return
     */
    public List<Borrow> clientAskReturnBookList(User user) {
        try (Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "110 "+user.toString();
            out.println(clientInput);

            List<Borrow> borrows = new ArrayList<>();

            String result = in.readLine();
            String[] resultSplit = result.split(" ");
            System.out.println(resultSplit[0]);
            for (int i = 1; i < resultSplit.length; i++) {
                String line = resultSplit[i];
                borrows.add(new Borrow(line));
            }
            return borrows;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The client provides a borrow instance.
     *
     * @param borrow the borrow instance to be returned
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientReturnBook(Borrow borrow){
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "111 "+borrow.toString();
            out.println(clientInput);
            System.out.println(in.readLine());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * The client requests the list of late returns.
     *
     * @return List of late borrowings
     */
    public List<Borrow> clientAskLateReturn() {
        List<Borrow> borrowList;
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "112";
            out.println(clientInput);
            String s = in.readLine();
            String[] output = s.split(" ");
            System.out.println(output[0]);
            String[] output2 = output[1].split("§");

            borrowList = new ArrayList<>();
            Borrow borrow;
            for (String str : output2) {
                borrow = new Borrow(str);
                borrowList.add(borrow);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return borrowList;
    }

    /**
     * The client provides a user instance return list of borrow history.
     *
     * @param user the user whose borrowing history is requested
     * @return List of borrowings made by the user
     */
    public List<Borrow> clientAskHistoryBookList(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "113 "+user.toString();
            out.println(clientInput);

            List<Borrow> history = new ArrayList<>();
            String result = in.readLine();
            String[] resultSplit = result.split(" ");
            System.out.println(resultSplit[0]);

            for (int i = 1; i < resultSplit.length; i++) {
                String line = resultSplit[i];
                history.add(new Borrow(line));
            }

            return history;

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        // OK System.out.println(new Client().clientAskReturnBookList(new User(1,"Hautecourt","Julien","0781287621")));
        // OK System.out.println(new Client().clientAskLateReturn());
        /*
        List<Borrow> test = (new Client().clientAskReturnBookList(new User(1,"Hautecourt","Julien","0781287621")));
        Borrow test2 = test.getFirst();
        OK System.out.println(new Client().clientReturnBook(test));
        */
        System.out.println(new Client().clientAskHistoryBookList(new User(0,"Aubert","Michel","0686502589")));
        // OK System.out.println(new Client().clientSendNewUser("Hautecourt","Marie","0000000000"));
        // OK System.out.println(new Client().clientUpdateUser(new User(2,"Marie","Hautecourt","0000000000"),"Hautecourt","Marie",null));
        // OK System.out.println(new Client().clientSearchUser("Hautecourt",null,null));
        // OK System.out.println(new Client().clientRemoveUser(new User(0,"Aubert","Michel","0686502589")));
        //System.out.println(new Client().clientBorrowBook(new Book(0,TypeStatue.FREE,"test","test","test",2020),new User(1,"Hautecourt","Julien","0781287621")));
    }
}
