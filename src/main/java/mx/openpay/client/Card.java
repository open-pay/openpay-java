package mx.openpay.client;

import static mx.openpay.client.core.OpenpayApiConfig.getJsonClient;
import static mx.openpay.client.core.OpenpayApiConfig.getMerchantId;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

@Getter
@Setter
public class Card {

    private static final Type CARD_LIST_TYPE = new TypeToken<Collection<Card>>() {
    }.getType();

    private static final String CARDS_PATH = "/%s/customers/%s/cards";

    private static final String GET_CARD_PATH = "/%s/customers/%s/cards/%s";

    public static Card create(final String customerId, final String cardNumber, final String holderName,
            final String cvv2, final String expMonth, final String expYear, final Address address)
            throws ServiceUnavailable, HttpError {
        String path = String.format(CARDS_PATH, getMerchantId(), customerId);
        Map<String, Object> cardData = new HashMap<String, Object>();
        cardData.put("card_number", cardNumber);
        cardData.put("cvv2", cvv2);
        cardData.put("expiration_month", expMonth);
        cardData.put("expiration_year", expYear);
        cardData.put("holder_name", holderName);
        cardData.put("address", address);
        return getJsonClient().post(path, cardData, Card.class);
    }

    public static List<Card> getList(final String customerId, final int offset, final int limit)
            throws ServiceUnavailable, HttpError {
        String path = String.format(CARDS_PATH, getMerchantId(), customerId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, CARD_LIST_TYPE);
    }

    public static Card get(final String customerId, final String cardId) throws ServiceUnavailable, HttpError {
        String path = String.format(GET_CARD_PATH, getMerchantId(), customerId, cardId);
        return getJsonClient().get(path, Card.class);
    }

    public static void delete(final String customerId, final String cardId) throws ServiceUnavailable, HttpError {
        String path = String.format(GET_CARD_PATH, getMerchantId(), customerId, cardId);
        getJsonClient().delete(path);
    }

    @SerializedName("creation_date")
    private Date creationDate;

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("allows_deposits")
    private boolean allowsDeposits;

    @SerializedName("holder_name")
    private String holderName;

    @SerializedName("expiration_month")
    private String expirationMonth;

    private Address address;

    @SerializedName("card_number")
    private String cardNumber;

    private String brand;

    @SerializedName("expiration_year")
    private String expirationYear;

    @SerializedName("allows_withdrawals")
    private boolean allowsWithdrawals;

    @SerializedName("bank_code")
    private String bankCode;

    private String type;

}
