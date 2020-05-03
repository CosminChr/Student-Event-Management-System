package student.event.management.domain.models;

import student.event.management.domain.util.BookingStatus;

import java.util.StringJoiner;

public class Booking {

    private Long id;

    private Long studentId;

    private Long eventId;

    private BookingStatus status;

    public static Booking builder() {
        return new Booking();
    }

    private Booking() {

    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Booking setId(Long id) {
        this.id = id;
        return this;
    }

    public Booking setStudentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public Booking setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Booking setStatus(BookingStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Booking.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("studentId=" + studentId)
                .add("eventId=" + eventId)
                .add("bookingStatus=" + status)
                .toString();
    }
}
