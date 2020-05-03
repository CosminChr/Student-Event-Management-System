package student.event.management.domain.util;

public enum DayOfWeekEnum {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    DayOfWeekEnum(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public static DayOfWeekEnum parse(String type) {
        for (DayOfWeekEnum dayOfWeekEnum : DayOfWeekEnum.values()) {
            if (dayOfWeekEnum.getLabel().equals(type)) {
                return dayOfWeekEnum;
            }
        }
        return null;
    }
}
