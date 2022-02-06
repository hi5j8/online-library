package sample.model.media;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * A book is a type of media. The list attributes are handled in separate databases to create an 1:m relationship.
 *
 * Corresponding database table: "books"
 */
public class Book extends Media {

    private int bookID;
    private ArrayList<BookGenre> genres;
    private ArrayList<String> authors;
    private ArrayList<String> publishers;

    /**
     * A book constructor for newly created books.
     *
     * This constructor should be used when adding a new book to the inventory/catalog, as a database entry is not yet
     * present and therefore bookID (and mediaID from the parent class Media) cannot be added upon creation.
     * bookID and mediaID are automatically set to -1, which will be ignored during INSERT in the database.
     *
     * @param title (for super constructor) The media's title (ex. book title, game title, film title, etc.)
     * @param releaseDate (for super constructor) The media's official release date
     * @param isAvailable (for super constructor) The media's state of availability. Can be used to un-list media from catalog
     * @param isBorrowed (for super constructor) The media's state of being borrowed by a user
     * @param genres A list of the book's genres
     * @param authors A list of the book's authors
     * @param publishers A list of the book's publishers
     */
    public Book(String title, Date releaseDate, boolean isAvailable, boolean isBorrowed, ArrayList<BookGenre> genres,
                ArrayList<String> authors, ArrayList<String> publishers) {

        super(title, releaseDate, isAvailable, isBorrowed);
        this.bookID = -1;
        this.genres = genres;
        this.authors = authors;
        this.publishers = publishers;

    }

    /**
     * A book constructor for already existing books.
     *
     * This constructor should be used when retrieving data from the database, as database-dependent values have
     * been assigned and exist within the database and can be set properly.
     *
     * @param mediaID (for super constructor) The media's unique ID - represents the primary key in the database table
     * @param title (for super constructor) The media's title (ex. book title, game title, film title, etc.)
     * @param releaseDate (for super constructor) The media's official release date
     * @param isAvailable (for super constructor) The media's state of availability. Can be used to un-list media
     *                    from catalog
     * @param isBorrowed (for super constructor) The media's state of being borrowed by a user
     * @param bookID The book's unique ID - represents the primry key in the database table
     * @param genres A list of the book's genres
     * @param authors A list of the book's authors
     * @param publishers A list of the book's publishers
     */
    public Book(int mediaID, String title, Date releaseDate, boolean isAvailable, boolean isBorrowed, int bookID,
                ArrayList<BookGenre> genres, ArrayList<String> authors, ArrayList<String> publishers) {

        super(mediaID, title, releaseDate, isAvailable, isBorrowed);
        this.bookID = bookID;
        this.genres = genres;
        this.authors = authors;
        this.publishers = publishers;

    }

    // Getter and setter attributes for object attributes.

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public List<BookGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<BookGenre> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(ArrayList<String> publishers) {
        this.publishers = publishers;
    }

    public String toString() {
        return "Media ID: "+this.getMediaID()+"\n"+
                "Released: "+this.getReleaseDate().toString()+"\n"+
                "Available: "+this.isAvailable()+"\n"+
                "Borrowed: "+this.isBorrowed()+"\n"+
                "Book ID: "+this.bookID+"\n"+
                "Book title: "+this.getTitle()+"\n"+
                "Genres: "+this.genres.toString()+"\n"+
                "Authors: "+this.authors.toString()+"\n"+
                "Publishers: "+this.publishers.toString()+"\n";
    }

    /*
    The following methods describe getter methods to fetch attributes as SimpleProperties. These properties are
    used in the catalog table. Usually, you set a TableColumn value this way:

    "tableColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));"

    - whereas "attribute" represents an object's attribute. Since Book objects contain attributes of type ArrayList
    (such as authors, publishers and genres), the TableColumn would simply call the toString() method of ArrayList
    which results in a non pleasing output to the front end.

    To customize this output, the following methods are implemented, which allow to simply set the TableColumn values
    using the following syntax:

    "tableColumn.setCellValueFactory(cellData -> cellData.getValue().attributeProperty());"

    Note: main usage in CatalogController.initialize()
     */

    public IntegerProperty bookIDProperty() {
        return new SimpleIntegerProperty(bookID);
    }

    public StringProperty genresProperty() {

        StringBuilder genreString = new StringBuilder();

        for(BookGenre genre : genres) {
            genreString.append(genre.getName()).append(", ");
        }

        // substring removes extra comma at the end of the string

        return new SimpleStringProperty(genreString.substring(0, genreString.length()-2));

    }

    public StringProperty authorsProperty() {

        StringBuilder authorsString = new StringBuilder();

        for(String author : authors) {
            authorsString.append(author).append(", ");
        }

        // substring removes extra comma at the end of the string

        return new SimpleStringProperty(authorsString.substring(0, authorsString.length()-2));

    }

    public StringProperty publishersProperty() {

        StringBuilder publishersString = new StringBuilder();

        for(String publisher : publishers) {
            publishersString.append(publisher).append(", ");
        }

        // substring removes extra comma at the end of the string

        return new SimpleStringProperty(publishersString.substring(0, publishersString.length()-2));

    }

}