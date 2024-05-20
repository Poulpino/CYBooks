package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.serveur.model.exception.UserNotFoundException;


import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Do a server and wait client to connect
 */
public class Server {
    private static final int port = 5543;

    public static void main(String[] argv) {
        ServerSocket serverSocket = null;

        try {
            //start server
            serverSocket = new ServerSocket(Server.port);
            System.out.println("Serveur démarré sur le port " + Server.port);
            boolean run = true;

            //Init objets
            ConnectDB connectDB = new ConnectDB();
            UserManager userManager = new UserManager(connectDB.RequestSelectDB("SELECT * FROM user"));
            BorrowManager borrowManager = new BorrowManager(connectDB.RequestSelectDB("SELECT * FROM borrowing"),connectDB.RequestSelectDB("SELECT * FROM history"), connectDB.RequestSelectDB("SELECT * FROM book"), userManager);

            //look for late return
            borrowManager.lateReturn(); //TODO : Voir que faire de la méthode (affichage JavaFX)

            //waiting client connexion's
            while (run) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté.");

                //input and output of the client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //reading client line's
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
                            try{
                                userManager.searchUser(user.getLastName(),user.getFirstName(),user.getPhone(),Boolean.TRUE);
                                out.println("404 A user already exist");
                            }catch (UserNotFoundException e) {
                                try {
                                    userManager.addUser(user.getLastName(), user.getFirstName(), user.getPhone());
                                    out.println("201");
                                } catch (Exception f) {
                                    out.println(STR."400\{f.getMessage()}");
                                }
                            }
                        }

                        case 107 -> {
                            User user = new User(inputLineSplit[1]);
                            String lastName=inputLineSplit[2];
                            String firstName=inputLineSplit[3];
                            String phone=inputLineSplit[4];
                            userManager.updateUser(user.getId(),lastName,firstName,phone);
                            out.println("201");
                        }

                        case 108 ->{
                            User user = new User(inputLineSplit[1]);

                            try {
                                userManager.updateUser(user.getId(),inputLineSplit[2],inputLineSplit[3],inputLineSplit[4]);
                                out.println("201");
                            }catch (Exception e){
                                out.println(STR."400\{e.getMessage()}");
                            }
                        }
                        case 150 ->{
                            System.out.println("Closing Server");
                            run = false;
                        }
                    }
                }

                //close all
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
