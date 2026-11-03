package mx.openpay.core.client.full;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.PaymentMethod;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.operations.CustomerOperations;
import mx.openpay.client.core.requests.transactions.CreateAlipayChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAlipayChargeTest {

    @Mock
    private OpenpayAPI api;

    @Mock
    private ChargeOperations chargeOperations;

    @Mock
    private CustomerOperations customerOperations;

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        when(api.customers()).thenReturn(customerOperations);
        when(api.charges()).thenReturn(chargeOperations);

        customer = new Customer();
        customer.setId("test-customer-id");
        when(customerOperations.create(any(Customer.class))).thenReturn(customer);

        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @Test
    public void testCreateAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUrl("https://www.example.com/alipayRedirection");

        Charge expectedCharge = new Charge();
        expectedCharge.setStatus("charge_pending");
        expectedCharge.setAmount(amount);
        expectedCharge.setDescription(desc);
        expectedCharge.setPaymentMethod(paymentMethod);
        expectedCharge.setDueDate(new Date());

        when(chargeOperations.createCharge(eq(customer.getId()), any())).thenReturn(expectedCharge);

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
    }

    @Test
    public void testGetAlipayCharge() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUrl("https://www.example.com/alipayRedirection");

        Charge createdCharge = new Charge();
        createdCharge.setId("charge-alipay-id");
        createdCharge.setStatus("charge_pending");
        createdCharge.setAmount(amount);
        createdCharge.setDescription(desc);
        createdCharge.setPaymentMethod(paymentMethod);
        createdCharge.setDueDate(new Date());

        when(chargeOperations.createCharge(eq(customer.getId()), any())).thenReturn(createdCharge);
        when(chargeOperations.get(eq(customer.getId()), eq("charge-alipay-id"))).thenReturn(createdCharge);

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
    }
}
