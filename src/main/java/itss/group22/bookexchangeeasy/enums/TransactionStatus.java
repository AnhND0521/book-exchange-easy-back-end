package itss.group22.bookexchangeeasy.enums;

public enum TransactionStatus {
    CONFIRMED(1, "Đã xác nhận"),
    EXCHANGED(2, "Đã trao đổi"),
    COMPLETED(3, "Đã hoàn tất"),
    CANCELLED(4, "Đã hủy");

    private final int intValue;
    private final String stringValue;

    private TransactionStatus(int intValue, String stringValue) {
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
