package student.event.management.infrastructure.util;

import student.event.management.application.service.util.PasswordEncodingService;

public enum SqlConstants {

    CREATE_EVENT_TABLE("CREATE TABLE IF NOT EXISTS event (\n "
            + "	id integer PRIMARY KEY,\n "
            + "	title text NOT NULL,\n "
            + "	description text NOT NULL,\n "
            + "	eventType text NOT NULL,\n "
            + " eventPlace text NOT NULL,\n "
            + " url text,\n "
            + " organisation text,\n "
            + "	location text,\n "
            + "	eventTime datetime,\n "
            + " requiresBooking,\n "
            + "	numberOfPlaces integer NOT NULL,\n "
            + "	createdBy integer NOT NULL\n "
            + ");"),
    CREATE_ADMINISTRATOR_TABLE("CREATE TABLE IF NOT EXISTS administrator (\n "
            + "	id integer PRIMARY KEY,\n "
            + "	username text NOT NULL,\n "
            + "	password text NOT NULL\n "
            + ");"),
    CREATE_STUDENT_TABLE("CREATE TABLE IF NOT EXISTS student (\n "
            + "	id integer PRIMARY KEY,\n "
            + "	firstName text NOT NULL,\n "
            + "	lastName text NOT NULL,\n "
            + "	username text NOT NULL,\n "
            + "	password text NOT NULL,\n "
            + "	hasOrganisationRights text NOT NULL\n "
            + ");"),
    CREATE_BOOKING_TABLE("CREATE TABLE IF NOT EXISTS booking (\n "
            + "	id integer PRIMARY KEY,\n "
            + "	studentId integer NOT NULL,\n "
            + "	eventId integer NOT NULL,\n "
            + " status text NOT NULL,\n "
            + " FOREIGN KEY(studentId) REFERENCES student(id),\n "
            + " FOREIGN KEY(eventId) REFERENCES event(id)\n "
            + ");"),
    CREATE_RECURRENCE_TABLE("CREATE TABLE IF NOT EXISTS recurrence (\n "
            + " id integer PRIMARY KEY,\n "
            + " startTime time NOT NULL,\n "
            + " eventId integer NOT NULL,\n "
            + " recurrencePatternId integer NOT NULL,\n "
            + " recurrenceRangeId integer NOT NULL,\n "
            + " FOREIGN KEY(eventId) REFERENCES event(id),\n "
            + " FOREIGN KEY(recurrencePatternId) REFERENCES RecurrencePattern(id),\n "
            + " FOREIGN KEY(recurrenceRangeId) REFERENCES RecurrenceRange(id)\n "
            + ");"),
    CREATE_RECURRENCE_PATTERN_TABLE("CREATE TABLE IF NOT EXISTS recurrencePattern (\n "
            + " id integer PRIMARY KEY,\n "
            + " recurrenceFrequency text NOT NULL,\n "
            + " everyXDays integer,\n "
            + " everyWeekday text,\n "
            + " everyXWeeks integer NOT NULL,\n "
            + " monday text,\n "
            + " tuesday text,\n "
            + " wednesday text,\n "
            + " thursday text,\n "
            + " friday text,\n "
            + " saturday text,\n "
            + " sunday text,\n "
            + " xDay integer,\n "
            + " ofEveryXMonthsFirstOption integer,\n "
            + " theXMonthly text,\n "
            + " dayOfWeekMonthly text,\n "
            + " ofEveryXMonthsSecondOption integer,\n "
            + " everyXYears integer,\n "
            + " month text,\n "
            + " x integer,\n "
            + " theXYearly integer,\n "
            + " dayOfWeekYearly text,\n "
            + " ofX integer\n "
            + ");"),
    CREATE_RECURRENCE_RANGE_TABLE("CREATE TABLE IF NOT EXISTS recurrenceRange (\n "
            + " id integer PRIMARY KEY,\n "
            + " startDate date NOT NULL,\n "
            + " endByDate date,\n "
            + " endAfter integer,\n "
            + " noEndDate text\n "
            + ");"),
    INSERT_ADMIN("INSERT OR IGNORE INTO administrator (id, username, password) \n "
                         +  "VALUES (" + 1 + ",'"  +  "admin"  + "',"  + "'" + PasswordEncodingService.encode("admin") + "') ");

    SqlConstants(String sql) {
        this.sql = sql;
    }

    private String sql;

    public String getSql() {
        return sql;
    }
}
