package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Fee;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeeOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testCreate() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal feeAmount = new BigDecimal("10.00");
        String desc = "Comisión general";
        String orderId = this.dateFormat.format(new Date());

        Fee transaction = Fee.create(customerId, feeAmount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_ZeroAmount() throws Exception {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal feeAmount = new BigDecimal("10.00");
        String desc = "Comisión general";
        String orderId = this.dateFormat.format(new Date());
        try {
            Fee.create(customerId, feeAmount, desc, orderId);
        } catch (OpenpayServiceException e) {
            assertEquals(422, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList() throws Exception {
        List<Fee> fees = Fee.list(search().limit(3));
        assertEquals(3, fees.size());
    }

    @Test
    public void testList_Empty() throws Exception {
        List<Fee> fees = Fee.list(search().limit(3).offset(100000));
        assertEquals(0, fees.size());
    }

}
