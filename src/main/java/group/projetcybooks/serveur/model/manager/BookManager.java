package group.projetcybooks.serveur.model.manager;

import group.projetcybooks.serveur.ConnectApi;
import group.projetcybooks.serveur.ConnectDB;
import group.projetcybooks.serveur.model.Book;
import group.projetcybooks.serveur.model.TypeStatue;
import group.projetcybooks.serveur.model.exception.BookNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManager {
    private HashMap<Long, Book> books;

    public BookManager(String requestBook) {
        this.books = new HashMap<>();

        String[] booksLines = requestBook.split("/");
        for(String line : booksLines)
        {
            String[] bookValues = line.split(";");

            if(bookValues.length==6)
            {
                Long idBnf = Long.parseLong(bookValues[0]);
                String stringStatue = bookValues[1];
                TypeStatue statue;
                if(stringStatue.equals("FREE")){
                    statue=TypeStatue.FREE;
                }else{
                    statue=TypeStatue.BORROW;}

                String editor = bookValues[2];
                String title = bookValues[3];
                String author = bookValues[4];
                String year = bookValues[5];
                Book book = new Book(idBnf,statue,editor,title,author,year);
                books.put(idBnf,book);
            }
            else{
                throw new IllegalArgumentException("The line doesn't have all the values wanted: " + line);
            }
        }
    }

    /**
     * Adds a new book to the system.
     *
     * @param idBnf    The idBnf of the book.
     * @param statue  The status of the book (e.g., FREE or BORROW).
     * @param editor  The editor of the book.
     * @param title   The title of the book.
     * @param author  The author of the book.
     * @param year    The publication year of the book.
     * @throws Exception If there's an error whit DB connection.
     */
    //TODO Faire les verifs que le livre n'existe pas deja
    public void addBook(Long idBnf,TypeStatue statue,String editor,String title, String author,String year) throws Exception {
        ConnectDB connectDB = new ConnectDB();

        Book book = new Book(idBnf,statue,editor,title,author,year);
        if(!books.containsKey(book.getidBnf())) {
            books.put(idBnf, book);
            connectDB.requestInsertDB("INSERT into book (idBnf,statue,editor,title,author,year) VALUES ('" + book.getidBnf() + "', '" + book.getStatue() + "', '" + book.getEditor() + "', '" + book.getTitle() + "', '" + book.getAuthor() + "', '" + book.getYear() + "');");;
        }
    }

    /**
     * Adds a new book to the system.
     *
     * @param book Book you want to add
     * @throws Exception If there's an error whit DB connection.
     */
    public void addBook(Book book) throws Exception {
        addBook(book.getidBnf(), book.getStatue(), book.getEditor(), book.getTitle(), book.getAuthor(), book.getYear());
    }

    /**
     * Searches for a book by its title or author.
     *
     * @param idBnf   The idBnf of the book to search for.
     * @param title  The title of the book to search for.
     * @param author The author of the book to search for.
     * @return A list of books matching the search criteria.
     * @throws BookNotFoundException If no books are found matching the search criteria.
     */
    public List<Book> searchBook(String idBnf, String title, String author) throws BookNotFoundException {

        List<Book> booksFind = new ArrayList<>();

        for(Map.Entry<Long, Book> entry : books.entrySet()) {
            if (entry.getValue().getTitle().contains(idBnf) | entry.getValue().getTitle().contains(title) | entry.getValue().getAuthor().contains(author)) {
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

    public  List<Book> searchBoookApi(String idBnf, String title,String author) throws Exception {
        return new ConnectApi(idBnf,title,author).getBooks();
    }

    public Book getBook(long idBnf){
        return books.get(idBnf);
    }

    public HashMap<Long, Book> getBooks() {
        return books;
    }
}