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

import java.math.BigDecimal;

import mx.openpay.client.enums.FeeDetailsType;
import mx.openpay.client.reports.OpenpayFeesSummary;

import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class OpenpayFeesTest extends BaseTest {

    @Test
    public void testSummary() throws Exception {
        OpenpayFeesSummary summary = this.api.openpayFees().getSummary(2013, 1);
        assertThat(summary.getTotal(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getCharged(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getChargedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getChargedAdjustmentsTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getChargedTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getRefunded(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getRefundedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getRefundedAdjustmentsTax(), comparesEqualTo(BigDecimal.ZERO));
        assertThat(summary.getRefundedTax(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void testDetails() throws Exception {
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED_ADJUSTMENTS, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED, null).size(), is(0));
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED_ADJUSTMENTS, null).size(), is(0));
    }

}
