package mx.openpay.core.client;

import mx.openpay.client.OpenPayServices;
import mx.openpay.client.Transaction;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionsOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8080/Services/";

    private String customerId = "m7psroutl8tycqtcmxly";

    private String apiKey = "e97b8bf7728242c0aa97b409a4c59236";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServices(this.customerId, this.apiKey, root);
    }
  
    @Test
    public void testGetTransaction() throws ServiceUnavailable, HttpError {
        String transactionId = "tryjgria484ta2hweaxz";
        Transaction transaction = this.openPayServices.getTransaction(transactionId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getAmount());
    }
}
