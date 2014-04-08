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
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import mx.openpay.client.Merchant;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantOperations extends ServiceOperations {

    private static final String MERCHANT_PATH = MERCHANT_ID;

    public MerchantOperations(final JsonServiceClient client) {
        super(client);
    }

    public Merchant get() throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().get(path, Merchant.class);
    };

}
