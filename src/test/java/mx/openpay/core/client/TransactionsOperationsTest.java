package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import mx.openpay.client.Withdrawal;
import mx.openpay.client.core.OpenpayApiConfig;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionsOperationsTest {

    @Before
    public void setUp() throws Exception {
        OpenpayApiConfig.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testGetTransaction() throws ServiceUnavailable, HttpError {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Withdrawal transaction = Withdrawal.get(transactionId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getAmount());
    }
}
