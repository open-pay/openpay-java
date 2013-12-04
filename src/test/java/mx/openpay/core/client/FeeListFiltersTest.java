package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Fee;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.FeeOperations;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
@Slf4j
public class FeeListFiltersTest {

    FeeOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).fees();
    }

    @Test
    public void testList() throws Exception {
        List<Fee> fees = this.ops.list(null);
        assertEquals(10, fees.size());
        log.info("fees: {}", fees);
        assertTrue(fees.get(0).getCreationDate().compareTo(fees.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Limit() throws Exception {
        List<Fee> fees = this.ops.list(search().limit(2));
        assertEquals(2, fees.size());
        assertTrue(fees.get(0).getCreationDate().compareTo(fees.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Offset() throws Exception {
        List<Fee> fees = this.ops.list(search().offset(1));
        assertEquals(10, fees.size());
        assertTrue(fees.get(0).getCreationDate().compareTo(fees.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<Fee> fees = this.ops.list(search().offset(1).limit(1));
        assertEquals(1, fees.size());
    }

}
