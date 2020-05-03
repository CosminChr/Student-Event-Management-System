package student.event.management.infrastructure.util;

public enum DatabaseConstants {

    DATABASE_URL("jdbc:sqlite:student-event-management.db");

    DatabaseConstants(String path) {
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
