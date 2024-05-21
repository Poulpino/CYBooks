package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.serveur.model.exception.*;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;

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
                            if (lastName.equals("null")){
                                lastName=null;
                            }if (firstName.equals("null")){
                                firstName=null;
                            }if (phone.equals("null")){
                                phone=null;
                            }
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
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < users.size(); i++) {
                                    result.append(users.get(i).toString());
                                    if (i < users.size() - 1) {
                                        result.append("/");
                                    }
                                }
                                out.println("201 "+result.toString());
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
                                out.println();
                            }
                            catch (NoBorrowForUser f){
                                out.println(f.getMessage());
                            }
                        }

                        //ClientReturnBook
                        case 111 ->{
                            try {
                                Borrow borrow = new Borrow(inputLineSplit[1]);
                                borrowManager.returnBook(borrow.getBook().getISBN(), borrow.getId());
                                out.println("201");
                            }catch (Exception e){
                                out.println("401 "+e.getMessage());
                            }
                        }
                        //ClientLateReturn
                        case  112 ->{
                            try {
                                List<Borrow> borrowList = borrowManager.lateReturn();
                                String output = "";
                                for (Borrow borrow : borrowList) {
                                    output+=borrow.toString()+"§";
                                }
                                output = output.substring(0, output.length() - 1);
                                out.println("201"+output);
                            }catch (Exception e){
                                out.println("401 "+e.getMessage());
                            }
                        }

                        //ClientAskHistoryBookList
                        case 113 ->{
                            User user = new User(inputLineSplit[1]);

                            try{
                                List<Borrow> history = borrowManager.searchHistoryByUser(user);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < history.size(); i++) {
                                    result.append(history.get(i).toString());
                                    if (i < history.size() - 1) {
                                        result.append("/");
                                    }
                                }
                                System.out.println(result.toString());
                                out.println("201 "+result.toString());
                            }
                            catch (NoHistoryForUser e){
                                out.println(e.getMessage());
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
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
