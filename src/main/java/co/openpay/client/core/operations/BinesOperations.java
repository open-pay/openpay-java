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
package co.openpay.client.core.operations;

import static co.openpay.client.utils.OpenpayPathComponents.BIN;
import static co.openpay.client.utils.OpenpayPathComponents.BINES;
import static co.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import co.openpay.client.Bin;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author elopez
 */
public class BinesOperations extends ServiceOperations {

    private static final String MERCHANT_CARDS_PATH = MERCHANT_ID + BINES + BIN;

    public BinesOperations(final JsonServiceClient client) {
        super(client);
    }

    public Bin get(final String bin) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(MERCHANT_CARDS_PATH, this.getMerchantId(), bin);
        return this.getJsonClient().get(path, Bin.class);
    }

}
