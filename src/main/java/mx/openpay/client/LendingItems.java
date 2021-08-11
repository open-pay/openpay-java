package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LendingItems {

    private String name;

    private String description;

    private Integer quantity;

    private double price;

    private double tax;

    private String sku;

    private double discount;

    private String currency;

}
