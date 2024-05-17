package group.projetcybooks.client;

import group.projetcybooks.serveur.model.Book;

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
            out.println("150");
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

    public static void main(String[] args){
        System.out.println(new Client().ClientISBN("9782810627196"));
    }
}
