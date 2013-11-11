package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.Transaction;
import mx.openpay.client.core.OpenpayApiConfig;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MerchantOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayApiConfig.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testCollectFee() throws ServiceUnavailable, HttpError {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal feeAmount = new BigDecimal("10.00");
        String desc = "Comisión general";
        String orderId = this.dateFormat.format(new Date());

        Transaction transaction = Fee.create(customerId, feeAmount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testGetCustomers() throws ServiceUnavailable, HttpError {
        List<Customer> customers = Customer.getList(0, 100);
        Assert.assertNotNull(customers);
        for (Customer customer : customers) {
            Assert.assertNotNull(customer.getId());
        }
    }
}
