package student.event.management.infrastructure.util;

import student.event.management.application.service.util.PasswordEncodingService;

public enum SqlConstants {

    CREATE_EVENT_TABLE("CREATE TABLE IF NOT EXISTS event (\n "
            + "	id INTEGER PRIMARY KEY,\n "
            + "	title TEXT NOT NULL,\n "
            + "	description TEXT NOT NULL,\n "
            + "	eventType TEXT NOT NULL,\n "
            + " eventPlace TEXT,\n "
            + " url TEXT,\n "
            + " organisation TEXT,\n "
            + "	location TEXT,\n "
            + "	eventTime DATETIME,\n "
            + " requiresBooking TEXT,\n "
            + "	numberOfPlaces INTEGER NOT NULL,\n "
            + "	createdBy INTEGER NOT NULL\n "
            + ");"),
    CREATE_ADMINISTRATOR_TABLE("CREATE TABLE IF NOT EXISTS administrator (\n "
            + "	id INTEGER PRIMARY KEY,\n "
            + "	username TEXT NOT NULL,\n "
            + "	password TEXT NOT NULL\n "
            + ");"),
    CREATE_STUDENT_TABLE("CREATE TABLE IF NOT EXISTS student (\n "
            + "	id INTEGER PRIMARY KEY,\n "
            + "	firstName TEXT NOT NULL,\n "
            + "	lastName TEXT NOT NULL,\n "
            + "	username TEXT NOT NULL,\n "
            + "	password TEXT NOT NULL,\n "
            + "	hasOrganisationRights TEXT NOT NULL\n "
            + ");"),
    CREATE_BOOKING_TABLE("CREATE TABLE IF NOT EXISTS booking (\n "
            + "	id INTEGER PRIMARY KEY,\n "
            + "	studentId INTEGER NOT NULL,\n "
            + "	eventId INTEGER NOT NULL,\n "
            + " status INTEGER NOT NULL,\n "
            + " FOREIGN KEY(studentId) REFERENCES student(id),\n "
            + " FOREIGN KEY(eventId) REFERENCES event(id)\n "
            + ");"),
    CREATE_RECURRENCE_TABLE("CREATE TABLE IF NOT EXISTS recurrence (\n "
            + " id INTEGER PRIMARY KEY,\n "
            + " startTime TIME NOT NULL,\n "
            + " eventId INTEGER NOT NULL,\n "
            + " recurrencePatternId INTEGER NOT NULL,\n "
            + " recurrenceRangeId INTEGER NOT NULL,\n "
            + " FOREIGN KEY(eventId) REFERENCES event(id),\n "
            + " FOREIGN KEY(recurrencePatternId) REFERENCES RecurrencePattern(id),\n "
            + " FOREIGN KEY(recurrenceRangeId) REFERENCES RecurrenceRange(id)\n "
            + ");"),
    CREATE_RECURRENCE_PATTERN_TABLE("CREATE TABLE IF NOT EXISTS recurrencePattern (\n "
            + " id INTEGER PRIMARY KEY,\n "
            + " recurrenceFrequency TEXT NOT NULL,\n "
            + " everyXDays INTEGER,\n "
            + " everyWeekday TEXT,\n "
            + " everyXWeeks INTEGER NOT NULL,\n "
            + " monday TEXT,\n "
            + " tuesday TEXT,\n "
            + " wednesday TEXT,\n "
            + " thursday TEXT,\n "
            + " friday TEXT,\n "
            + " saturday TEXT,\n "
            + " sunday TEXT,\n "
            + " xDay INTEGER,\n "
            + " ofEveryXMonthsFirstOption INTEGER,\n "
            + " theXMonthly TEXT,\n "
            + " dayOfWeekMonthly TEXT,\n "
            + " ofEveryXMonthsSecondOption INTEGER,\n "
            + " everyXYears INTEGER,\n "
            + " month TEXT,\n "
            + " x INTEGER,\n "
            + " theXYearly INTEGER,\n "
            + " dayOfWeekYearly TEXT,\n "
            + " ofX INTEGER\n "
            + ");"),
    CREATE_RECURRENCE_RANGE_TABLE("CREATE TABLE IF NOT EXISTS recurrenceRange (\n "
            + " id INTEGER PRIMARY KEY,\n "
            + " startDate DATE NOT NULL,\n "
            + " endByDate DATE,\n "
            + " endAfter INTEGER,\n "
            + " noEndDate TEXT\n "
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
