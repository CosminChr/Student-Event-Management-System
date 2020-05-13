package student.event.management.infrastructure.database;

import org.sqlite.SQLiteConfig;
import student.event.management.infrastructure.repository.GenericRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static student.event.management.infrastructure.util.DatabaseConstants.DATABASE_URL;
import static student.event.management.infrastructure.util.SqlConstants.*;

public class DatabaseHandler {

    private static DatabaseHandler handler;

    private GenericRepository repository;

    private static Connection conn;

    private DatabaseHandler() {
        repository = new GenericRepository();
    }

    /**
     * get an instance to the database handler
     *
     * @return
     */
    public static DatabaseHandler getInstance() {
        return handler;
    }

    /**
     * get an instance to the database connection
     *
     * @return
     */
    public static Connection getConnection() {
        return conn;
    }



    /**
     * set up the database by getting a connection
     * creating the tables if they don't exist
     * and creating the admin if he doesn't exist
     */
    public static void setupDatabase() {
        if (handler == null) {
            handler = new DatabaseHandler();
            handler.connect();
            handler.setupTables();
            handler.insertAdmin();
        }
    }

    /**
     * set the database handler singleton instance into the generic repository
     * and create all the tables if they don't exist yet
     */
    private void setupTables() {
        repository.setHandler(handler);
        repository.execute(CREATE_STUDENT_TABLE.getSql());
        repository.execute(CREATE_EVENT_TABLE.getSql());
        repository.execute(CREATE_BOOKING_TABLE.getSql());
        repository.execute(CREATE_ADMINISTRATOR_TABLE.getSql());
        repository.execute(CREATE_RECURRENCE_TABLE.getSql());
        repository.execute(CREATE_RECURRENCE_PATTERN_TABLE.getSql());
        repository.execute(CREATE_RECURRENCE_RANGE_TABLE.getSql());

    }

    /**
     * insert the admin into the database if he doesn't exist
     */
    private void insertAdmin() {
        repository.execute(INSERT_ADMIN.getSql());
    }

    /**
     * connect to the database
     */
    private void connect() {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(DATABASE_URL.getPath(), config.toProperties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
