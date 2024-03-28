package itss.group22.bookexchangeeasy.enums;

public enum Gender {
    MALE(1, "Nam"),
    FEMALE(2, "Nữ"),
    OTHER(3, "Khác");

    private final int intValue;
    private final String stringValue;

    private Gender(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int intValue() {
        return intValue;
    }

    public String stringValue() {
        return stringValue;
    }
}
