/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class CustomerCardsTest extends BaseTest {

    private Customer customer;

    private List<Card> cardsToDelete;

    @Before
    public void setUp() throws Exception {
        this.cardsToDelete = new ArrayList<Card>();
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        for (Card card : this.cardsToDelete) {
            this.api.cards().delete(this.customer.getId(), card.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerCard_Old() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), "4242424242424242", "Juanito Pérez Nuñez", "111",
                "09", "14", TestUtils.prepareAddress());
        this.cardsToDelete.add(card);
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testCreateCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testGetCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        card = this.api.cards().get(this.customer.getId(), card.getId());
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testDeleteCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14));
        this.api.cards().delete(this.customer.getId(), card.getId());
        try {
            card = this.api.cards().get(this.customer.getId(), card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerCard_NoAddress_Old() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), "4242424242424242", "Juanito Pérez Nuñez", "111",
                "09", "14", null);
        this.cardsToDelete.add(card);
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testCreateCustomerCard_NoAddress() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14));
        this.cardsToDelete.add(card);
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testDeleteCustomerCard_DoesNotExist() throws Exception {
        try {
            this.api.cards().delete(this.customer.getId(), "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGetCustomerCard_DoesNotExist() throws Exception {
        try {
            this.api.cards().get(this.customer.getId(), "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testListCustomerCards_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.api.cards().list(this.customer.getId(), null);
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.isEmpty());
    }

    @Test
    public void testListCustomerCards() throws ServiceUnavailableException, OpenpayServiceException {
        this.cardsToDelete.add(this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("5555555555554444").holderName("Juan Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(14)));
        this.cardsToDelete.add(this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4111111111111111").holderName("Ruben Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(14)));
        this.cardsToDelete.add(this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242").holderName("Carlos Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(14)));
        List<Card> cards = this.api.cards().list(this.customer.getId(), null);
        assertThat(cards.size(), is(3));
        for (Card card : cards) {
            Assert.assertNotNull(card.getId());
        }
    }

}
