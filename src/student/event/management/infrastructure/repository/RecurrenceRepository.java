package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Event;
import student.event.management.domain.models.Recurrence;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RecurrenceRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public RecurrenceRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }

    /**
     * update a recurrence into the database
     *
     * @param recurrence
     * @return
     * @throws SQLException
     */
    public boolean update(Recurrence recurrence) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        Optional<Recurrence> recurrenceOptional = findById(recurrence.getId());
        if (recurrenceOptional.isPresent()) {

            String sql = "UPDATE recurrence SET startTime = ?, eventId = ?, recurrencePatternId = ?, recurrenceRangeId = ? WHERE id = ?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, formatter.format(recurrence.getStartTime()));
            stmt.setLong(2, recurrence.getEventId());
            stmt.setLong(3, recurrence.getRecurrencePatternId());
            stmt.setLong(4, recurrence.getRecurrenceRangeId());
            stmt.setLong(5, recurrenceOptional.get().getId());
            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * save a recurrence into the database
     *
     * @param recurrence
     * @return
     * @throws SQLException
     */
    public boolean save(Recurrence recurrence) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String sql = "SELECT MAX(ID) FROM recurrence;";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        Long lastId = 0L;
        if (resultSet.next()) {
            lastId = resultSet.getLong(1);
        }
        stmt.close();
        sql = "INSERT INTO recurrence (id, startTime, eventId, recurrencePatternId, recurrenceRangeId ) VALUES (?,?,?,?,?);";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, lastId + 1);
        stmt.setString(2, formatter.format(recurrence.getStartTime()));
        stmt.setLong(3, recurrence.getEventId());
        stmt.setLong(4, recurrence.getRecurrencePatternId());
        stmt.setLong(5, recurrence.getRecurrenceRangeId());

        int result = stmt.executeUpdate();
        stmt.close();
        if (result == 1) {
            return true;
        }

        return false;
    }

    /**
     * get a recurrence from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<Recurrence> findById(Long id) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String sql = "SELECT * FROM recurrence WHERE id = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            LocalTime startTime = LocalTime.parse(resultSet.getString("startTime"), formatter);
            Long eventId = resultSet.getLong("eventId");
            Long recurrencePatternId = resultSet.getLong("recurrencePatternId");
            Long recurrenceRangeId = resultSet.getLong("recurrenceRangeId");


            Recurrence recurrence = Recurrence.builder()
                    .setId(id)
                    .setStartTime(startTime)
                    .setEventId(eventId)
                    .setRecurrencePatternId(recurrencePatternId)
                    .setRecurrenceRangeId(recurrenceRangeId);
            stmt.close();

            return Optional.of(recurrence);
        }
        return Optional.empty();
    }

    /**
     * get a recurrence from the database by its event
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public Optional<Recurrence> findByEvent(Event event) throws SQLException {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String sql = "SELECT * from recurrence r " +
                "JOIN event e ON e.id = r.eventId WHERE e.id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, event.getId());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            LocalTime startTime = LocalTime.parse(resultSet.getString("startTime"), formatter);
            Long eventId = resultSet.getLong("eventId");
            Long recurrencePatternId = resultSet.getLong("recurrencePatternId");
            Long recurrenceRangeId = resultSet.getLong("recurrenceRangeId");

            Recurrence recurrence = Recurrence.builder()
                    .setId(id)
                    .setStartTime(startTime)
                    .setEventId(eventId)
                    .setRecurrencePatternId(recurrencePatternId)
                    .setRecurrenceRangeId(recurrenceRangeId);
            stmt.close();

            return Optional.of(recurrence);
        }
        return Optional.empty();
    }

    /**
     * get a recurrence from the database by its event title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public Optional<Recurrence> findByEventTitle(String title) throws SQLException {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String sql = "SELECT * FROM recurrence r " +
                "JOIN event e ON e.id = r.eventId WHERE e.title = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            LocalTime startTime = LocalTime.parse(resultSet.getString("startTime"), formatter);
            Long eventId = resultSet.getLong("eventId");
            Long recurrencePatternId = resultSet.getLong("recurrencePatternId");
            Long recurrenceRangeId = resultSet.getLong("recurrenceRangeId");

            Recurrence recurrence = Recurrence.builder()
                    .setId(id)
                    .setStartTime(startTime)
                    .setEventId(eventId)
                    .setRecurrencePatternId(recurrencePatternId)
                    .setRecurrenceRangeId(recurrenceRangeId);
            stmt.close();

            return Optional.of(recurrence);
        }
        return Optional.empty();
    }

    /**
     * delete a recurrence from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM recurrence WHERE id = ?";
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
