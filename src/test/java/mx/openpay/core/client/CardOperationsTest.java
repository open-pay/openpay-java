package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.util.List;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.core.OpenPayServices;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

	private OpenPayServices openPayServices;

	@Before
	public void setUp() throws Exception {
		this.openPayServices = new OpenPayServices(ENDPOINT, API_KEY, MERCHANT_ID);
	}

	@Test
	public void testGetCards() throws ServiceUnavailable, HttpError {
		String customerId = "afk4csrazjp1udezj1po";
		List<Card> cards = this.openPayServices.getCards(customerId, 0, 100);
		Assert.assertNotNull(cards);
		for (Card card : cards) {
			Assert.assertNotNull(card);
			Assert.assertNotNull(card.getId());
		}
	}

	@Test
	public void testCreateCardAndDelete() throws ServiceUnavailable {
		String customerId = "afk4csrazjp1udezj1po";

		Address address = new Address();
		address.setCity("Querétaro");
		address.setExteriorNumber("11");
		address.setInteriorNumber("01");
		address.setPostalCode("76090");
		address.setRegion("Corregidora");
		address.setStreet("Camino");

		try {
			Card card = this.openPayServices.createCard(customerId, "5243385358972033", "Juanito Perez Perez", "111", "09", "14", address);
			Assert.assertNotNull(card);
			this.openPayServices.deleteCard(customerId, card.getId());
		} catch (HttpError e) {
			Assert.fail(e.getMessage());
		}
	}
}
