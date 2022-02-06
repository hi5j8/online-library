package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.database.DatabaseConnection;

/**
 * Starts the program. I don't know ask the Java devs.
 */
public class Main extends Application {

    /**
     * Loads the index view and starts the program. I don't know really but it does the magic.
     *
     * @param primaryStage The index stage. In this case the login form.
     * @throws Exception for missing view component
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("Starting application...");

        // Connect to the database using JDBC and MySQL Connector/J

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        dbConnection.connect();

        // Init index stage

        Parent root = FXMLLoader.load(getClass().getResource("/sample/views/login/login.fxml"));
        primaryStage.setTitle("Online library");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }

}


