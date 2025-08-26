package mx.openpay.core.client.full;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.requests.transactions.CreateAlipayChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

public class MerchantAlipayChargeTest extends BaseTest {

    @Test
    public void testCreateAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().createCharge(new CreateAlipayChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .currency("MXN")
                .customer(new Customer()
                        .name("Test customer")
                        .email("testcustomer@example.com"))
                .redirectUrl("https://www.example.com/alipayRedirection"));

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod());
        assertNotNull(transaction.getPaymentMethod().getUrl());
        assertNotNull(transaction.getDueDate());
        assertEquals("charge_pending", transaction.getStatus());
        assertThat(amount).isEqualByComparingTo(transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        System.out.println(transaction.getPaymentMethod().getUrl());
    }

    @Test
    public void testGetAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().createCharge(new CreateAlipayChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .currency("MXN")
                .customer(new Customer()
                        .name("Test customer")
                        .email("testcustomer@example.com"))
                .redirectUrl("https://www.example.com/alipayRedirection"));

        transaction = this.api.charges().get(transaction.getId());

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod());
        assertNotNull(transaction.getPaymentMethod().getUrl());
        assertNotNull(transaction.getDueDate());
        assertEquals("charge_pending", transaction.getStatus());
        assertThat(amount).isEqualByComparingTo(transaction.getAmount());
        assertEquals(desc, transaction.getDescription());
        System.out.println(transaction.getPaymentMethod().getUrl());
    }

}
