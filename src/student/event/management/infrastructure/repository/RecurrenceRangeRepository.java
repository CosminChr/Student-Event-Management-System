package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Event;
import student.event.management.domain.models.RecurrenceRange;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RecurrenceRangeRepository {
    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public RecurrenceRangeRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }

    /**
     * update a recurrenceRange from the database by its id
     *
     * @param recurrenceRange
     * @return
     * @throws SQLException
     */
    public boolean update(RecurrenceRange recurrenceRange) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Optional<RecurrenceRange> recurrenceRangeOptional = findById(recurrenceRange.getId());
        if (recurrenceRangeOptional.isPresent()) {

            String sql = "UPDATE recurrenceRange SET startDate = ?, endByDate = ?, endAfter = ?, noEndDate = ? WHERE id = ?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, formatter.format(recurrenceRange.getStartDate()));
            stmt.setString(2, recurrenceRange.getEndByDate() != null ? formatter.format(recurrenceRange.getEndByDate()) : null);
            stmt.setInt(3, recurrenceRange.getEndAfter() != null ? recurrenceRange.getEndAfter() : 0);
            stmt.setString(4, recurrenceRange.getNoEndDate() != null ? recurrenceRange.getNoEndDate().toString() : null);
            stmt.setLong(5, recurrenceRangeOptional.get().getId());
            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * save a recurrenceRange into the database
     *
     * @param recurrenceRange
     * @return
     * @throws SQLException
     */
    public boolean save(RecurrenceRange recurrenceRange) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String sql = "SELECT MAX(ID) FROM recurrenceRange;";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        Long lastId = 0L;
        if (resultSet.next()) {
            lastId = resultSet.getLong(1);
        }
        stmt.close();
        sql = "INSERT INTO recurrenceRange (id, startDate, endByDate, endAfter, noEndDate ) VALUES (?,?,?,?,?);";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, lastId + 1);
        stmt.setString(2, formatter.format(recurrenceRange.getStartDate()));
        stmt.setString(3, recurrenceRange.getEndByDate() != null ? formatter.format(recurrenceRange.getEndByDate()) : null);
        stmt.setInt(4, recurrenceRange.getEndAfter() != null ? recurrenceRange.getEndAfter() : 0);
        stmt.setString(5, recurrenceRange.getNoEndDate() != null ? recurrenceRange.getNoEndDate().toString() : null);

        int result = stmt.executeUpdate();
        stmt.close();
        if (result == 1) {
            return true;
        }

        return false;
    }

    /**
     * get a recurrenceRange from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<RecurrenceRange> findById(Long id) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String sql = "SELECT * FROM recurrenceRange WHERE id = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            LocalDate startDate = LocalDate.parse(resultSet.getString("startDate"), formatter);
            LocalDate endByDate = LocalDate.parse(resultSet.getString("endByDate"), formatter);
            Integer endAfter = resultSet.getInt("endAfter");
            Boolean noEndDate = Boolean.parseBoolean(resultSet.getString("noEndDate"));


            RecurrenceRange recurrenceRange = RecurrenceRange.builder()
                    .setId(id)
                    .setStartDate(startDate)
                    .setEndByDate(endByDate)
                    .setEndAfter(endAfter)
                    .setNoEndDate(noEndDate);

            stmt.close();

            return Optional.of(recurrenceRange);
        }
        return Optional.empty();
    }

    /**
     * get the last recurrenceRange's id from the database
     *
     * @return
     * @throws SQLException
     */
    public Optional<Long> findLastId() throws SQLException {

        String sql = "SELECT MAX(ID) FROM recurrenceRange;";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong(1);

            stmt.close();

            return Optional.of(id);
        }
        return Optional.empty();
    }

    /**
     * get the recurrenceRange by its event from the database
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public Optional<RecurrenceRange> findByEvent(Event event) throws SQLException {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String sql = "SELECT * FROM recurrenceRange rr " +
                "JOIN recurrence r ON r.recurrenceRangeId = rr.id " +
                "JOIN event e ON e.id = r.eventId WHERE e.id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, event.getId());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            LocalDate startDate = LocalDate.parse(resultSet.getString("startDate"), formatter);
            LocalDate endByDate = LocalDate.parse(resultSet.getString("endByDate"), formatter);
            Integer endAfter = resultSet.getInt("endAfter");
            Boolean noEndDate = Boolean.parseBoolean(resultSet.getString("noEndDate"));


            RecurrenceRange recurrenceRange = RecurrenceRange.builder()
                    .setId(id)
                    .setStartDate(startDate)
                    .setEndByDate(endByDate)
                    .setEndAfter(endAfter)
                    .setNoEndDate(noEndDate);

            stmt.close();

            return Optional.of(recurrenceRange);
        }
        return Optional.empty();
    }

    /**
     * get the recurrenceRange by its event title from the database
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public Optional<RecurrenceRange> findByEventTitle(String title) throws SQLException {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String sql = "SELECT * from recurrenceRange rr " +
                "JOIN recurrence r ON r.recurrenceRangeId = rr.id " +
                "JOIN event e ON e.id = r.eventId WHERE e.title = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            LocalDate startDate = LocalDate.parse(resultSet.getString("startDate"), formatter);
            LocalDate endByDate = LocalDate.parse(resultSet.getString("endByDate"), formatter);
            Integer endAfter = resultSet.getInt("endAfter");
            Boolean noEndDate = Boolean.parseBoolean(resultSet.getString("noEndDate"));


            RecurrenceRange recurrenceRange = RecurrenceRange.builder()
                    .setId(id)
                    .setStartDate(startDate)
                    .setEndByDate(endByDate)
                    .setEndAfter(endAfter)
                    .setNoEndDate(noEndDate);

            stmt.close();

            return Optional.of(recurrenceRange);
        }
        return Optional.empty();
    }

    /**
     * delete a recurrenceRange by its id from the database
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM recurrenceRange WHERE id = ?";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        int result = this.stmt.executeUpdate();
        this.stmt.close();
        if (result == 1) {
            return true;
        }
        return false;
    }
}
