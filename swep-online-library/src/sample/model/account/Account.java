package sample.model.account;

import java.sql.Timestamp;

/**
 * Accounts serve a security reason by saving each user's password and account state separately from their user
 * profiles.
 *
 * In case of changes, the database layout needs to be changed manually as well.
 */
public abstract class Account {

    // Passwords are being saved in their hashed form using "BCrypt":
    // https://www.mindrot.org/projects/jBCrypt/

    private int accountID;
    private String password;
    private AccountState accountState;
    private Timestamp registeredSince; // java.sql.TimeStamp = MySQL DateTime
    private PermissionLevel permissionLevel;

    /**
     * An account constructor for newly registered accounts.
     *
     * Used to create new Account objects when registering a new user to the service. Database-dependent values such as
     * the account id and the registration timestamp are not needed as the account has not been added to the database
     * yet. Once the account is inserted into the database, the advanced constructor should be used to fetch accounts
     * with all included values.
     *
     * @param password The account's chosen (already encrypted) password.
     * @param accountState The account's account state. Upon new registrations usually 1 (= user).
     */
    public Account(String password, AccountState accountState) {

        this.accountID = -1;
        this.password = password;
        this.accountState = accountState;
        this.registeredSince = null; // Created by database upon insertion
        this.permissionLevel = null;

    }

    /**
     * An account constructor for already existing accounts.
     *
     * Used when fetching data from the database. In this case, database-dependent values such as the account id and
     * the registration timestamp already exist for the account and therefore are required in the constructor.
     *
     * @param accountID The account's assigned account id.
     * @param password The account's (encrypted) password.
     * @param accountState The account's account state.
     * @param registeredSince The account's time of registration.
     * @param permissionLevel The account's permission level.
     */
    public Account(int accountID, String password, AccountState accountState, Timestamp registeredSince,
                   PermissionLevel permissionLevel) {

        this.accountID = accountID;
        this.password = password;
        this.accountState = accountState;
        this.registeredSince = registeredSince;
        this.permissionLevel = permissionLevel;

    }

    // Getter and setter methods for object attributes

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountId) {
        this.accountID = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public Timestamp getRegisteredSince() {
        return registeredSince;
    }

    public void setRegisteredSince(Timestamp registeredSince) {
        this.registeredSince = registeredSince;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

}