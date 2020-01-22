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
package co.openpay.client.core.operations;

import static co.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static co.openpay.client.utils.OpenpayPathComponents.OPENPAY_FEES;
import static co.openpay.client.utils.OpenpayPathComponents.REPORTS;
import static co.openpay.client.utils.OpenpayPathComponents.REPORT_DETAILS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.openpay.client.GenericTransaction;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.enums.FeeDetailsType;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.reports.OpenpayFeesSummary;
import co.openpay.client.utils.PaginationParams;

/**
 * Gets information about the fees that Openpay has charged the merchant in a given period of time.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class OpenpayFeesOperations extends ServiceOperations {

    private static final String FEES_PATH = MERCHANT_ID + REPORTS + OPENPAY_FEES;

    private static final String FEES_DETAILS_PATH = FEES_PATH + REPORT_DETAILS;

    public OpenpayFeesOperations(final JsonServiceClient client) {
        super(client);
    }

    /**
     * Retrieve the summary of the charged fees in the given month. The amounts retrieved in the current month may
     * change on a daily basis.
     * @param year Year of the date to retrieve
     * @param month Month to retrieve
     * @return The summary of the fees charged by Openpay.
     * @throws OpenpayServiceException If the service returns an error
     * @throws ServiceUnavailableException If the service is not available
     */
    public OpenpayFeesSummary getSummary(final int year, final int month) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FEES_PATH, this.getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        return this.getJsonClient().get(path, params, OpenpayFeesSummary.class);
    }

    /**
     * Retrieves the list of transactions that affected the charged or refunded fees on a given month. Transactions in
     * the CHARGED or REFUNDED type of detail add their fee to the total, while the ones in CHARGED_ADJUSTMENTS and
     * REFUNDED_ADJUSTMENTS add their amount.
     * @param year Year to report
     * @param month Month to report
     * @param feeType Type of the details to get.
     * @param pagination Pagination. Optional, retrieves the latest 10 transactions if ommited.
     * @return List of transactions that affect the fees in the given month.
     * @throws ServiceUnavailableException
     * @throws OpenpayServiceException
     */
    public List<GenericTransaction> getDetails(final int year, final int month, final FeeDetailsType feeType,
            final PaginationParams pagination) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FEES_DETAILS_PATH, this.getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        if (pagination != null) {
            params.putAll(pagination.asMap());
        }
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        params.put("fee_type", feeType.name().toLowerCase());
        return this.getJsonClient().list(path, params, GenericTransaction.class);
    }
}
