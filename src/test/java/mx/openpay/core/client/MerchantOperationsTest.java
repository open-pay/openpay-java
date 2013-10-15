package mx.openpay.core.client;

import java.util.List;

import mx.openpay.client.Customer;
import mx.openpay.client.OpenPayServices;
import mx.openpay.client.Transaction;
import mx.openpay.client.core.OpenPayServicesImpl;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MerchantOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8080/Services";

    private String customerId = "m7psroutl8tycqtcmxly";

    private String apiKey = "e97b8bf7728242c0aa97b409a4c59236";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testCollectFee() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        Double feeAmount = 10.00;
        String desc = "Pago de taxi";
        
        Transaction transaction = this.openPayServices.collectFee(customerId, feeAmount, desc, "12");
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getMemo());
    }
    
    @Test
    public void testGetCustomers() throws ServiceUnavailable, HttpError {
        List<Customer> customers  = this.openPayServices.getCustomers(0, 100);
        Assert.assertNotNull(customers);
        for (Customer customer : customers) {
            Assert.assertNotNull(customer.getId());
        }
    }
}
