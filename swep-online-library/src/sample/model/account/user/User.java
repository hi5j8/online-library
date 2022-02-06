package sample.model.account.user;

import sample.model.account.Account;
import sample.model.account.AccountState;
import sample.model.account.PermissionLevel;

import java.sql.Timestamp;

/**
 * Defines the structure of the applications user profiles. Persistent user data is stored in a separate database.
 * For safety separation, the user's password is stored within its account.
 *
 * In case of changes, the database layout needs to be changed manually as well.
 */
public class User extends Account {

    private int userID;
    private String firstname;
    private String lastname;
    private String email;
    private String username;

    /**
     * A user constructor for newly registered users.
     *
     * Used to create new users when registering a new user to the service. Database-dependent values such as the
     * user id and account-based values are not needed as the user has not been added to the database yet. Once the
     * user is inserted into the database, the advanced constructor should be used to fetch users with all included
     * values.
     *
     * @param password The user's (encrypted) password. Used in super constructor (account).
     * @param accountState The user's account state. Used in super constructor (account).
     * @param firstname The user's first name.
     * @param lastname The user's last name.
     * @param email The user's email address.
     * @param username The user's username.
     */
    public User(String password, AccountState accountState, String firstname,
                String lastname, String email, String username) {

        super(password, accountState);
        this.userID = -1;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;

    }

    /**
     * A user constructor for already existing users.
     *
     * Used when fetching data from the database. In this case, database-dependent values such as the user id and
     * account-based values already exist for the user and therefore are required in the constructor.
     *
     * @param accountID The user's account id. Used in super constructor (account).
     * @param password The user's (encrypted) password. Used in super constructor (account).
     * @param accountState The user's account state. Used in super constructor (account).
     * @param registeredSince The user's time of registration. Used in super constructor (account).
     * @param permissionLevel The user's permission level. Used in super constructor (account).
     * @param userID The user's user id.
     * @param firstname The user's first name.
     * @param lastname The user's last name.
     * @param email The user's email address.
     * @param username The user's username.
     */
    public User(int accountID, String password, AccountState accountState, Timestamp registeredSince,
                PermissionLevel permissionLevel, int userID, String firstname, String lastname, String email,
                String username) {

        super(accountID, password, accountState, registeredSince, permissionLevel);
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;

    }

    // Getter and setter methods for object attributes

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}