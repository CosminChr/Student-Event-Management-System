package student.event.management.domain.models;

import java.time.LocalTime;
import java.util.StringJoiner;

public class Recurrence {

    private Long id;

    private LocalTime startTime;

    private Long eventId;

    private Long recurrencePatternId;

    private Long recurrenceRangeId;

    public static Recurrence builder() {
        return new Recurrence();
    }

    private Recurrence() {
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getRecurrencePatternId() {
        return recurrencePatternId;
    }

    public Long getRecurrenceRangeId() {
        return recurrenceRangeId;
    }

    public Recurrence setId(Long id) {
        this.id = id;
        return this;
    }

    public Recurrence setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Recurrence setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Recurrence setRecurrencePatternId(Long recurrencePatternId) {
        this.recurrencePatternId = recurrencePatternId;
        return this;
    }

    public Recurrence setRecurrenceRangeId(Long recurrenceRangeId) {
        this.recurrenceRangeId = recurrenceRangeId;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Recurrence.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("startTime=" + startTime)
                .add("recurrencePatternId=" + recurrencePatternId)
                .add("recurrenceRangeId=" + recurrenceRangeId)
                .toString();
    }
}
