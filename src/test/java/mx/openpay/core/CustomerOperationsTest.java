package mx.openpay.core;

import java.util.List;

import mx.openpay.Ewallet;
import mx.openpay.OpenPayServices;
import mx.openpay.Transaction;
import mx.openpay.exceptions.HttpError;
import mx.openpay.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomerOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8081/Services";

    private String customerId = "YATTOS-1324546765";

    private String apiKey = "1092834756";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testCollectFee() throws ServiceUnavailable, HttpError {
        String ewalletId = "Yattos-User-10-12345";
        Double feeAmount = 10.00;
        String desc = "Pago de taxi";
        
        Transaction transaction = this.openPayServices.collectFee(ewalletId, feeAmount, desc, "1");
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getMemo());
        Assert.assertEquals(ewalletId, transaction.getEwalletId());
    }
    
    @Test
    public void testGetEwallets() throws ServiceUnavailable, HttpError {
        List<Ewallet> ewallets  = this.openPayServices.getEwallets(0, 100);
        Assert.assertNotNull(ewallets);
        for (Ewallet ewallet : ewallets) {
            Assert.assertNotNull(ewallet.getId());
        }
    }
}
