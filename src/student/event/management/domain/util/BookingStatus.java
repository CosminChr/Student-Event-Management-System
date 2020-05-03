package student.event.management.domain.util;

import student.event.management.ui.util.RecurrencePatternFrequencyEnum;

public enum BookingStatus {

    WAITING_FOR_APPROVAL("Waiting for approval"),
    APPROVED("Approved");

    BookingStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public static BookingStatus parse(String type) {
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            if (bookingStatus.getStatus().equals(type)) {
                return bookingStatus;
            }
        }
        return null;
    }
}
