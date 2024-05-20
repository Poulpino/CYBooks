package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.serveur.model.exception.BookNotReturnException;
import group.projetcybooks.serveur.model.exception.NoBorrowForUser;
import group.projetcybooks.serveur.model.exception.NoLateReturnBook;
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
            try {
                List<Borrow> lateReturn = borrowManager.lateReturn();
            }catch (NoLateReturnBook e){
                System.out.println(e.getMessage());
            }

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

                //Pop up at start to tell there is late return
                try {
                    List<Borrow> lateReturn = borrowManager.lateReturn();
                    out.println("There is late return to check");
                }catch (NoLateReturnBook e){
                    System.out.println(e.getMessage());
                }

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Reçu du client: " + inputLine);
                    inputLineSplit = inputLine.split(" ");

                    switch (Integer.parseInt(inputLineSplit[0])) {

                        //ClientBorrowISBN //TODO peut etre gerer un isbn qui existe pas
                        case 105 -> {
                            try {
                                Book book = new ConnectApi(inputLineSplit[1]).getBook();
                                User user = new User(inputLineSplit[2]);
                                if (userManager.userExiste(user.getId())){
                                    borrowManager.addBook(book);
                                    borrowManager.borrowBook(book.getISBN(),user.getId(),userManager);
                                    out.println(book.toString());
                                    out.println("201");
                                }
                            }catch (Exception e){
                                out.println("400 "+e.getMessage());
                            }
                        }

                        //ClientSendNewUser
                        case 106 -> {
                            User user = new User(inputLineSplit[1]);
                            try{
                                userManager.searchUser(user.getLastName(),user.getFirstName(),user.getPhone(),Boolean.TRUE);
                                out.println("401 A user already exist");
                            }catch (UserNotFoundException e) {
                                try {
                                    userManager.addUser(user.getLastName(), user.getFirstName(), user.getPhone());
                                    out.println("201");
                                } catch (Exception f) {
                                    out.println("400 "+f.getMessage());
                                }
                            }
                        }

                        //ClientUpdateUser
                        case 107 -> {
                            User user = new User(inputLineSplit[1]);
                            String lastName=inputLineSplit[2];
                            String firstName=inputLineSplit[3];
                            String phone=inputLineSplit[4];
                            userManager.updateUser(user.getId(),lastName,firstName,phone);
                            out.println("201");
                        }

                        //ClientSearchUser
                        case 108 -> {
                            String lastName=inputLineSplit[1];
                            String firstName=inputLineSplit[2];
                            String phone=inputLineSplit[3];
                            try {
                                List<User> users = userManager.searchUser(lastName, firstName, phone, Boolean.FALSE);
                                out.println("201"); //TODO : voir quoi return (toString() ?)
                            }catch (UserNotFoundException e){
                                out.println(e.getMessage());
                            }
                        }

                        //ClientRemoveUser
                        case 109 ->{
                            User user = new User(inputLineSplit[1]);

                            try {
                                userManager.removeUser(user.getId(),borrowManager);
                                out.println("201");
                            }catch (BookNotReturnException e){
                                out.println(e.getMessage());
                            }
                            catch (Exception e){
                                out.println("400" + e.getMessage());
                            }
                        }

                        //ClientAskReturnBookList
                        case 110 ->{
                            User user = new User(inputLineSplit[1]);

                            try{
                                List<Borrow> borrows = borrowManager.searchBorrowByUser(user);
                                out.println(); //TODO : voir return
                            }
                            catch (UserNotFoundException e){
                                out.println(e.getMessage());
                            }
                            catch (NoBorrowForUser f){
                                out.println(f.getMessage());
                            }
                        }

                        //ClientReturnBook
                        case 111 ->{

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
