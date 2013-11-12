package mx.openpay.client;

import static mx.openpay.client.core.OpenpayAPI.getJsonClient;
import static mx.openpay.client.core.OpenpayAPI.getMerchantId;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Customer {

    private static final String CUSTOMERS_PATH = MERCHANT_ID + CUSTOMERS;

    private static final String GET_CUSTOMER_PATH = CUSTOMERS_PATH + ID;

    public static Customer create(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("phone_number", phoneNumber);
        data.put("address", address);
        return getJsonClient().post(path, data, Customer.class);
    };

    public static List<Customer> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.CUSTOMER);
    }

    public static Customer get(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, getMerchantId(), customerId);
        return getJsonClient().get(path, Customer.class);
    };

    public static Customer update(final Customer customer) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, getMerchantId(), customer.getId());
        return getJsonClient().put(path, customer, Customer.class);
    }

    public static void delete(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, getMerchantId(), customerId);
        getJsonClient().delete(path);
    }

    private String name;

    private String id;

    private String email;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone_number")
    private String phoneNumber;

    private Address address;

    private String status;

    private BigDecimal balance;

    @SerializedName("creation_date")
    private Date creationDate;

}
