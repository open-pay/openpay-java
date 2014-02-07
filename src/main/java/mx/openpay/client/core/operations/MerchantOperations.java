/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
