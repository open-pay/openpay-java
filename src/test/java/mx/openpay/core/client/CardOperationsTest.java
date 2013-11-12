package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

    private final String customerId = "afk4csrazjp1udezj1po";

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testGetList() throws ServiceUnavailable, HttpError {
        List<Card> cards = Card.getList(this.customerId, search().offset(0).limit(100));
        Assert.assertNotNull(cards);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
        }
    }

    @Test
    public void testCreateAndDelete() throws Exception {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("76090");
        address.setRegion("Corregidora");
        address.setStreet("Camino");
        address.setState("Queretaro");

        Card card = Card.create(this.customerId, "5243385358972033", "Juanito Pérez Nuñez", "111", "09",
                "14", address);
        Assert.assertNotNull(card);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
        card = Card.get(this.customerId, card.getId());
        Assert.assertNotNull(card);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
        Card.delete(this.customerId, card.getId());
    }

    @Test
    public void testGet() throws Exception {
        Card card = Card.get(this.customerId, "kfaq5dm5pq1qefzev1nz");
        assertNotNull(card);
    }
}
