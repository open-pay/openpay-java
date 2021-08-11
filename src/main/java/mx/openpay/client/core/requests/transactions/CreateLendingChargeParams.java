package mx.openpay.client.core.requests.transactions;

import mx.openpay.client.Customer;
import mx.openpay.client.LendingData;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;
import mx.openpay.client.enums.Currency;

import java.math.BigDecimal;

/**
 *
 */
public class CreateLendingChargeParams extends RequestBuilder {

    /**
     *
     */
    public CreateLendingChargeParams() {
        this.with("method", ChargeMethods.LENDING.name().toLowerCase());
    }

    /**
     * @param amount
     * @return
     */
    public CreateLendingChargeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * @param currency
     * @return
     */
    public CreateLendingChargeParams currency(final Currency currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency.name());
    }

    /**
     * @param currency
     * @return
     */
    public CreateLendingChargeParams currency(final String currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency);
    }

    /**
     * @param description
     * @return
     */
    public CreateLendingChargeParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * @param orderId
     * @return
     */
    public CreateLendingChargeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    /**
     * @param confirm
     * @return
     */
    public CreateLendingChargeParams confirm(final Boolean confirm) {
        return this.with("confirm", confirm);
    }

    /**
     * @param sendEmail
     * @return
     */
    public CreateLendingChargeParams sendEmail(final boolean sendEmail) {
        return this.with("send_email", sendEmail);
    }

    /**
     * @param customer
     * @return
     */
    public CreateLendingChargeParams customer(final Customer customer) {
        return this.with("customer", customer);
    }

    /**
     * @param lendingData
     * @return
     */
    public CreateLendingChargeParams lendingData(final LendingData lendingData) {
        return this.with("lending_data", lendingData);
    }

}
