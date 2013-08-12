package mx.openpay.core;

import java.util.List;

import mx.openpay.Address;
import mx.openpay.Card;
import mx.openpay.OpenPayServices;
import mx.openpay.exceptions.HttpError;
import mx.openpay.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8081/Services";

    private String customerId = "YATTOS-1324546765";

    private String apiKey = "1092834756";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testGetCards() throws ServiceUnavailable, HttpError {
        String ewalletId = "Yattos-User-10-12345";
        List<Card> cards = this.openPayServices.getCards(ewalletId, 0, 100);
        Assert.assertNotNull(cards);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
        }
    }

    @Test
    public void testGetCard() throws ServiceUnavailable, HttpError {
        String ewalletId = "Yattos-User-10-12345";
        String cardId = "1234567811";
        Card card = this.openPayServices.getCard(ewalletId, cardId);
        Assert.assertNotNull(card);

        card = this.openPayServices.inactivateCard(ewalletId, cardId);
        Assert.assertNotNull(card);
        Assert.assertEquals("INACTIVE", card.getStatus());

        card = this.openPayServices.activateCard(ewalletId, cardId);
        Assert.assertNotNull(card);
        Assert.assertEquals("ACTIVE", card.getStatus());
    }

    @Test
    public void testCreateCard() throws ServiceUnavailable, HttpError {
        String ewalletId = "Yattos-User-10-12345";

        Address address = new Address();
        address.setCity("Querétaro");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("76090");
        address.setRegion("Corregidora");
        address.setStreet("Camino");

        Card card = this.openPayServices.createCard(ewalletId, "5243385358972033", "heber lazcano", "111", "09", "14",
                address);
        Assert.assertNotNull(card);
        Assert.assertNotNull(card.getId());
    }
}
