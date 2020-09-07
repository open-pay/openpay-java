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

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * Parameters to refund a completed charge. Currently only card charges can be refunded this way.
 * @author Oswaldo Martinez oswaldo.martinez@openpay.mx
 */
public class ConfirmChargeParams extends RequestBuilder {

    @Getter
    private String chargeId;

    /**
     * The ID of the charge to confirm. Required.
     */
    public ConfirmChargeParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

    /**
     * The token of the card to charge. Required, a valid token.
     */
    public ConfirmChargeParams tokenId(final String tokenId) {
        return this.with("token_id", tokenId);
    }

    /**
     * The device session id generated in client side. Required,
     */
    public ConfirmChargeParams deviceSessionId(final String deviceSessionId) {
        return this.with("device_session_id", deviceSessionId);
    }
    
    /**
     * Selected payments. Optional 
     */
    public ConfirmChargeParams payments(final Integer payments){
        return this.with("payments", payments);
    }
    
}
