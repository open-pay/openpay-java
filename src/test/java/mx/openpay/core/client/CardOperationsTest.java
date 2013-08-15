package mx.openpay.core.client;

import java.util.List;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.OpenPayServices;
import mx.openpay.client.core.OpenPayServicesImpl;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

    private OpenPayServices openPayServices;

    private static String root = "http://localhost:8081/Services";

    private String customerId = "hgqemgk8g368fqw79i35";

    private String apiKey = "5eb59e956b614015b0a81cb311b892f4";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testGetCards() throws ServiceUnavailable, HttpError {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";
        List<Card> cards = this.openPayServices.getCards(ewalletId, 0, 100);
        Assert.assertNotNull(cards);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
            Assert.assertEquals("BANCOMER", card.getBankName());
        }
    }

    @Test
    public void testGetCard() throws ServiceUnavailable, HttpError {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";
        String cardId = "wndf8vqzk7pzgvfiraiu";
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
    public void testCreateCard() throws ServiceUnavailable {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";

        Address address = new Address();
        address.setCity("Querétaro");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("76090");
        address.setRegion("Corregidora");
        address.setStreet("Camino");

        try {
            this.openPayServices.createCard(ewalletId, "5243385358972033", "Juanito Perez Perez", "111", "09", "14", address);
            Assert.fail("Card should be exists.");
        } catch (HttpError e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
        }
    }
    
    @Test
    public void testCreateDespositCard() throws ServiceUnavailable {
        String ewalletId = "ls0jzlyrwvjqm1kk3vwg";

        try {
            this.openPayServices.createDepositCard(ewalletId, "5243385358972033", "Juanito Perez", "012");
        } catch (HttpError e) {
            Assert.fail(e.getMessage());
        }
    }
}
