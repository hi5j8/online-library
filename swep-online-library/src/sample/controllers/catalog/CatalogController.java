package sample.controllers.catalog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import sample.controllers.QueryController;
import sample.model.account.UserSession;
import sample.model.media.Book;
import sample.model.media.Media;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * The catalog consists of multiple components that all work with their respective controllers.
 *
 * See "<fx:include>":
 * https://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html#include_elements
 */
public class CatalogController implements Initializable {

    @FXML
    private BorderPane catalogViewContainer;
    @FXML
    private TableView<Book> catalogBooks;
    @FXML
    private TableColumn<Book, String> colBookTitle;
    @FXML
    private TableColumn<Book, String> colBookAuthors;
    @FXML
    private TableColumn<Book, String> colBookPublishers;
    @FXML
    private TableColumn<Book, String> colBookGenres;
    @FXML
    private TableColumn<Book, String> colBookAvailable;
    @FXML
    private TableColumn<Book, Date> colBookReleaseDate;
    @FXML
    private TableColumn<Book, String> colBookBorrwed;

    // Watchlist buttons

    @FXML
    private TableColumn<Media, Void> colBookAddToWatchlist;

    private UserSession userSession;
    private QueryController queryController = new QueryController();
    private ObservableList<Book> books;

    private Alert failAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!UserSession.isEmpty()) {
            userSession = UserSession.getInstance();
        }

        // Bind book table column properties to object attributes

        colBookTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colBookAuthors.setCellValueFactory(cellData -> cellData.getValue().authorsProperty());
        colBookPublishers.setCellValueFactory(cellData -> cellData.getValue().publishersProperty());
        colBookGenres.setCellValueFactory(cellData -> cellData.getValue().genresProperty());
        colBookAvailable.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());
        colBookReleaseDate.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        colBookBorrwed.setCellValueFactory(cellData -> cellData.getValue().isBorrowedProperty());
        colBookAddToWatchlist.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        // Fill tables

        // Book table

        ArrayList<Book> fetchedBooks = queryController.getBooks();

        if(fetchedBooks.isEmpty()) {
            System.out.println("Books catalog seems to be empty right now...");
        } else {
            books = FXCollections.observableArrayList(fetchedBooks);
            catalogBooks.setItems(books);

            // Add "add" buttons to column

            addButtonToTable(colBookAddToWatchlist);
        }

        // Film table

        // Game table

    }


    /**
     * Creates an add button for every entry in the catalogs.
     * Build after: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
     *
     * @param column The target column to fill with buttons from a table
     */
    private void addButtonToTable(TableColumn<Media, Void> column) {

        Callback<TableColumn<Media, Void>, TableCell<Media, Void>> cellFactory = new Callback<TableColumn<Media, Void>, TableCell<Media, Void>>() {
            @Override
            public TableCell<Media, Void> call(final TableColumn<Media, Void> param) {
                final TableCell<Media, Void> cell = new TableCell<Media, Void>() {

                    private final Button btn = new Button("Add");

                    {
                        btn.setOnAction((ActionEvent event) -> {

                            // Add selected item to user's watch list

                            boolean addedToWatchlist = queryController.addToWatchlist(
                                    UserSession.getInstance().getUser().getUserID(),
                                    getTableView().getSelectionModel().getSelectedIndex()
                            );

                            if(addedToWatchlist) {

                                System.out.println(
                                        "Item \""+column.getTableView().getSelectionModel().getSelectedItem().getTitle()+
                                                "\" added to watch list!");

                            } else {

                                System.out.println("Failed to add item to watch list - database addition failed!");
                                failAlert.setContentText("Failed to add item to watch list!");
                                failAlert.showAndWait();

                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {

                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }

                    }
                };
                return cell;
            }
        };

        column.setCellFactory(cellFactory);

    }

}