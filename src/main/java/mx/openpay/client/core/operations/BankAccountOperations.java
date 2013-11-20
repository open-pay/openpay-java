/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.BANK_ACCOUNTS;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class BankAccountOperations extends ServiceOperations {

    private static final String BANK_ACCOUNTS_PATH = MERCHANT_ID + CUSTOMERS + ID + BANK_ACCOUNTS;

    private static final String GET_BANK_ACCOUNT = BANK_ACCOUNTS_PATH + ID;

    public BankAccountOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public BankAccount create(final String customerId, final String clabe, final String ownerName,
            final String alias)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(BANK_ACCOUNTS_PATH, this.getMerchantId(), customerId);
        Map<String, Object> bankData = new HashMap<String, Object>();
        bankData.put("clabe", clabe);
        bankData.put("holder_name", ownerName);
        bankData.put("alias", alias);
        return this.getJsonClient().post(path, bankData, BankAccount.class);
    }

    public List<BankAccount> list(final String customerId, final SearchParams params)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(BANK_ACCOUNTS_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.BANK_ACCOUNT);
    }

    public BankAccount get(final String customerId, final String bankId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_BANK_ACCOUNT, this.getMerchantId(), customerId, bankId);
        return this.getJsonClient().get(path, BankAccount.class);
    }

    public void delete(final String customerId, final String bankId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_BANK_ACCOUNT, this.getMerchantId(), customerId, bankId);
        this.getJsonClient().delete(path);
    }

}
