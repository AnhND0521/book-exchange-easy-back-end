package itss.group22.bookexchangeeasy.enums;

public enum BookStatus {
    AVAILABLE(1, "Có sẵn"),
    EXCHANGED(2, "Đã trao đổi"),
    REMOVED(3, "Đã xóa");

    private final int intValue;
    private final String stringValue;

    private BookStatus(int intValue, String stringValue) {
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
