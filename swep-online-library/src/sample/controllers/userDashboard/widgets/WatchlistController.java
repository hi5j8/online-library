package sample.controllers.userDashboard.widgets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.controllers.QueryController;
import sample.model.account.UserSession;
import sample.model.media.Media;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {

    @FXML
    private TableView<Media> watchlist;
    @FXML
    private TableColumn<Media, String> colTitle;
    @FXML
    private TableColumn<Media, Date> colReleaseDate;
    @FXML
    private TableColumn<Media, String> colAvailable;
    @FXML
    private TableColumn<Media, String> colBorrowed;
    @FXML
    private TableColumn<Media, Void> colRemove;

    private QueryController queryController = new QueryController();
    private Alert failAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
    private UserSession userSession = UserSession.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bind column properties to object attributes

        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colReleaseDate.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        colAvailable.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());
        colBorrowed.setCellValueFactory(cellData -> cellData.getValue().isBorrowedProperty());
        colRemove.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

    }

    /**
     * Creates a remove button for every entry in the watch list.
     * Build after: https://riptutorial.com/javafx/example/27946/add-button-to-tableview
     *
     */
    private void addButtonToTable() {

        Callback<TableColumn<Media, Void>, TableCell<Media, Void>> cellFactory = new Callback<TableColumn<Media, Void>, TableCell<Media, Void>>() {
            @Override
            public TableCell<Media, Void> call(final TableColumn<Media, Void> param) {
                final TableCell<Media, Void> cell = new TableCell<Media, Void>() {

                    private final Button btn = new Button("Add");

                    {
                        btn.setOnAction((ActionEvent event) -> {

                            // Removes the row the remove button was triggered on

                            boolean mediaRemoved = queryController.removeFromWatchlist(
                                    userSession.getUser().getUserID(),
                                    getTableView().getSelectionModel().getSelectedIndex()
                            );

                            // If removal successful, remove item from view

                            if(mediaRemoved) {

                                watchlist.getItems().remove(watchlist.getSelectionModel().getSelectedIndex());
                                System.out.println(
                                        "Item \""+watchlist.getSelectionModel().getSelectedItem().getTitle()+"\"" +
                                                " removed from watch list!");

                            } else {

                                System.out.println("Could nto remove media from watch list - database removal failed!");
                                failAlert.setContentText("Could not remove media from watchlist!");
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

        colRemove.setCellFactory(cellFactory);

    }

}
