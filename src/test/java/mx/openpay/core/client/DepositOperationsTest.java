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

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Card;
import mx.openpay.client.Deposit;
import mx.openpay.client.core.OpenpayApiConfig;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class DepositOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayApiConfig.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testDeposit() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("10000.00");
        String desc = "Pago de taxi";

        List<Card> cards = Card.getList(customerId, 0, 10);
        Assert.assertNotNull(cards);

        String orderId = this.dateFormat.format(new Date());
        Deposit transaction = Deposit.create(customerId, cards.get(0).getId(), amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

}
