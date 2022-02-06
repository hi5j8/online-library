package sample.controllers.userDashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The UserDashboardController handles all events given by the user dashboard. It is constructed by itself and the
 * top nav bar component with its own controller. In his/her dashboard, the user can click the side menu to show one
 * of the listed widgets.
 */
public class UserDashboardController implements Initializable {

    // Widget container. Contains Pane-nodes whereas each Pane includes an own widget component. Depending on the
    // selected menu point, the respective widget will be shown and all others will be hidden.

    @FXML
    private Pane dashboardWidgetContainer;

    // All available widgets

    @FXML
    private Pane accountDetailsContainer;
    @FXML
    private Pane watchlistContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboardOverview();
    }

    /**
     * Switches the active widget to account details.
     */
    public void showAccountDetails() {

        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));
        accountDetailsContainer.setVisible(true);

    }

    /**
     * Switches the active widget back to the tiled overview.
     */
    public void showDashboardOverview() {

        // Show no widgets upon opening the overview
        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));

    }

    public void showWatchlist() {

        dashboardWidgetContainer.getChildren().forEach(node -> node.setVisible(false));
        watchlistContainer.setVisible(true);

    }

}
