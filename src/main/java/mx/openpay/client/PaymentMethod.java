package mx.openpay.client;

import lombok.Getter;

/**
 * @author elopez
 */
@Getter
public class PaymentMethod {

    private String type;

    private String bank;

    private String clabe;

    private String name;
}
