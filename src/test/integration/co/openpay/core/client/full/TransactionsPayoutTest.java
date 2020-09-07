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

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import mx.openpay.client.TransactionsPayoutResume;
import mx.openpay.client.enums.TransactionsPayoutType;

/**
 * The Class TransactionsPayoutTest.
 * 
 * @author Luis Delucio
 */
public class TransactionsPayoutTest extends BaseTest {

	@Test
	public void testResume() throws Exception {
		TransactionsPayoutResume resume = this.api.transactionsPayout().getResume("trutv6djm2suh2bo4v1f");
		assertThat(resume.getIn(), comparesEqualTo(BigDecimal.ZERO));
		assertThat(resume.getOut(), comparesEqualTo(BigDecimal.ZERO));
		assertThat(resume.getChargedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
		assertThat(resume.getRefundedAdjustments(), comparesEqualTo(BigDecimal.ZERO));
	}

	@Test
	public void testDetails() throws Exception {
		assertThat(this.api.transactionsPayout().getDetails("trutv6djm2suh2bo4v1f", TransactionsPayoutType.IN, null)
				.size(), is(0));
		assertThat(this.api.transactionsPayout().getDetails("trutv6djm2suh2bo4v1f", TransactionsPayoutType.OUT, null)
				.size(), is(0));
		assertThat(
				this.api.transactionsPayout()
						.getDetails("trutv6djm2suh2bo4v1f", TransactionsPayoutType.CHARGED_ADJUSTMENTS, null)
				.size(), is(0));
		assertThat(
				this.api.transactionsPayout()
						.getDetails("trutv6djm2suh2bo4v1f", TransactionsPayoutType.REFUNDED_ADJUSTMENTS, null)
				.size(), is(0));
	}
}
