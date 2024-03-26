package itss.group22.bookexchangeeasy.enums;

public enum ExchangeItemType {
    BOOK(1, "Trao đổi bằng sách"),
    MONEY(2, "Trao đổi bằng tiền");

    private final int intValue;
    private final String stringValue;

    private ExchangeItemType(int intValue, String stringValue) {
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
