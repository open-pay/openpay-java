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
package mx.openpay.core.client.full.groups;

import static mx.openpay.client.utils.SearchParams.search;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.core.client.test.TestUtils;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class GroupCustomerCardsTest extends GroupBaseTest {

    private Customer customer;

    private List<Card> cardsToDelete;

    @Before
    public void setUp() throws Exception {
        this.cardsToDelete = new ArrayList<Card>();
        this.customer = this.groupApi.groupCustomers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        for (Card card : this.cardsToDelete) {
            this.groupApi.groupCards().delete(this.customer.getId(), card.getId());
        }
        this.groupApi.groupCustomers().delete(this.customer.getId());
    }

    @Test
    public void testCreateCustomerCard() throws Exception {
        Card card = this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    @Test
    public void testGetCustomerCard() throws Exception {
        Card card = this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        card = this.groupApi.groupCards().get(this.customer.getId(), card.getId());
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    // @Test
    // public void testGetCustomerPointsCard() throws Exception {
    // Card card = this.groupApi.groupCards().create(this.customer.getId(), new Card()
    // .cardNumber("4242424242424242")
    // .holderName("Juanito Perez Nunez")
    // .cvv2("111")
    // .expirationMonth(12)
    // .expirationYear(30)
    // .address(TestUtils.prepareAddress()));
    // this.cardsToDelete.add(card);
    // PointsBalance balance = this.groupApi.groupCards().points(this.customer.getId(), card.getId());
    // assertEquals("424242XXXXXX4242", card.getCardNumber());
    // assertEquals("Juanito Perez Nunez", card.getHolderName());
    // assertEquals(new BigInteger("450"), balance.getRemainingPoints());
    // System.out.println("id " + card.getId());
    // }
    
    @Test
    public void testDeleteCustomerCard() throws Exception {
        Card card = this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20));
        this.groupApi.groupCards().delete(this.customer.getId(), card.getId());
        try {
            card = this.groupApi.groupCards().get(this.customer.getId(), card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testDeleteCustomerCard_DoesNotExist() throws Exception {
        try {
            this.groupApi.groupCards().delete(this.customer.getId(), "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGetCustomerCard_DoesNotExist() throws Exception {
        try {
            this.groupApi.groupCards().get(this.customer.getId(), "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testListCustomerCards_Empty() throws Exception {
        List<Card> cards = this.groupApi.groupCards().list(this.customer.getId(), null);
        assertNotNull(cards);
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testListCustomerCards() throws Exception {
        this.cardsToDelete.add(this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("5555555555554444").holderName("Juan Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(20)));
        this.cardsToDelete.add(this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4111111111111111").holderName("Ruben Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(20)));
        this.cardsToDelete.add(this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242").holderName("Carlos Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(20)));
        List<Card> cards = this.groupApi.groupCards().list(this.customer.getId(), null);
        assertThat(cards.size(), is(3));
        for (Card card : cards) {
            assertNotNull(card.getId());
        }
        cards = this.groupApi.groupCards().list(this.customer.getId(), search().limit(2));
        assertThat(cards.size(), is(2));
        cards = this.groupApi.groupCards().list(this.customer.getId(), search().limit(2).offset(2));
        assertThat(cards.size(), is(1));
    }

}
