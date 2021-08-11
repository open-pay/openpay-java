package mx.openpay.client;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum LendingShippingType {

    HOME("HOME"),
    WORK("WORK"),
    GIFT("GIFT");

    @Getter
    private String typeStr;

    private LendingShippingType(final String typeStr) {
        this.typeStr = typeStr;
    }
}
