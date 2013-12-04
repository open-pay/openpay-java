/*
 * Copyright 2013 Opencard Inc.
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
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.CUSTOMER_CARD_ID;
import static mx.openpay.core.client.TestConstans.CUSTOMER_ID;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CardOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CardOperationsTest {

    CardOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).cards();
    }

    @Test
    public void testList() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.ops.list(CUSTOMER_ID, search().offset(0).limit(100));
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.size() > 0);
        for (Card card : cards) {
            Assert.assertNotNull(card);
            Assert.assertNotNull(card.getId());
        }
    }

    @Test
    public void testList_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Card> cards = this.ops.list(CUSTOMER_ID, search().offset(1000).limit(2));
        Assert.assertNotNull(cards);
        Assert.assertTrue(cards.isEmpty());
    }

    @Test
    public void testCreateAndDelete() throws Exception {
        Address address = this.getAddress();
        Card card = this.ops.create(CUSTOMER_ID, "5243385358972033", "Juanito Pérez Nuñez", "111", "09", "14", address);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        card = this.ops.get(CUSTOMER_ID, card.getId());
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        this.ops.delete(CUSTOMER_ID, card.getId());
        try {
            card = this.ops.get(CUSTOMER_ID, card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreateAndDelete_NoAddress() throws Exception {
        Card card = this.ops.create(CUSTOMER_ID, "5243385358972033", "Juanito Pérez Nuñez", "111", "09", "14", null);
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        card = this.ops.get(CUSTOMER_ID, card.getId());
        assertEquals("2033", card.getCardNumber());
        assertEquals("Juanito Pérez Nuñez", card.getHolderName());

        this.ops.delete(CUSTOMER_ID, card.getId());
        try {
            card = this.ops.get(CUSTOMER_ID, card.getId());
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testDelete_DoesNotExist() throws Exception {
        try {
            this.ops.delete(CUSTOMER_ID, "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    @Test
    public void testGet() throws Exception {
        Card card = this.ops.get(CUSTOMER_ID, CUSTOMER_CARD_ID);
        assertNotNull(card);
    }

    @Test
    public void testGet_DoesNotExist() throws Exception {
        try {
            this.ops.get(CUSTOMER_ID, "kfaq5dm5pq1qefzev3nz");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

    private Address getAddress() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 int 15");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
        return address;
    }

}
