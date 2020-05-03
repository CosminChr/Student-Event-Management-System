package student.event.management.domain.util;

public enum EventPlace {

    INTERNAL("Internal"),
    EXTERNAL("External");

    EventPlace(String place) {
        this.place = place;
    }

    private String place;

    public String getPlace() {
        return place;
    }

    public static EventPlace parse(String type) {
        for (EventPlace eventPlace : EventPlace.values()) {
            if (eventPlace.getPlace().equals(type)) {
                return eventPlace;
            }
        }
        return null;
    }

}
