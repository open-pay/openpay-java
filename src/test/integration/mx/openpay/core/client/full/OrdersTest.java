/**
 * 
 */
package mx.openpay.core.client.full;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Order;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luis Delucio
 *
 */
public class OrdersTest extends BaseTest {

	private final String paymentPlanId = "apgvpxtsvlhrfaa6anem";
	
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Manuelito").email("manuelito.perez@gmail.com")
                .phoneNumber("55-25634014"));
    }

    @After
    public void tearDown() throws Exception {
        //this.api.customers().delete(this.customer.getId());
    }

	

	@Test
	public void createOrderTest() throws OpenpayServiceException, ServiceUnavailableException {
		String customerId = customer.getId();
		BigDecimal orderAmount = new BigDecimal("3500.00");
		Order request = new Order().description("producto de prueba 123444").amount(orderAmount)
				.paymentPlanId(this.paymentPlanId);
		Order newOrder = this.api.orders().create(customerId, request);
		assertEquals(customerId, newOrder.getCustomerId());
		assertEquals(this.paymentPlanId, newOrder.getPaymentPlanId());
		assertEquals("waiting_first_pay", newOrder.getStatus());
		assertThat(newOrder.getAmount(), equalTo(orderAmount));
		assertThat(newOrder.getTotalAmountToPay(), equalTo(orderAmount));
		assertThat(newOrder.getTotalAmountPaid(), equalTo(BigDecimal.ZERO));
		assertThat(newOrder.getNumberOfPaymentsMade(), equalTo(new Integer("0")));
		assertThat(newOrder.getMaximunNumberOfPayments(), equalTo(new Integer("5")));
		this.assertNotNullValues(newOrder);
	}

	@Test
	public void cancelOrderTest() throws OpenpayServiceException, ServiceUnavailableException {
		String customerId = customer.getId();
		BigDecimal orderAmount = new BigDecimal("3500.00");
		Order request = new Order().description("producto de prueba 123444").amount(orderAmount)
				.paymentPlanId(this.paymentPlanId);
		Order newOrder = this.api.orders().create(customerId, request);
		this.assertNotNullValues(newOrder);
		this.api.orders().delete(customerId, newOrder.getId());
		try {
			this.api.orders().get(customerId, newOrder.getId());
		} catch (OpenpayServiceException e) {
			assertThat(e.getHttpCode(), equalTo(404));
			assertThat(e.getErrorCode(), equalTo(1005));
		}

	}

	@Test
	public void getOrderTest() throws OpenpayServiceException, ServiceUnavailableException {
		String customerId = customer.getId();
		BigDecimal orderAmount = new BigDecimal("3500.00");
		Order request = new Order().description("producto de prueba 123444").amount(orderAmount)
				.paymentPlanId(this.paymentPlanId);
		Order newOrder = this.api.orders().create(customerId, request);
		Order order = this.api.orders().get(customerId, newOrder.getId());
		this.assertNotNullValues(order);
	}

	@Test
	public void getOrderList() throws OpenpayServiceException, ServiceUnavailableException {
		String customerId = customer.getId();
		List<Order> orderList = this.api.orders().list(customerId, null);
		assertNotNull(orderList);
		assertTrue(orderList.size() > 1);
	}

	private void assertNotNullValues(final Order order) {
		assertNotNull(order);
		assertNotNull(order.getId());
		assertNotNull(order.getCreationDate());
		assertNotNull(order.getLimitDate());
		assertNotNull(order.getFirstPayLimitDate());
		assertNotNull(order.getReference());
		assertNotNull(order.getCustomerId());
		assertNotNull(order.getPaymentPlanId());
		assertNotNull(order.getStatus());
		assertNotNull(order.getAmount());
		assertNotNull(order.getTotalAmountToPay());
		assertNotNull(order.getTotalAmountPaid());
		assertNotNull(order.getNumberOfPaymentsMade());
		assertNotNull(order.getMaximunNumberOfPayments());
		assertNotNull(order.getBarcodeUrl());
	}
}
