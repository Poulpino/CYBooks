package group.projetcybooks.serveur.model;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a borrow instance in the system.
 */
public class Borrow {
    private int id;
    private User User;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Boolean restore;
    private Book book;

    /**
     * Constructs a new Borrow object, check if the format of borrowDate is "dd/MM/yyyy" and affect the returnDate 1 month after
     *
     * @param id              The ID of the borrow.
     * @param user            The user who borrowed the book.
     * @param borrowDate      The date the book was borrowed.
     * @param book            The book that was borrowed.
     * @param restore         A boolean value indicating whether the book has been restored.
     * @throws ParseException If there is an error in the date format.
     */
    public Borrow(int id, User user, String borrowDate, Book book,Boolean restore) throws ParseException {
        this.id = id;
        this.User = user;
        this.book = book;
        this.restore= restore;

        // Force the format of borrowDate to "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            this.borrowDate = LocalDate.parse(borrowDate, formatter);
        } catch (Exception e) {
            throw new ParseException("The format needs to be 'year-mouth-day'", 0);
        }

        // Force the returnDate 1 month after the borrow
        this.returnDate = this.borrowDate.plusMonths(1);
    }

    /**
     * Constructs a new Borrow object if returnDate already fixed, check if the format of borrowDate is "dd/MM/yyyy"
     *
     * @param id              The ID of the borrow.
     * @param user            The user who borrowed the book.
     * @param borrowDate      The date the book was borrowed.
     * @param returnDate      The date the book is expected to be returned.
     * @param book            The book that was borrowed.
     * @param restore         A boolean value indicating whether the book has been restored.
     * @throws ParseException If there is an error in the date format.
     */
    public Borrow(int id, User user, String borrowDate, String returnDate,Book book,Boolean restore) throws ParseException {
        this.id=id;
        this.User= user;
        this.book=book;
        this.restore=restore;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            this.borrowDate = LocalDate.parse(borrowDate, formatter);
        } catch (Exception e) {
            throw new ParseException("The format needs to be 'year-mouth-day'", 0);
        }
        try {
            this.returnDate = LocalDate.parse(returnDate, formatter);
        } catch (Exception e) {
            throw new ParseException("The format needs to be 'year-mouth-day'", 0);
        }
    }


    //Getter
    public int getId() {
        return id;
    }
    public User getUser() {
        return User;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public Book getBook() {
        return book;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public Boolean getRestore(){
        return restore;
    }

    //Setter
    public void setUser(User User) {
        this.User = User;
    }
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public void setId(int id) {
    this.id = id;
    }
    public void setRestore(Boolean restore){
        this.restore=restore;
    }
}
