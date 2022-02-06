package sample.controllers.login;

import externalLibraries.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.controllers.QueryController;
import sample.model.account.UserSession;
import sample.model.account.user.User;
import sample.model.database.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The LoginController manages events given by the login form.
 *
 * The login form serves as a main entry point to the program. A user can log into his account with his credentials,
 * register a new account, or just continue to the library's catalog as a guest.
 */
public class LoginController implements Initializable {

    @FXML
    private VBox loginFormContainer;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private Label connectionStatus;

    private QueryController queryController = new QueryController();
    private UserSession userSession;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    private Alert failAlert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(databaseConnection.getConnection() == null) {
            connectionStatus.setText("Database connection status: Disconnected");
        } else {
            connectionStatus.setText("Database connection status: Connected");
        }

    }

    /**
     * Logs the user into his/her account.
     *
     * @throws IOException for missing catalog view
     */
    public void login() throws IOException {

        // Login process:
        //
        // 1. user enters username and password
        // 2. database checks for username
        // 3. username doesn't exist: output "no user found"
        // 4. username exists:
        // 5. get account id from username ("select account_id from users where username=?;")
        // 6. check if given password matches with password from account id ("select password_hash from accounts where account_id=?;")
        // 7. password doesn't match: output "password wrong, try again"
        // 8. password matches:
        // 9. get user account info
        // 10.get user user info ("select * from accounts join users where username=? and password_hash=?;")

        if(inputUsername.getText().isEmpty() || inputPassword.getText().isEmpty()) {

            System.out.println("Missing credentials! Please fill out username and password!");
            failAlert.setContentText("Missing credentials! Please fill out username and password!");
            failAlert.showAndWait();

        } else {

            String username = inputUsername.getText();
            // queryController.connectToDataBase();

            // Check if given username exists in database

            if(!queryController.doesUsernameExist(username)) {

                System.out.println("User \""+username+"\" does not exist in database!");
                failAlert.setContentText("User \""+username+"\" does not exist in database!");
                failAlert.showAndWait();

            } else {

                // Hash given password and match it with the given username in the database

                String passwordHash = queryController.getPassword(username);

                if (!BCrypt.checkpw(inputPassword.getText(), passwordHash)) {

                    System.out.println("Could not login - Wrong password!");
                    failAlert.setContentText("Could not login - Wrong password!");
                    failAlert.showAndWait();

                } else {

                    User loggedInUser = queryController.getUser(username, passwordHash);

                    if(loggedInUser == null) {

                        System.out.println("Could not login - User is null!");
                        failAlert.setContentText("Could not login - User is null!");
                        failAlert.showAndWait();

                    } else {

                        userSession = UserSession.getInstance();
                        userSession.createSession(loggedInUser);

                        infoAlert.setContentText("Successfully logged in! Welcome, "+loggedInUser.getUsername()+"!");
                        infoAlert.showAndWait();

                        openCatalog();

                    }

                }

            }

        }

    }

    /**
     * Used within the login form. Switches to the register form.
     *
     * @throws IOException
     */
    public void openRegisterForm() throws IOException {

        Stage currentStage = (Stage) loginFormContainer.getScene().getWindow();
        Parent registerRoot = FXMLLoader.load(getClass().getResource("/sample/views/login/register.fxml"));
        currentStage.setScene(new Scene(registerRoot));

    }

    /**
     * Skips the login process and brings the user to the main catalog. Doesn't start a user session.
     *
     * @throws IOException for missing catalog view
     */
    public void continueAsGuest() throws IOException {

        openCatalog();

    }

    /**
     * Gets called when a user (or guest) proceeds from the login/register form on to the main application.
     * The catalog view servers as the main homepage.
     *
     * @throws IOException for missing catalog view
     */
    private void openCatalog() throws IOException {

        Stage currentStage = (Stage) loginFormContainer.getScene().getWindow();
        Parent imprintRoot = FXMLLoader.load(getClass().getResource("/sample/views/catalog/catalog.fxml"));
        currentStage.setScene(new Scene(imprintRoot));

    }

}