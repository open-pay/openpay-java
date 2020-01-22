/*
 * Copyright 2013 Opencard Inc.
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
package co.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import co.openpay.client.GatewayParams;
import co.openpay.client.core.requests.RequestBuilder;
import lombok.Getter;

/**
 * Parameters to refund a completed charge. Currently only card charges can be refunded this way.
 * @author elopez
 */
public class RefundParams extends RequestBuilder {

    @Getter
    private String chargeId;

    /**
     * The ID of the charge to refund. Required. The charge must belong to the merchant, or to the customer if the
     * customer id is set.
     */
    public RefundParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

    /** Cause of the refund. Optional. */
    public RefundParams description(final String description) {
        return this.with("description", description);
    }
    
    /** Address to refund bitcoins. Optional. */
    public RefundParams bitcoinAddress(final String bitcoinAddress) {
        return this.with("bitcoin_address", bitcoinAddress);
    }
    
    /** amount to refund. Optional, used in partial refunds */
    public RefundParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }
    
    /** Gateway options during refund. */
    public RefundParams gateway(final GatewayParams gateway) {
        return this.with("gateway", gateway);
    }
}
