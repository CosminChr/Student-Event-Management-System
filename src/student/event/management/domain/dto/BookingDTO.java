package student.event.management.domain.dto;

import student.event.management.domain.models.Booking;
import student.event.management.domain.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class BookingDTO {

    private Long id;
    private String studentFirstName;
    private String studentLastName;
    private String eventTitle;
    private boolean isRecurrent;
    private LocalDateTime eventTime;
    private LocalDateTime startTime;
    private BookingStatus status;

    private BookingDTO() {
    }

    public static BookingDTO builder() {
        return new BookingDTO();
    }

    public Long getId() {
        return id;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public boolean isRecurrent() {
        return isRecurrent;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public BookingDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public BookingDTO setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
        return this;
    }

    public BookingDTO setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
        return this;
    }

    public BookingDTO setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
        return this;
    }

    public BookingDTO setRecurrent(boolean recurrent) {
        isRecurrent = recurrent;
        return this;
    }

    public BookingDTO setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public BookingDTO setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public BookingDTO setStatus(BookingStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BookingDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("studentFirstName='" + studentFirstName + "'")
                .add("studentLastName='" + studentLastName + "'")
                .add("eventTitle='" + eventTitle + "'")
                .add("isRecurrent=" + isRecurrent)
                .add("eventTime=" + eventTime)
                .add("startTime=" + startTime)
                .add("status=" + status)
                .toString();
    }
}
