package group.projetcybooks.serveur.model;

import group.projetcybooks.serveur.ConnectDB;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;


/**
 * This class permits to manage all borrow
 */
public class BorrowManager {

    private HashMap<Integer,Borrow> borrowing;
    private HashMap<Integer,Borrow> history;
    private HashMap<Book,Integer> nbborrowperbook;

    public BorrowManager(String requestBorrow,String requestHistory, UserManager userManager) throws Exception {
        this.nbborrowperbook= new HashMap<>();

        ConnectDB connectDB = new ConnectDB();
        this.borrowing = new HashMap<>();
        //Split lines and values
        String[] lines = requestBorrow.split("/");
        for(String line : lines){
            String[] values = line.split(";");

            if(values.length==5){
                int id = Integer.parseInt(values[0]);

                int userId = Integer.parseInt(values[1]);
                User user = userManager.getUsers().get(userId);

                String borrowDate = values[2];
                String return_date = values[3];

                //On recupère les informations du livre d'ID bookId pour crée un objet book pour le constructeur borow
                int bookId = Integer.parseInt(values[4]);
                String infoBook = connectDB.RequestSelectDB("SELECT * FROM book WHERE id='"+user.getId()+"'");
                String[] bookValues = infoBook.split(";");

                if(bookValues.length==8){
                    //TODO : Verifier ordre atribut avec la table d'Adrien
                    int ISBN = Integer.parseInt(bookValues[0]);

                    String stringStatue = bookValues[1];
                    TypeStatue statue;
                    if(stringStatue.equals("FREE")){
                        statue=TypeStatue.FREE;
                    }else{ statue=TypeStatue.BORROW;}

                    String editor = bookValues[2];
                    String title = bookValues[3];
                    String author = bookValues[4];
                    int year = Integer.parseInt(bookValues[5]);
                    String genre = bookValues[6];
                    Book book = new Book(ISBN,statue,editor,title,author,year,genre);
                    borrowing.put(id,new Borrow(id,user,borrowDate,return_date,book));
                }
                else{
                    System.out.println("The lines doesn't have all the values wanted");}
                    return;
            }
            else{
                System.out.println("The lines doesn't have all the values wanted");
                return;
            }
        }

        //On fait la même chose pour history
        this.history = new HashMap<>();
        String[] linesHistory = requestHistory.split("/");
        for(String line : linesHistory){
            String[] values = line.split(";");

            if(values.length==5){
                int id = Integer.parseInt(values[0]);

                int userId = Integer.parseInt(values[1]);
                User user = userManager.getUsers().get(userId);

                String borrowDate = values[2];
                String return_date = values[3];

                //On recupère les informations du livre d'ID bookId pour crée un objet book pour le constructeur borow
                int bookId = Integer.parseInt(values[4]);
                String infoBook = connectDB.RequestSelectDB("SELECT * FROM book WHERE id='"+user.getId()+"'");
                String[] bookValues = infoBook.split(";");

                if(bookValues.length==8){
                    //TODO : Verifier ordre atribut avec la table d'Adrien
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
                    history.put(id,new Borrow(id,user,borrowDate,return_date,book));
                }
                else{
                    System.out.println("The lines doesn't have all the values wanted");}
                return;
            }
            else{
                System.out.println("The lines doesn't have all the values wanted");
                return;
            }
        }
    }

    /**
     * This method permits to borrow a book for a specific user
     *
     * @param book;
     * @param user;
     * @throws ParseException Error of date format;
     */
    public void borrow_book(Book book,User user) throws ParseException {

        if(book.getStatue().equals(TypeStatue.FREE)){
            //Find new ID available in borrowing
            int newID=0;
            while(borrowing.containsKey(newID)){
                newID+=1;
            }

            Borrow borrow = new Borrow(newID,user,LocalDate.now().toString(),book);
            borrowing.put(newID,borrow);
            System.out.println(user.toString() + "have borrow" + book.toString());
        }
        else{System.out.println("This book is not free");}
    }

    /**
     * This method permits to return a book from a user
     * @param id of the borrow;
     */
    public void return_book(int id){
        borrowing.get(id).getBook().setStatue(TypeStatue.FREE);
        borrowing.get(id).setRestore(Boolean.TRUE);
        //Find a new ID available in history
        int newID=0;
        while(history.containsKey(newID)){
            newID+=1;
        }
        history.put(newID,borrowing.get(id));
        borrowing.remove(id);
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

        System.out.println("Most popular book : ");
        for (Map.Entry<Book, Integer> entry : list) {
            System.out.println(entry.getKey() + " - Borrow number : " + entry.getValue());
        }
    }

    public HashMap<Integer, Borrow> getBorrowing() {
        return borrowing;
    }
}
