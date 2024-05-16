package group.projetcybooks.client;

import java.io.*;
import java.net.*;

public class Client {
    private static final String host = "localhost";
    private static final int port = 5543;

    public Client(){}

    public int ClientISBN(String isbn) {
        int output = -1;
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String userInput = "105 "+isbn;
            out.println(userInput);
            output = Integer.parseInt(in.readLine());
            out.println("150");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            return -2;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            e.printStackTrace();
            return -3;
        }
        return output;
    }

    public static void main(String[] args){
        System.out.println(new Client().ClientISBN("9782810627196"));
    }
}
