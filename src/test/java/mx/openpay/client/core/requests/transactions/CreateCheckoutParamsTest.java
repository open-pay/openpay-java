package mx.openpay.client.core.requests.transactions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Map;

import mx.openpay.client.Customer;
import mx.openpay.client.Taxes;
import mx.openpay.client.enums.Currency;
import org.junit.jupiter.api.Test;

class CreateCheckoutParamsTest {

    @Test
    void shouldMapAllCheckoutParameters() {
        Taxes taxes = new Taxes()
                .baseAmount(new BigDecimal("80.00"))
                .ivaAmount(new BigDecimal("15.00"))
                .consumptionTaxAmount(new BigDecimal("3.00"))
                .airportTax(new BigDecimal("1.50"));

        Customer customer = new Customer()
                .name("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .externalId("customer-123")
                .requiresAccount(Boolean.FALSE);

        CreateCheckoutParams params = new CreateCheckoutParams()
                .amount(new BigDecimal("100.00"))
                .description("Test checkout")
                .orderId("order-123")
                .currency(Currency.COP)
                .iva("16.00")
                .redirectUrl("https://example.com/return")
                .expirationDate("2026-06-01 20:00:00")
                .sendEmail(false)
                .redirectStorePayment(Boolean.TRUE)
                .planIdPublic("plan-public-123")
                .customer(customer)
                .taxes(taxes)
                .tip(Boolean.TRUE);

        Map<String, Object> map = params.asMap();

        assertThat(map).containsEntry("amount", new BigDecimal("100.00"));
        assertThat(map).containsEntry("description", "Test checkout");
        assertThat(map).containsEntry("order_id", "order-123");
        assertThat(map).containsEntry("currency", "COP");
        assertThat(map).containsEntry("iva", "16.00");
        assertThat(map).containsEntry("redirect_url", "https://example.com/return");
        assertThat(map).containsEntry("expiration_date", "2026-06-01 20:00:00");
        assertThat(map).containsEntry("send_email", false);
        assertThat(map).containsEntry("redirect_store_payment", Boolean.TRUE);
        assertThat(map).containsEntry("plan_id_public", "plan-public-123");
        assertThat(map).containsEntry("customer", customer);
        assertThat(map).containsEntry("taxes", taxes);
        assertThat(map).containsEntry("tip", Boolean.TRUE);
    }

    @Test
    void shouldDefaultCurrencyToMxnWhenEnumIsNull() {
        CreateCheckoutParams params = new CreateCheckoutParams()
                .currency((Currency) null);

        assertThat(params.asMap()).containsEntry("currency", "MXN");
    }

    @Test
    void shouldDefaultCurrencyToMxnWhenStringIsNull() {
        CreateCheckoutParams params = new CreateCheckoutParams()
                .currency((String) null);

        assertThat(params.asMap()).containsEntry("currency", "MXN");
    }

    @Test
    void shouldExposeUnmodifiableMap() {
        CreateCheckoutParams params = new CreateCheckoutParams().amount(new BigDecimal("10.00"));

        assertThatThrownBy(() -> params.asMap().put("description", "not allowed"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
