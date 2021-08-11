package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LendingItems {

    private String name;

    private String description;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal tax;

    private String sku;

    private BigDecimal discount;

    private String currency;

}
