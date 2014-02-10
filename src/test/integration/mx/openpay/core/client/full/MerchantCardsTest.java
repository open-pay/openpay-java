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

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mx.openpay.client.Card;
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
public class MerchantCardsTest extends BaseTest {

    private List<Card> cardsToDelete;

    @Before
    public void setUp() throws Exception {
        this.cardsToDelete = new ArrayList<Card>();
    }

    @After
    public void tearDown() throws Exception {
        for (Card card : this.cardsToDelete) {
            this.api.cards().delete(card.getId());
        }
    }

    @Test
    public void testCreateMerchantCard() throws Exception {
        Card card = this.api.cards().create(new Card()
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
    public void testGetMerchantCard() throws Exception {
        Card card = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        card = this.api.cards().get(card.getId());
        assertEquals("4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testDeleteMerchantCard() throws Exception {
        Card card = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14));
        this.api.cards().delete(card.getId());
        try {
            card = this.api.cards().get(card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreateMerchantCard_NoAddress() throws Exception {
        Card card = this.api.cards().create(new Card()
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
    public void testDeleteMerchantCard_DoesNotExist() throws Exception {
        try {
            this.api.cards().delete("kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGetMerchantCard_DoesNotExist() throws Exception {
        try {
            this.api.cards().get("kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testListMerchantCards_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.api.cards().list(null);
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.isEmpty());
    }

    @Test
    public void testListMerchantCards() throws ServiceUnavailableException, OpenpayServiceException {
        this.cardsToDelete.add(this.api.cards().create(
                new Card().cardNumber("5555555555554444").holderName("Juan Pérez Nuñez").cvv2("111").expirationMonth(9)
                        .expirationYear(14)));
        this.cardsToDelete.add(this.api.cards().create(
                new Card().cardNumber("4111111111111111").holderName("Ruben Pérez Nuñez").cvv2("111")
                        .expirationMonth(9).expirationYear(14)));
        this.cardsToDelete.add(this.api.cards().create(
                new Card().cardNumber("4242424242424242").holderName("Carlos Pérez Nuñez").cvv2("111")
                        .expirationMonth(9).expirationYear(14)));
        List<Card> cards = this.api.cards().list(null);
        assertThat(cards.size(), is(3));
        for (Card card : cards) {
            Assert.assertNotNull(card.getId());
        }
        cards = this.api.cards().list(search().limit(2));
        assertThat(cards.size(), is(2));
        cards = this.api.cards().list(search().limit(2).offset(2));
        assertThat(cards.size(), is(1));
    }
}
