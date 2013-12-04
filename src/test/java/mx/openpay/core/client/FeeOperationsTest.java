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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Fee;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.FeeOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeeOperationsTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    FeeOperations ops;

    @Before
    public void setUp() throws Exception {
        this.ops = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID).fees();
    }

    @Test
    public void testCreate() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = CUSTOMER_ID;
        BigDecimal feeAmount = new BigDecimal("10.00");
        String desc = "Comisión general";
        String orderId = this.dateFormat.format(new Date());

        Fee transaction = this.ops.create(customerId, feeAmount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_ZeroAmount() throws Exception {
        String customerId = CUSTOMER_ID;
        BigDecimal feeAmount = new BigDecimal("0.00");
        String desc = "Comisión general";
        String orderId = String.valueOf(System.currentTimeMillis());
        try {
            this.ops.create(customerId, feeAmount, desc, orderId);
        } catch (OpenpayServiceException e) {
            assertEquals(422, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList() throws Exception {
        List<Fee> fees = this.ops.list(search().limit(3));
        assertEquals(3, fees.size());
    }

    @Test
    public void testList_Empty() throws Exception {
        List<Fee> fees = this.ops.list(search().limit(3).offset(100000));
        assertEquals(0, fees.size());
    }

}
