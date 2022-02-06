package sample.model.account;

import sample.model.account.user.User;

/**
 * Manages user sessions, remembers login credentials and transfers user information to different views.
 *
 * Implemented after the Singleton pattern. Lets all controllers consistently handle the same user data from a single
 * instance throughout the program's runtime.
 */
public class UserSession {

    // Singleton instance

    private static UserSession instance;

    // Object attributes

    private User user;

    /**
     * Internal constructor for a new session that gets called when getInstance finds no already existing session.
     */
    private UserSession(){
        this.user = null;
    }

    /**
     * Returns the static instance of the currently existing user session. If there is no existing session, a new
     * session will be created and saved.
     *
     * @return The current session or a new session if non existent before call.
     */
    public static UserSession getInstance() {

        if(instance == null) {
            instance = new UserSession();
        }
        return instance;

    }

    /**
     * Creates a new session for a logged in user.
     *
     * @param user The currently logged in user.
     */
    public void createSession(User user) {
        this.user = user;
    }

    /**
     * Destroys the current user's session.
     */
    public void destroySession() {
        instance = null;
    }

    /**
     * Returns the user saved in the currently active session.
     *
     * @return The user saved in the current session.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Checks if a currently active session exists.
     *
     * @return True if an active session exists, else false.
     */
    public static boolean isEmpty() {
        return instance == null;
    }

}