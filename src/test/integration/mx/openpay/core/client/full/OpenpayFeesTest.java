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

import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.OpenpayFeesOperations;
import mx.openpay.client.enums.FeeDetailsType;
import mx.openpay.client.reports.OpenpayFeesSummary;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@RunWith(MockitoJUnitRunner.class)
public class OpenpayFeesTest {

    @Mock
    private OpenpayAPI api;

    @Mock
    private OpenpayFeesOperations openpayFeesOperations;

    @Before
    public void setUp() {
        when(api.openpayFees()).thenReturn(openpayFeesOperations);
    }

    @Test
    public void testSummary() throws Exception {
        OpenpayFeesSummary summary = new OpenpayFeesSummary();
        summary.setTotal(BigDecimal.ZERO);
        summary.setCharged(BigDecimal.ZERO);
        summary.setChargedAdjustments(BigDecimal.ZERO);
        summary.setChargedAdjustmentsTax(BigDecimal.ZERO);
        summary.setChargedTax(BigDecimal.ZERO);
        summary.setRefunded(BigDecimal.ZERO);
        summary.setRefundedAdjustments(BigDecimal.ZERO);
        summary.setRefundedAdjustmentsTax(BigDecimal.ZERO);
        summary.setRefundedTax(BigDecimal.ZERO);

        when(openpayFeesOperations.getSummary(eq(2013), eq(1))).thenReturn(summary);

        OpenpayFeesSummary result = this.api.openpayFees().getSummary(2013, 1);

        assertThat(result.getTotal(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getCharged(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getChargedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getChargedAdjustmentsTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getChargedTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getRefunded(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getRefundedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getRefundedAdjustmentsTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(result.getRefundedTax(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void testDetails() throws Exception {
        when(openpayFeesOperations.getDetails(eq(2013), eq(6), any(FeeDetailsType.class), any()))
                .thenReturn(Collections.emptyList());

        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED_ADJUSTMENTS, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED_ADJUSTMENTS, null).size(), is(0));
    }
}
