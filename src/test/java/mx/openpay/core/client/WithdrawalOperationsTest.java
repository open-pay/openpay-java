/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Withdrawal;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class WithdrawalOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testGetTransaction() throws ServiceUnavailable, HttpError {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Withdrawal transaction = Withdrawal.get(transactionId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testWithdrawal() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        List<BankAccount> bankAccounts = BankAccount.getList(customerId, search().offset(0).limit(10));
        Assert.assertNotNull(bankAccounts);

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForCustomer(customerId, bankAccounts.get(0).getId(),
                amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

}
