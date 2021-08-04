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
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class FeesTest extends BaseTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan Nuñez").email("juan.perez@example.com")
                .phoneNumber("44200000000")
                .requiresAccount(true));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Jorge Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(Calendar.getInstance().get(Calendar.YEAR) % 100 + 1)));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testCreate() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal feeAmount = new BigDecimal("2.00");
        String desc = "Comision general";
        String orderId = this.dateFormat.format(new Date());

        Fee transaction = this.api.fees().create(this.customer.getId(), feeAmount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getFee());
    }

    @Test
    public void testCreate_ZeroAmount() throws Exception {
        BigDecimal amount = BigDecimal.ONE;
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(amount.multiply(new BigDecimal(3)))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Jorge Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(Calendar.getInstance().get(Calendar.YEAR) % 100 + 1)));
        this.api.fees().create(this.customer.getId(), amount, "desc 1", null);
        this.api.fees().create(this.customer.getId(), amount, "desc 2", null);
        this.api.fees().create(this.customer.getId(), amount, "desc 3", null);
        BigDecimal feeAmount = new BigDecimal("0.00");
        String desc = "Comisión general";
        String orderId = String.valueOf(System.currentTimeMillis());
        try {
            this.api.fees().create(this.customer.getId(), feeAmount, desc, orderId);
        } catch (OpenpayServiceException e) {
            assertEquals(422, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testList() throws Exception {
        List<Fee> fees = this.api.fees().list(search().limit(3));
        assertEquals(3, fees.size());
    }

    @Test
    public void testList_Empty() throws Exception {
        List<Fee> fees = this.api.fees().list(
                search().creation(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))));
        assertEquals(0, fees.size());
    }

}
