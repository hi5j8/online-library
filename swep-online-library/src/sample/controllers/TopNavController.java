package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.model.account.PermissionLevel;
import sample.model.account.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class to map functionality to the nav bar.
 *
 * The top nav bar gets inserted as a component into most view elements and serves functionality that can be used
 * anywhere in the program, such as logging in/out and searching an item in the catalog.
 *
 **/
public class TopNavController implements Initializable {

    @FXML
    private Button buttonLogout;
    @FXML
    private Button buttonLogin;
    @FXML
    private HBox topNavContainer;
    @FXML
    private MenuButton menuAccount;
    @FXML
    private MenuItem menuAccountBackend;
    @FXML
    private Label welcomeUser;

    private UserSession userSession;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(UserSession.isEmpty()) {

            topNavContainer.getChildren().removeAll(buttonLogout, menuAccount);

            welcomeUser.setText("Welcome, Guest!");

        } else {

            topNavContainer.getChildren().remove(buttonLogin);
            userSession = UserSession.getInstance();

            if(userSession.getUser().getPermissionLevel() == PermissionLevel.ADMINISTRATOR) {

                welcomeUser.setText("Welcome, "+userSession.getUser().getUsername()+" (Admin)!");

            } else {

                menuAccount.getItems().remove(menuAccountBackend);
                welcomeUser.setText("Welcome, "+userSession.getUser().getUsername()+"!");

            }

        }


    }

    /**
     * Logs the user out of his session sends him back to the homepage. Also removes the account menu and exchanges
     * the logout Button with a login Button.
     *
     * @throws IOException for missing catalog view
     */
    public void logout() throws IOException {

        // End session

        userSession.destroySession();

        // Switch logout button with login

        topNavContainer.getChildren().remove(buttonLogout);
        topNavContainer.getChildren().add(buttonLogin);

        welcomeUser.setText("Welcome, Guest!");

        // Switch to main catalog view

        Stage currentStage = (Stage) topNavContainer.getScene().getWindow();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/sample/views/catalog/catalog.fxml"));
        currentStage.setScene(new Scene(loginRoot));

    }

    /**
     * Sends the user to the login screen.
     *
     * @throws IOException for missing login view
     */
    public void login() throws IOException {

        // Switch to login view to login

        Stage currentStage = (Stage) topNavContainer.getScene().getWindow();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/sample/views/login/login.fxml"));
        currentStage.setScene(new Scene(loginRoot));

    }

    /**
     * Opens the current user's dashboard.
     *
     * @throws IOException for missing user dashboard view
     */
    public void openDashboard() throws IOException {

        Stage currentStage = (Stage) topNavContainer.getScene().getWindow();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/sample/views/userDashboard/userDashboard.fxml"));
        currentStage.setScene(new Scene(loginRoot));

    }

    /**
     * Opens the backend dashboard for administrators.
     *
     * @throws IOException for missing backend dashboard component
     */
    public void openBackend() throws IOException {

        Stage currentStage = (Stage) topNavContainer.getScene().getWindow();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/sample/views/backendDashboard/backendDashboard.fxml"));
        currentStage.setScene(new Scene(loginRoot));

    }

}