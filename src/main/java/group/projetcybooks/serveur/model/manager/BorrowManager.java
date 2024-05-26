package group.projetcybooks.serveur.model.manager;

import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.Borrow;
import group.projetcybooks.serveur.model.TypeStatue;
import group.projetcybooks.serveur.model.User;
import group.projetcybooks.serveur.model.exception.*;


import java.time.LocalDate;
import java.util.*;


/**
 * This class permits to manage all borrow
 * Attributes :
 *     HashMap<Integer,Borrow> <b>borrowing</b>          List of current borrow ;
 *     HashMap<Integer,Borrow> <b>history</b>            List of old borrow;
 */
public class BorrowManager {

    private HashMap<Integer, Borrow> borrowing;
    private HashMap<Integer,Borrow> history;
    private HashMap<Book,Integer> nbborrowperbook;

    /**
     * Constructs a BorrowManager and populates the books, borrowing, and history maps based on the input request strings.
     *
     * @param requestBorrow  A string containing borrow data,
     *                       example "id;userId;borrowDate;returnDate;idBnf;restore".
     *
     * @param requestHistory A string containing history data,
     *                       example "id;userId;borrowDate;returnDate;idBnf;restore".
     *
     * @param userManager    An instance of UserManager that provides access to user data.
     * @throws IllegalArgumentException If any line in the input strings does not contain the expected number of values.
     * @throws Exception;
     */
    public BorrowManager(String requestBorrow,String requestHistory, UserManager userManager,BookManager books) throws Exception {
        this.nbborrowperbook= new HashMap<>();


        this.borrowing = new HashMap<>();
        if (!requestBorrow.isEmpty()) {
            //Split lines and values
            String[] borrowLines = requestBorrow.split("/");
            for (String line : borrowLines) {
                String[] values = line.split(";");

                if (values.length == 6) {
                    int id = Integer.parseInt(values[0]);

                    int userId = Integer.parseInt(values[1]);
                    User user = userManager.getUsers().get(userId);

                    String borrowDate = values[2];
                    String return_date = values[3];
                    Boolean restore = Boolean.parseBoolean(values[5]);

                    long idBnf = Long.parseLong(values[4]);
                    Book book = books.getBook(idBnf);
                    borrowing.put(id, new Borrow(id, user, borrowDate, return_date, book, restore));
                } else {
                    throw new IllegalArgumentException("The line doesn't have all the values wanted: " + line);
                }
            }

        }

        //On fait la même chose pour history
        this.history = new HashMap<>();

        if (!requestHistory.isEmpty()) {
            String[] historyLine = requestHistory.split("/");
            for (String line : historyLine) {
                String[] values = line.split(";");
                if (values.length == 6) {
                    int id = Integer.parseInt(values[0]);

                    int userId = Integer.parseInt(values[1]);
                    User user = userManager.getUsers().get(userId);

                    String borrowDate = values[2];
                    String return_date = values[3];
                    Boolean restore = Boolean.parseBoolean(values[5]);
                    long idBnf = Long.parseLong(values[4]);
                    Book book = books.getBook(idBnf);
                    history.put(id, new Borrow(id, user, borrowDate, return_date, book, restore));
                } else {
                    throw new IllegalArgumentException("The line doesn't have all the values wanted: " + line);
                }
            }
        }
    }
    /**
     * Checks for any late returns in the borrowing records.
     * If a borrowed item has a return date that is after the current date,
     * it indicates a late return.
     */
    public List<Borrow> lateReturn() throws NoLateReturnBook {

        List<Borrow> lateReturnBook = new ArrayList<>();
        for (Map.Entry<Integer, Borrow> entry : borrowing.entrySet()) {
            Borrow borrow = entry.getValue();
            if (!borrow.getReturnDate().isAfter(LocalDate.now())) {
                lateReturnBook.add(borrow);
            }
        }
        if (!(lateReturnBook.isEmpty()))
            return lateReturnBook;
        else {
            throw new NoLateReturnBook("No late return book");
        }
    }

    /**
     * This method permits to borrow a book for a specific user
     *
     * @param idBnf         The idBnf of the book to be borrowed.
     * @param userID       The ID of the user borrowing the book.
     * @param userManager  An instance of UserManager that provides access to user data.
     * @throws Exception   If an error occurs during the borrowing process.
     */
    public void borrowBook(long idBnf,int userID,UserManager userManager,BookManager books) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        Book book = books.getBook(idBnf);
        if(book.getStatue().equals(TypeStatue.FREE)){
            book.setStatue(TypeStatue.BORROW);
            User user = userManager.getUsers().get(userID);

            //Find new ID available in borrowing
            int newID=0;
            while(borrowing.containsKey(newID)){
                newID+=1;
            }

            Borrow borrow = new Borrow(newID,user,LocalDate.now().toString(),book,Boolean.FALSE);
            borrowing.put(newID,borrow);
            connectDB.requestInsertDB("INSERT into borrowing (id, userId,borrowDate,returnDate,bookidBnf,restored) VALUES ('"+borrow.getId()+"', '"+user.getId()+"', '"+borrow.getBorrowDate()+"', '"+borrow.getReturnDate()+"', '"+borrow.getBook().getidBnf()+"', '"+borrow.getRestore()+"');");
            connectDB.requestInsertDB("UPDATE book SET statue='BORROW' where idBnf='"+idBnf+"';");
        }
        else{
            throw new BookNotFreeException("Book not free.");
        }
    }

    /**
     * This method permits to return a book from a user.
     *
     * @param idBnf     The idBnf of the book being returned.
     * @param borrowId The ID of the borrow record.
     * @throws Exception If an error occurs during the return process.
     */
    public void returnBook(long idBnf,int borrowId,BookManager books) throws Exception {
        ConnectDB connectDB = new ConnectDB();

        books.getBook(idBnf).setStatue(TypeStatue.FREE);
        connectDB.requestInsertDB("UPDATE book SET statue='FREE' WHERE idBnf = '"+idBnf+"';");
        borrowing.get(borrowId).setRestore(Boolean.TRUE);
        borrowing.get(borrowId).setReturnDate(LocalDate.now());

        //Find a new ID available in history
        int newID=0;
        while(history.containsKey(newID)){
            newID+=1;
        }

        connectDB.requestInsertDB("INSERT into history (id, userId,borrowDate,returnDate,bookidBnf,restored) VALUES ('"+newID+"', '"+borrowing.get(borrowId).getUser().getId()+"', '"+borrowing.get(borrowId).getBorrowDate()+"', '"+borrowing.get(borrowId).getReturnDate()+"', '"+idBnf+"', '"+borrowing.get(borrowId).getRestore()+"');");
        history.put(newID,borrowing.get(borrowId));
        connectDB.requestInsertDB("DELETE FROM borrowing WHERE id='"+borrowId+"'");
        borrowing.remove(borrowId);
    }

    /**
     * This method return a list of the book with more borrow
     *
     * @return ArrayList
     */
    public ArrayList<Map.Entry<Book, Integer>> getPopularBook() {
        // Establish a Hashmap with key : Book , value : number of Borrow
        for (Integer key : history.keySet()) {
            Borrow borrow = history.get(key);
            Book book = borrow.getBook();
            // Add to the hashmap the number of borrow
            nbborrowperbook.put(book,nbborrowperbook.getOrDefault(book,0) + 1);
        }
        // Create a list and sort that list by value decreasing
        ArrayList<Map.Entry<Book, Integer>> list = new ArrayList<>(nbborrowperbook.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list;
    }

    /**
     * Searches for borrows by a user.
     *
     * @param user The user whose borrows are to be searched.
     * @return A list of borrows associated with the specified user.
     * @throws NoBorrowForUser If the user has no borrows.
     */
    public List<Borrow> searchBorrowByUser(User user) throws NoBorrowForUser {

        List<Borrow> borrows = new ArrayList<>();

        int id = user.getId();
        for (Map.Entry<Integer, Borrow> entry : borrowing.entrySet()) {
            Borrow borrow = entry.getValue();
            if (borrow.getUser().getId() == id) {
                borrows.add(borrow);
            }
        }
        if (!borrows.isEmpty()){
            return borrows;
        }
        else{
            throw new NoBorrowForUser("This user have no borrow");
        }
    }


    public List<Borrow> searchHistoryByUser(User user) throws NoHistoryForUser {

        List<Borrow> historys = new ArrayList<>();

        int id = user.getId();
        for (Map.Entry<Integer, Borrow> entry : history.entrySet()) {
            Borrow hist = entry.getValue();
            if (hist.getUser().getId() == id) {
                historys.add(hist);
            }
        }
        if (!historys.isEmpty()) {
            return historys;
        } else {
            throw new NoHistoryForUser("This user have no history");
        }
    }

    public List<Borrow> searchBorrowByBook(Book book) throws NoBorrowForBook {

        List<Borrow> borrows = new ArrayList<>();

        long idBnf = book.getidBnf();
        for (Map.Entry<Integer, Borrow> entry : borrowing.entrySet()) {
            Borrow borrow = entry.getValue();
            if (borrow.getBook().getidBnf() == idBnf) {
                borrows.add(borrow);
            }
        }
        if (!borrows.isEmpty()){
            return borrows;
        }
        else{
            throw new NoBorrowForBook("This book have no borrow");
        }
    }

    public HashMap<Integer, Borrow> getBorrowing() {
        return borrowing;
    }

    public HashMap<Integer, Borrow> getHistory() {
        return history;
    }

    public HashMap<Book, Integer> getNbborrowperbook() {
        return nbborrowperbook;
    }

}
