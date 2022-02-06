package sample.controllers.login;

import externalLibraries.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.controllers.QueryController;
import sample.model.account.AccountState;
import sample.model.account.UserSession;
import sample.model.account.user.User;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Handles events of the register form.
 */
public class RegisterController implements Initializable {

    @FXML
    private GridPane registerFormContainer;
    @FXML
    private Hyperlink hyperBackToLogin;
    @FXML
    private Button buttonRegister;

    // Inputs for account information

    @FXML
    private TextField inputFirstname;
    @FXML
    private TextField inputLastname;
    @FXML
    private TextField inputEmail;
    @FXML
    private TextField inputEmailRepeat;
    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private PasswordField inputPasswordRepeat;
    @FXML
    private TextField inputMembershipID;

    // Database connection handler

    private QueryController queryController = new QueryController();

    // User session

    private UserSession userSession;

    // Used for testing

    private boolean debug = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Debug settings

        if(debug) {

            // Fills inputs with placeholder inputs

            inputFirstname.setText("John");
            inputLastname.setText("Doe");
            inputEmail.setText("john-doe@example.com");
            inputEmailRepeat.setText(inputEmail.getText());
            inputUsername.setText("JohnDoe");
            inputPassword.setText("johndoeJOHNDOE0000");
            inputPasswordRepeat.setText(inputPassword.getText());

        }

    }

    /**
     * Takes and checks all input from the register form and creates a new user account based on it.
     *
     * Password hashing and salting done following this tutorial:
     * https://www.baeldung.com/java-password-hashing
     *
     * @throws IOException for missing view components
     * @throws NoSuchAlgorithmException for missing password hashing algorithm
     */
    public void register() throws IOException, NoSuchAlgorithmException {

        // For registration validation

        boolean userRegistered;

        // Validate required inputs, abort registration if inputs are false or missing

        if (!validateProfileInputs()) {

            // Validation failed

            System.out.println("\n- INPUT VALIDATION failed!\n");

        } else {

            // All fields are valid

            System.out.println("+ INPUT VALIDATION successfull!\n\nRegistering new user...");

            AccountState accountState = AccountState.ACTIVE;

            String emailAddress = inputEmail.getText();
            String firstname = inputFirstname.getText();
            String lastname = inputLastname.getText();
            String username = inputUsername.getText();

            // Hashing password using BCrypt

            String password = BCrypt.hashpw(inputPassword.getText(), BCrypt.gensalt(10));

            // Create user
            // accountID, userID and registeredSince will be automatically assigned upon database insertion
            // A different User constructor will take care of users when fetching data from the database.

            User newUser = new User(password, accountState, firstname, lastname, emailAddress, username);

            // Send new user to database

            // queryController.connectToDataBase();
            userRegistered = queryController.registerUser(newUser);

            // Continue if user was successfully registered

            if(userRegistered) {

                // Fetch the same user just created again from the database and check if it's the same
                // Sort of a security check to verify that the registration was successful

                User registeredUser = queryController.getUser(newUser.getUsername(), password);

                if(registeredUser == null) {

                    Alert failAlert = new Alert(Alert.AlertType.ERROR, "Account registration failed! " +
                            "New user is not valid!");
                    failAlert.showAndWait();
                    System.out.println("Account registration failed! New user is not valid! Please try again.");

                } else {

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Account registered! " +
                            "Welcome, "+registeredUser.getUsername()+"!");
                    successAlert.showAndWait();
                    System.out.println("Account registered! Welcome! Switching to catalog...");

                    // Create new user session using newly created User

                    userSession = UserSession.getInstance();
                    userSession.createSession(registeredUser);

                    openCatalog();

                }

            } else {

                Alert failAlert = new Alert(Alert.AlertType.ERROR, "Account registration failed! " +
                        "Registration was not successful");
                failAlert.showAndWait();
                System.out.println("Account registration failed! Registration was not successful!");

            }

        }

    }

    /**
     * Switches back to the login form.
     *
     * @throws IOException for missing view component
     */
    public void openLoginForm() throws IOException {

        Stage currentStage = (Stage) registerFormContainer.getScene().getWindow();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/sample/views/login/login.fxml"));
        currentStage.setScene(new Scene(loginRoot));

    }

    /**
     * Gets called when a user (or guest) proceeds from the login/register form on to the main application.
     * The catalog view servers as the main homepage.
     *
     * @throws IOException for missing view component
     */
    public void openCatalog() throws IOException {

        Stage currentStage = (Stage) registerFormContainer.getScene().getWindow();
        Parent catalogRoot = FXMLLoader.load(getClass().getResource("/sample/views/catalog/catalog.fxml"));
        currentStage.setScene(new Scene(catalogRoot));

    }

    /**
     * Checks if all required fields are filled and valid according to respective Regex. If so, allow the user to
     * register. Takes care of visual representation of what inputs are wrong/missing.
     *
     * @return True if inputs are valid, false if inputs are invalid (empty, wrong conventions)
     */
    private boolean validateProfileInputs() {

        // Following rules are applied to inputs:
        //
        // - First and last name:   A-Z, a-z
        // - Email & repeat:        valid E-Mail address
        // - Username:              A-Z, a-z, -, _, 0-9
        // - Password & repeat:     -
        //
        // Check via Regex
        // Inputs can also not be empty (except for membership)

        // Regex patterns
        //
        // Name pattern: https://stackoverflow.com/a/39134560
        // Mail pattern: https://stackoverflow.com/a/44674038
        // Password pattern: https://howtodoinjava.com/regex/how-to-build-regex-based-password-validator-in-java/
        // Username pattern: https://stackoverflow.com/a/40653022

        // TODO: EMAIL_PATTERN identifies "jonas.hiller@hof-university.de" as invalid. Needs fixing.

        final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z\\u00C0-\\u024F\\u1E00-\\u1EFF \\-\\.\\']*");
        final Pattern EMAIL_PATTERN = Pattern.compile("([\\p{L}-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}");
        final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,64}$");
        final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z_0-9_-]{4,20}$");

        // Boolean switches to determine return value

        boolean firstnameValid, lastnameValid, emailValid, emailRepeatValid, emailIdent,
                usernameValid, passwordValid, passwordRepeatValid, passwordIdent;

        // TODO: Improve input validation check, still missing some loggable checks like 'empty' etc.
        // TODO: Highlight errors in red, not implemented yet but make sure to add it once the styling gets added.
        // Side note: change object css with "X.setStyle("-fx-background: red");"

        // Check first name

        if(NAME_PATTERN.matcher(inputFirstname.getText()).matches() && !inputFirstname.getText().isEmpty()) {

            firstnameValid = true;
            System.out.println("FIRST NAME is valid!");

        } else {

            firstnameValid = false;
            System.out.println("- FIRST NAME is not valid!");
        }

        // Check last name

        if(NAME_PATTERN.matcher(inputLastname.getText()).matches() && !inputLastname.getText().isEmpty()) {

            lastnameValid = true;
            System.out.println("+ LAST NAME is valid!");

        } else {

            lastnameValid = false;
            System.out.println("- LAST NAME is not valid!");

        }

        // Check Email

        if(EMAIL_PATTERN.matcher(inputEmail.getText()).matches() && !inputEmail.getText().isEmpty()) {

            emailValid = true;
            System.out.println("+ EMAIL is valid!");

        } else {

            emailValid = false;
            System.out.println("- EMAIL is not valid!");

        }

        // Check Email repeat

        if(EMAIL_PATTERN.matcher(inputEmailRepeat.getText()).matches() && !inputEmailRepeat.getText().isEmpty()) {

            emailRepeatValid = true;
            System.out.println("+ EMAIL REPEAT is valid!");

        } else {

            emailRepeatValid = false;
            System.out.println("- EMAIL REPEAT is not valid!");

        }

        // Check if both email fields are identical

        if(inputEmail.getText().equals(inputEmailRepeat.getText())) {

            emailIdent = true;
            System.out.println("+ EMAIL & REPEAT are identical!");

        } else {

            emailIdent = false;
            System.out.println("- EMAIL & REPEAT do not match!");

        }

        // Check username

        if(USERNAME_PATTERN.matcher(inputUsername.getText()).matches() && !inputUsername.getText().isEmpty()) {

            usernameValid = true;
            System.out.println("+ USERNAME is valid!");

        } else {

            usernameValid = false;
            System.out.println("- USERNAME is invalid!");

        }

        // Check password

        if(PASSWORD_PATTERN.matcher(inputPassword.getText()).matches() && !inputPassword.getText().isEmpty()) {

            passwordValid = true;
            System.out.println("+ PASSWORD is valid!");

        } else {

            passwordValid = false;
            System.out.println("- PASSWORD is not valid!");

        }

        // Check password repeat

        if(PASSWORD_PATTERN.matcher(inputPasswordRepeat.getText()).matches() && !inputPasswordRepeat.getText().isEmpty()) {

            passwordRepeatValid = true;
            System.out.println("+ PASSWORD is valid!");

        } else {

            passwordRepeatValid = false;
            System.out.println("- PASSWORD is not valid!");

        }

        // Check if both password fields are identical

        if(inputPassword.getText().equals(inputPasswordRepeat.getText())) {

            passwordIdent = true;
            System.out.println("+ PASSWORD & REPEAT are identical!");

        } else {

            passwordIdent = false;
            System.out.println("- PASSWORD & REPEAT do not match!");

        }

        // True = All inputs are valid, user creation is possible
        // False = They're not.

        return firstnameValid && lastnameValid && emailValid && emailRepeatValid && emailIdent &&
                usernameValid && passwordValid && passwordRepeatValid && passwordIdent;

    }

}