package group.projetcybooks.serveur.model;

/**
 * This class define books
 */
public class Book extends Artwork{
    private long ISBN;
    private TypeStatue statue;
    private String editor;

    public Book(long ISBN, TypeStatue statue, String editor, String title, String author, int year, String genre) {
        super(title,author,year,genre);
        this.ISBN = ISBN;
        this.statue = statue;
        this.editor = editor;
    }
    //Getter
    public long getISBN() {
        return ISBN;
    }
    public TypeStatue getStatue() {
        return statue;
    }
    public String getEditor() {
        return editor;
    }

    //Setter
    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }
    public void setStatue(TypeStatue statue) {
        this.statue = statue;
    }
    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN=" + ISBN +
                ", statue=" + statue +
                ", editor=" + editor +
                ", title=" + super.getTitle() +
                ", author=" + super.getAuthor() +
                ", year=" + super.getYear() +
                ", year=" + super.getYear() +
                '}';
    }
}
