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

import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.PAYOUT;
import static mx.openpay.client.utils.OpenpayPathComponents.REPORTS;
import static mx.openpay.client.utils.OpenpayPathComponents.REPORT_DETAILS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.GenericTransaction;
import mx.openpay.client.TransactionsPayoutResume;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.enums.TransactionsPayoutType;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.PaginationParams;

/**
 * The Class TransactionsPayoutOperations.
 * 
 * @author Luis Delucio
 */
public class TransactionsPayoutOperations extends ServiceOperations {

	private static final String PAYOUT_TRANSACTIONS_PATH = MERCHANT_ID + REPORTS + PAYOUT + ID;

	private static final String PAYOUT_TRANSACTIONS_DETAILS_PATH = PAYOUT_TRANSACTIONS_PATH + REPORT_DETAILS;

	public TransactionsPayoutOperations(JsonServiceClient client) {
		super(client);
	}

	/**
	 * Retrieve the resume of the transactions of the given payoutid.
	 * 
	 * @param payoutId
	 *            The payoutid.
	 * @return The transactions resume.
	 * @throws OpenpayServiceException
	 *             If the service returns an error
	 * @throws ServiceUnavailableException
	 *             If the service is not available
	 */
	public TransactionsPayoutResume getResume(final String payoutId) throws OpenpayServiceException,
            ServiceUnavailableException {
		String path = String.format(PAYOUT_TRANSACTIONS_PATH, this.getMerchantId(), payoutId);
		return this.getJsonClient().get(path, TransactionsPayoutResume.class);
    }

	/**
	 * Retrieves the list of transactions that affected the payout. .
	 * Transactions in the IN or OUT type of detail add their fee to the total,
	 * while the ones in CHARGED_ADJUSTMENTS and REFUNDED_ADJUSTMENTS add their
	 * amount.
	 * 
	 * @param payoutId
	 *            the payout id
	 * @param transactionsPayoutType
	 *            the transactions payout type
	 * @param pagination
	 *            Pagination. Optional, retrieves the latest 10 transactions if
	 *            ommited.
	 * @return List of transactions .
	 * @throws OpenpayServiceException
	 *             the openpay service exception
	 * @throws ServiceUnavailableException
	 *             the service unavailable exception
	 */
	public List<GenericTransaction> getDetails(final String payoutId,
			final TransactionsPayoutType transactionsPayoutType,
			final PaginationParams pagination) throws OpenpayServiceException, ServiceUnavailableException {
		String path = String.format(PAYOUT_TRANSACTIONS_DETAILS_PATH, this.getMerchantId(), payoutId);
		Map<String, String> params = new HashMap<String, String>();
		if (pagination != null) {
			params.putAll(pagination.asMap());
		}
		params.put("detail_type", transactionsPayoutType.name().toLowerCase());
		return this.getJsonClient().list(path, params, GenericTransaction.class);
	}

}
