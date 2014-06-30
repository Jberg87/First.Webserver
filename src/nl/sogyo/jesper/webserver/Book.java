package nl.sogyo.jesper.webserver;

/**
 * Created by jvdberg on 17/06/2014.
 */
public class Book {
    private String author, title, isbn;

    public Book(String author, String title, String isbn) {
        setAuthor(author);
        setTitle(title);
        setIsbn(isbn);
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }



}
