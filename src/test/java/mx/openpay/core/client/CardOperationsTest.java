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

    private static String root = "http://localhost:8080/Services";

    private String customerId = "m7psroutl8tycqtcmxly";

    private String apiKey = "e97b8bf7728242c0aa97b409a4c59236";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServicesImpl(this.customerId, this.apiKey, root);
    }

    @Test
    public void testGetCards() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        List<Card> cards = this.openPayServices.getCards(customerId, 0, 100);
        Assert.assertNotNull(cards);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
        }
    }

    @Test
    public void testGetCard() throws ServiceUnavailable, HttpError {
        String customerId = "agt0tslutb7tyz4nu1ce";
        String cardId = "kmqdv4jmn6lq82h5ykfv";
        Card card = this.openPayServices.getCard(customerId, cardId);
        Assert.assertNotNull(card);

        card = this.openPayServices.inactivateCard(customerId, cardId);
        Assert.assertNotNull(card);
        Assert.assertEquals("INACTIVE", card.getStatus());

        card = this.openPayServices.activateCard(customerId, cardId);
        Assert.assertNotNull(card);
        Assert.assertEquals("ACTIVE", card.getStatus());
    }

    @Test
    public void testCreateCard() throws ServiceUnavailable {
        String customerId = "agt0tslutb7tyz4nu1ce";

        Address address = new Address();
        address.setCity("Querétaro");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("76090");
        address.setRegion("Corregidora");
        address.setStreet("Camino");

        try {
            this.openPayServices.createCard(customerId, "5243385358972033", "Juanito Perez Perez", "111", "09", "14", address);
            Assert.fail("Card should be exists.");
        } catch (HttpError e) {
            Assert.assertEquals(409, e.getHttpCode().intValue());
        }
    }
}
