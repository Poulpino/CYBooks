package group.projetcybooks.serveur.model;

public class Artwork {
    private String title;
    private String author;
    private int year;
    private String genre;

    //Constructor
    public Artwork(String title, String author, int year, String genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

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
