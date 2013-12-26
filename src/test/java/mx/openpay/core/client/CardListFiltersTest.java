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
import static mx.openpay.core.client.TestConstans.CUSTOMER_ID;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.TimeZone;

import mx.openpay.client.Card;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.CardOperations;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class CardListFiltersTest {

    CardOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).cards();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testList() throws Exception {
        List<Card> cards = this.ops.list(CUSTOMER_ID, null);
        assertEquals(3, cards.size());
        assertTrue(cards.get(0).getCreationDate().compareTo(cards.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Limit() throws Exception {
        List<Card> cards = this.ops.list(CUSTOMER_ID, search().limit(2));
        assertEquals(2, cards.size());
        assertTrue(cards.get(0).getCreationDate().compareTo(cards.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Offset() throws Exception {
        List<Card> cards = this.ops.list(CUSTOMER_ID, search().offset(1));
        assertEquals(2, cards.size());
        assertTrue(cards.get(0).getCreationDate().compareTo(cards.get(1).getCreationDate()) >= 0);
    }

    @Test
    public void testList_Offset_Limit() throws Exception {
        List<Card> cards = this.ops.list(CUSTOMER_ID, search().offset(1).limit(1));
        assertEquals(1, cards.size());
    }

}
