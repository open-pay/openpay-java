package mx.openpay.core.client.full;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mx.openpay.client.Taxes;
import mx.openpay.client.core.impl.DefaultSerializer;
import mx.openpay.client.core.requests.transactions.CreateCheckoutParams;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CreateCheckoutParamsUnitTest {

    @Test
    void serializesTaxesAndTipForCheckoutRequest() {
        Taxes taxes = new Taxes()
                .baseAmount(new BigDecimal("80.00"))
                .ivaAmount(new BigDecimal("15.00"))
                .consumptionTaxAmount(new BigDecimal("3.00"))
                .airportTax(new BigDecimal("1.50"));

        CreateCheckoutParams params = new CreateCheckoutParams()
                .amount(new BigDecimal("100.00"))
                .description("Test taxes Colombia")
                .currency("COP")
                .orderId("test-taxes-012")
                .redirectUrl("https://www.openpay.co/")
                .expirationDate("2026-06-01 20:00:00")
                .sendEmail(false)
                .taxes(taxes)
                .tip(true);

        String json = new DefaultSerializer().serialize(params.asMap());
        JsonObject payload = JsonParser.parseString(json).getAsJsonObject();
        JsonObject taxesPayload = payload.getAsJsonObject("taxes");

        assertThat(payload.get("amount").getAsBigDecimal()).isEqualByComparingTo("100.00");
        assertThat(payload.get("description").getAsString()).isEqualTo("Test taxes Colombia");
        assertThat(payload.get("currency").getAsString()).isEqualTo("COP");
        assertThat(payload.get("order_id").getAsString()).isEqualTo("test-taxes-012");
        assertThat(payload.get("redirect_url").getAsString()).isEqualTo("https://www.openpay.co/");
        assertThat(payload.get("expiration_date").getAsString()).isEqualTo("2026-06-01 20:00:00");
        assertThat(payload.get("send_email").getAsBoolean()).isFalse();
        assertThat(payload.get("tip").getAsBoolean()).isTrue();
        assertThat(taxesPayload.get("base_amount").getAsBigDecimal()).isEqualByComparingTo("80.00");
        assertThat(taxesPayload.get("iva_amount").getAsBigDecimal()).isEqualByComparingTo("15.00");
        assertThat(taxesPayload.get("consumption_tax_amount").getAsBigDecimal()).isEqualByComparingTo("3.00");
        assertThat(taxesPayload.get("airport_tax").getAsBigDecimal()).isEqualByComparingTo("1.50");
    }
}
