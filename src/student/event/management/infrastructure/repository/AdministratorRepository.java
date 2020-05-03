package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Administrator;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdministratorRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public AdministratorRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }


    /**
     * get the admin from the database by its username
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public Optional<Administrator> findByUsername(String username) throws SQLException {

        String sql = "SELECT * FROM administrator WHERE username = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, username);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String password = resultSet.getString("password");

            final Administrator administrator = Administrator.builder()
                    .setId(id)
                    .setUsername(username)
                    .setPassword(password);
            this.stmt.close();
            return Optional.ofNullable(administrator);
        }
        this.stmt.close();
        return Optional.empty();
    }
}
