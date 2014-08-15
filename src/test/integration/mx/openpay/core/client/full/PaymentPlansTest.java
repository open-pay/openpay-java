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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.PaymentPlan;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Slf4j
public class PaymentPlansTest extends BaseTest {

        private List<PaymentPlan> plansToDelete;

    @Before
    public void setUp() throws Exception {
        this.plansToDelete = new ArrayList<PaymentPlan>();
    }

    @After
    public void tearDown() throws Exception {
        //TODO Borrar payment plan
    }
    
    @Test
    public void testListAll() throws OpenpayServiceException, ServiceUnavailableException {
        List<PaymentPlan> paymentPlan = this.api.paymentsPlans().listAll();
        assertThat(paymentPlan.size(), is(3));
    }

    //'4', '209', 'apgvpxtsvlhrfaa6anem', 'junit test plan 1407804060605', '0.300', '12', '6', '2014-08-11 19:41:01', 'ACTIVE', '15'

    @Test
    public void testGetPaymentPlan() throws OpenpayServiceException, ServiceUnavailableException {
        String paymentPlanId = "apgvpxtsvlhrfaa6anem";
        PaymentPlan paymentPlan = this.api.paymentsPlans().get(paymentPlanId);
        assertThat(paymentPlan.getId(), is(paymentPlanId));
        assertThat(paymentPlan.getDaysToFirstPayment(), is(15));
        assertThat(paymentPlan.getFirstPaymentPercentage(), is(new BigDecimal("0.300")));
        assertThat(paymentPlan.getMaxNumberOfPayments(), is(12));
        assertThat(paymentPlan.getMonthsToPay(), is(6));
        assertThat(paymentPlan.getName(), is("junit test plan 1407804060605"));
        assertThat(paymentPlan.getStatus(), is("active"));
    }
    

    @Test
    @Ignore //se ignora por  que no hay manera de borra en este momento
    public void testCreate() throws Exception {
        String name = "junit test plan " + System.currentTimeMillis();
        BigDecimal firstPaymentPercentage = new BigDecimal("0.3");
        int maxNumberOfPayments = 12;
        int monthsToPay = 6;
        int daysToFirstPayment = 15;
        PaymentPlan request =  new PaymentPlan()
                .name(name)
                .firstPaymentPercentage(firstPaymentPercentage)
                .maxNumberOfPayments(maxNumberOfPayments)
                .monthsToPay(monthsToPay)
                .daysToFirstPayment(daysToFirstPayment);
               
           PaymentPlan paymentPlan = this.api.paymentsPlans().create(request);
//        this.plansToDelete.add(plan);
        log.info("{}", paymentPlan);
        assertNotNull(paymentPlan.getId());
        assertThat(paymentPlan.getStatus(), is("active"));
        assertTrue(firstPaymentPercentage.compareTo(paymentPlan.getFirstPaymentPercentage()) == 0);
        assertThat(paymentPlan.getMaxNumberOfPayments(), is(maxNumberOfPayments));
        assertThat(paymentPlan.getName(), is(name));
        assertThat(paymentPlan.getMonthsToPay(), is(monthsToPay));
        assertThat(paymentPlan.getDaysToFirstPayment(), is(daysToFirstPayment));
        
    }

}
