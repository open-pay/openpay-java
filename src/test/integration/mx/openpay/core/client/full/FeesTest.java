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
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class FeesTest extends BaseTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(14)));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testCreate() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal feeAmount = new BigDecimal("2.00");
        String desc = "Comisión general";
        String orderId = this.dateFormat.format(new Date());

        Fee transaction = this.api.fees().create(this.customer.getId(), feeAmount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(feeAmount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
    }

    @Test
    public void testCreate_ZeroAmount() throws Exception {
        BigDecimal amount = BigDecimal.ONE;
        this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .amount(amount.multiply(new BigDecimal(3)))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(14)));
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
