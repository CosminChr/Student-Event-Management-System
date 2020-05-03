package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Student;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public StudentRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }

    /**
     * update a student in the database
     *
     * @param student
     * @return
     * @throws SQLException
     */
    public boolean update(Student student) throws SQLException {
        Optional<Student> studentOptional = findByUsername(student.getUsername());
        if (studentOptional.isPresent()) {

            String sql = "UPDATE Student " +
                    "SET firstName = ?," +
                    "SET lastName = ?," +
                    "SET username = ?," +
                    "SET password = ?," +
                    "SET hasOrganisationRights = ?," +
                    "WHERE id = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getUsername());
            stmt.setString(4, student.getPassword());
            stmt.setBoolean(5, student.isHasOrganisationRights());
            stmt.setLong(6, student.getId());
            int result = stmt.executeUpdate();

            stmt.close();
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * save a student into the database
     *
     * @param student
     * @return
     * @throws SQLException
     */
    public boolean save(Student student) throws SQLException {

        Optional<Student> studentOptional = findByUsername(student.getUsername());
        if (!studentOptional.isPresent())  {


            String sql = "SELECT MAX(id) FROM student";
            stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            Long lastId = 0L;
            if (resultSet.next()) {
                lastId = resultSet.getLong(1);
            }

            stmt.close();
            sql = "INSERT INTO Student (id, firstName, lastName, username, password, hasOrganisationRights) VALUES (?,?,?,?,?,?);";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, (lastId + 1));
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getUsername());
            stmt.setString(5, student.getPassword());
            stmt.setString(6, String.valueOf(student.isHasOrganisationRights()));
            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * get a student from the database by its username
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public Optional<Student> findByUsername(String username) throws SQLException {

        String sql = "SELECT * FROM STUDENT WHERE username = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, username);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String password = resultSet.getString("password");
            boolean hasOrganisationRights = Boolean.parseBoolean(resultSet.getString("hasOrganisationRights"));

            final Student student = Student.builder()
                    .setId(id)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUsername(username)
                    .setPassword(password)
                    .setHasOrganisationRights(hasOrganisationRights);
            this.stmt.close();
            return Optional.ofNullable(student);
        }
        this.stmt.close();
        return Optional.empty();
    }

    /**
     * get a student from the database by its firstName and lastName
     *
     * @param lastName
     * @param firstName
     * @return
     * @throws SQLException
     */
    public List<Student> findByLastNameAndFirstName(String lastName, String firstName) throws SQLException {

        String sql = "SELECT * FROM STUDENT WHERE lastName = ? AND firstName = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, lastName);
        this.stmt.setString(2, firstName);
        ResultSet resultSet = stmt.executeQuery();
        List<Student> students = new ArrayList<>();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            boolean hasOrganisationRights = Boolean.parseBoolean(resultSet.getString("hasOrganisationRights"));

            final Student student = Student.builder()
                    .setId(id)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUsername(username)
                    .setPassword(password)
                    .setHasOrganisationRights(hasOrganisationRights);
            students.add(student);
        }
        this.stmt.close();
        return students;
    }

    /**
     * get all the students from the database
     *
     * @return
     * @throws SQLException
     */
    public List<Student> findAll() throws SQLException {

        String sql = "SELECT * FROM student;";

        this.stmt = conn.prepareStatement(sql);
        List<Student> students = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String username = resultSet.getString("username");
            boolean hasOrganisationRights = Boolean.parseBoolean(resultSet.getString("hasOrganisationRights"));

            final Student student = Student.builder()
                    .setId(id)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUsername(username)
                    .setHasOrganisationRights(hasOrganisationRights);

            students.add(student);
        }
        return students;
    }

    /**
     * update the student's hasOrganisationRights flag by its username
     *
     * @param username
     * @param hasOrganisationRights
     * @return
     * @throws SQLException
     */
    public boolean updateHasOrganisationRightsByUsername(String username, boolean hasOrganisationRights) throws SQLException {

        String sql = " UPDATE student " +
                " SET hasOrganisationRights = ?" +
                " WHERE username = ?;";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, String.valueOf(hasOrganisationRights));
        this.stmt.setString(2, username);
        int result = this.stmt.executeUpdate();
        if (result == 1) {
            return true;
        }

        return false;
    }
}
