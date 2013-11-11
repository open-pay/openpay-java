package mx.openpay.client;

import static mx.openpay.client.core.OpenpayApiConfig.getJsonClient;
import static mx.openpay.client.core.OpenpayApiConfig.getMerchantId;

import java.lang.reflect.Type;
import java.util.Collection;
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
public class BankAccount {

    /**
     * 
     */
    private static final Type BANK_ACCOUNT_LIST_TYPE = new TypeToken<Collection<BankAccount>>() {
    }.getType();

    private static final String BANK_ACCOUNTS_PATH = "/%s/customers/%s/bankaccounts";

    private static final String GET_BANK_ACCOUNT = "/%s/customers/%s/bankaccounts/%s";

    public static BankAccount create(final String customerId, final String clabe, final String ownerName,
            final String alias)
            throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_ACCOUNTS_PATH, getMerchantId(), customerId);
        Map<String, Object> bankData = new HashMap<String, Object>();
        bankData.put("clabe", clabe);
        bankData.put("holder_name", ownerName);
        bankData.put("alias", alias);
        return getJsonClient().post(path, bankData, BankAccount.class);
    }

    public static List<BankAccount> getList(final String customerId, final int offset, final int limit)
            throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_ACCOUNTS_PATH, getMerchantId(), customerId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, BANK_ACCOUNT_LIST_TYPE);
    }

    public static BankAccount get(final String customerId, final String bankId) throws ServiceUnavailable,
            HttpError {
        String path = String.format(GET_BANK_ACCOUNT, getMerchantId(), customerId, bankId);
        return getJsonClient().get(path, BankAccount.class);
    }

    public static void delete(final String customerId, final String bankId) throws ServiceUnavailable, HttpError {
        String path = String.format(GET_BANK_ACCOUNT, getMerchantId(), customerId, bankId);
        getJsonClient().delete(path);
    }

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    private String holderName;

    private String clabe;

    private String alias;

    @SerializedName("bank_code")
    private String bankCode;

}
