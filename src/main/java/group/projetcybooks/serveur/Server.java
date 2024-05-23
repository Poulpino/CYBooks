package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.*;
import group.projetcybooks.serveur.model.exception.*;
import group.projetcybooks.SceneController;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class sets up a server and waits for clients to connect.
 * Attribute:
 *      int <b>port</b> The port on which the server listens.
 */
public class Server {
    private static final int port = 5543;

    public static void main(String[] argv) {
        ServerSocket serverSocket = null;

        try {
            //start server
            serverSocket = new ServerSocket(Server.port);
            boolean run = true;

            //start wamp for DB
            try {
                String wampPath = "C:/wamp64/wampmanager.exe";
                //Check if Wamp already start
                boolean isMySqlRunning = isPortInUse();
                if(!isMySqlRunning){
                    Runtime.getRuntime().exec(wampPath);
                    boolean isWampRunning = waitForWampToStart("http://localhost");

                    if (!isWampRunning) {
                        throw new RuntimeException("Wamp didn't start");
                    }
                }
            } catch (Exception e) {
                SceneController.showError("Server Error", "400: " + e.getMessage());
            }

            //Init objets
            ConnectDB connectDB = new ConnectDB();
            UserManager userManager = new UserManager(connectDB.RequestSelectDB("SELECT * FROM user"));
            BookManager bookManager = new BookManager(connectDB.RequestSelectDB("SELECT * FROM book"));
            BorrowManager borrowManager = new BorrowManager(connectDB.RequestSelectDB("SELECT * FROM borrowing"), connectDB.RequestSelectDB("SELECT * FROM history"), userManager, bookManager);

            //waiting client connexion's
            while (run) {
                Socket clientSocket = serverSocket.accept();

                //input and output of the client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //reading client line's
                String inputLine;
                String[] inputLineSplit;

                while ((inputLine = in.readLine()) != null) {
                    inputLineSplit = inputLine.split(" ");

                    switch (Integer.parseInt(inputLineSplit[0])) {


                        case 103 -> {
                            try {
                                ArrayList<Map.Entry<Book, Integer>> popularBooks = borrowManager.getPopularBook();
                                out.println("201 " + popularBooks.toString());

                            } catch (Exception e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }


                        }
                        //clientSearchBook NOT WORKING
                        case 104 -> {
                            String idBnf = inputLineSplit[1];
                            String title = inputLineSplit[2];
                            String author = inputLineSplit[3];
                            if (idBnf.equals("null")) {
                                idBnf = null;
                            }
                            if (title.equals("null")) {
                                title = null;
                            }
                            if (author.equals("null")) {
                                author = null;
                            }
                            /*TODO : Je veux que connectApi.getBook(idBnf,title,editor) renvoie une List<Book> de
                              TODO tout les livres qui correspond à la description. Si l'un des argument est null alors
                              TODO il n'effectue pas la recherche par rapport à cette argument mais seulement les autres
                             */
                            try {
                                List<Book> bookList = new ConnectApi(idBnf, title, author).getBooks();
                                String output = "";
                                for (Book book : bookList) {
                                    output += book.toString() + "/";
                                }
                                output = output.substring(0, output.length() - 1);
                                out.println("201 " + output);
                            } catch (Exception e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }
                        }
                        //clientBorrowBook WORKING
                        case 105 -> {
                            try {
                                Book book = new Book(inputLineSplit[1]);
                                User user = new User(inputLineSplit[2]);
                                if (userManager.userExiste(user.getId())) {
                                    bookManager.addBook(book);
                                    borrowManager.borrowBook(book.getidBnf(), user.getId(), userManager, bookManager);
                                    out.println("201");
                                }
                            } catch (Exception e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }
                        }

                        //clientSendNewUser WORKING
                        case 106 -> {
                            try {
                                String[] userSplit = inputLineSplit[1].split(";");
                                String lastName = userSplit[1];
                                String firsName = userSplit[2];
                                String phone = userSplit[3];
                                if (lastName.isEmpty() || firsName.isEmpty() || phone.isEmpty()) {
                                    throw new IllegalArgumentException("Fill in all 3 boxes");
                                }
                                User user = new User(0, lastName, firsName, phone);
                                userManager.searchUser(user.getLastName(), user.getFirstName(), user.getPhone(), Boolean.TRUE);
                                SceneController.showError("Server Error", "401 : User already exist");
                            } catch (IllegalArgumentException e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            } catch (UserNotFoundException e) {
                                try {
                                    User user = new User(inputLineSplit[1]);
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
                            String[] inputLineSplit3 = inputLineSplit2[1].split(";");
                            String lastName = inputLineSplit3[0];
                            String firstName = inputLineSplit3[1];
                            String phone = inputLineSplit3[2];

                            if (lastName.equals("null")) {
                                lastName = null;
                            }
                            if (firstName.equals("null")) {
                                firstName = null;
                            }
                            if (phone.equals("null")) {
                                phone = null;
                            }
                            userManager.updateUser(user.getId(), lastName, firstName, phone);
                            out.println("201");
                        }

                        //clientSearchUser WORKING
                        case 108 -> {
                            String lastName = inputLineSplit[1];
                            String firstName = inputLineSplit[2];
                            String phone = inputLineSplit[3];

                            try {
                                List<User> users = userManager.searchUser(lastName, firstName, phone, Boolean.FALSE);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < users.size(); i++) {
                                    result.append(users.get(i).toString());
                                    if (i < users.size() - 1) {
                                        result.append(" ");
                                    }
                                }
                                out.println("201 " + result.toString());
                            } catch (UserNotFoundException e) {
                                SceneController.showError("Server Error", e.getMessage());
                            }
                        }

                        //clientRemoveUser WORKING
                        case 109 -> {
                            User user = new User(inputLineSplit[1]);

                            try {
                                userManager.removeUser(user.getId(), borrowManager);
                                out.println("201");
                            } catch (BookNotReturnException e) {
                                SceneController.showError("Server Error", e.getMessage());
                            } catch (Exception e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                            }
                        }

                        //clientAskReturnBookList WORKING
                        case 110 -> {
                            User user = new User(inputLineSplit[1]);
                            try {
                                List<Borrow> borrows = borrowManager.searchBorrowByUser(user);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < borrows.size(); i++) {
                                    result.append(borrows.get(i).toString());
                                    if (i < borrows.size() - 1) {
                                        result.append(" ");
                                    }
                                }
                                out.println("201 " + result.toString());
                            } catch (NoBorrowForUser f) {
                                SceneController.showError("Server Error", f.getMessage());
                            }
                        }

                        //clientReturnBook WORKING
                        case 111 -> {
                            try {
                                Borrow borrow = new Borrow(inputLineSplit[1]);
                                borrowManager.returnBook(borrow.getBook().getidBnf(), borrow.getId(), bookManager);
                                out.println("201");
                            } catch (Exception e) {
                                SceneController.showError("Server Error", "400: " + e.getMessage());
                                //SceneController.showError("Server Error", "401: " + e.getMessage());
                            }
                        }

                        //clientAskLateReturn WORKING
                        case 112 -> {
                            try {
                                List<Borrow> borrowList = borrowManager.lateReturn();
                                String output = "";
                                for (Borrow borrow : borrowList) {
                                    output += borrow.toString() + "§";
                                }
                                output = output.substring(0, output.length() - 1);
                                out.println("201 " + output);
                            } catch (Exception e) {
                                SceneController.showError("Server Error", "401: " + e.getMessage());
                            }
                        }

                        //clientAskHistoryBookList
                        case 113 -> {
                            User user = new User(inputLineSplit[1]);
                            try {
                                List<Borrow> history = borrowManager.searchHistoryByUser(user);
                                StringBuilder result = new StringBuilder();
                                for (int i = 0; i < history.size(); i++) {
                                    result.append(history.get(i).toString());
                                    if (i < history.size() - 1) {
                                        result.append(" ");
                                    }
                                }
                                out.println("201 " + result.toString());
                            } catch (NoHistoryForUser e) {
                                SceneController.showError("Server Error", e.getMessage());
                            }
                        }
                        case 150 -> {
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

    private static boolean waitForWampToStart(String url) {
        int maxRetries = 30;
        int retryInterval = 10000;

        for (int i = 0; i < maxRetries; i++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return true;
                }
            } catch (IOException e) {
                //Retry
            }

            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
        private static boolean isPortInUse() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 3308), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
