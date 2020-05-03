package student.event.management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static student.event.management.infrastructure.database.DatabaseHandler.setupDatabase;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // setup the database
        // create the tables if they don't exists
        // create a connection to the database and connect
        setupDatabase();

        // load the login stage
        Parent root = FXMLLoader.load(getClass().getResource("/student/event/management/ui/login/login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
