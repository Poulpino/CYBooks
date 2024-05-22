package group.projetcybooks.serveur.model;

/**
 * Represents a book instance in the system.
 * Attributes:
 *      long <b>idBnf</b>           The unique identifier from BNF of the book.
 *      TypeStatue <b>statue</b>    The current status of the book (free, borrowed).
 *      String <b>editor</b>        The name of the book's editor.
 *      String <b>title</b>         The title of the book.
 *      String <b>author</b>        The author of the book.
 *      String <b>year</b>          The publication year of the book.
 */
public class Book{
    private long idBnf;
    private TypeStatue statue;
    private String editor;
    private String title;
    private String author;
    private String year;

    public Book(long idBnf, TypeStatue statue, String editor, String title, String author, String year) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.idBnf = idBnf;
        this.statue = statue;
        this.editor = editor;
    }
    public Book(String str){
        String[] strSplit = str.split(";");
        setTitle(strSplit[3]);
        setAuthor(strSplit[4]);
        setYear(strSplit[5]);
        this.idBnf = Long.parseLong(strSplit[0]);
        String statue = strSplit[1];
        if (statue.equals("FREE")){
            this.statue = TypeStatue.FREE;
        }
        else {
            this.statue = TypeStatue.BORROW;
        }
        this.editor = strSplit[2];
    }
    //Getter
    public long getidBnf() {
        return idBnf;
    }
    public TypeStatue getStatue() {
        return statue;
    }
    public String getEditor() {
        return editor;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getYear() {
        return year;
    }

    //Setter
    public void setidBnf(long idBnf) {
        this.idBnf = idBnf;
    }
    public void setStatue(TypeStatue statue) {
        this.statue = statue;
    }
    public void setEditor(String editor) {
        this.editor = editor;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String afficher() {
        return "Book{" +
                "idBnf=" + idBnf +
                ", statue=" + statue +
                ", editor=" + editor +
                ", title=" + title +
                ", author=" + author +
                ", year=" + year +
                '}';
    }

    @Override
    public String toString(){
        return  idBnf +";"+ statue +";"+ editor +";"+ title +";"+ author +";" + year;
    }
}
