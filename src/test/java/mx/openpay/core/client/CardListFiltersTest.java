/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
