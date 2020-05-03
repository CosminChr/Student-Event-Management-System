package student.event.management.domain.util;

public enum EventType {

    IN_PERSON("In person"),
    ONLINE("Online");

    EventType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public static EventType parse(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getType().equals(type)) {
                return eventType;
            }
        }
        return null;
    }
}
