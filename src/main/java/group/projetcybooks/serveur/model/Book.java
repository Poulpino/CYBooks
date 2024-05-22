package group.projetcybooks.serveur.model;

/**
 * Represents a book instance in the system.
 */
public class Book extends Artwork{
    private long idBnf;
    private TypeStatue statue;
    private String editor;

    public Book(long idBnf, TypeStatue statue, String editor, String title, String author, String year) {
        super(title,author,year);
        this.idBnf = idBnf;
        this.statue = statue;
        this.editor = editor;
    }
    public Book(String str){
        super();
        String[] strSplit = str.split(";");
        super.setTitle(strSplit[3]);
        super.setAuthor(strSplit[4]);
        super.setYear(strSplit[5]);
        this.idBnf = Long.parseLong(strSplit[0]);
        //TODO insere le bon statue
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

    public String afficher() {
        return "Book{" +
                "idBnf=" + idBnf +
                ", statue=" + statue +
                ", editor=" + editor +
                ", title=" + super.getTitle() +
                ", author=" + super.getAuthor() +
                ", year=" + super.getYear() +
                '}';
    }

    @Override
    public String toString(){
        return  idBnf +";"+ statue +";"+ editor +";"+ super.getTitle() +";"+ super.getAuthor() +";" + super.getYear();
    }
}
