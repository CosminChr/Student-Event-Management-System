package student.event.management.domain.util;

public enum WeekOrdinalEnum {
    
    FIRST("First"),
    SECOND("Second"),
    THIRD("Third"),
    FOURTH("Fourth"),
    LAST("Last");

    WeekOrdinalEnum(String ordinal) {
        this.ordinal = ordinal;
    }

    private String ordinal;

    public String getOrdinal() {
        return ordinal;
    }

    public static WeekOrdinalEnum parse(String type) {
        for (WeekOrdinalEnum weekOrdinalEnum : WeekOrdinalEnum.values()) {
            if (weekOrdinalEnum.getOrdinal().equals(type)) {
                return weekOrdinalEnum;
            }
        }
        return null;
    }
}
