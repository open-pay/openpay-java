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
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import lombok.Getter;
import mx.openpay.client.GatewayParams;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * Parameters to capture a preauthorized charge.
 * @author elopez
 */
public class ConfirmCaptureParams extends RequestBuilder {

    @Getter
    private String chargeId;

    /**
     * The ID of the charge to confirm. Required.
     */
    public ConfirmCaptureParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

    /**
     * The amount to confirm. Required, may be less than the amount that was authorized.
     */
    public ConfirmCaptureParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /** Additional Gateway Options. */
    public ConfirmCaptureParams gateway(final GatewayParams gatewayParams) {
        return this.with("gateway", gatewayParams);
    }
}
