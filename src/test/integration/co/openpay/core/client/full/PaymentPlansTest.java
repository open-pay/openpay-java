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
package co.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.PaymentPlan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class PaymentPlansTest extends BaseTest {

    private int currentPlans = 0;

    @Before
    public void setUp() throws Exception {
        List<PaymentPlan> paymentPlans = this.api.paymentsPlans().listAll();
        this.currentPlans = paymentPlans.size();
    }

    @After
    public void tearDown() throws Exception {
        // TODO Borrar payment plan
    }

    @Test
    public void testCreate() throws Exception {
        String name = "junit test plan " + System.currentTimeMillis();
        BigDecimal firstPaymentPercentage = new BigDecimal("0.3");
        int maxNumberOfPayments = 12;
        int monthsToPay = 6;
        int daysToFirstPayment = 15;
        PaymentPlan request = new PaymentPlan()
                .name(name)
                .firstPaymentPercentage(firstPaymentPercentage)
                .maxNumberOfPayments(maxNumberOfPayments)
                .monthsToPay(monthsToPay)
                .daysToFirstPayment(daysToFirstPayment);

        PaymentPlan paymentPlan = this.api.paymentsPlans().create(request);
        log.info("{}", paymentPlan);
        assertNotNull(paymentPlan.getId());
        assertThat(paymentPlan.getStatus(), is("active"));
        assertThat(paymentPlan.getFirstPaymentPercentage(), comparesEqualTo(firstPaymentPercentage));
        assertThat(paymentPlan.getMaxNumberOfPayments(), is(maxNumberOfPayments));
        assertThat(paymentPlan.getName(), is(name));
        assertThat(paymentPlan.getMonthsToPay(), is(monthsToPay));
        assertThat(paymentPlan.getDaysToFirstPayment(), is(daysToFirstPayment));

        List<PaymentPlan> paymentPlans = this.api.paymentsPlans().listAll();
        assertThat(paymentPlans.size(), is(this.currentPlans + 1));
        this.currentPlans++;

        paymentPlan = this.api.paymentsPlans().get(paymentPlan.getId());
        assertThat(paymentPlan.getId(), is(paymentPlan.getId()));
        assertThat(paymentPlan.getDaysToFirstPayment(), is(daysToFirstPayment));
        assertThat(paymentPlan.getFirstPaymentPercentage(), comparesEqualTo(firstPaymentPercentage));
        assertThat(paymentPlan.getMaxNumberOfPayments(), is(maxNumberOfPayments));
        assertThat(paymentPlan.getMonthsToPay(), is(monthsToPay));
        assertThat(paymentPlan.getName(), is(name));
        assertThat(paymentPlan.getStatus(), is("active"));

    }

}
