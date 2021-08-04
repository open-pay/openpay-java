/**
 * 
 */
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.ORDERS;

import java.util.List;
import java.util.Map;
import mx.openpay.client.Order;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author Luis Delucio
 *
 */
@Deprecated
public class OrderOperations extends ServiceOperations {

	private static final String CUSTOMER_ORDERS_PATH = MERCHANT_ID + CUSTOMERS + ID + ORDERS;

	private static final String CUSTOMER_ORDER_PATH = MERCHANT_ID + CUSTOMERS + ID + ORDERS + ID;

	public OrderOperations(final JsonServiceClient client) {
		super(client);
	}

	public Order create(final String customerId, final Order request) throws OpenpayServiceException,
			ServiceUnavailableException {
		String path = String.format(CUSTOMER_ORDERS_PATH, this.getMerchantId(), customerId);
		return this.getJsonClient().post(path, request, Order.class);
	}

	public void delete(final String customerId, final String orderId) throws OpenpayServiceException,
			ServiceUnavailableException {
		String path = String.format(CUSTOMER_ORDER_PATH, this.getMerchantId(), customerId, orderId);
		this.getJsonClient().delete(path);
	};

	public Order get(final String customerId, final String orderId) throws OpenpayServiceException,
			ServiceUnavailableException {
		String path = String.format(CUSTOMER_ORDER_PATH, this.getMerchantId(), customerId, orderId);
		return this.getJsonClient().get(path, Order.class);
	};

	public List<Order> list(final String customerId, final SearchParams params) throws OpenpayServiceException,
			ServiceUnavailableException {
		String path = String.format(CUSTOMER_ORDERS_PATH, this.getMerchantId(), customerId);
		Map<String, String> map = params == null ? null : params.asMap();
		return this.getJsonClient().list(path, map, Order.class);
	};

}
