package mx.openpay.client.core.requests.transactions;

import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Request builder for QR charges against the public {@code /charges} endpoint.
 */
public class CreateQrChargeParams extends RequestBuilder {

    public CreateQrChargeParams() {
        this.with("method", ChargeMethods.QR.name().toLowerCase());
    }

    public CreateQrChargeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * Sends iva
     */
    public CreateQrChargeParams iva(final String iva) {
        return this.with("iva", iva);
    }

    public CreateQrChargeParams currency(final String currency) {
        return this.with("currency", currency);
    }

    public CreateQrChargeParams description(final String description) {
        return this.with("description", description);
    }

    public CreateQrChargeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    public CreateQrChargeParams originChannel(final String originChannel) {
        return this.with("origin_channel", originChannel);
    }

    public CreateQrChargeParams productType(final String productType) {
        return this.with("product_type", productType);
    }

    public CreateQrChargeParams customer(final Customer customer) {
        return this.with("customer", customer);
    }

    public CreateQrChargeParams dueDate(final Date dueDate) {
        return this.with("due_date", dueDate);
    }
}
