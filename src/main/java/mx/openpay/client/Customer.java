package mx.openpay.client;

import static mx.openpay.client.core.OpenpayApiConfig.getJsonClient;
import static mx.openpay.client.core.OpenpayApiConfig.getMerchantId;
import static mx.openpay.client.utils.OpenpayPaths.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPaths.ID;
import static mx.openpay.client.utils.OpenpayPaths.MERCHANT_ID;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

@Getter
@Setter
@ToString
public class Customer {

    private static final String CUSTOMERS_PATH = MERCHANT_ID + CUSTOMERS;

    private static final String GET_CUSTOMER_PATH = CUSTOMERS_PATH + ID;

    private static final Type CUSTOMER_LIST_TYPE = new TypeToken<Collection<Customer>>() {
    }.getType();

    public static Customer create(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws HttpError, ServiceUnavailable {
        String path = String.format(CUSTOMERS_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("phone_number", phoneNumber);
        data.put("address", address);
        return getJsonClient().post(path, data, Customer.class);
    };

    public static List<Customer> getList(final Integer offset, final Integer limit) throws HttpError,
            ServiceUnavailable {
        String path = String.format(CUSTOMERS_PATH, getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, CUSTOMER_LIST_TYPE);
    }

    public static Customer get(final String customerId) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_CUSTOMER_PATH, getMerchantId(), customerId);
        return getJsonClient().get(path, Customer.class);
    };

    public static Customer update(final Customer customer) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_CUSTOMER_PATH, getMerchantId(), customer.getId());
        return getJsonClient().put(path, customer, Customer.class);
    }

    public static void delete(final String customerId) throws HttpError, ServiceUnavailable {
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
