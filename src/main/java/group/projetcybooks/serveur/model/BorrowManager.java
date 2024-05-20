package group.projetcybooks.serveur.model;

import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.exception.*;


import java.time.LocalDate;
import java.util.*;


/**
 * This class permits to manage all borrow
 */
public class BorrowManager {

    private HashMap<Integer,Borrow> borrowing;
    private HashMap<Integer,Borrow> history;
    private HashMap<Book,Integer> nbborrowperbook;
    private HashMap<Integer,Book> books;

    /**
     * Constructs a BorrowManager and populates the books, borrowing, and history maps based on the input request strings.
     *
     * @param requestBorrow  A string containing borrow data,
     *                       example "id;userId;borrowDate;returnDate;ISBN;restore".
     *
     * @param requestHistory A string containing history data,
     *                       example "id;userId;borrowDate;returnDate;ISBN;restore".
     *
     * @param requestBook    A string containing book data,
     *                       example "ISBN;statue;editor;title;author;year;genre".
     *
     * @param userManager    An instance of UserManager that provides access to user data.
     * @throws IllegalArgumentException If any line in the input strings does not contain the expected number of values.
     * @throws Exception;
     */
    public BorrowManager(String requestBorrow,String requestHistory,String requestBook, UserManager userManager) throws Exception {
        this.nbborrowperbook= new HashMap<>();

        this.books = new HashMap<>();

        if (!requestBook.isEmpty()) {
            String[] booksLines = requestBook.split("/");
            for(String line : booksLines)
            {
                String[] bookValues = line.split(";");

                if(bookValues.length==7)
                {
                    int ISBN = Integer.parseInt(bookValues[0]);
                    String stringStatue = bookValues[1];
                    TypeStatue statue;
                    if(stringStatue.equals("FREE")){
                        statue=TypeStatue.FREE;
                    }else{
                        statue=TypeStatue.BORROW;}

                    String editor = bookValues[2];
                    String title = bookValues[3];
                    String author = bookValues[4];
                    int year = Integer.parseInt(bookValues[5]);
                    String genre = bookValues[6];
                    Book book = new Book(ISBN,statue,editor,title,author,year,genre);
                    books.put(ISBN,book);
                }
                else{
                    throw new IllegalArgumentException("The line doesn't have all the values wanted: " + line);
                }
            }
        }


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

                    int ISBN = Integer.parseInt(values[4]);
                    Book book = books.get(ISBN);
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
                    int ISBN = Integer.parseInt(values[4]);
                    Book book = books.get(ISBN);
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
            if (borrow.getReturnDate().isAfter(LocalDate.now())) {
                lateReturnBook.add(borrow);
            }
        }
        if (!lateReturnBook.isEmpty())
            return lateReturnBook;
        else {
            throw new NoLateReturnBook("No late return book");
        }
    }

    /**
     * Adds a new book to the system.
     *
     * @param ISBN    The ISBN of the book.
     * @param statue  The status of the book (e.g., FREE or BORROW).
     * @param editor  The editor of the book.
     * @param title   The title of the book.
     * @param author  The author of the book.
     * @param year    The publication year of the book.
     * @param genre   The genre of the book.
     * @throws Exception If there's an error whit DB connection.
     */
    public void addBook(int ISBN,TypeStatue statue,String editor,String title, String author,int year,String genre) throws Exception {
        ConnectDB connectDB = new ConnectDB();

        Book book = new Book(ISBN,statue,editor,title,author,year,genre);
        books.put(ISBN, book);
        connectDB.requestInsertDB(STR."INSERT into book (isbn,statue,editor,title,author,year,genre) VALUES ('\{book.getISBN()}', '\{book.getStatue()}', '\{book.getEditor()}', '\{book.getTitle()}', '\{book.getAuthor()}', '\{book.getYear()}', '\{book.getGenre()}'}');");
        System.out.println(STR."\{books.get(ISBN).toString()}added");

    }
    /**
     * This method permits to borrow a book for a specific user
     *
     * @param ISBN         The ISBN of the book to be borrowed.
     * @param userID       The ID of the user borrowing the book.
     * @param userManager  An instance of UserManager that provides access to user data.
     * @throws Exception   If an error occurs during the borrowing process.
     */
    public void borrowBook(int ISBN,int userID,UserManager userManager) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        Book book = books.get(ISBN);

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
            connectDB.requestInsertDB(STR."INSERT into borrowing (id, userId,borrowDate,returnDate,bookIsbn,restored) VALUES ('\{borrow.getId()}', '\{user.getId()}', '\{borrow.getBorrowDate()}', '\{borrow.getReturnDate()}', '\{borrow.getBook().getISBN()}', '\{borrow.getRestore()}');");
            connectDB.requestInsertDB(STR."UPDATE book SET statue='BORROW' where isbn='\{ISBN}';");
            System.out.println(STR."\{user.toString()}have borrow\{book.toString()}");
        }
        else{
            throw new BookNotFreeException("Book not free.");
        }
    }

    /**
     * This method permits to return a book from a user.
     *
     * @param ISBN     The ISBN of the book being returned.
     * @param borrowId The ID of the borrow record.
     * @throws Exception If an error occurs during the return process.
     */
    public void returnBook(int ISBN,int borrowId) throws Exception {
        ConnectDB connectDB = new ConnectDB();
        books.get(ISBN).setStatue(TypeStatue.FREE);
        connectDB.requestInsertDB(STR."UPDATE book SET statue='FREE' WHERE isbn='\{ISBN}'");

        borrowing.get(borrowId).setRestore(Boolean.TRUE);
        borrowing.get(borrowId).setReturnDate(LocalDate.now());

        //Find a new ID available in history
        int newID=0;
        while(history.containsKey(newID)){
            newID+=1;
        }

        connectDB.requestInsertDB(STR."INSERT into history (id, userId,borrowDate,returnDate,bookIsbn,restored) VALUES ('\{newID}', '\{borrowing.get(borrowId).getUser().getId()}', '\{borrowing.get(borrowId).getBorrowDate()}', '\{borrowing.get(borrowId).getReturnDate()}', '\{borrowing.get(borrowId).getBook().getISBN()}', '\{borrowing.get(borrowId).getRestore()}');");
        history.put(newID,borrowing.get(ISBN));
        connectDB.requestInsertDB(STR."DELETE FROM borrowing WHERE id='\{borrowId}'");
        borrowing.remove(ISBN);
        System.out.println("Book restored");
    }

    /**
     * This method return a list of the book with more borrow
     */
    public void getPopularBook() {
        // Establish a Hashmap with key : Book , value : number of Borrow
        for (Integer key : history.keySet()) {
            Borrow borrow = history.get(key);
            Book book = borrow.getBook();
            // Add to the hashmap the number of borrow
            nbborrowperbook.put(book, nbborrowperbook.getOrDefault(book, 0) + 1);
        }
        // Create a list and sort that list by value decreasing
        ArrayList<Map.Entry<Book, Integer>> list = new ArrayList<>(nbborrowperbook.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        //TODO : voir format a rendre javaFX
        System.out.println("Most popular book : ");
        for (Map.Entry<Book, Integer> entry : list) {
            System.out.println(entry.getKey() + " - Borrow number : " + entry.getValue());
        }
    }
    /**
     * Searches for borrows by a user.
     *
     * @param user The user whose borrows are to be searched.
     * @return A list of borrows associated with the specified user.
     * @throws UserNotFoundException If the user is not found in the system.
     * @throws NoBorrowForUser If the user has no borrows.
     */
    public List<Borrow> searchBorrowByUser(User user) throws UserNotFoundException, NoBorrowForUser {

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

    /**
     * Searches for a book by its title or author.
     *
     * @param ISBN   The ISBN of the book to search for.
     * @param title  The title of the book to search for.
     * @param author The author of the book to search for.
     * @return A list of books matching the search criteria.
     * @throws BookNotFoundException If no books are found matching the search criteria.
     */
    public List<Book> searchBook(String ISBN, String title, String author) throws BookNotFoundException {

        List<Book> booksFind = new ArrayList<>();

        for(Map.Entry<Integer, Book> entry : books.entrySet()) {
            if (entry.getValue().getTitle().equals(ISBN) | entry.getValue().getTitle().equals(title) | entry.getValue().getAuthor().equals(author)) {
                booksFind.add(entry.getValue());
            }
        }
        if (!booksFind.isEmpty()){
            return booksFind;
        }
        else{
            throw new BookNotFoundException("No book find with given details.");
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

    public HashMap<Integer, Book> getBooks() {
        return books;
    }
}
