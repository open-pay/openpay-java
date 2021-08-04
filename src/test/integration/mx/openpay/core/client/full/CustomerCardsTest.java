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
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.PointsBalance;
import mx.openpay.client.core.requests.cards.UpdateCardParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.core.client.test.TestUtils;

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
                .name("Jorge Perez").email("juan.perez@example.com")
                .phoneNumber("44200000000"));
    }

//    @After
    public void tearDown() throws Exception {
        for (Card card : this.cardsToDelete) {
            this.api.cards().delete(this.customer.getId(), card.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerCard_Old() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), "4242424242424242", "Juanito Perez Nunez", "111",
                "09", Integer.toString(getYear()), TestUtils.prepareAddress());
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    @Test
    public void testCreateCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(getYear())
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    @Test
    public void testGetCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(getYear())
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        card = this.api.cards().get(this.customer.getId(), card.getId());
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    @Test
    public void testGetCustomerPointsCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(12)
                .expirationYear(Calendar.getInstance().get(Calendar.YEAR) % 100 + 10)
                .address(TestUtils.prepareAddress()));
        this.cardsToDelete.add(card);
        PointsBalance balance = this.api.cards().points(this.customer.getId(), card.getId());
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
        assertEquals(new BigInteger("450"), balance.getRemainingPoints());
        System.out.println("id " + card.getId());
    }
    
    @Test
    public void testDeleteCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(getYear()));
        this.api.cards().delete(this.customer.getId(), card.getId());
        try {
            card = this.api.cards().get(this.customer.getId(), card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }
    
    @Test
    public void testUpdateCustomerCard() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(23));
        int year = Calendar.getInstance().get(Calendar.YEAR) % 100 + 5;
        this.api.cards().update(this.customer.getId(), new UpdateCardParams()
              .cardId(card.getId())
              .holderName("Jorge Rodriguez")
              .expirationYear(year)
              .expirationMonth(2)
              .cvv2("222")
              );
        Card updatedCard = this.api.cards().get(this.customer.getId(), card.getId());
        assertThat(updatedCard.getId(),is(card.getId()));
        assertThat(updatedCard.getHolderName(), is("Jorge Rodriguez"));
        assertThat(updatedCard.getExpirationMonth(), is("02"));
        assertThat(updatedCard.getExpirationYear(), is(Integer.toString(year)));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerCard_NoAddress_Old() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), "4242424242424242", "Juanito Perez Nunez", "111",
                "09", Integer.toString(getYear()), null);
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
    }

    @Test
    public void testCreateCustomerCard_NoAddress() throws Exception {
        Card card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Perez Nunez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(getYear()));
        this.cardsToDelete.add(card);
        assertEquals("424242XXXXXX4242", card.getCardNumber());
        assertEquals("Juanito Perez Nunez", card.getHolderName());
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
                .cvv2("111").expirationMonth(9).expirationYear(getYear())));
        this.cardsToDelete.add(this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4111111111111111").holderName("Ruben Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(getYear())));
        this.cardsToDelete.add(this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242").holderName("Carlos Pérez Nuñez")
                .cvv2("111").expirationMonth(9).expirationYear(getYear())));
        List<Card> cards = this.api.cards().list(this.customer.getId(), null);
        assertThat(cards.size(), is(3));
        for (Card card : cards) {
            Assert.assertNotNull(card.getId());
        }
        cards = this.api.cards().list(this.customer.getId(), search().limit(2));
        assertThat(cards.size(), is(2));
        cards = this.api.cards().list(this.customer.getId(), search().limit(2).offset(2));
        assertThat(cards.size(), is(1));
    }

   private int getYear() {
      return Calendar.getInstance().get(Calendar.YEAR) % 100 + 1;
   }

}
