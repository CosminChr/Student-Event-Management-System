package student.event.management.domain.util;

public enum MonthEnum {

    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    MonthEnum(String month) {
        this.month = month;
    }

    private String month;

    public String getMonth() {
        return month;
    }

    public static MonthEnum parse(String type) {
        for (MonthEnum monthEnum : MonthEnum.values()) {
            if (monthEnum.getMonth().equals(type)) {
                return monthEnum;
            }
        }
        return null;
    }

}
