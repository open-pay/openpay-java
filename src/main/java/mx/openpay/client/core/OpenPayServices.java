package mx.openpay.client.core;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Transaction;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

public class OpenPayServices {

	private static String VERSION = "v1";

	private static String CARD_PATH = "/%s/customers/%s/cards";

	private static String CUSTOMER_PATH = "/%s/customers";

	private static String MERCHANT_PATH = "/%s";

	private static String BANK_PATH = "/%s/customers/%s/bankaccounts";

	private static String TRANSACTIONS_PATH = "/%s/transactions";

	private final String merchantId;

	private HttpClient serviceClient;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static final String HTTP_RESOURCE_SEPARATOR = "/";

	public OpenPayServices(final String location, final String apiKey, final String merchantId) {
		if (location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
			this.serviceClient = new HttpClient(location + VERSION, apiKey);
		} else {
			this.serviceClient = new HttpClient(location + HTTP_RESOURCE_SEPARATOR + VERSION, apiKey);
		}
		this.merchantId = merchantId;
	}

	// MERCHANT

	public Transaction collectFee(String customerId, Double amount, String desc, String orderID) throws ServiceUnavailable, HttpError {
		String path = String.format(MERCHANT_PATH, this.merchantId) + "/fees";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("customer_id", customerId);
		data.put("amount", amount);
		data.put("description", desc);
		data.put("order_id", orderID);
		return this.serviceClient.post(path, data, Transaction.class);
	}

	public List<Customer> getCustomers(Integer offset, Integer limit) throws ServiceUnavailable, HttpError {
		String path = String.format(MERCHANT_PATH, this.merchantId) + "/customers";
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", String.valueOf(limit));
		params.put("offset", String.valueOf(offset));
		return this.serviceClient.getList(path, params, new TypeToken<Collection<Customer>>() {
		}.getType());
	}

	public List<Transaction> getCollectedFees(Date fromDate, Date toDate) throws ServiceUnavailable, HttpError {
		String path = String.format(MERCHANT_PATH, this.merchantId) + "/fees";
		Map<String, String> params = new HashMap<String, String>();
		params.put("from", this.simpleDateFormat.format(fromDate));
		params.put("to", this.simpleDateFormat.format(toDate));
		return this.serviceClient.getList(path, params, new TypeToken<Collection<Transaction>>() {
		}.getType());
	}

	// CUSTOMER

	public Customer createCustomer(final String name, final String lastName, final String email, final String phoneNumber,
			final Address address) throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("last_name", lastName);
		data.put("email", email);
		data.put("phone_number", phoneNumber);
		data.put("address", address);
		return this.serviceClient.post(path, data, Customer.class);
	}

	public Customer updateCustomer(final Customer customer) throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customer.getId();
		return this.serviceClient.put(path, customer, Customer.class);
	}

	public Customer getCustomer(String customerId) throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId;
		return this.serviceClient.get(path, Customer.class);
	}

	public Customer activateCustomer(String customerId) throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/activate";
		return this.serviceClient.put(path, null, Customer.class);
	}

	public Customer inactivateCustomer(String customerId) throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/inactivate";
		return this.serviceClient.put(path, null, Customer.class);
	}
	
	//SEND FUNDS
	
	public Transaction sendFunds(String customerId, String destinationId, Double amount, String description, String orderID)
			throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/send_funds";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("destination_id", destinationId);
		data.put("amount", amount);
		data.put("description", description);
		data.put("order_id", orderID);
		return this.serviceClient.post(path, data, Transaction.class);
	}
	
	public Transaction transferFunds(String customerId, String destinationId, Double amount, String description, String orderID)
			throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/transfer";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("customer_id", destinationId);
		data.put("amount", amount);
		data.put("description", description);
		data.put("order_id", orderID);
		return this.serviceClient.post(path, data, Transaction.class);
	}
	
	
	//COLLECT FUNDS
	
	public Transaction collectFunds(String customerId, Card card, Double amount, String description, String orderID)
			throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/collect_funds";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("card", card);
		data.put("amount", amount);
		data.put("description", description);
		data.put("order_id", orderID);
		return this.serviceClient.post(path, data, Transaction.class);
	}

	public Transaction collectFunds(String customerId, String sourceId, Double amount, String description, String orderID)
			throws ServiceUnavailable, HttpError {
		String path = String.format(CUSTOMER_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + customerId + "/collect_funds";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("source_id", sourceId);
		data.put("amount", amount);
		data.put("description", description);
		data.put("order_id", orderID);
		return this.serviceClient.post(path, data, Transaction.class);
	}

	// CARDS

	public Card getCard(String customerId, String cardId) throws ServiceUnavailable, HttpError {
		String path = String.format(CARD_PATH, this.merchantId, customerId) + HTTP_RESOURCE_SEPARATOR + cardId;
		return this.serviceClient.get(path, Card.class);
	}

	public List<Card> getCards(String customerId, int offset, int limit) throws ServiceUnavailable, HttpError {
		String path = String.format(CARD_PATH, this.merchantId, customerId);
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", String.valueOf(limit));
		params.put("offset", String.valueOf(offset));
		return this.serviceClient.getList(path, params, new TypeToken<Collection<Card>>() {
		}.getType());
	}

	public Card createCard(String customerId, String cardNumber, String holderName, String cvv2, String expMonth, String expYear,
			Address address) throws ServiceUnavailable, HttpError {
		String path = String.format(CARD_PATH, this.merchantId, customerId);
		Map<String, Object> cardData = new HashMap<String, Object>();
		cardData.put("card_number", cardNumber);
		cardData.put("cvv2", cvv2);
		cardData.put("expiration_month", expMonth);
		cardData.put("expiration_year", expYear);
		cardData.put("holder_name", holderName);
		cardData.put("address", address);
		return this.serviceClient.post(path, cardData, Card.class);
	}

	public void deleteCard(String customerId, String cardId) throws ServiceUnavailable, HttpError {
		String path = String.format(CARD_PATH, this.merchantId, customerId) + HTTP_RESOURCE_SEPARATOR + cardId;
		this.serviceClient.delete(path);
	}

	public Card updateCard(final String customerId, final String carId, final Address address) throws ServiceUnavailable, HttpError {
		String path = String.format(CARD_PATH, this.merchantId, customerId) + HTTP_RESOURCE_SEPARATOR + carId;
		Map<String, Object> cardData = new HashMap<String, Object>();
		cardData.put("address", address);
		return this.serviceClient.put(path, cardData, Card.class);
	}
	
	// BankAccount

	public BankAccount createBank(String customerId, final String clabe) throws ServiceUnavailable, HttpError {
		String path = String.format(BANK_PATH, this.merchantId, customerId);
		Map<String, Object> bankData = new HashMap<String, Object>();
		bankData.put("clabe", clabe);
		return this.serviceClient.post(path, bankData, BankAccount.class);
	}

	public BankAccount getBank(String customerId, String bankId) throws ServiceUnavailable, HttpError {
		String path = String.format(BANK_PATH, this.merchantId, customerId) + HTTP_RESOURCE_SEPARATOR + bankId;
		return this.serviceClient.get(path, BankAccount.class);
	}

	public List<BankAccount> getBankAccounts(final String customerId, int offset, int limit) throws ServiceUnavailable, HttpError {
		String path = String.format(BANK_PATH, this.merchantId, customerId);
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", String.valueOf(limit));
		params.put("offset", String.valueOf(offset));
		return this.serviceClient.getList(path, params, new TypeToken<Collection<BankAccount>>() {
		}.getType());
	}

	public void deleteBankAccount(String customerId, String bankId) throws ServiceUnavailable, HttpError {
		String path = String.format(BANK_PATH, this.merchantId, customerId) + HTTP_RESOURCE_SEPARATOR + bankId;
		this.serviceClient.delete(path);
	}

	// Transactions

	public Transaction getTransaction(String transactionId) throws ServiceUnavailable, HttpError {
		String path = String.format(TRANSACTIONS_PATH, this.merchantId) + HTTP_RESOURCE_SEPARATOR + transactionId;
		return this.serviceClient.get(path, Transaction.class);
	}
}
