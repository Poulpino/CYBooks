package group.projetcybooks.serveur.model;

/**
 * Represents a book instance in the system.
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
    public Book(String str){
        super();
        String[] strSplit = str.split(";");
        super.setTitle(strSplit[3]);
        super.setAuthor(strSplit[4]);
        super.setYear(Integer.parseInt(strSplit[5]));
        super.setGenre(strSplit[6]);
        this.ISBN = Long.parseLong(strSplit[0]);
        //TODO insere le bon statue
        this.statue = TypeStatue.FREE;
        this.editor = strSplit[2];
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

    public String afficher() {
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

    @Override
    public String toString(){
        return  ISBN +";"+ statue +";"+ editor +";"+ super.getTitle() +";"+ super.getAuthor() +";" + super.getYear() +";"+ super.getYear();
    }
}
