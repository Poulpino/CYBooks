package group.projetcybooks.serveur.model;

/**
 * Represents an artwork instance in the system.
 */
public class Artwork {
    private String title;
    private String author;
    private int year;

    /**
     * Constructs a new Artwork object with the specified parameters.
     *
     * @param title  The title of the artwork.
     * @param author The author/creator of the artwork.
     * @param year   The year of creation/publication of the artwork.
     */
    public Artwork(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
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

}
