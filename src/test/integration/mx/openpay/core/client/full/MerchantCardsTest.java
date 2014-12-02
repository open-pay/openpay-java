/*
 * Copyright 2014 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.core.client.full;

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testGetMerchantCard() throws Exception {
        Card card = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        card = this.api.cards().get(card.getId());
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());
    }

    @Test
    public void testDeleteMerchantCard() throws Exception {
        Card card = this.api.cards().create(new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20));
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
                .expirationYear(20));
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
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
                        .expirationYear(20)));
        this.cardsToDelete.add(this.api.cards().create(
                new Card().cardNumber("4111111111111111").holderName("Ruben Pérez Nuñez").cvv2("111")
                        .expirationMonth(9).expirationYear(20)));
        this.cardsToDelete.add(this.api.cards().create(
                new Card().cardNumber("4242424242424242").holderName("Carlos Pérez Nuñez").cvv2("111")
                        .expirationMonth(9).expirationYear(20)));
        List<Card> cards = this.api.cards().list(null);
		assertNotNull(cards);
		assertTrue(cards.size() > 3);
        for (Card card : cards) {
            Assert.assertNotNull(card.getId());
        }
        cards = this.api.cards().list(search().limit(2));
        assertThat(cards.size(), is(2));
        cards = this.api.cards().list(search().limit(2).offset(2));
		assertTrue(cards.size() > 1);
    }
}
