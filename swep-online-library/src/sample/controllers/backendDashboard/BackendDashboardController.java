package sample.controllers.backendDashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The BackendDashboardController handles all actions given by the admin dashboard view.
 *
 * In the backend dashboard, a logged in administrator can manage users and media.
 */
public class BackendDashboardController implements Initializable {

    @FXML
    private Pane dashboardWidgetContainer;
    @FXML
    private Pane addNewMediaContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));

    }

    /**
     * Switches the active widget back to the tiled overview.
     */
    public void showDashboardOverview() {

        // Show no widgets upon opening the overview
        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));

    }

    public void showAddNewBookMenu() {

        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));
        addNewMediaContainer.setVisible(true);

    }

}
