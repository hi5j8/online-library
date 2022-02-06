package sample.controllers.userDashboard.widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import sample.model.account.UserSession;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Handles events of the widget that appears when the user clicks on "account details" in the user dashboard.
 */
public class AccountInfoController implements Initializable {

    @FXML
    private Pane dashboardContentContainer;

    @FXML
    private Label displayUserID;
    @FXML
    private Label displayRegisteredSince;
    @FXML
    private TextField displayFirstname;
    @FXML
    private TextField displayLastname;
    @FXML
    private TextField displayEmail;
    @FXML
    private TextField displayUsername;

    private UserSession userSession = UserSession.getInstance();
    private Alert failAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Fill displays with user information taken from usersession

        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(userSession.getUser().getRegisteredSince());

        displayUserID.setText(String.valueOf(userSession.getUser().getUserID()));
        displayRegisteredSince.setText(timeStamp);
        displayFirstname.setText(userSession.getUser().getFirstname());
        displayLastname.setText(userSession.getUser().getLastname());
        displayEmail.setText(userSession.getUser().getEmail());
        displayUsername.setText(userSession.getUser().getUsername());

    }

    /**
     * Updates the user's account information taken from the form's inputs.
     */
    public void updateUserInformation() {



    }

}
