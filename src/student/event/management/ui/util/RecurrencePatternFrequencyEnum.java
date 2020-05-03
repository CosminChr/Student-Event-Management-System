package student.event.management.ui.util;

import student.event.management.domain.util.MonthEnum;

public enum RecurrencePatternFrequencyEnum {

    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    RecurrencePatternFrequencyEnum(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public static RecurrencePatternFrequencyEnum parse(String type) {
        for (RecurrencePatternFrequencyEnum recurrencePatternFrequencyEnum : RecurrencePatternFrequencyEnum.values()) {
            if (recurrencePatternFrequencyEnum.getLabel().equals(type)) {
                return recurrencePatternFrequencyEnum;
            }
        }
        return null;
    }
}
