package group.projetcybooks.client;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.client.scene.SceneController;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.*;

/**
 * Client class for handling server communication.
 * Attributes :
 *      String <b>host</b>    The hostname of the server to connect to.
 *      String <b>port</b>    The port number of the server to connect to.
 */
public class Client {
    private static final String host = "localhost";
    private static final int port = 5543;

    public Client() {
    }


    /**
     * Client provides information about the book they want, including ISBN, title, and author.
     * If you don't want to fill in a parameter, enter "null".
     *
     * @param idBnf  the ISBN of the book
     * @param title  the title of the book
     * @param author the author of the book
     * @return The list of books that match the criteria
     */
    public List<Book> clientAskListBook(String idBnf, String title, String author) {
        List<Book> booksList;
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "104#" + idBnf + "#" + title + "#" + author;
            out.println(clientInput);
            String s = in.readLine();
            String s2 = s.substring(4);
            String[] output = s2.split("/");

            booksList = new ArrayList<>();
            Book book;
            for (String str : output) {
                book = new Book(str);
                booksList.add(book);
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
        return booksList;
    }

    /**
     * The client provides an instance of a book and a user. Create a new book and borrow in the DataBase
     *
     * @param book the book to be borrowed
     * @param user the user who wants to borrow the book
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientBorrowBook(Book book, User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String clientInput = "105#" + book.toString() + "#" + user.toString();
            out.println(clientInput);
            String result = in.readLine();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknown host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * The client provides the details of a new user and the method creates a user on the server.
     *
     * @param lastName  the last name of the user
     * @param firstName the first name of the user
     * @param phone     the phone number of the user
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientSendNewUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "106#" + "0;" + lastName + ";" + firstName + ";" + phone;
            out.println(clientInput);
            String result = in.readLine();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * The client provides an instance of the user they want to modify and the changes they want to make.
     * If you don't want to fill in a parameter, enter "null".
     *
     * @param user      the user to be updated
     * @param lastName  the new last name of the user
     * @param firstName the new first name of the user
     * @param phone     the new phone number of the user
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientUpdateUser(User user, String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "107#" + user.toString() + "/" + lastName + ";" + firstName + ";" + phone + ";";
            out.println(clientInput);
            String result = in.readLine();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknown host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * The client provides search criteria. If you don't want to fill in a parameter, enter "null".
     *
     * @param lastName  the last name of the user
     * @param firstName the first name of the user
     * @param phone     the phone number of the user
     * @return List of users matching the criteria
     */
    public List<User> clientSearchUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "108#" + lastName + "#" + firstName + "#" + phone;
            out.println(clientInput);

            List<User> users = new ArrayList<>();
            String result = in.readLine();
            String[] resultSplit = result.split("#");

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

            String clientInput = "109#" + user.toString();
            out.println(clientInput);
            String result = in.readLine();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * The client provides a user instance return the list of books to be returned by a user.
     *
     * @param user the user who borrowed the books
     */
    public List<Borrow> clientAskReturnBookList(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "110#" + user.toString();
            out.println(clientInput);

            List<Borrow> borrows = new ArrayList<>();

            String result = in.readLine();
            String[] resultSplit = result.split("#");
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
            return null;
        }
    }

    /**
     * The client provides a borrow instance.
     *
     * @param borrow the borrow instance to be returned
     * @return 1 if the operation is successful, -1 otherwise
     */
    public int clientReturnBook(Borrow borrow) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "111#" + borrow.toString();
            out.println(clientInput);
            String result = in.readLine();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
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
        List<Borrow> borrowList = new ArrayList<>();
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "112";
            out.println(clientInput);
            String s = in.readLine();
            String[] output = s.split("#");
            if (output.length >= 2){ String[] output2 = output[1].split("§");
                Borrow borrow;
                for (String str : output2) {
                    borrow = new Borrow(str);
                    borrowList.add(borrow);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
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

            String clientInput = "113#" + user.toString();
            out.println(clientInput);

            List<Borrow> history = new ArrayList<>();
            String result = in.readLine();
            String[] resultSplit = result.split("#");

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
            return null;
        }
    }

    /**
     * Requests the server for the most popular books and their borrow counts.
     *
     * @return A HashMap containing Book objects as keys and their borrow counts as values sorted by borrow counts,
     * or null if there was a problem with the connection to the server.
     */
    public HashMap<Book, Integer> clientAskPopularBook() {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "103#";
            out.println(clientInput);

            HashMap<Book, Integer> popularBooks = new HashMap<>();

            String result = in.readLine().replace("[", "").replace("]", "").replace(", ", "§").replace("201#", "");

            String[] resultSplit = result.split("§");
            for (String line : resultSplit) {
                String[] resultSplit2 = line.split("=");
                Book book = new Book(resultSplit2[0]);
                popularBooks.put(book, Integer.parseInt(resultSplit2[1]));
            }
            return popularBooks;

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            SceneController.showError("Client Error", "Unknow host problem: " + host + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            SceneController.showError("Client Error", "Couldn't get I/O for the connection to: " + host + e.getMessage());
            return null;
        }
    }
}