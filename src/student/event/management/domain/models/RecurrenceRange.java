package student.event.management.domain.models;

import java.time.LocalDate;
import java.util.StringJoiner;

public class RecurrenceRange {

    private Long id;

    private LocalDate startDate;

    private LocalDate endByDate;

    private Integer endAfter;

    private Boolean noEndDate;

    private RecurrenceRange() {

    }

    public static RecurrenceRange builder() {
        return new RecurrenceRange();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndByDate() {
        return endByDate;
    }

    public Integer getEndAfter() {
        return endAfter;
    }

    public Boolean getNoEndDate() {
        return noEndDate;
    }

    public RecurrenceRange setId(Long id) {
        this.id = id;
        return  this;
    }

    public RecurrenceRange setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public RecurrenceRange setEndByDate(LocalDate endByDate) {
        this.endByDate = endByDate;
        return this;
    }

    public RecurrenceRange setEndAfter(Integer endAfter) {
        this.endAfter = endAfter;
        return this;
    }

    public RecurrenceRange setNoEndDate(Boolean noEndDate) {
        this.noEndDate = noEndDate;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RecurrenceRange.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("startDate=" + startDate)
                .add("endByDate=" + endByDate)
                .add("endAfter=" + endAfter)
                .add("noEndDate=" + noEndDate)
                .toString();
    }
}
