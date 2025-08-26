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

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import mx.openpay.client.enums.FeeDetailsType;
import mx.openpay.client.reports.OpenpayFeesSummary;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class OpenpayFeesTest extends BaseTest {

    @Test
    public void testSummary() throws Exception {
        OpenpayFeesSummary summary = this.api.openpayFees().getSummary(2013, 1);
        assertThat(summary.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getCharged()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getChargedAdjustments()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getChargedAdjustmentsTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getChargedTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getRefunded()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getRefundedAdjustments()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getRefundedAdjustmentsTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getRefundedTax()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void testDetails() throws Exception {
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED, null).size()).isZero();
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.CHARGED_ADJUSTMENTS, null).size()).isZero();
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED, null).size()).isZero();
        assertThat(this.api.openpayFees().getDetails(2013, 6, FeeDetailsType.REFUNDED_ADJUSTMENTS, null).size()).isZero();
    }

}
