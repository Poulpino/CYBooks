package group.projetcybooks.client;

import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.User;

import java.io.*;
import java.net.*;

public class Client {
    private static final String host = "localhost";
    private static final int port = 5543;

    public Client(){}

    public Book ClientISBN(String isbn) {
        Book book =null;
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

    public int ClientSendNewUser(User user) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "106 "+user.toString();
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

            String clientInput = "107 "+user.toString()+lastName+firstName+phone;
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

    public int ClientRemoveUser(User user){
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String clientInput = "108 "+user.toString();
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

    public int ClientAskReturnBookList(User user){
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

    public static void main(String[] args){
        System.out.println(new Client().ClientSendNewUser(new User(1,"Michel","Aubert", "0686502589")));
        System.out.println(new Client().ClientUpdateUser(new User(1,"Michel","Aubert", "0686502589"),"Hautecourt","Julien",""));
    }
}
