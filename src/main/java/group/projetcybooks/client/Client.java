package group.projetcybooks.client;

import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String host = "localhost";
    private static final int port = 5543;

    public Client(){}

    public Book ClientISBN(String isbn) {
        Book book = null;
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "105 "+isbn;
            out.println(clientInput);
            book = new Book(in.readLine());
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public int ClientSendNewUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "106 "+ "0" + ";"+ lastName +";"+ firstName+";"+phone;
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int ClientUpdateUser(User user, String lastName, String firstName, String phone){
        try (Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "107 "+user.toString()+ " " + lastName+ " " + firstName+ " " +phone;
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public List<User> ClientSearchUser(String lastName, String firstName, String phone) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "108 " + lastName + " " + firstName + " " + phone;
            out.println(clientInput);

            String inputLine = in.readLine();
            String[] inputLineSplit = inputLine.split(" ");

            List<User> users = new ArrayList<>();
            String result = inputLineSplit[1];
            String[] resultSplit = result.split("/");

            for (String line : resultSplit) {
                users.add(new User(line));
            }
            out.println("150");
            return users;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return null;
        }
    }

    public int ClientRemoveUser(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "109 "+user.toString();
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int ClientAskReturnBookList(User user) {
        try (Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "110 "+user.toString();
            out.println(clientInput);
            System.out.println(Integer.parseInt(in.readLine()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -1;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return -1;
        }
        return 1;

    }

    public List<Borrow> ClientAskHistoryBookList(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "112 "+user.toString();
            out.println(clientInput);
            System.out.println(in.readLine());
            String inputLine = in.readLine();
            String[] inputLineSplit = inputLine.split(" ");

            List<Borrow> history = new ArrayList<>();
            String result = inputLineSplit[1];
            String[] resultSplit = result.split("/");

            for (String line : resultSplit) {
                history.add(new Borrow(line));
            }
            return history;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){

        System.out.println(new Client().ClientAskHistoryBookList(new User(1,"Hautecourt","Julien","0781287621")));
        // Ok System.out.println(new Client().ClientSendNewUser("Maestri","Adrien","0000000000"));
        // OK System.out.println(new Client().ClientUpdateUser(new User(2,"Marie","Hautecourt","0000000000"),"Hautecourt","Marie",null));
        // OK System.out.println(new Client().ClientSearchUser("Hautecourt",null,null));
        // OK System.out.println(new Client().ClientRemoveUser(new User(0,"Aubert","Michel","0686502589")));

        }
}
