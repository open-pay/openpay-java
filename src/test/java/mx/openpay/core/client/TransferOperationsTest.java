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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.openpay.client.Transfer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.enums.TransactionType;
import mx.openpay.client.exceptions.HttpError;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class TransferOperationsTest {

    String customerId = "afk4csrazjp1udezj1po";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testCreateTransfer() throws Exception {
        String orderId = this.dateFormat.format(new Date());
        Transfer transfer = Transfer.create(this.customerId, "agdn58ngcnogqmzruz1i", new BigDecimal("10.0"),
                "Una descripcion", orderId);
        assertNotNull(transfer.getId());
        assertNotNull(transfer.getCreationDate());
        assertNull(transfer.getCard());
        assertNull(transfer.getBankAccount());
        assertEquals(orderId, transfer.getOrderId());
    }

    @Test
    public void testGetMerchantTransfer() throws Exception {
        String transactionId = "tvyuad0uzf5mtq4uvywd";
        Transfer transfer = Transfer.get(transactionId);
        assertEquals(transactionId, transfer.getId());
        assertEquals("OID12345", transfer.getOrderId());
        assertEquals(TransactionType.TRANSFER_FROM.name().toLowerCase(), transfer.getTransactionType());
    }

    @Test
    public void testGetMerchantTransferTo() throws Exception {
        String transactionId = "tvyuad0uzf5mtq4uvywd";
        Transfer transfer = Transfer.get(transactionId, TransactionType.TRANSFER_TO);
        assertEquals(transactionId, transfer.getId());
        assertNull(transfer.getOrderId());
        assertEquals(TransactionType.TRANSFER_TO.name().toLowerCase(), transfer.getTransactionType());
    }

    @Test
    public void testGetMerchantTransferInvalidType() throws Exception {
        String transactionId = "tvyuad0uzf5mtq4uvywd";
        try {
            Transfer.get(transactionId, TransactionType.SPEI);
            fail();
        } catch (HttpError e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testGetCustomerTransfer() throws Exception {
        String transactionId = "tvyuad0uzf5mtq4uvywd";
        Transfer transfer = Transfer.get(this.customerId, transactionId);
        assertEquals(transactionId, transfer.getId());
        assertEquals("OID12345", transfer.getOrderId());
    }

    @Test
    public void testGetListTransfers() throws Exception {
        Transfer.getList(search().limit(2));
    }
}
