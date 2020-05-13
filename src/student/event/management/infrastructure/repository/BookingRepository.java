package student.event.management.infrastructure.repository;

import student.event.management.domain.dto.BookingDTO;
import student.event.management.domain.models.Booking;
import student.event.management.domain.util.BookingStatus;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public BookingRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }

    /**
     * update the booking status in the database
     *
     * @param booking
     * @return
     * @throws SQLException
     */
    public boolean updateBookingStatus(Booking booking) throws SQLException {

        Optional<Booking> bookingOptional = findById(booking.getId());
        if (bookingOptional.isPresent()) {
            String sql = " UPDATE Booking " +
                    " SET status = ? " +
                    " WHERE id = ?;";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, booking.getStatus().getStatus());
            stmt.setLong(2, booking.getId());
            int result = stmt.executeUpdate();
            if (result == 1) {
                return true;
            }
            stmt.close();
        }
        return false;
    }

    /**
     * save the booking in the database
     *
     * @param booking
     * @return
     * @throws SQLException
     */
    public boolean save(Booking booking) throws SQLException {

        String sql = "SELECT MAX(id) FROM booking";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        Long lastId = 0L;
        if (resultSet.next()) {
            lastId = resultSet.getLong(1);
        }

        sql = "INSERT INTO Booking (id, studentId, eventId, status) VALUES (?,?,?,?);";
        stmt = conn.prepareStatement(sql);

        stmt.setLong(1, (lastId + 1));
        stmt.setLong(2, booking.getStudentId());
        stmt.setLong(3, booking.getEventId());
        stmt.setString(4, booking.getStatus().getStatus());
        int result = stmt.executeUpdate();
        stmt.close();
        if (result == 1) {
            return true;
        }
        return false;
    }

    /**
     * get the booking from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<Booking> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM booking WHERE id = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {

            Long studentId = resultSet.getLong("studentId");
            Long eventId = resultSet.getLong("eventId");
            BookingStatus status = resultSet.getString("status") != null ? BookingStatus.parse(resultSet.getString("status")) : null;

            final Booking booking = Booking.builder()
                    .setId(id)
                    .setStudentId(studentId)
                    .setEventId(eventId)
                    .setStatus(status != null ? status : null);
            this.stmt.close();
            return Optional.ofNullable(booking);
        }
        return Optional.empty();

    }

    /**
     * get all the bookings from the database
     *
     * @return
     * @throws SQLException
     */
    public List<BookingDTO> findAllDtos() throws SQLException {

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String sql = "SELECT b.id, s.firstName, s.lastName, e.title, r.startTime, rr.startDate, b.status  FROM booking b " +
                "JOIN event e ON b.eventId = e.id " +
                "JOIN student s ON b.studentId = s.id " +
                "JOIN recurrence r ON e.id = r.eventId " +
                "JOIN recurrenceRange rr ON r.recurrenceRangeId = rr.id;";

        this.stmt = conn.prepareStatement(sql);
        List<BookingDTO> bookings = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String studentFirstName = resultSet.getString("firstName");
            String studentLastName = resultSet.getString("lastName");
            String eventTitle = resultSet.getString("title");
            LocalTime startTime = resultSet.getString("startTime") != null ? LocalTime.parse(resultSet.getString("startTime"), timeFormatter) : null;
            LocalDate startDate = resultSet.getString("startDate") != null ? LocalDate.parse(resultSet.getString("startDate"), dateFormatter) : null;
            BookingStatus bookingStatus = resultSet.getString("status") != null ? BookingStatus.parse(resultSet.getString("status")) : null;

            LocalDateTime start = LocalDateTime.of(startDate, startTime);

            final BookingDTO booking = BookingDTO.builder()
                    .setId(id)
                    .setStudentFirstName(studentFirstName)
                    .setStudentLastName(studentLastName)
                    .setEventTitle(eventTitle)
                    .setRecurrent(true)
                    .setStartTime(start)
                    .setStatus(bookingStatus);

            bookings.add(booking);
        }

        sql = "SELECT b.id, s.firstName, s.lastName, e.title, e.eventTime, b.status  FROM booking b " +
                "JOIN event e ON b.eventId = e.id " +
                "JOIN student s ON b.studentId = s.id; ";
        this.stmt = conn.prepareStatement(sql);
        resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String studentFirstName = resultSet.getString("firstName");
            String studentLastName = resultSet.getString("lastName");
            String eventTitle = resultSet.getString("title");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), dateTimeFormatter) : null;
            BookingStatus bookingStatus = resultSet.getString("status") != null ? BookingStatus.parse(resultSet.getString("status")) : null;

            final BookingDTO booking = BookingDTO.builder()
                    .setId(id)
                    .setStudentFirstName(studentFirstName)
                    .setStudentLastName(studentLastName)
                    .setEventTitle(eventTitle)
                    .setEventTime(eventTime)
                    .setRecurrent(false)
                    .setStatus(bookingStatus);

            bookings.add(booking);
        }

        return bookings;
    }


    /**
     * delete a booking from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM Booking WHERE id = ?";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        int result = this.stmt.executeUpdate();
        this.stmt.close();
        if (result == 1) {
            return true;
        }
        return false;
    }

    /**
     * delete a booking from the database by its title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public boolean deleteByEventTitle(String title) throws SQLException {

        String sql = "DELETE FROM booking " +
                "WHERE id IN ( " +
                "SELECT b.id FROM booking b " +
                "JOIN event e ON b.eventId = e.id " +
                "WHERE e.title = ?);";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, title);
        int result = this.stmt.executeUpdate();
        this.stmt.close();
        if (result == 1) {
            return true;
        }
        return false;
    }

    /**
     * get a booking from the database by its studentId and bookingId
     *
     * @param studentId
     * @param eventId
     * @return
     * @throws SQLException
     */
    public Optional<Booking> findBookingByStudentIdAndBookingId(Long studentId, Long eventId) throws SQLException {
        String sql = "SELECT * FROM booking WHERE studentId = ? AND eventId = ?;";

        this.stmt = conn.prepareStatement(sql);
        stmt.setLong(1, studentId);
        stmt.setLong(2, eventId);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {

            Long id = resultSet.getLong("id");
            BookingStatus status = resultSet.getString("status") != null ? BookingStatus.parse(resultSet.getString("status")) : null;

            final Booking booking = Booking.builder()
                    .setId(id)
                    .setStudentId(studentId)
                    .setEventId(eventId)
                    .setStatus(status != null ? status : null);
            this.stmt.close();
            return Optional.ofNullable(booking);
        }
        return Optional.empty();

    }

    /**
     * find all the bookings from the database by the event title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public List<Booking> findBookingsByEventTitle(String title) throws SQLException {

        String sql = "SELECT * FROM booking B " +
                "JOIN EVENT E ON E.id = B.eventId WHERE E.title = ?;";

        this.stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        List<Booking> bookings = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {

            Long id = resultSet.getLong("id");
            Long studentId = resultSet.getLong("studentId");
            Long eventId = resultSet.getLong("eventId");
            BookingStatus bookingStatus = resultSet.getString("status") != null ? BookingStatus.parse(resultSet.getString("status")) : null;

            final Booking booking = Booking.builder()
                    .setId(id)
                    .setStudentId(studentId)
                    .setEventId(eventId)
                    .setStatus(bookingStatus);
            bookings.add(booking);
        }
        this.stmt.close();
        return bookings;
    }

    /**
     * delete a booking by its event title and student id
     *
     * @param title
     * @param studentId
     * @return
     * @throws SQLException
     */
    public boolean deleteByEventTitleAndStudentId(String title, Long studentId) throws SQLException {

        String sql = "DELETE FROM booking " +
                "WHERE id IN ( " +
                "SELECT b.id FROM booking b " +
                "JOIN event e ON b.eventId = e.id " +
                "WHERE e.title = ? AND b.studentId = ?);";

        this.stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setLong(2, studentId);
        int result = this.stmt.executeUpdate();
        this.stmt.close();
        if (result == 1) {
            return true;
        }
        return false;
    }
}
