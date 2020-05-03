package student.event.management.infrastructure.repository;

import student.event.management.domain.models.Event;
import student.event.management.domain.models.RecurrencePattern;
import student.event.management.domain.util.DayOfWeekEnum;
import student.event.management.domain.util.MonthEnum;
import student.event.management.domain.util.WeekOrdinalEnum;
import student.event.management.infrastructure.database.DatabaseHandler;
import student.event.management.ui.util.RecurrencePatternFrequencyEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RecurrencePatternRepository {

    private DatabaseHandler handler;
    private Connection conn;
    private PreparedStatement stmt;

    public RecurrencePatternRepository() {
        this.handler = DatabaseHandler.getInstance();
        this.conn = handler.getConnection();
    }

    /**
     * update the recurrencePattern into the database
     *
     * @param recurrencePattern
     * @return
     * @throws SQLException
     */
    public boolean update(RecurrencePattern recurrencePattern) throws SQLException {


        Optional<RecurrencePattern> recurrencePatternOptional = findById(recurrencePattern.getId());
        if (recurrencePatternOptional.isPresent()) {


            String sql = "UPDATE recurrencePattern set recurrenceFrequency = ?, everyXDays = ?, everyWeekday = ?, everyXWeeks = ?, monday = ?, " +
                    "tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ?, sunday = ?, xDay = ?, ofEveryXMonthsFirstOption = ?," +
                    "theXMonthly = ?, dayOfWeekMonthly = ?, ofEveryXMonthsSecondOption = ?, everyXYears = ?, month = ?," +
                    "x = ?, theXYearly = ?, dayOfWeekYearly = ?, ofX = ? where id = ?;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, recurrencePattern.getRecurrenceFrequency() != null ? recurrencePattern.getRecurrenceFrequency().getLabel() : null);
            stmt.setInt(2, recurrencePattern.getEveryXDays() != null ? recurrencePattern.getEveryXDays() : 0);
            stmt.setString(3, recurrencePattern.getEveryWeekday() != null ? recurrencePattern.getEveryWeekday().toString() : null);
            stmt.setInt(4, recurrencePattern.getEveryXWeeks() != null ? recurrencePattern.getEveryXWeeks() : 0);
            stmt.setString(5, recurrencePattern.getMonday() != null ? recurrencePattern.getMonday().toString() : null);
            stmt.setString(6, recurrencePattern.getTuesday() != null ? recurrencePattern.getTuesday().toString() : null);
            stmt.setString(7, recurrencePattern.getWednesday() != null ? recurrencePattern.getWednesday().toString() : null);
            stmt.setString(8, recurrencePattern.getThursday() != null ? recurrencePattern.getThursday().toString() : null);
            stmt.setString(9, recurrencePattern.getFriday() != null ? recurrencePattern.getFriday().toString() : null);
            stmt.setString(10, recurrencePattern.getSaturday() != null ? recurrencePattern.getSaturday().toString() : null);
            stmt.setString(11, recurrencePattern.getSunday() != null ? recurrencePattern.getSunday().toString() : null);
            stmt.setInt(12, recurrencePattern.getXDay() != null ? recurrencePattern.getXDay() : 0);
            stmt.setInt(13, recurrencePattern.getOfEveryXMonthsFirstOption() != null ? recurrencePattern.getOfEveryXMonthsFirstOption() : 0);
            stmt.setString(14, recurrencePattern.getTheXMonthly() != null ? recurrencePattern.getTheXMonthly().getOrdinal() : null);
            stmt.setString(15, recurrencePattern.getDayOfWeekMonthly() != null ? recurrencePattern.getDayOfWeekMonthly().getLabel() : null);
            stmt.setInt(16, recurrencePattern.getOfEveryXMonthsSecondOption() != null ? recurrencePattern.getOfEveryXMonthsSecondOption() : 0);
            stmt.setInt(17, recurrencePattern.getEveryXYears() != null ? recurrencePattern.getEveryXYears() : 0);
            stmt.setString(18, recurrencePattern.getMonth() != null ? recurrencePattern.getMonth().getMonth() : null);
            stmt.setInt(19, recurrencePattern.getX() != null ? recurrencePattern.getX() : 0);
            stmt.setString(20, recurrencePattern.getTheXYearly() != null ? recurrencePattern.getTheXYearly().getOrdinal() : null);
            stmt.setString(21, recurrencePattern.getDayOfWeekYearly() != null ? recurrencePattern.getDayOfWeekYearly().getLabel() : null);
            stmt.setString(22, recurrencePattern.getOfX() != null ? recurrencePattern.getOfX().getMonth() : null);
            stmt.setLong(23, recurrencePatternOptional.get().getId());
            int result = stmt.executeUpdate();
            stmt.close();
            if (result == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * save the recurrencePattern into the database
     *
     * @param recurrencePattern
     * @return
     * @throws SQLException
     */
    public boolean save(RecurrencePattern recurrencePattern) throws SQLException {

        String sql = "SELECT MAX(ID) FROM recurrencePattern;";
        stmt = conn.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        Long lastId = 0L;
        if (resultSet.next()) {
            lastId = resultSet.getLong(1);
        }
        stmt.close();
        sql = "INSERT INTO recurrencePattern (id, recurrenceFrequency, everyXDays, everyWeekday, everyXWeeks, monday, " +
                "tuesday, wednesday, thursday, friday, saturday, sunday, xDay, ofEveryXMonthsFirstOption," +
                "theXMonthly, dayOfWeekMonthly, ofEveryXMonthsSecondOption, everyXYears, month," +
                "x, theXYearly, dayOfWeekYearly, ofX) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, lastId + 1);
        stmt.setString(2, recurrencePattern.getRecurrenceFrequency() != null ? recurrencePattern.getRecurrenceFrequency().getLabel() : null);
        stmt.setInt(3, recurrencePattern.getEveryXDays() != null ? recurrencePattern.getEveryXDays() : 0);
        stmt.setString(4, recurrencePattern.getEveryWeekday() != null ? recurrencePattern.getEveryWeekday().toString() : null);
        stmt.setInt(5, recurrencePattern.getEveryXWeeks() != null ? recurrencePattern.getEveryXWeeks() : 0);
        stmt.setString(6, recurrencePattern.getMonday() != null ? recurrencePattern.getMonday().toString() : null);
        stmt.setString(7, recurrencePattern.getTuesday() != null ? recurrencePattern.getTuesday().toString() : null);
        stmt.setString(8, recurrencePattern.getWednesday() != null ? recurrencePattern.getWednesday().toString() : null);
        stmt.setString(9, recurrencePattern.getThursday() != null ? recurrencePattern.getThursday().toString() : null);
        stmt.setString(10, recurrencePattern.getFriday() != null ? recurrencePattern.getFriday().toString() : null);
        stmt.setString(11, recurrencePattern.getSaturday() != null ? recurrencePattern.getSaturday().toString() : null);
        stmt.setString(12, recurrencePattern.getSunday() != null ? recurrencePattern.getSunday().toString() : null);
        stmt.setInt(13, recurrencePattern.getXDay() != null ? recurrencePattern.getXDay() : 0);
        stmt.setInt(14, recurrencePattern.getOfEveryXMonthsFirstOption() != null ? recurrencePattern.getOfEveryXMonthsFirstOption() : 0);
        stmt.setString(15, recurrencePattern.getTheXMonthly() != null ? recurrencePattern.getTheXMonthly().getOrdinal() : null);
        stmt.setString(16, recurrencePattern.getDayOfWeekMonthly() != null ? recurrencePattern.getDayOfWeekMonthly().getLabel() : null);
        stmt.setInt(17, recurrencePattern.getOfEveryXMonthsSecondOption() != null ? recurrencePattern.getOfEveryXMonthsSecondOption() : 0);
        stmt.setInt(18, recurrencePattern.getEveryXYears() != null ? recurrencePattern.getEveryXYears() : 0);
        stmt.setString(19, recurrencePattern.getMonth() != null ? recurrencePattern.getMonth().getMonth() : null);
        stmt.setInt(20, recurrencePattern.getX() != null ? recurrencePattern.getX() : 0);
        stmt.setString(21, recurrencePattern.getTheXYearly() != null ? recurrencePattern.getTheXYearly().getOrdinal() : null);
        stmt.setString(22, recurrencePattern.getDayOfWeekYearly() != null ? recurrencePattern.getDayOfWeekYearly().getLabel() : null);
        stmt.setString(23, recurrencePattern.getOfX() != null ? recurrencePattern.getOfX().getMonth() : null);

        int result = stmt.executeUpdate();
        stmt.close();
        if (result == 1) {
            return true;
        }

        return false;
    }


    /**
     * get a recurrencePattern by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Optional<RecurrencePattern> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM recurrencePattern WHERE id = ?;";
        this.stmt = conn.prepareStatement(sql);
        this.stmt.setLong(1, id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            RecurrencePatternFrequencyEnum recurrencePatternFrequencyEnum = RecurrencePatternFrequencyEnum.parse(resultSet.getString("recurrenceFrequency"));
            Integer everyXDays = resultSet.getInt("everyXDays");
            Boolean everyWeekday = Boolean.parseBoolean(resultSet.getString("everyWeekday"));
            Integer everyXWeeks = resultSet.getInt("everyXWeeks");
            Boolean monday = Boolean.parseBoolean(resultSet.getString("monday"));
            Boolean tuesday = Boolean.parseBoolean(resultSet.getString("tuesday"));
            Boolean wednesday = Boolean.parseBoolean(resultSet.getString("wednesday"));
            Boolean thursday = Boolean.parseBoolean(resultSet.getString("thursday"));
            Boolean friday = Boolean.parseBoolean(resultSet.getString("friday"));
            Boolean saturday = Boolean.parseBoolean(resultSet.getString("saturday"));
            Boolean sunday = Boolean.parseBoolean(resultSet.getString("sunday"));
            Integer xDay = resultSet.getInt("xDay");
            Integer ofEveryXMonthsFirstOption = resultSet.getInt("ofEveryXMonthsFirstOption");
            WeekOrdinalEnum theXMonthly = WeekOrdinalEnum.parse(resultSet.getString("theXMonthly"));
            DayOfWeekEnum dayOfWeekMonthly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekMonthly"));
            Integer ofEveryXMonthsSecondOption = resultSet.getInt("ofEveryXMonthsSecondOption");
            Integer everyXYears = resultSet.getInt("everyXYears");
            MonthEnum month = MonthEnum.parse(resultSet.getString("month"));
            Integer x = Integer.parseInt(resultSet.getString("x"));
            WeekOrdinalEnum theXYearly = WeekOrdinalEnum.parse(resultSet.getString("theXYearly"));
            DayOfWeekEnum dayOfWeekYearly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekYearly"));
            MonthEnum ofX = MonthEnum.parse(resultSet.getString("ofX"));

            RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                    .setId(id)
                    .setRecurrenceFrequency(recurrencePatternFrequencyEnum)
                    .setEveryXDays(everyXDays)
                    .setEveryWeekday(everyWeekday)
                    .setEveryXWeeks(everyXWeeks)
                    .setMonday(monday)
                    .setTuesday(tuesday)
                    .setWednesday(wednesday)
                    .setThursday(thursday)
                    .setFriday(friday)
                    .setSaturday(saturday)
                    .setSunday(sunday)
                    .setxDay(xDay)
                    .setOfEveryXMonthsFirstOption(ofEveryXMonthsFirstOption)
                    .setTheXMonthly(theXMonthly)
                    .setDayOfWeekMonthly(dayOfWeekMonthly)
                    .setOfEveryXMonthsSecondOption(ofEveryXMonthsSecondOption)
                    .setEveryXYears(everyXYears)
                    .setMonth(month)
                    .setX(x)
                    .setTheXYearly(theXYearly)
                    .setDayOfWeekYearly(dayOfWeekYearly)
                    .setOfX(ofX);

            stmt.close();

            return Optional.of(recurrencePattern);
        }
        return Optional.empty();
    }


    /**
     * get the last recurrencePattern's id from the database
     *
     * @return
     * @throws SQLException
     */
    public Optional<Long> findLastId() throws SQLException {

        String sql = "SELECT MAX(ID) FROM recurrencePattern;";
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
     * get a recurrencepattern by its event
     *
     * @param event
     * @return
     * @throws SQLException
     */
    public Optional<RecurrencePattern> findByEvent(Event event) throws SQLException {

        String sql = "SELECT * from recurrencePattern rp " +
                "JOIN recurrence r ON r.recurrencePatternId = rp.id " +
                "JOIN event e ON e.id = r.eventId WHERE e.id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setLong(1, event.getId());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            RecurrencePatternFrequencyEnum recurrencePatternFrequencyEnum = resultSet.getString("recurrenceFrequency") != null ?
                    RecurrencePatternFrequencyEnum.parse(resultSet.getString("recurrenceFrequency")) : null;
            Integer everyXDays = resultSet.getInt("everyXDays");
            Boolean everyWeekday = Boolean.parseBoolean(resultSet.getString("everyWeekday"));
            Integer everyXWeeks = resultSet.getInt("everyXWeeks");
            Boolean monday = Boolean.parseBoolean(resultSet.getString("monday"));
            Boolean tuesday = Boolean.parseBoolean(resultSet.getString("tuesday"));
            Boolean wednesday = Boolean.parseBoolean(resultSet.getString("wednesday"));
            Boolean thursday = Boolean.parseBoolean(resultSet.getString("thursday"));
            Boolean friday = Boolean.parseBoolean(resultSet.getString("friday"));
            Boolean saturday = Boolean.parseBoolean(resultSet.getString("saturday"));
            Boolean sunday = Boolean.parseBoolean(resultSet.getString("sunday"));
            Integer xDay = resultSet.getInt("xDay");
            Integer ofEveryXMonthsFirstOption = resultSet.getInt("ofEveryXMonthsFirstOption");
            WeekOrdinalEnum theXMonthly = WeekOrdinalEnum.parse(resultSet.getString("theXMonthly"));
            DayOfWeekEnum dayOfWeekMonthly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekMonthly"));
            Integer ofEveryXMonthsSecondOption = resultSet.getInt("ofEveryXMonthsSecondOption");
            Integer everyXYears = resultSet.getInt("everyXYears");
            MonthEnum month = MonthEnum.parse(resultSet.getString("month"));
            Integer x = Integer.parseInt(resultSet.getString("x"));
            WeekOrdinalEnum theXYearly = WeekOrdinalEnum.parse(resultSet.getString("theXYearly"));
            DayOfWeekEnum dayOfWeekYearly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekYearly"));
            MonthEnum ofX = MonthEnum.parse(resultSet.getString("ofX"));

            RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                    .setId(id)
                    .setRecurrenceFrequency(recurrencePatternFrequencyEnum)
                    .setEveryXDays(everyXDays)
                    .setEveryWeekday(everyWeekday)
                    .setEveryXWeeks(everyXWeeks)
                    .setMonday(monday)
                    .setTuesday(tuesday)
                    .setWednesday(wednesday)
                    .setThursday(thursday)
                    .setFriday(friday)
                    .setSaturday(saturday)
                    .setSunday(sunday)
                    .setxDay(xDay)
                    .setOfEveryXMonthsFirstOption(ofEveryXMonthsFirstOption)
                    .setTheXMonthly(theXMonthly)
                    .setDayOfWeekMonthly(dayOfWeekMonthly)
                    .setOfEveryXMonthsSecondOption(ofEveryXMonthsSecondOption)
                    .setEveryXYears(everyXYears)
                    .setMonth(month)
                    .setX(x)
                    .setTheXYearly(theXYearly)
                    .setDayOfWeekYearly(dayOfWeekYearly)
                    .setOfX(ofX);

            stmt.close();

            return Optional.of(recurrencePattern);
        }
        return Optional.empty();
    }

    /**
     * get a recurrencepattern by its event title
     *
     * @param title
     * @return
     * @throws SQLException
     */
    public Optional<RecurrencePattern> findByEventTitle(String title) throws SQLException {

        String sql = "SELECT * from recurrencePattern rp " +
                "JOIN recurrence r ON r.recurrencePatternId = rp.id " +
                "JOIN event e ON e.id = r.eventId WHERE e.title = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            RecurrencePatternFrequencyEnum recurrencePatternFrequencyEnum = resultSet.getString("recurrenceFrequency") != null ?
                    RecurrencePatternFrequencyEnum.parse(resultSet.getString("recurrenceFrequency")) : null;
            Integer everyXDays = resultSet.getInt("everyXDays");
            Boolean everyWeekday = Boolean.parseBoolean(resultSet.getString("everyWeekday"));
            Integer everyXWeeks = resultSet.getInt("everyXWeeks");
            Boolean monday = Boolean.parseBoolean(resultSet.getString("monday"));
            Boolean tuesday = Boolean.parseBoolean(resultSet.getString("tuesday"));
            Boolean wednesday = Boolean.parseBoolean(resultSet.getString("wednesday"));
            Boolean thursday = Boolean.parseBoolean(resultSet.getString("thursday"));
            Boolean friday = Boolean.parseBoolean(resultSet.getString("friday"));
            Boolean saturday = Boolean.parseBoolean(resultSet.getString("saturday"));
            Boolean sunday = Boolean.parseBoolean(resultSet.getString("sunday"));
            Integer xDay = resultSet.getInt("xDay");
            Integer ofEveryXMonthsFirstOption = resultSet.getInt("ofEveryXMonthsFirstOption");
            WeekOrdinalEnum theXMonthly = WeekOrdinalEnum.parse(resultSet.getString("theXMonthly"));
            DayOfWeekEnum dayOfWeekMonthly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekMonthly"));
            Integer ofEveryXMonthsSecondOption = resultSet.getInt("ofEveryXMonthsSecondOption");
            Integer everyXYears = resultSet.getInt("everyXYears");
            MonthEnum month = MonthEnum.parse(resultSet.getString("month"));
            Integer x = Integer.parseInt(resultSet.getString("x"));
            WeekOrdinalEnum theXYearly = WeekOrdinalEnum.parse(resultSet.getString("theXYearly"));
            DayOfWeekEnum dayOfWeekYearly = DayOfWeekEnum.parse(resultSet.getString("dayOfWeekYearly"));
            MonthEnum ofX = MonthEnum.parse(resultSet.getString("ofX"));

            RecurrencePattern recurrencePattern = RecurrencePattern.builder()
                    .setId(id)
                    .setRecurrenceFrequency(recurrencePatternFrequencyEnum)
                    .setEveryXDays(everyXDays)
                    .setEveryWeekday(everyWeekday)
                    .setEveryXWeeks(everyXWeeks)
                    .setMonday(monday)
                    .setTuesday(tuesday)
                    .setWednesday(wednesday)
                    .setThursday(thursday)
                    .setFriday(friday)
                    .setSaturday(saturday)
                    .setSunday(sunday)
                    .setxDay(xDay)
                    .setOfEveryXMonthsFirstOption(ofEveryXMonthsFirstOption)
                    .setTheXMonthly(theXMonthly)
                    .setDayOfWeekMonthly(dayOfWeekMonthly)
                    .setOfEveryXMonthsSecondOption(ofEveryXMonthsSecondOption)
                    .setEveryXYears(everyXYears)
                    .setMonth(month)
                    .setX(x)
                    .setTheXYearly(theXYearly)
                    .setDayOfWeekYearly(dayOfWeekYearly)
                    .setOfX(ofX);

            stmt.close();

            return Optional.of(recurrencePattern);
        }
        return Optional.empty();
    }

    /**
     * de;ete a recurrencePattern by its id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM recurrencePattern WHERE id = ?";
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
