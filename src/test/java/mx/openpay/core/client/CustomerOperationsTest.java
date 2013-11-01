package mx.openpay.core.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Transaction;
import mx.openpay.client.core.OpenPayServices;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Before;
import org.junit.Test;

import static mx.openpay.core.client.TestConstans.*;

public class CustomerOperationsTest {

	private OpenPayServices openPayServices;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Before
	public void setUp() throws Exception {
		this.openPayServices = new OpenPayServices(ENDPOINT, API_KEY, MERCHANT_ID);
	}

	@Test
	public void testCollectFunds() throws ServiceUnavailable, HttpError {
		String customerId = "afk4csrazjp1udezj1po";
		Double amount = 10000.00;
		String desc = "Pago de taxi";

		List<Card> cards = this.openPayServices.getCards(customerId, 0, 10);
		Assert.assertNotNull(cards);

		String orderId = this.dateFormat.format(new Date());
		Transaction transaction = this.openPayServices.collectFunds(customerId, cards.get(0).getId(), amount, desc, orderId);
		Assert.assertNotNull(transaction);
		Assert.assertEquals(amount, transaction.getAmount());
		Assert.assertEquals(desc, transaction.getDescription());
	}

	@Test
	public void testSendFunds() throws ServiceUnavailable, HttpError {
		String customerId = "afk4csrazjp1udezj1po";
		Double amount = 1.00;
		String desc = "Ganancias";

		List<BankAccount> bankAccounts = this.openPayServices.getBankAccounts(customerId, 0, 10);
		Assert.assertNotNull(bankAccounts);

		String orderId = this.dateFormat.format(new Date());
		Transaction transaction = this.openPayServices.sendFunds(customerId, bankAccounts.get(0).getId(), amount, desc, orderId);
		Assert.assertNotNull(transaction);
		Assert.assertNotNull(transaction.getCreationDate());
		Assert.assertEquals(amount, transaction.getAmount());
		Assert.assertEquals(desc, transaction.getDescription());
		Assert.assertEquals(customerId, transaction.getCustomer().getId());
	}

	@Test
	public void testGetCustomers() throws ServiceUnavailable, HttpError {
		List<Customer> customers = this.openPayServices.getCustomers(0, 100);
		Assert.assertNotNull(customers);
		for (Customer customer : customers) {
			Assert.assertNotNull(customer.getId());
			Assert.assertNotNull(customer.getBalance());
			Assert.assertNotNull(customer.getCreationDate());
			Assert.assertNotNull(customer.getEmail());
			Assert.assertNotNull(customer.getName());
			Assert.assertNotNull(customer.getStatus());
			Assert.assertNotNull(customer.getAddress());
		}
	}

	@Test
	public void testGetAndUpdateCustomer() throws ServiceUnavailable, HttpError {
		String customerId = "afk4csrazjp1udezj1po";
		Customer customer = this.openPayServices.getCustomer(customerId);
		Assert.assertNotNull(customer);

		customer.setName("Juanito 2");
		customer.setCreationDate(null);
		customer.setBalance(null);
		customer = this.openPayServices.updateCustomer(customer);
		Assert.assertEquals("Juanito 2", customer.getName());
	}

	@Test
	public void testActivateAndInactivateCustomer() throws ServiceUnavailable, HttpError {
		String customerId = "afk4csrazjp1udezj1po";
		Customer customer = this.openPayServices.getCustomer(customerId);
		Assert.assertNotNull(customer);

		if (customer.getStatus().equals("ACTIVE")) {
			customer = this.openPayServices.inactivateCustomer(customer.getId());
			Assert.assertEquals("INACTIVE", customer.getStatus());
			customer = this.openPayServices.activateCustomer(customer.getId());
			Assert.assertEquals("ACTIVE", customer.getStatus());
		} else {
			customer = this.openPayServices.activateCustomer(customer.getId());
			Assert.assertEquals("ACTIVE", customer.getStatus());
			customer = this.openPayServices.inactivateCustomer(customer.getId());
			Assert.assertEquals("INACTIVE", customer.getStatus());
		}
	}

	@Test
	public void testCreateCustomer() throws ServiceUnavailable, HttpError {
		Address address = new Address();
		address.setCity("Distrito Federal");
		address.setExteriorNumber("11");
		address.setInteriorNumber("01");
		address.setPostalCode("12345");
		address.setRegion("Naucalpan");
		address.setStreet("Camino Real");
		Customer customer = this.openPayServices.createCustomer("Juan", "Perez Perez", "juan.perez@gmail.com", "55-25634013", address);
		Assert.assertNotNull(customer);
		Assert.assertNotNull(customer.getId());
	}
}
