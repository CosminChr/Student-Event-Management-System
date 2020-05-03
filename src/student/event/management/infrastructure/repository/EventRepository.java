package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Event;
import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;
import student.event.management.infrastructure.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public EventRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }


    /**
     * update the event in the database
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public boolean update(Event event) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        Optional<Event> eventOptional = findById(event.getId());
        if (eventOptional.isPresent()) {

            String sql = " UPDATE Event " +
                    " SET title = ?," +
                    " description = ? ," +
                    " eventType = ? ," +
                    " eventPlace = ? ," +
                    " url = ? ," +
                    " organisation= ? ," +
                    " location = ? ," +
                    " eventTime = ? ," +
                    " requiresBooking = ? ," +
                    " numberOfPlaces = ? , " +
                    " createdBy = ?" +
                    " WHERE id = ?;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getEventType().getType());
            stmt.setString(4, event.getEventPlace().getPlace());
            stmt.setString(5, event.getUrl());
            stmt.setString(6, event.getOrganisation());
            stmt.setString(7, event.getLocation());
            stmt.setString(8, event.getEventTime() != null ? formatter.format(event.getEventTime()) : null);
            stmt.setString(9, String.valueOf(event.isRequiresBooking()));
            stmt.setInt(10, event.getNumberOfPlaces() != null ? event.getNumberOfPlaces() : 0);
            stmt.setLong(11, event.getCreatedBy());
            stmt.setLong(12, eventOptional.get().getId());

            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }

        }
        return false;
    }


    /**
     * save the event in the database
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public boolean save(Event event) throws SQLException {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Optional<Event> eventOptional = findByTitle(event.getTitle());
        if (!eventOptional.isPresent()) {


            String sql = "SELECT MAX(ID) FROM EVENT;";
            stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            Long lastId = 0L;
            if (resultSet.next()) {
                lastId = resultSet.getLong(1);
            }
            stmt.close();
            sql = "INSERT INTO Event (id, title, description, eventType, eventPlace, url, organisation, location, eventTime, requiresBooking, numberOfPlaces, createdBy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, lastId + 1);
            stmt.setString(2, event.getTitle());
            stmt.setString(3, event.getDescription());
            stmt.setString(4, event.getEventType().getType());
            stmt.setString(5, event.getEventPlace().getPlace());
            stmt.setString(6, event.getUrl());
            stmt.setString(7, event.getOrganisation());
            stmt.setString(8, event.getLocation());
            stmt.setString(9, event.getEventTime() != null ? formatter.format(event.getEventTime()) : null);
            stmt.setString(10, String.valueOf(event.isRequiresBooking()));
            stmt.setInt(11, event.getNumberOfPlaces() != null ? event.getNumberOfPlaces() : 0);
            stmt.setLong(12, event.getCreatedBy());
            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * get an event from database by its title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public Optional<Event> findByTitle(String title) throws SQLException {

        String sql = "SELECT * FROM EVENT WHERE title = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setString(1, title);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            Long id = resultSet.getLong("id");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            this.stmt.close();
            return Optional.ofNullable(event);
        }
        this.stmt.close();
        return Optional.empty();
    }

    /**
     * get an event from the database by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<Event> findById(Long id) throws SQLException {

        String sql = "SELECT * FROM EVENT WHERE id = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            this.stmt.close();
            return Optional.ofNullable(event);
        }
        this.stmt.close();
        return Optional.empty();
    }

    /**
     * delete an event from the database by its title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public boolean deleteByTitle(String title) throws SQLException {
        String sql = "DELETE FROM Event WHERE title = ?";
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
     * get all the events from the database
     *
     * @return
     * @throws SQLException
     */
    public List<Event> findAll() throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String sql = "SELECT * FROM EVENT;";

        this.stmt = conn.prepareStatement(sql);
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            events.add(event);
        }
        return events;
    }

    /**
     * get all the events by the student's id
     *
     * @param studentId
     * @return
     * @throws SQLException
     */
    public List<Event> findAllByStudentId(Long studentId) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String sql = "SELECT * FROM event INNER JOIN booking ON event.id = booking.eventId WHERE studentId = ?;";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, studentId);
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            events.add(event);
        }
        return events;
    }


    /**
     * get all the events from the database by title and studentId
     *
     * @param title
     * @param studentId
     * @return
     * @throws SQLException
     */
    public Optional<Event> findByEventTitleAndStudentId(String title, Long studentId) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String sql = "SELECT * FROM event e INNER JOIN BOOKING b ON e.id = b.eventId INNER JOIN student s on b.studentId = s.id WHERE s.id = ? AND e.title = ?;";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, studentId);
        this.stmt.setString(2, title);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            return Optional.of(event);
        }
        return Optional.empty();
    }

    /**
     * get all the events from the database by the id of the student who created them
     *
     * @param createdBy
     * @return
     * @throws SQLException
     */
    public List<Event> findAllByCreatedBy(Long createdBy) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String sql = "SELECT * FROM event WHERE createdBy = ?;";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, createdBy);
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            events.add(event);
        }
        return events;
    }

    /**
     * get an event by it's booking id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<Event> findByBookingId(Long id) throws SQLException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String sql = "SELECT * FROM event e INNER JOIN BOOKING b ON e.id = b.eventId WHERE b.id = ?;";

        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            EventType eventType = resultSet.getString("eventType") != null ? EventType.parse(resultSet.getString("eventType")) : null;
            EventPlace eventPlace = resultSet.getString("eventPlace") != null ? EventPlace.parse(resultSet.getString("eventPlace")) : null;
            String url = resultSet.getString("url");
            String organisation = resultSet.getString("organisation");
            String location = resultSet.getString("location");
            LocalDateTime eventTime = resultSet.getString("eventTime") != null ? LocalDateTime.parse(resultSet.getString("eventTime"), formatter) : null;
            boolean requiresBooking = Boolean.parseBoolean(resultSet.getString("requiresBooking"));
            Integer numberOfPlaces = resultSet.getInt("numberOfPlaces");
            Long createdBy = resultSet.getLong("createdBy");

            final Event event = Event.builder()
                    .setId(id)
                    .setTitle(title)
                    .setDescription(description)
                    .setEventType(eventType)
                    .setEventPlace(eventPlace)
                    .setUrl(url)
                    .setOrganisation(organisation)
                    .setLocation(location)
                    .setEventTime(eventTime)
                    .setRequiresBooking(requiresBooking)
                    .setNumberOfPlaces(numberOfPlaces)
                    .setCreatedBy(createdBy);
            return Optional.of(event);
        }
        return Optional.empty();
    }


    /**
     * get the last event's id from the database
     *
     * @return
     * @throws SQLException
     */
    public Optional<Long> findLastId() throws SQLException {

        String sql = "SELECT MAX(ID) FROM event;";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong(1);

            stmt.close();

            return Optional.of(id);
        }
        return Optional.empty();
    }
}
