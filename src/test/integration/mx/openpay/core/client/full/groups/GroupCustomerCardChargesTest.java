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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.groups.OpenpayGroupAPI;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.operations.groups.GroupCardOperations;
import mx.openpay.client.core.operations.groups.GroupChargeOperations;
import mx.openpay.client.core.operations.groups.GroupCustomerOperations;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupCustomerCardChargesTest {

    private static final String FIRST_MERCHANT_ID = "first-merchant-id";
    private static final String SECOND_MERCHANT_ID = "second-merchant-id";
    private static final String CUSTOMER_ID = "group-customer-id";
    private static final String CARD_ID = "group-card-id";

    @Mock
    private OpenpayGroupAPI groupApi;

    @Mock
    private OpenpayAPI firstMerchantApi;

    @Mock
    private OpenpayAPI secondMerchantApi;

    @Mock
    private GroupCustomerOperations groupCustomerOperations;

    @Mock
    private GroupCardOperations groupCardOperations;

    @Mock
    private GroupChargeOperations groupChargeOperations;

    @Mock
    private ChargeOperations firstMerchantChargeOperations;

    @Mock
    private ChargeOperations secondMerchantChargeOperations;

    private Customer customer;
    private Card customerRegisteredCard;

    @Before
    public void setUp() throws Exception {
        when(groupApi.groupCustomers()).thenReturn(groupCustomerOperations);
        when(groupApi.groupCards()).thenReturn(groupCardOperations);
        when(groupApi.groupCharges()).thenReturn(groupChargeOperations);
        when(firstMerchantApi.charges()).thenReturn(firstMerchantChargeOperations);
        when(secondMerchantApi.charges()).thenReturn(secondMerchantChargeOperations);

        customer = new Customer();
        customer.setId(CUSTOMER_ID);
        when(groupCustomerOperations.create(any(Customer.class))).thenReturn(customer);

        customerRegisteredCard = new Card();
        customerRegisteredCard.setId(CARD_ID);
        when(groupCardOperations.create(eq(CUSTOMER_ID), any(Card.class))).thenReturn(customerRegisteredCard);

        this.customer = this.groupApi.groupCustomers().create(new Customer()
                .name("Jorge Perez").email("juan.perez@correo.com")
                .phoneNumber("44212033000").requiresAccount(false));
        this.customerRegisteredCard = this.groupApi.groupCards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(27));
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
            Charge firstCharge = new Charge();
            firstCharge.setId("charge-first-merchant-id");
            firstCharge.setAmount(amount);
            firstCharge.setDescription(desc);

            when(groupChargeOperations.create(eq(FIRST_MERCHANT_ID), eq(CUSTOMER_ID), any(CreateCardChargeParams.class)))
                    .thenReturn(firstCharge);
            when(firstMerchantChargeOperations.get(eq("charge-first-merchant-id"))).thenReturn(firstCharge);

            Charge transaction = this.groupApi.groupCharges().create(FIRST_MERCHANT_ID,
                    this.customer.getId(), chargeParams);
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());

            String transactionId = transaction.getId();
            transaction = this.firstMerchantApi.charges().get(transactionId);
            assertThat(transaction.getId(), is(transactionId));
            assertNotNull(transaction);
            assertEquals(amount.setScale(2), transaction.getAmount().setScale(2));
            assertEquals(desc, transaction.getDescription());

            this.groupApi.groupCharges().refund(FIRST_MERCHANT_ID, this.customer.getId(),
                    new RefundParams().chargeId(transactionId));
        }

        // SECOND MERCHANT
        {
            Charge secondCharge = new Charge();
            secondCharge.setId("charge-second-merchant-id");
            secondCharge.setAmount(amount);
            secondCharge.setDescription(desc);

            when(groupChargeOperations.create(eq(SECOND_MERCHANT_ID), eq(CUSTOMER_ID), any(CreateCardChargeParams.class)))
                    .thenReturn(secondCharge);
            when(secondMerchantChargeOperations.get(eq("charge-second-merchant-id"))).thenReturn(secondCharge);

            Charge transaction = this.groupApi.groupCharges().create(SECOND_MERCHANT_ID,
                    this.customer.getId(), chargeParams);
            assertNotNull(transaction);
            assertEquals(amount, transaction.getAmount());
            assertEquals(desc, transaction.getDescription());

            String transactionId = transaction.getId();
            transaction = this.secondMerchantApi.charges().get(transactionId);
            assertThat(transaction.getId(), is(transactionId));
            assertNotNull(transaction);
            assertEquals(amount.setScale(2), transaction.getAmount().setScale(2));
            assertEquals(desc, transaction.getDescription());

            this.groupApi.groupCharges().refund(SECOND_MERCHANT_ID, this.customer.getId(),
                    new RefundParams().chargeId(transactionId));
        }
    }
}
