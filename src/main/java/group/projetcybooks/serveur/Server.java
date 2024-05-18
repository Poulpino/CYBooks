package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;


import java.io.*;
import java.net.*;

/**
 * Do a server and wait client to connect
 */
public class Server {
    private static final int port = 5543;

    public static void main(String[] argv) {
        ServerSocket serverSocket = null;

        try {
            //démarage serveur
            serverSocket = new ServerSocket(Server.port);
            System.out.println("Serveur démarré sur le port " + Server.port);
            boolean run = true;

            //Init objets
            ConnectDB connectDB = new ConnectDB();
            UserManager userManager = new UserManager(connectDB.RequestSelectDB("SELECT * FROM user"));
            BorrowManager borrowManager = new BorrowManager(connectDB.RequestSelectDB("SELECT * FROM borrowing"),connectDB.RequestSelectDB("SELECT * FROM history"),userManager);

            //attente de la connection client
            while (run) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté.");

                //pour lire et ecrire au client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //on lit les lignes envoyer par le client
                String inputLine;
                String[] inputLineSplit;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Reçu du client: " + inputLine);
                    inputLineSplit = inputLine.split(" ");

                    switch (Integer.parseInt(inputLineSplit[0])) {
                        case 105 -> {
                            Book book = new ConnectApi(inputLineSplit[1]).getBook();
                            out.println(book.toString());
                            out.println("201");
                        }
                        case 106 -> {
                            User user = new User(inputLineSplit[1]);
                            int n  = userManager.searchUser(user.getLastName(),user.getFirstName(),user.getPhone());
                            if (n!=-1){
                                out.println("400 User already existe");
                            }
                            else {
                                userManager.addUser(user.getLastName(),user.getFirstName(),user.getPhone());
                                out.println("201");
                            }
                        }
                        case 150 ->{
                            System.out.println("Fermeture du serveur");
                            run = false;
                        }
                    }
                }

                //on ferme tout
                out.close();
                in.close();
                clientSocket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + port + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
