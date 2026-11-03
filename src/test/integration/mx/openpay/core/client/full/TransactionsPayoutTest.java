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

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import mx.openpay.client.TransactionsPayoutResume;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.TransactionsPayoutOperations;
import mx.openpay.client.enums.TransactionsPayoutType;

/**
 * The Class TransactionsPayoutTest.
 *
 * @author Luis Delucio
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionsPayoutTest {

    private static final String PAYOUT_ID = "trutv6djm2suh2bo4v1f";

    @Mock
    private OpenpayAPI api;

    @Mock
    private TransactionsPayoutOperations transactionsPayoutOperations;

    @Before
    public void setUp() {
        when(api.transactionsPayout()).thenReturn(transactionsPayoutOperations);
    }

    @Test
    public void testResume() throws Exception {
        TransactionsPayoutResume resume = new TransactionsPayoutResume();
        resume.setIn(BigDecimal.ZERO);
        resume.setOut(BigDecimal.ZERO);
        resume.setChargedAdjustments(BigDecimal.ZERO);
        resume.setRefundedAdjustments(BigDecimal.ZERO);

        when(transactionsPayoutOperations.getResume(eq(PAYOUT_ID))).thenReturn(resume);

        TransactionsPayoutResume result = this.api.transactionsPayout().getResume(PAYOUT_ID);

        assertThat(result.getIn(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getOut(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getChargedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getRefundedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void testDetails() throws Exception {
        when(transactionsPayoutOperations.getDetails(eq(PAYOUT_ID), any(TransactionsPayoutType.class), any()))
                .thenReturn(Collections.emptyList());

        assertThat(this.api.transactionsPayout().getDetails(PAYOUT_ID, TransactionsPayoutType.IN, null).size(), is(0));
        assertThat(this.api.transactionsPayout().getDetails(PAYOUT_ID, TransactionsPayoutType.OUT, null).size(), is(0));
        assertThat(this.api.transactionsPayout()
                .getDetails(PAYOUT_ID, TransactionsPayoutType.CHARGED_ADJUSTMENTS, null).size(), is(0));
        assertThat(this.api.transactionsPayout()
                .getDetails(PAYOUT_ID, TransactionsPayoutType.REFUNDED_ADJUSTMENTS, null).size(), is(0));
    }
}
