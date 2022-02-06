package sample.model.media;

import javafx.beans.property.*;

import java.sql.Date;

/**
 * The media class represents basic attributes all media can have. As of right now, this application only offers
 * the work with books, but in case of future expansion, extra sub classes of media can be created, representing
 * other media types (ex. films, games, etc.).
 *
 * Corresponding database table: "media"
 */
public abstract class Media {

    private int mediaID;
    private String title;
    private Date releaseDate;
    private boolean isAvailable;
    private boolean isBorrowed;

    /**
     * A media constructor used for newly added media.
     *
     * This constructor should be used when adding a new medium to the inventory/catalog, as a database entry is not
     * yet present and therefore mediaID cannot be added upon creation. The mediaID is automatically set to -1, which
     * will be ignored during INSERT in the database.
     *
     * @param title The media's title (ex. book title, game title, film title, etc.)
     * @param releaseDate The media's official release date
     * @param isAvailable The media's state of availability.
     * @param isBorrowed The media's state of being borrowed by a user
     */
    public Media(String title, Date releaseDate, boolean isAvailable, boolean isBorrowed) {

        this.mediaID = -1;
        this.title = title;
        this.releaseDate = releaseDate;
        this.isAvailable = isAvailable;
        this.isBorrowed = isBorrowed;

    }

    /**
     * A media constructor used for already existing media.
     *
     * This constructor should be used to fetch data from the database, as a mediaID is already represent and can
     * be added upon construction.
     *
     * @param mediaID The media's unique ID - represents the primary key in the database table
     * @param title The media's title (ex. book title, game title, film title, etc.)
     * @param releaseDate The media's official release date
     * @param isAvailable The media's state of availability.
     * @param isBorrowed The media's state of being borrowed by a user.
     */
    public Media(int mediaID, String title, Date releaseDate, boolean isAvailable, boolean isBorrowed) {

        this.mediaID = mediaID;
        this.title = title;
        this.releaseDate = releaseDate;
        this.isAvailable = isAvailable;
        this.isBorrowed = isBorrowed;

    }

    // Getter and setter methods for object attributes.

    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    /*
    The following methods describe getter methods to fetch attributes as SimpleProperties. These properties are used
    in the catalog table. Usually, you set a TableColumn value this way:

    "tableColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));"

    - whereas "attribute" represents an object's attribute. Since Book objects contain attributes of type ArrayList
    (such as authors, publishers and genres), the TableColumn would simply call the toString() method of ArrayList,
    resulting in a non pleasing output to the front end.

    To customize this output, the following methods are implemented, which allow to simply set the TableColumn values
    using the following syntax:

    "tableColumn.setCellValueFactory(cellData -> cellData.getValue().attributeProperty());"

    Note: main usage in CatalogController.initialize()
     */

    public IntegerProperty mediaIDProperty() {
        return new SimpleIntegerProperty(mediaID);
    }

    public StringProperty titleProperty() {
        return new SimpleStringProperty(title);
    }

    public ObjectProperty<Date> releaseDateProperty() {
        return new SimpleObjectProperty<>(releaseDate);
    }

    public StringProperty isAvailableProperty() {

        if(isAvailable) {
            return new SimpleStringProperty("Available");
        } else {
            return new SimpleStringProperty("Unavailable");
        }

    }

    public StringProperty isBorrowedProperty() {

        if(isBorrowed) {
            return new SimpleStringProperty("Currently borrowed");
        } else {
            return new SimpleStringProperty("Free to borrow");
        }

    }

}