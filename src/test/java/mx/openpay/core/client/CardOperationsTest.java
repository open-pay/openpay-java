package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CardOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

    CardOperations ops;

    private final String customerId = "afk4csrazjp1udezj1po";

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).cards();
    }

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.ops.list(this.customerId, search().offset(0).limit(100));
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.size() > 0);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
        }
    }

    @Test
    public void testList_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.ops.list(this.customerId, search().offset(1000).limit(2));
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.isEmpty());
    }

    @Test
    public void testCreateAndDelete() throws Exception {
        Address address = this.getAddress();
        Card card = this.ops.create(this.customerId, "5243385358972033", "Juanito Pérez Nuñez", "111", "09",
                "14", address);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        card = this.ops.get(this.customerId, card.getId());
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        this.ops.delete(this.customerId, card.getId());
        try {
            card = this.ops.get(this.customerId, card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreateAndDelete_NoAddress() throws Exception {
        Card card = this.ops.create(this.customerId, "5243385358972033", "Juanito Pérez Nuñez", "111", "09",
                "14", null);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        card = this.ops.get(this.customerId, card.getId());
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        this.ops.delete(this.customerId, card.getId());
        try {
            card = this.ops.get(this.customerId, card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.ops.delete(this.customerId, "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGet() throws Exception {
        Card card = this.ops.get(this.customerId, "kfaq5dm5pq1qefzev1nz");
        assertNotNull(card);
    }

    @Test
    public void testGet_DoesNotExist() throws Exception {
        try {
            this.ops.get(this.customerId, "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    private Address getAddress() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 int 15");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
        return address;
    }

}
