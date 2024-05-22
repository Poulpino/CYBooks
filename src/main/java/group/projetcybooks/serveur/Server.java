package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.serveur.model.exception.*;
import group.projetcybooks.SceneController;

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


                        //clientSearchBook NOT WORKING
                        case 104 -> {
                            String isbn = inputLineSplit[1];
                            String title = inputLineSplit[2];
                            String editor = inputLineSplit[3];
                            if (isbn.equals("null")){
                                isbn=null;
                            }if (title.equals("null")){
                                title=null;
                            }if (editor.equals("null")){
                                editor=null;
                            }
                            /*TODO : Je veux que connectApi.getBook(isbn,title,editor) renvoie une List<Book> de
                              TODO tout les livres qui correspond à la description. Si l'un des argument est null alors
                              TODO il n'effectue pas la recherche par rapport à cette argument mais seulement les autres
                             */
                            //ConnectApi connectApi = new ConnectApi();
                            //String result = connectApi.getBook(isbn,title,editor)

                        }
                        //clientBorrowBook WORKING
                        case 105 -> {
                            try {
                                Book book = new Book(inputLineSplit[1]);
                                User user = new User(inputLineSplit[2]);
                                if (userManager.userExiste(user.getId())){
                                    borrowManager.addBook(book);
                                    borrowManager.borrowBook(book.getISBN(),user.getId(),userManager);
                                    out.println("201");
                                }
                            }catch (Exception e){
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }
                        }

                        //clientSendNewUser WORKING
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
                                    SceneController.showError("Server Error", "400: " + f.getMessage());
                                }
                            }
                        }

                        //clientUpdateUser WORKING
                        case 107 -> {
                            String[] inputLineSplit2 = inputLineSplit[1].split("/");
                            User user = new User(inputLineSplit2[0]);
                            String[] inputLineSplit3= inputLineSplit2[1].split(";");
                            String lastName=inputLineSplit3[0];
                            String firstName=inputLineSplit3[1];
                            String phone=inputLineSplit3[2];

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

                        //clientSearchUser
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
                                        result.append(" ");
                                    }
                                }
                                out.println("201 "+result.toString());
                            }catch (UserNotFoundException e){
                                SceneController.showError("Server Error", e.getMessage());
                            }
                        }

                        //clientRemoveUser WORKING
                        case 109 ->{
                            User user = new User(inputLineSplit[1]);

                            try {
                                userManager.removeUser(user.getId(),borrowManager);
                                out.println("201");
                            }catch (BookNotReturnException e){
                                SceneController.showError("Server Error", e.getMessage());
                            }
                            catch (Exception e){
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }
                        }

                        //clientAskReturnBookList WORKING
                        case 110 ->{
                            User user = new User(inputLineSplit[1]);
                            try{
                                List<Borrow> borrows = borrowManager.searchBorrowByUser(user);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < borrows.size(); i++) {
                                    result.append(borrows.get(i).toString());
                                    if (i < borrows.size() - 1) {
                                        result.append(" ");
                                    }
                                }
                                System.out.println("201 "+result.toString());
                                out.println("201 "+result.toString());
                            }
                            catch (NoBorrowForUser f){
                                SceneController.showError("Server Error", f.getMessage());
                            }
                        }

                        //clientReturnBook WORKING
                        case 111 ->{
                            try {
                                Borrow borrow = new Borrow(inputLineSplit[1]);
                                borrowManager.returnBook(borrow.getBook().getISBN(), borrow.getId());
                                out.println("201");
                            }catch (Exception e){
                                SceneController.showError("Server Error", "401: " + e.getMessage());
                            }
                        }

                        //clientAskLateReturn WORKING
                        case  112 ->{
                            try {
                                List<Borrow> borrowList = borrowManager.lateReturn();
                                String output = "";
                                for (Borrow borrow : borrowList) {
                                    output+=borrow.toString()+"§";
                                }
                                output = output.substring(0, output.length() - 1);
                                out.println("201 "+output);
                            }catch (Exception e){
                                SceneController.showError("Server Error", "401: " + e.getMessage());
                            }
                        }

                        //clientAskHistoryBookList
                        case 113 ->{
                            User user = new User(inputLineSplit[1]);
                            try{
                                List<Borrow> history = borrowManager.searchHistoryByUser(user);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < history.size(); i++) {
                                    result.append(history.get(i).toString());
                                    if (i < history.size() - 1) {
                                        result.append(" ");
                                    }
                                }
                                System.out.println("201 "+result.toString());
                                out.println("201 "+result.toString());
                            }
                            catch (NoHistoryForUser e){
                                SceneController.showError("Server Error", e.getMessage());
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
            SceneController.showError("Server Error", "Exception caught when trying to listen on port: " + port + " or listening for a connection" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
