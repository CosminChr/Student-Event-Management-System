package student.event.management.domain.models;

import student.event.management.domain.util.EventPlace;
import student.event.management.domain.util.EventType;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Event {

    private Long id;

    private String title;

    private String description;

    private EventType eventType;

    private EventPlace eventPlace;

    private String url;

    private String organisation;

    private String location;

    private LocalDateTime eventTime;

    private boolean requiresBooking;

    private Integer numberOfPlaces;

    private Long createdBy;

    private Event() {

    }

    public static Event builder() {
        return new Event();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventPlace getEventPlace() {
        return eventPlace;
    }

    public String getUrl() {
        return url;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public boolean isRequiresBooking() {
        return requiresBooking;
    }

    public Integer getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public Event setTitle(String title) {
        this.title = title;
        return this;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public Event setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Event setEventPlace(EventPlace eventPlace) {
        this.eventPlace = eventPlace;
        return this;
    }

    public Event setUrl(String url) {
        this.url = url;
        return this;
    }

    public Event setOrganisation(String organisation) {
        this.organisation = organisation;
        return this;
    }

    public Event setLocation(String location) {
        this.location = location;
        return this;
    }

    public Event setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public Event setRequiresBooking(boolean requiresBooking) {
        this.requiresBooking = requiresBooking;
        return this;
    }

    public Event setNumberOfPlaces(Integer numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
        return this;
    }

    public Event setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("eventType=" + eventType)
                .add("eventPlace=" + eventPlace)
                .add("url='" + url + "'")
                .add("organisation='" + organisation + "'")
                .add("location='" + location + "'")
                .add("eventTime=" + eventTime)
                .add("requiresBooking=" + requiresBooking)
                .add("numberOfPlaces=" + numberOfPlaces)
                .add("createdBy=" + createdBy)
                .toString();
    }
}
