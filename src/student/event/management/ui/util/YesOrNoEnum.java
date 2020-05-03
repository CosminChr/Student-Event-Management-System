package student.event.management.ui.util;

public enum YesOrNoEnum {

    YES("Yes"),
    NO("No");

    YesOrNoEnum(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public static YesOrNoEnum parse(String type) {
        for (YesOrNoEnum yesOrNoEnum : YesOrNoEnum.values()) {
            if (yesOrNoEnum.getLabel().equals(type)) {
                return yesOrNoEnum;
            }
        }
        return null;
    }
}
