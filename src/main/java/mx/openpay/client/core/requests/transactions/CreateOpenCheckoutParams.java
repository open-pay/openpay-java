package mx.openpay.client.core.requests.transactions;

import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.Currency;

import java.math.BigDecimal;

public class CreateOpenCheckoutParams extends RequestBuilder {


    /**
     * The amount to charge Required.
     */
    public CreateCheckoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * The amount to charge Required.
     */
    public CreateCheckoutParams name(final String name) {
        return this.with("name", name);
    }

    /**
     * A description to give to the charge. Optional.
     */
    public CreateCheckoutParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * An unique custom identifier for the charge. Optional.
     */
    public CreateCheckoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    /**
     * A currency to give to the charge. Optional.<br>
     * Default value is MXN<br>
     *
     * @param currency
     * @return
     */
    public CreateCheckoutParams currency(final Currency currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency.name());
    }

    /**
     * A currency to give to the charge in ISO 4217 alphanumeric code. Optional.<br>
     * Default value is MXN<br>
     * @param currency
     * @return
     */
    public CreateCheckoutParams currency(final String currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency);
    }

    /**
     * Sends iva
     */
    public CreateCheckoutParams iva(final String iva) {
        return this.with("iva", iva);
    }

    /**
     * Redirect Url indicate the url to redirect after completed the transaction.
     */
    public CreateCheckoutParams redirectUrl(final String redirectUrl) {
        return this.with("redirect_url", redirectUrl);
    }

    /**
     * Expiration dato to checkout
     */
    public CreateCheckoutParams expirationDate(final String dueDate) { return this.with("expiration_date", dueDate); }



    /**
     * Flag do open amount
     */
    public CreateCheckoutParams openAmount(Boolean openAmount) {
        return this.with("open_amount", openAmount);
    }


}
