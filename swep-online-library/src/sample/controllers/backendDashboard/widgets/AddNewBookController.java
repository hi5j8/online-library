package sample.controllers.backendDashboard.widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.controllers.QueryController;
import sample.model.media.Book;
import sample.model.media.BookGenre;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddNewBookController implements Initializable {

    @FXML
    private ListView<String> bookGenreList;
    @FXML
    private ToggleGroup bookAvailable;
    @FXML
    private RadioButton availableYes;
    @FXML
    private RadioButton availableNo;
    @FXML
    private TextField inputBookTitle;
    @FXML
    private DatePicker inputReleaseDate;
    @FXML
    private TextField inputAuthors;
    @FXML
    private TextField inputPublishers;

    private Alert failAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    private QueryController queryController = new QueryController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Fill list of available book genres

        bookGenreList.getItems().add("None");
        BookGenre[] bookGenres = BookGenre.class.getEnumConstants();
        for(BookGenre genre : bookGenres) {
            bookGenreList.getItems().add(genre.getName());
        }

        bookGenreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Set "available" to yes to prevent an error for no selection

        availableYes.setSelected(true);

    }

    /**
     * Takes the user input from the "add new book" form, creates and adds a new book to the library catalog.
     */
    public void addBookToInventory() {

        // Fetch book title

        if(inputBookTitle.getText().isEmpty()) {
            System.out.println("Could not add new book - missing book title!");
            failAlert.setContentText("Could not add new book - missing book title!");
            failAlert.showAndWait();
            return;
        }

        String title = inputBookTitle.getText();

        // Fetch release date

        if(inputReleaseDate.getValue() == null) {
            System.out.println("Could not add new book - no release date selected!");
            failAlert.setContentText("Could not add new book - no release date selected!");
            failAlert.showAndWait();
            return;
        }

        LocalDate localDate = inputReleaseDate.getValue();
        Date releaseDate = Date.valueOf(localDate);

        // Fetch state of availability

        if(bookAvailable.getSelectedToggle() == null) {
            System.out.println("Could not add new book - no availability state selected!");
            failAlert.setContentText("Could not add new book - no availability state selected!");
            failAlert.showAndWait();
            return;
        }

        boolean isAvailable = false;

        if(availableYes.isSelected()) {
            isAvailable = true;
        } else if(availableNo.isSelected()) {
            isAvailable = false;
        }

        // New books are automatically not borrowed

        boolean isBorrowed = false;

        // Fetch authors

        if(inputAuthors.getText().isEmpty()) {
            System.out.println("Could not add new book - no author(s) specified!");
            failAlert.setContentText("Could not add new book - no author(s) specified!");
            failAlert.showAndWait();
            return;
        }

        String fetchedAuthors = inputAuthors.getText();

        // Regex removes commas and spaces after commas

        ArrayList<String> authors = new ArrayList<>(Arrays.asList(fetchedAuthors.split("\\s*,\\s*")));

        // Fetch genres

        ArrayList<BookGenre> bookGenres = new ArrayList<>();

        for(String genre : bookGenreList.getSelectionModel().getSelectedItems()) {

            // If the genre "None" was in selection, remove all genres from list

            if(genre.equals("None")) {
                bookGenres.clear();
                bookGenres.add(BookGenre.UNDEFINED);
            } else {
                bookGenres.add(BookGenre.valueOf(genre.toUpperCase().replace(" ", "_")));
            }

        }

        // Fetch publishers

        if(inputPublishers.getText().isEmpty()) {
            System.out.println("Could not add new book - no publisher(s) specified!");
            failAlert.setContentText("Could not add new book - no publisher(s) specified!");
            failAlert.showAndWait();
            return;
        }

        String fetchedPublishers = inputPublishers.getText();
        ArrayList<String> publishers = new ArrayList<>(Arrays.asList(fetchedPublishers.split("\\s*,\\s*")));

        // Create new book and add it to database

        Book book = new Book(title, releaseDate, isAvailable, isBorrowed, bookGenres, authors, publishers);
        System.out.println("New book created! Inserting new book into database...");
        // queryController.connectToDataBase();

        boolean addedBookSuccessfully = queryController.addNewBook(book);

        // Display end result to frontend

        if(addedBookSuccessfully) {

            System.out.println("New book \""+book.getTitle()+"\" added to catalog!");
            infoAlert.setContentText("New book \""+book.getTitle()+"\" added to catalog!");
            infoAlert.showAndWait();

        } else {

            System.out.println("Could not add new book - database insertion failed!");
            failAlert.setContentText("Could not add new book - database insertion failed!");
            failAlert.showAndWait();

        }

    }

}
