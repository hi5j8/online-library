package sample.controllers;

import sample.model.account.AccountState;
import sample.model.account.PermissionLevel;
import sample.model.account.user.User;
import sample.model.database.DatabaseConnection;
import sample.model.media.Book;
import sample.model.media.BookGenre;
import sample.model.media.Media;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Connects and manages database queries throughout the application runtime. Currently, the QueryController holds
 * ALL functions that execute queries, regardless of the objects they work for/with. For consistency purposes, the
 * QueryController should get split into respective Controller classes, which store all query functions in context
 * to their model object (ex.: "Book" and "BookController", whereas BookController holds functions such as
 * "addNewBook()".
 *
 * //TODO: Split QueryController into separate, dedicated Controller classes for each of the model objects
 *
 * All queries and connections in this class have been written by hand and without the usage of an external
 * library (ex. Hibernate).
 *
 * Note to self for next time: User hibernate. Don't write queries by hand. Idiot.
 */
public class QueryController {

    // Database connection and credentials
    // This application connects to a MySQL database. To emulate the server-side functionality, the database
    // needs to be set up on a localhost server using "MySQL workbench" and the credentials below.

    private DatabaseConnection dbConnection = DatabaseConnection.getInstance();

    /**
     * Registers a new user + account to the database.
     *
     * Built after: https://www.youtube.com/watch?v=Q4T7jg0Lv4E
     * Returning the created account's id: https://stackoverflow.com/a/1915197
     *
     * @param user A user object to be inserted as a new index in the USERS table of the database.
     * @return true if registration successful, false if registration failed
     */
    public boolean registerUser(User user) {

        try {

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Cannot register user (connection to database is closed)!");
                return false;

            }

            // Account variables: passwordSalt, passwordHashed, accountState
            // User variables: firstname, lastname, emailAddress, username
            // To reset account_id increment start back to 1: "ALTER TABLE accounts AUTO_INCREMENT = 1"

            // Step 1 - Check if username is already existing in database

            if (doesUsernameExist(user.getUsername())) {

                System.out.println("Could not register user: Username already existing!");
                return false;

            }

            // Step 2: Create a new account for user
            // Permission level 1 = standard user permissions (0 = guest, 2 = moderator, 3 = admin)

            // Q: Why not use a PreparedStatement here instead of a Statement?
            // A: This INSERT query apparently only works using a normal Statement. A PreparedStatement does not seem to
            // offer any return value of the newly inserted row, which in this case would be needed to fetch the
            // account id to bind it to the corresponding user.

            final String REGISTER_ACCOUNT_SQL =
                    "INSERT INTO accounts (`password_hash`, `state_id`, " +
                            "`permission_level`) VALUES (\"" + user.getPassword() + "\", " +
                            user.getAccountState().getStateID() + ", " + PermissionLevel.USER.getPermissionID() + ")";

            int registeredAccountID = -1; // Will be set later through returned ResultSet

            Statement registerAccountStatement = dbConnection.getConnection().createStatement();
            registerAccountStatement.execute(REGISTER_ACCOUNT_SQL, Statement.RETURN_GENERATED_KEYS);

            System.out.println("New account for user " + user.getUsername() + " added to database!");

            ResultSet registeredAccountResultSet = registerAccountStatement.getGeneratedKeys();

            while (registeredAccountResultSet.next()) {
                registeredAccountID = registeredAccountResultSet.getInt(1);
            }

            System.out.println("New users account id: " + registeredAccountID);

            // Step 3: Create a new user (database table) associated with the created account's id

            if (registeredAccountID == -1) {

                // This should be impossible to trigger since the INSERT would fail before even getting here,
                // but I'm going to leave it here just in case.

                System.out.println("Invalid account id generated");
                return false;

            } else {

                // Note: 'registerUserStatement' references the 'USERS' table in the database, NOT the User object!

                Statement registerUserStatement = dbConnection.getConnection().createStatement();
                final String REGISTER_USER_SQL =
                        "INSERT INTO USERS (account_id, email_address, first_name, last_name, username) " +
                                "VALUES (\"" + registeredAccountID + "\", \""+user.getEmail()+"\", \"" +
                                user.getFirstname()+"\", \""+user.getLastname()+"\", \""+user.getUsername()+"\");";

                registerUserStatement.execute(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);

                System.out.println("New user for user " + user.getUsername() + " (account id " + registeredAccountID +
                        ") created!");
                registerUserStatement.close();

            }

            // Insert successfully finished

            System.out.println("User registration successfully finished!");
            registerAccountStatement.close();

            return true;

        } catch(SQLException sqlEx) {

            sqlEx.printStackTrace();
            return false;

        }

    }

    /**
     * Gets a specified user based on their username and the password hash.
     *
     * @param username The account's specified username
     * @param passwordHash The account's specified password
     * @return A new user with information based on given credential parameters
     */
    public User getUser(String username, String passwordHash) {

        try {

            final String GET_USER_SQL = "SELECT * FROM accounts JOIN users WHERE username=? AND password_hash=?";
            PreparedStatement getUserStatement = dbConnection.getConnection().prepareStatement(GET_USER_SQL);
            getUserStatement.setString(1, username);
            getUserStatement.setString(2, passwordHash);
            ResultSet getUserResult = getUserStatement.executeQuery();

            // Required parameters for User object

            int accountID = -1, userID = -1;
            String password = null, firstname = null, lastname = null, email = null;
            Timestamp registeredSince = null;
            AccountState accountState = null;
            PermissionLevel permissionLevel = null;

            while(getUserResult.next()) {

                accountID = getUserResult.getInt("account_id");
                userID = getUserResult.getInt("user_id");
                password = getUserResult.getString("password_hash");
                firstname = getUserResult.getString("first_name");
                lastname = getUserResult.getString("last_name");
                email = getUserResult.getString("email_address");
                registeredSince = getUserResult.getTimestamp("registered_since");

                switch(getUserResult.getInt("state_id")) {

                    case 5:
                        accountState = AccountState.ACTIVE;
                        break;
                    case 6:
                        accountState = AccountState.MARKED_FOR_DELETION;
                        break;
                    case 7:
                        accountState = AccountState.FROZEN;
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value for AccountState: "
                                + getUserResult.getInt("state_id"));

                }

                switch(getUserResult.getInt("permission_level")) {

                    case 0:
                        permissionLevel = PermissionLevel.GUEST;
                        break;
                    case 1:
                        permissionLevel = PermissionLevel.USER;
                        break;
                    case 2:
                        permissionLevel = PermissionLevel.MODERATOR;
                        break;
                    case 3:
                        permissionLevel = PermissionLevel.ADMINISTRATOR;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value for permission level: "
                                + getUserResult.getInt("permission_level"));
                }

                User user = new User(
                        accountID, password, accountState, registeredSince, permissionLevel, userID, firstname,
                        lastname, email, username);

                return user;

            }

            getUserStatement.close();

        } catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return null;

    }

    /**
     * Checks if a given username exists in the database.
     *
     * In case this method shows compiler errors: Make sure 'mysql-connector-java-8.0.20.jar' is integrated into
     * the project (IntelliJ: Settings -> Project Structure... -> Project Settings -> Libraries)
     *
     * You can find the .jar in "/src/externalLibraries/mysql-connector-java-8.0.20.jar"
     *
     * @param username The given username to check for.
     * @return True if the username exists in the database, false if it doesn't
     * @throws SQLException For SQL errors
     */
    public boolean doesUsernameExist(String username) {

        try {

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Cannot search for username - database connection is closed!");
                return false;

            }

            final String CHECK_USERNAME_SQL = "SELECT username FROM users WHERE username=?;";
            PreparedStatement findUsernameStatement = dbConnection.getConnection().prepareStatement(CHECK_USERNAME_SQL);
            findUsernameStatement.setString(1, username);
            ResultSet usernameResults = findUsernameStatement.executeQuery();

            while (usernameResults.next()) {

                System.out.println("Checking " + usernameResults.getString(1).toString() + " against " +
                        username + " ...");

                if (usernameResults.getString(1).equals(username)) {

                    System.out.println("Username exists in Database!");
                    return true;

                }

            }

            System.out.println("Username does not exist in Database!");
            findUsernameStatement.close();
            return false;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return false;

    }

    /**
     * Gets the password hash of the given username from the database.
     *
     * @param username The specified username to get the password from
     * @return The password hash of given username
     */
    public String getPassword(String username) {

        try {

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Can not get password! Database connection is closed!");
                return null;

            }

            // Get account ID of username

            int accountID = -1;

            final String GET_ACCOUNT_ID_SQL = "SELECT account_id from users WHERE username=?;";
            PreparedStatement getAccountIDStatement = dbConnection.getConnection().prepareStatement(GET_ACCOUNT_ID_SQL);
            getAccountIDStatement.setString(1, username);
            ResultSet accountIDResults = getAccountIDStatement.executeQuery();

            while (accountIDResults.next()) {
                accountID = accountIDResults.getInt(1);
            }

            // Check if an account id was found

            if(accountID == -1) {

                System.out.println("No account id for given username found in database!");
                return null;

            }

            // Get and return hash of said account ID

            String passwordHash = null;

            final String GET_PASSWORD_HASH_SQL = "SELECT password_hash FROM accounts WHERE account_id=?;";
            PreparedStatement getPasswordHashStatement = dbConnection.getConnection().prepareStatement(GET_PASSWORD_HASH_SQL);
            getPasswordHashStatement.setInt(1, accountID);
            ResultSet passwordHashResults = getPasswordHashStatement.executeQuery();

            while(passwordHashResults.next()) {
                passwordHash = passwordHashResults.getString(1);
            }

            if(passwordHash == null) {
                System.out.println("No password hash for given account id found in database!");
                return null;
            }

            System.out.println("Password hash for \""+username+"\" successfully found in database!");
            getPasswordHashStatement.close();
            return passwordHash;

        } catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return null;

    }

    /**
     * Adds a new book to the database.
     *
     * @param book Given book
     * @return True if addition was successful, false if addition failed
     */
    public boolean addNewBook(Book book){

        try {

            // Check if database connection is open

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Can not get password! Database connection is closed!");
                return false;

            }

            // 1. Add new media to database with given media data

            int insertedMediaID = addNewMedia(book);

            // 2. Add new book to database linked to media ID

            if(insertedMediaID == -1) {

                System.out.println("Could not add book! Unexpected media ID (-1)!");
                return false;

            }

            final String ADD_BOOK_SQL = "INSERT INTO books (media_media_id) VALUES ("+insertedMediaID+");";
            Statement addBookStatement = dbConnection.getConnection().createStatement();
            addBookStatement.execute(ADD_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
            ResultSet addBookResult = addBookStatement.getGeneratedKeys();

            int insertedBookID = -1;

            while(addBookResult.next()) {
                insertedBookID = addBookResult.getInt(1);
            }

            System.out.println("New book [ID: "+insertedBookID+"] added!");

            // 3. Add authors to database linked to book ID

            Statement addAuthorStatement = dbConnection.getConnection().createStatement();

            for(String author : book.getAuthors()) {

                final String ADD_AUTHOR_SQL = "INSERT INTO book_authors (books_book_id, author) " +
                        "VALUES ("+insertedBookID+", \""+author+"\");";
                addAuthorStatement.execute(ADD_AUTHOR_SQL);

                System.out.println("Author \""+author+"\" added for book ID "+insertedBookID+"!");

            }

            // 4. Add publishers to database linked to book ID

            Statement addPublisherStatement = dbConnection.getConnection().createStatement();

            for(String publisher : book.getPublishers()) {

                final String ADD_AUTHOR_SQL = "INSERT INTO book_publishers (books_book_id, publisher) " +
                        "VALUES ("+insertedBookID+", \""+publisher+"\");";
                addPublisherStatement.execute(ADD_AUTHOR_SQL);

                System.out.println("Publisher \""+publisher+"\" added for book ID "+insertedBookID+"!");

            }

            // 5. Add genres to database linked to book ID and genre ID

            PreparedStatement getBookGenreIDStatement;
            Statement addBookGenreStatement = dbConnection.getConnection().createStatement();

            for(BookGenre bookGenre : book.getGenres()) {

                // Get genre_id corresponding to given bookGenre name from db

                final String GET_BOOK_GENRE_ID_SQL = "SELECT genre_id FROM book_genres_list WHERE genre_name=?";
                getBookGenreIDStatement = dbConnection.getConnection().prepareStatement(GET_BOOK_GENRE_ID_SQL);
                getBookGenreIDStatement.setString(1, bookGenre.getName());
                ResultSet getBookGenreIDResult = getBookGenreIDStatement.executeQuery();
                int fetchedBookGenreID = -1;
                while(getBookGenreIDResult.next()) {
                    fetchedBookGenreID = getBookGenreIDResult.getInt("genre_id");
                }

                if(fetchedBookGenreID == 1) {
                    System.out.println("Corresponding genre id for \""+bookGenre.getName()+"\"not found!");
                } else {

                    // Add genre_id to book_id into book_genres table

                    final String ADD_BOOK_GENRE_SQL =
                            "INSERT INTO book_genres (books_book_id, book_genres_list_genre_id) " +
                                    "VALUES("+insertedBookID+", "+fetchedBookGenreID+");";
                    addBookGenreStatement.execute(ADD_BOOK_GENRE_SQL);

                    System.out.println("New book genre [Genre_ID: "+fetchedBookGenreID+"] added to book [ID: "+
                            insertedBookID+"]!");

                }

                getBookGenreIDStatement.close();

            }

            // 6. Close statements and return

            addBookStatement.close();
            addAuthorStatement.close();
            addPublisherStatement.close();
            addBookGenreStatement.close();
            return true;

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            return false;

        }

    }

    /**
     * Inserts a new index into the "media" table.
     * @param media The to-be inserted media
     * @return The media id of the newly added row or -1 in case of failure to insert
     */
    private int addNewMedia(Media media) {

        try {

            // Check if database connection is open

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Could not add media! Database connection is closed!");
                return -1;

            }

            // Q: Why not use a PreparedStatement here instead of Statement?
            // A: This INSERT query apparently only works using a normal Statement. A PreparedStatement does not seem
            // to offer any return value of the newly inserted row, which in this case would be needed to fetch the
            // media id.

            // Convert booleans to integers

            int givenMediaIsAvailable = 0;
            int givenMediaIsBorrowed = 0;

            if(media.isAvailable()) givenMediaIsAvailable = 1;
            if(media.isBorrowed()) givenMediaIsBorrowed = 1;

            final String ADD_MEDIA_SQL =
                    "INSERT INTO media (title, release_date, is_available, is_borrowed)"+
                            "VALUES (\""+media.getTitle()+"\", \""+media.getReleaseDate()+"\", \""+givenMediaIsAvailable
                            +"\", \""+givenMediaIsBorrowed+"\")";
            Statement addMediaStatement = dbConnection.getConnection().createStatement();
            addMediaStatement.execute(ADD_MEDIA_SQL, Statement.RETURN_GENERATED_KEYS);
            ResultSet addMediaResult = addMediaStatement.getGeneratedKeys();

            int insertedMediaID = -1;

            while(addMediaResult.next()) {
                insertedMediaID = addMediaResult.getInt(1);
            }

            System.out.println("New media [ID: "+insertedMediaID+"] added!");
            addMediaStatement.close();
            return insertedMediaID;


        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            return -1;

        }

    }

    /**
     * Selects all media items from the database
     *
     * @return a list of all media in the database
     */
    public ArrayList<Book> getBooks() {

        try {

            System.out.println("Loading books catalog...");

            // Check if database connection is open

            if(dbConnection.getConnection().isClosed()) {

                System.out.println("Could not fetch media! Database connection is closed!");
                return null;

            }

            ArrayList<Book> media = new ArrayList<>();

            final String GET_BOOKS_SQL =
                    "SELECT \n" +
                    "\tmedia.media_id AS media_id, \n" +
                    "\tmedia.title AS title, \n" +
                    "\tmedia.release_date AS release_date, \n" +
                    "\tmedia.is_available AS is_available, \n" +
                    "\tmedia.is_borrowed AS is_borrowed, \n" +
                    "\tbooks.book_id AS book_id, \n" +
                    "    group_concat(DISTINCT publishers.publisher) AS publishers,\n" +
                    "\tgroup_concat(DISTINCT authors.author) AS authors,\n" +
                    "    group_concat(DISTINCT genres.genre_name) AS genres\n" +
                    "FROM media media\n" +
                    "LEFT JOIN books books \n" +
                    "\tON media.media_id = books.media_media_id\n" +
                    "LEFT JOIN book_authors authors\n" +
                    "\tON books.book_id = authors.books_book_id\n" +
                    "LEFT JOIN book_publishers publishers\n" +
                    "\tON books.book_id = publishers.books_book_id\n" +
                    "LEFT JOIN book_genres book_genres\n" +
                    "\tON books.book_id = book_genres.books_book_id\n" +
                    "LEFT JOIN book_genres_list genres\n" +
                    "\tON genres.genre_id = book_genres.book_genres_list_genre_id\n" +
                    "\n" +
                    "GROUP BY books.book_id;";

            PreparedStatement getBooksStatement = dbConnection.getConnection().prepareStatement(GET_BOOKS_SQL);
            ResultSet getBooksResult = getBooksStatement.executeQuery();

            while(getBooksResult.next()) {

                System.out.println("Fetching book \""+getBooksResult.getString("title")+"\"...");

                int mediaID = getBooksResult.getInt("media_id");
                String title = getBooksResult.getString("title");
                Date releaseDate = getBooksResult.getDate("release_date");
                boolean isAvailable = getBooksResult.getBoolean("is_available");
                boolean isBorrowed = getBooksResult.getBoolean("is_borrowed");
                int bookID = getBooksResult.getInt("book_id");
                ArrayList<String> authors = new ArrayList<>(Arrays.asList(
                        getBooksResult.getString("authors").split("\\s*,\\s*")
                ));
                ArrayList<String> publishers = new ArrayList<>(Arrays.asList(
                        getBooksResult.getString("publishers").split("\\s*,\\s*")
                ));

                // Fetching genres requires an extra step of identification of the BookGenre constant according to each
                // fetched substring of genres in the query.

                ArrayList<BookGenre> genres = new ArrayList<>();

                if(getBooksResult.getString("genres") == null) {

                    genres.add(BookGenre.UNDEFINED);

                } else {

                    ArrayList<String> fetchedGenres = new ArrayList<>(Arrays.asList(
                            getBooksResult.getString("genres").split("\\s*,\\s*")
                    ));
                    fetchedGenres.forEach(genre -> {
                        genres.add(BookGenre.valueOf(genre.toUpperCase().replace(" ", "_")));
                    });

                }

                // Add new Book to media list

                media.add(new Book(
                        mediaID, title, releaseDate, isAvailable, isBorrowed, bookID, genres, authors, publishers
                ));

            }

            // Older statements that don't really worked but helped me figure out how to build the query:

            // This statement works, but it's horrible

            // SELECT media_id, title, release_date, is_available, book_id, author, publisher, genre_name
            // FROM media media
            // JOIN books books
            // ON media.media_id = books.media_media_id
            // JOIN book_authors authors
            // ON books.book_id = authors.books_book_id
            // JOIN book_genres genres
            // ON books.book_id = genres.books_book_id
            // JOIN book_genres_list genrelist
            // ON genrelist.genre_id = genres.book_genres_list_genre_id
            // JOIN book_publishers publishers
            // ON books.book_id = publishers.books_book_id;
            //
            // Might want to split this into multiple statements, else the algorithm for object creation out of
            // the resulting ResultSet might make you go insane.

            // This statement groups all authors to each book_id
            //
            // SELECT books_book_id AS book_id, group_concat(author) AS authors
            // FROM book_authors
            // GROUP BY by books_book_id;

            return media;

        } catch(SQLException sqlEx) {
            sqlEx.printStackTrace();
            return null;
        }

    }

    /**
     * Removes a media item of given id from the user's watch list.
     *
     * @param userID The id of the current user saved in the user session
     * @param mediaID The media id of given media
     * @return true if removal successful, else false
     */
    public boolean removeFromWatchlist(int userID, int mediaID) {

        return false;

    }

    /**
     * Adds an item to a user's watch list. Gets called when the "Add" button in the catalog for an item is clicked.
     *
     * @param userID The id of the current user saved in the user session
     * @param mediaID The id of the selected media item
     * @return true if addition successful, else false
     */
    public boolean addToWatchlist(int userID, int mediaID) {

        return false;

    }

}