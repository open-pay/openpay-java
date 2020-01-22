package co.openpay.core.client.full;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import co.openpay.client.Charge;
import co.openpay.client.Customer;
import co.openpay.client.core.requests.transactions.CreateAlipayChargeParams;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;

public class CustomerAlipayChargeTest extends BaseTest {

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testCreateAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().createCharge(customer.getId(), new CreateAlipayChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .currency("MXN")
                .redirectUrl("https://www.example.com/alipayRedirection"));

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod());
        assertNotNull(transaction.getPaymentMethod().getUrl());
        assertNotNull(transaction.getDueDate());
        assertEquals("charge_pending", transaction.getStatus());
        assertThat(amount, comparesEqualTo(transaction.getAmount()));
        assertEquals(desc, transaction.getDescription());
        System.out.println(transaction.getPaymentMethod().getUrl());
    }

    @Test
    public void testGetAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        Charge transaction = this.api.charges().createCharge(customer.getId(), new CreateAlipayChargeParams()
                .amount(amount)
                .description(desc)
                .orderId(orderId)
                .currency("MXN")
                .redirectUrl("https://www.example.com/alipayRedirection"));

        transaction = this.api.charges().get(customer.getId(), transaction.getId());

        assertNotNull(transaction);
        assertNotNull(transaction.getPaymentMethod());
        assertNotNull(transaction.getPaymentMethod().getUrl());
        assertNotNull(transaction.getDueDate());
        assertEquals("charge_pending", transaction.getStatus());
        assertThat(amount, comparesEqualTo(transaction.getAmount()));
        assertEquals(desc, transaction.getDescription());
        System.out.println(transaction.getPaymentMethod().getUrl());
    }

}
