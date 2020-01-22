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
package co.openpay.core.client.full.groups;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import co.openpay.client.Card;
import co.openpay.client.Charge;
import co.openpay.client.Customer;
import co.openpay.client.core.requests.transactions.CreateCardChargeParams;
import co.openpay.client.core.requests.transactions.RefundParams;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.core.client.test.TestUtils;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
public class GroupCustomerCardChargesTest extends GroupBaseTest {

    private Customer customer;

    private Card customerRegisteredCard;
    
    @Before
    public void setUp() throws Exception {
        this.customer = this.groupApi.groupCustomers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013").requiresAccount(false));
        this.customerRegisteredCard = this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(20)
                .address(TestUtils.prepareAddress()));
        
    }

    @After
    public void tearDown() throws Exception {
        if (this.customerRegisteredCard != null) {
            this.groupApi.groupCards().delete(this.customer.getId(), this.customerRegisteredCard.getId());
        }
    }

    @Test
    public void testCreate_Customer_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = new BigDecimal("10.00");
        String desc = "Pago de taxi";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateCardChargeParams chargeParams = new CreateCardChargeParams()
                .cardId(this.customerRegisteredCard.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId);
        
        // FIRST MERCHANT
        {
            Charge transaction = this.groupApi.groupCharges().create(this.firstMerchantCredentials.getId(),
                    this.customer.getId(), chargeParams);
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());
            // Se obtiene desde el merchant
            String transactionId = transaction.getId();
            transaction = this.firstMerchantApi.charges().get(transactionId);
            assertThat(transaction.getId(), is(transactionId));
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());
            // Reembolso desde el grupo
            this.groupApi.groupCharges().refund(this.firstMerchantCredentials.getId(), this.customer.getId(),
                    new RefundParams().chargeId(transactionId));
        }
        // SECOND MERCHANT
        {
            Charge transaction = this.groupApi.groupCharges().create(this.secondMerchantCredentials.getId(),
                    this.customer.getId(), chargeParams);
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());
            // Se obtiene desde el merchant
            String transactionId = transaction.getId();
            transaction = this.secondMerchantApi.charges().get(transactionId);
            assertThat(transaction.getId(), is(transactionId));
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());
            // Reembolso desde el grupo
            this.groupApi.groupCharges().refund(this.secondMerchantCredentials.getId(), this.customer.getId(),
                    new RefundParams().chargeId(transactionId));
        }
        
    }
    
}
