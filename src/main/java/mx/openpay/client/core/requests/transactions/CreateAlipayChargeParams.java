package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;
import java.util.Date;

import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;
import mx.openpay.client.enums.Currency;

/**
 * Parameters to create charges that are confirmed using Alipay.
 * @since 1.0.10
 */
public class CreateAlipayChargeParams extends RequestBuilder {

    public CreateAlipayChargeParams() {
        this.with("method", ChargeMethods.ALIPAY.name().toLowerCase());
    }

    /**
     * The amount to charge to the card. Required.
     */
    public CreateAlipayChargeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A description to give to the charge. Optional.
     */
    public CreateAlipayChargeParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * An unique custom identifier for the charge. Optional.
     */
    public CreateAlipayChargeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    /**
     * A currency to give to the charge. Optional.<br>
     * Default value is MXN<br>
     * @param currency
     * @return
     */
    public CreateAlipayChargeParams currency(final Currency currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency.name());
    }

    /**
     * A currency to give to the charge in ISO 4217 alphanumeric code. Optional.<br>
     * Default value is MXN<br>
     * @param currency
     * @return
     */
    public CreateAlipayChargeParams currency(final String currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency);
    }

    /**
     * Customer Information when you want to send info but not create the resource
     */
    public CreateAlipayChargeParams customer(final Customer customer) {
        return this.with("customer", customer);
    }

    /**
     * Due Date when you create an unconfirmed charge you may want to define the date limit to confirm.
     */
    public CreateAlipayChargeParams dueDate(final Date dueDate) {
        return this.with("due_date", dueDate);
    }

    /**
     * Redirect Url indicate the url to redirect after completed the transaction.
     */
    public CreateAlipayChargeParams redirectUrl(final String redirectUrl) {
        return this.with("redirect_url", redirectUrl);
    }

}
