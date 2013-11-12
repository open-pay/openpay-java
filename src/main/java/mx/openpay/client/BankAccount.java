package mx.openpay.client;

import static mx.openpay.client.core.OpenpayAPI.getJsonClient;
import static mx.openpay.client.core.OpenpayAPI.getMerchantId;
import static mx.openpay.client.utils.OpenpayPathComponents.BANK_ACCOUNTS;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

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
public class BankAccount {

    private static final String BANK_ACCOUNTS_PATH = MERCHANT_ID + CUSTOMERS + ID + BANK_ACCOUNTS;

    private static final String GET_BANK_ACCOUNT = BANK_ACCOUNTS_PATH + ID;

    public static BankAccount create(final String customerId, final String clabe, final String ownerName,
            final String alias)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(BANK_ACCOUNTS_PATH, getMerchantId(), customerId);
        Map<String, Object> bankData = new HashMap<String, Object>();
        bankData.put("clabe", clabe);
        bankData.put("holder_name", ownerName);
        bankData.put("alias", alias);
        return getJsonClient().post(path, bankData, BankAccount.class);
    }

    public static List<BankAccount> getList(final String customerId, final SearchParams params)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(BANK_ACCOUNTS_PATH, getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.BANK_ACCOUNT);
    }

    public static BankAccount get(final String customerId, final String bankId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_BANK_ACCOUNT, getMerchantId(), customerId, bankId);
        return getJsonClient().get(path, BankAccount.class);
    }

    public static void delete(final String customerId, final String bankId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(GET_BANK_ACCOUNT, getMerchantId(), customerId, bankId);
        getJsonClient().delete(path);
    }

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("holder_name")
    private String accountOwner;

    private String clabe;

    private String alias;

    @SerializedName("bank_code")
    private String bankCode;

}
