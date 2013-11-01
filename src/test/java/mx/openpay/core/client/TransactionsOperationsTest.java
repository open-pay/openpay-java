package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import mx.openpay.client.Transaction;
import mx.openpay.client.core.OpenPayServices;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionsOperationsTest {

    private OpenPayServices openPayServices;

    @Before
    public void setUp() throws Exception {
    	 this.openPayServices = new OpenPayServices(ENDPOINT, API_KEY, MERCHANT_ID);
    }
  
    @Test
    public void testGetTransaction() throws ServiceUnavailable, HttpError {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Transaction transaction = this.openPayServices.getTransaction(transactionId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getAmount());
    }
}
