/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Ewallet;
import mx.openpay.client.OpenPayServices;
import mx.openpay.client.Transaction;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import com.google.gson.reflect.TypeToken;

/**
 * @author Heber Lazcano, heber.lazcano@gmail.com
 */
public class OpenPayServicesImpl implements OpenPayServices {

    private static String VERSION = "v1";

    private static String CARD_PATH = "/%s/ewallets/%s/cards";

    private static String EWALLET_PATH = "/%s/ewallets";

    private static String CUSTOMER_PATH = "/%s";

    private static String BANK_PATH = "/%s/ewallets/%s/bankaccounts";

    private final String customerId;

    private Client client;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    public OpenPayServicesImpl(final String customerId, final String apiKey, final String location) {
        if (location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            this.client = new Client(location + VERSION, apiKey);
        } else {
            this.client = new Client(location + HTTP_RESOURCE_SEPARATOR + VERSION, apiKey);
        }
        this.customerId = customerId;
    }

    // CUSTOMER

    public Transaction collectFee(String ewalletID, Double amount, String desc, String orderID)
            throws ServiceUnavailable, HttpError {
        String path = String.format(CUSTOMER_PATH, this.customerId) + "/collect_fees";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("ewallet_id", ewalletID);
        data.put("amount", amount);
        data.put("description", desc);
        data.put("order_id", orderID);
        return this.client.post(path, data, Transaction.class);
    }

    public List<Ewallet> getEwallets(Integer offset, Integer limit) throws ServiceUnavailable, HttpError {
        String path = String.format(CUSTOMER_PATH, this.customerId) + "/ewallets";
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return this.client.getList(path, params, new TypeToken<Collection<Ewallet>>() {
        }.getType());
    }

    public List<Transaction> getCollectedFees(Date fromDate, Date toDate) throws ServiceUnavailable, HttpError {
        String path = String.format(CUSTOMER_PATH, this.customerId) + "/fees";
        Map<String, String> params = new HashMap<String, String>();
        params.put("from", this.simpleDateFormat.format(fromDate));
        params.put("to", this.simpleDateFormat.format(toDate));
        return this.client.getList(path, params, new TypeToken<Collection<Transaction>>() {
        }.getType());
    }

    // EWALLET

    public Ewallet createEwallet(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("phone_number", phoneNumber);
        data.put("address", address);
        return this.client.post(path, data, Ewallet.class);
    }

    public Ewallet updateEwallet(final Ewallet ewallet) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewallet.getId();
        return this.client.put(path, ewallet, Ewallet.class);
    }

    public Ewallet getEwallet(String ewalletId) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId;
        return this.client.get(path, Ewallet.class);
    }

    public Ewallet activateEwallet(String ewalletId) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId + "/activate";
        return this.client.put(path, null, Ewallet.class);
    }

    public Ewallet inactivateEwallet(String ewalletId) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId
                + "/inactivate";
        return this.client.put(path, null, Ewallet.class);
    }

    public Double getBalance(String ewalletId) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId + "/balance";
        return this.client.get(path, Double.class);
    }

    public Transaction sendFunds(String ewalletId, String destinationId, Double amount, String description,
            String orderID) throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId
                + "/send_funds";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("destination_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return this.client.post(path, data, Transaction.class);
    }

    public Transaction collectFunds(String ewalletId, String sourceId, Double amount, String description, String orderID)
            throws ServiceUnavailable, HttpError {
        String path = String.format(EWALLET_PATH, this.customerId) + HTTP_RESOURCE_SEPARATOR + ewalletId
                + "/collect_funds";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return this.client.post(path, data, Transaction.class);
    }

    // CARDS

    public Card getCard(String ewalletId, String cardId) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + cardId;
        return this.client.get(path, Card.class);
    }

    public List<Card> getCards(String ewalletId, int offset, int limit) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return this.client.getList(path, params, new TypeToken<Collection<Card>>() {
        }.getType());
    }

    public Card createCard(String ewalletId, String cardNumber, String holderName, String cvv2, String expMonth,
            String expYear, Address address) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId);
        Map<String, Object> cardData = new HashMap<String, Object>();
        cardData.put("card_number", cardNumber);
        cardData.put("cvv2", cvv2);
        cardData.put("expiration_month", expMonth);
        cardData.put("expiration_year", expYear);
        cardData.put("holder_name", holderName);
        cardData.put("address", address);
        return this.client.post(path, cardData, Card.class);
    }

    public Card createDepositCard(String ewalletId, String cardNumber, String holderName, String bankCode) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId) + "/deposit";
        Map<String, Object> cardData = new HashMap<String, Object>();
        cardData.put("card_number", cardNumber);
        cardData.put("holder_name", holderName);
        cardData.put("bank_code", bankCode);
        return this.client.post(path, cardData, Card.class);
    }

    public Card activateCard(String ewalletId, String cardId) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + cardId
                + "/activate";
        return this.client.put(path, null, Card.class);
    }

    public Card inactivateCard(String ewalletId, String cardId) throws ServiceUnavailable, HttpError {
        String path = String.format(CARD_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + cardId
                + "/inactivate";
        return this.client.put(path, null, Card.class);
    }

    // BankAccount

    public BankAccount createBank(String ewalletId, final String clabe) throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_PATH, this.customerId, ewalletId);
        Map<String, Object> bankData = new HashMap<String, Object>();
        bankData.put("clabe", clabe);
        return this.client.post(path, bankData, BankAccount.class);
    }

    public BankAccount getBank(String ewalletId, String bankId) throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + bankId;
        return this.client.get(path, BankAccount.class);
    }

    public List<BankAccount> getBankAccounts(String ewalletId, int offset, int limit) throws ServiceUnavailable,
            HttpError {
        String path = String.format(BANK_PATH, this.customerId, ewalletId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return this.client.getList(path, params, new TypeToken<Collection<BankAccount>>() {
        }.getType());
    }

    public BankAccount activateBank(String ewalletId, String bankId) throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + bankId
                + "/activate";
        return this.client.put(path, null, BankAccount.class);
    }

    public BankAccount inactivateBank(String ewalletId, String bankId) throws ServiceUnavailable, HttpError {
        String path = String.format(BANK_PATH, this.customerId, ewalletId) + HTTP_RESOURCE_SEPARATOR + bankId
                + "/inactivate";
        return this.client.put(path, null, BankAccount.class);
    }

}
