package group.projetcybooks.serveur.model;

/**
 * Represents an artwork instance in the system.
 */
public class Artwork {
    private String title;
    private String author;
    private int year;
    private String genre;

    /**
     * Constructs a new Artwork object with the specified parameters.
     *
     * @param title  The title of the artwork.
     * @param author The author/creator of the artwork.
     * @param year   The year of creation/publication of the artwork.
     * @param genre  The genre/category of the artwork.
     */
    public Artwork(String title, String author, int year, String genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

    public Artwork(){}

    //Getter
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public int getYear() {
        return year;
    }
    public String getGenre() {
        return genre;
    }

    //Setter
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
