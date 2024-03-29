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
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.CANCEL;
import static mx.openpay.client.utils.OpenpayPathComponents.CAPTURE;
import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.CONFIRM;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.transactions.CancelParams;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.ConfirmChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBankChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBitcoinChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.core.requests.transactions.UpdateTransactionParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * Operations for Openpay Charges.
 * @author elopez
 */
public class ChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;

    protected static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + REFUND;
    
    private static final String CANCEL_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CANCEL;

    private static final String CAPTURE_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CAPTURE;

    private static final String CONFIRM_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + CONFIRM;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    protected static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;
    
    private static final String CANCEL_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CANCEL;

    private static final String CAPTURE_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CAPTURE;

    private static final String CONFIRM_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + CONFIRM;

    public ChargeOperations(final JsonServiceClient client) {
        super(client);
    }

    /**
     * Creates any kind of charge at the Merchant level.
     * @param request Generic request params.
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @since 1.0.10
     */
    public Charge createCharge(RequestBuilder request) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    /**
     * Creates any kind of charge at the Customer level.
     * @param customerId ID of the Customer created previously in Openpay.
     * @param request Generic request params.
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @since 1.0.10
     */
    public Charge createCharge(final String customerId, RequestBuilder request)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request.asMap(), Charge.class);
    }

    public Charge update(final UpdateTransactionParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), request.getChargeId());
        return this.getJsonClient().put(path, request.asMap(), Charge.class);
    }

    public Charge update(final String customerId, final UpdateTransactionParams request)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, request.getChargeId());
        return this.getJsonClient().put(path, request.asMap(), Charge.class);
    }

    public List<Charge> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Charge.class);
    }

    public List<Charge> list(final String customerId, final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Charge.class);
    }

    public Charge get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    public Charge get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    public Charge refund(final RefundParams params) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    public Charge refund(final String customerId, final RefundParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }
    
    public Charge cancel(final CancelParams params) throws OpenpayServiceException, ServiceUnavailableException {
    	String path = String.format(CANCEL_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
    	return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }
    
    public Charge cancel(final String customerId, final CancelParams params) throws OpenpayServiceException,
    ServiceUnavailableException {
    	String path = String.format(CANCEL_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, params.getChargeId());
    	return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /**
     * Confirms a charge that was made with the option capture set to false.
     */
    public Charge confirmCapture(final ConfirmCaptureParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CAPTURE_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /**
     * Confirms a charge that was made with the option capture set to false.
     */
    public Charge confirmCapture(final String customerId, final ConfirmCaptureParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CAPTURE_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId,
                params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /**
     * Confirms a charge that was made with the option confirm set to false.
     */
    public Charge confirmCharge(final ConfirmChargeParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CONFIRM_FOR_MERCHANT_PATH, this.getMerchantId(), params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /**
     * Confirms a charge that was made with the option confirm set to false.
     */
    public Charge confirmCharge(final String customerId, final ConfirmChargeParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CONFIRM_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId,
                params.getChargeId());
        return this.getJsonClient().post(path, params.asMap(), Charge.class);
    }

    /**
     * @deprecated Use {@link #createCharge(RequestBuilder)} with {@link CreateCardChargeParams}
     */
    @Deprecated
    public Charge create(final Card card, final BigDecimal amount, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .amount(amount)
                .description(description).orderId(orderId)
                .card(card);
        return this.create(charge);
    }

    /**
     * @deprecated Use {@link #createCharge(String, RequestBuilder)} with {@link CreateCardChargeParams}
     */
    @Deprecated
    public Charge create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderId)
            throws ServiceUnavailableException, OpenpayServiceException {
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .amount(amount)
                .description(description)
                .orderId(orderId)
                .card(card);
        return this.create(customerId, charge);
    }

    /**
     * @deprecated Use {@link #createCharge(String, RequestBuilder)} with {@link CreateCardChargeParams}
     */
    @Deprecated
    public Charge create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderId)
            throws ServiceUnavailableException, OpenpayServiceException {
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .cardId(sourceId)
                .amount(amount)
                .description(description)
                .orderId(orderId);
        return this.create(customerId, charge);
    }

    /**
     * @deprecated Use {@link #createCharge(RequestBuilder)} with {@link CreateBankChargeParams}
     */
    @Deprecated
    public Charge createMerchantBankTransfer(final BigDecimal amount, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        CreateBankChargeParams request = new CreateBankChargeParams()
                .amount(amount)
                .description(description)
                .orderId(orderId);
        return this.create(request);
    }

    /**
     * @deprecated Use {@link #createCharge(String, RequestBuilder)} with {@link CreateBankChargeParams}
     */
    @Deprecated
    public Charge createCustomerBankTransfer(final String customerId, final BigDecimal amount,
            final String description,
            final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        CreateBankChargeParams request = new CreateBankChargeParams()
                .amount(amount)
                .description(description)
                .orderId(orderId);
        return this.create(customerId, request);
    }

    /**
     * Creates a card charge at the Merchant level
     * @param request Card charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final CreateCardChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(request);
    }

    /**
     * Creates a card charge at the Customer level.
     * @param customerId ID of the Customer created previously in Openpay.
     * @param request Card charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(String, RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final String customerId, final CreateCardChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(customerId, request);
    }

    /**
     * Creates a bank account charge at the Merchant level
     * @param request bank charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final CreateBankChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(request);
    }

    /**
     * Creates a bank account charge at the Customer level.
     * @param customerId ID of the Customer created previously in Openpay.
     * @param request bank charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(String, RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final String customerId, final CreateBankChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(customerId, request);
    }

    /**
     * Creates a store charge at the Merchant level
     * @param request bank charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final CreateStoreChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(request);
    }

    /**
     * Creates a store charge at the Customer level.
     * @param customerId ID of the Customer created previously in Openpay.
     * @param request Store charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated You can use {@link #createCharge(String, RequestBuilder)} for all charge methods.
     */
    @Deprecated
    public Charge create(final String customerId, final CreateStoreChargeParams request)
            throws OpenpayServiceException, ServiceUnavailableException {
        return createCharge(customerId, request);
    }

    /**
     * Creates a Bitcoin charge at the Merchant level.
     * @param request bank charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated Bitcoin requests have been discontinued, but the method remains for code compatibility.
     */
    @Deprecated
    public Charge create(final CreateBitcoinChargeParams request) throws OpenpayServiceException,
            ServiceUnavailableException {
        return createCharge(request);
    }

    /**
     * Creates a Bitcoin charge at the Customer level.
     * @param customerId ID of the Customer created previously in Openpay.
     * @param request Card charge params
     * @return Charge data returned by Openpay
     * @throws OpenpayServiceException When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     * @deprecated Bitcoin requests have been discontinued, but the method remains for code compatibility.
     */
    @Deprecated
    public Charge create(final String customerId, final CreateBitcoinChargeParams request)
            throws OpenpayServiceException, ServiceUnavailableException {
        return createCharge(customerId, request);
    }

    @Deprecated
    public Charge refund(final String transactionId, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        return this.refund(new RefundParams().chargeId(transactionId).description(description));
    }

    @Deprecated
    public Charge refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        return this.refund(customerId, new RefundParams().chargeId(transactionId).description(description));
    }

}
