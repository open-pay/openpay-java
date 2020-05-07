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

import static mx.openpay.client.utils.OpenpayPathComponents.CARDS;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.POINTS;

import java.util.List;
import java.util.Map;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.PointsBalance;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class CardOperations extends ServiceOperations {

    private static final String MERCHANT_CARDS_PATH = MERCHANT_ID + CARDS;

    private static final String CUSTOMER_CARDS_PATH = MERCHANT_ID + CUSTOMERS + ID + CARDS;
    
    private static final String PUT_MERCHANT_CARDS_PATH = MERCHANT_ID + CARDS + ID;

    private static final String PUT_CUSTOMER_CARDS_PATH = MERCHANT_ID + CUSTOMERS + ID + CARDS + ID;

    private static final String GET_MERCHANT_CARD_PATH = MERCHANT_CARDS_PATH + ID;

    private static final String GET_MERCHANT_CARD_POINTS_PATH = GET_MERCHANT_CARD_PATH + POINTS;
    
    private static final String GET_CUSTOMER_CARD_PATH = CUSTOMER_CARDS_PATH + ID;
    
    private static final String GET_CUSTOMER_CARD_POINTS_PATH = GET_CUSTOMER_CARD_PATH + POINTS;

    public CardOperations(final JsonServiceClient client) {
        super(client);
    }

    public Card create(final Card card) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_CARDS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, card, Card.class);
    }

    public Card create(final String customerId, final Card card) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_CARDS_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, card, Card.class);
    }

    public Card update(final Card card) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(PUT_MERCHANT_CARDS_PATH, this.getMerchantId(), card.getId());
        return this.getJsonClient().put(path, card, Card.class);
    }

    public Card update(final String customerId, final Card card) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(PUT_CUSTOMER_CARDS_PATH, this.getMerchantId(), customerId, card.getId());
        return this.getJsonClient().put(path, card, Card.class);
    }

    public List<Card> list(final SearchParams params) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(MERCHANT_CARDS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Card.class);
    }

    public List<Card> list(final String customerId, final SearchParams params)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(CUSTOMER_CARDS_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Card.class);
    }

    public Card get(final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_MERCHANT_CARD_PATH, this.getMerchantId(), cardId);
        return this.getJsonClient().get(path, Card.class);
    }

    public Card get(final String customerId, final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_CUSTOMER_CARD_PATH, this.getMerchantId(), customerId, cardId);
        return this.getJsonClient().get(path, Card.class);
    }

    public PointsBalance points(final String cardId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(GET_MERCHANT_CARD_POINTS_PATH, this.getMerchantId(), cardId);
        return this.getJsonClient().get(path, PointsBalance.class);
    }
    
    public PointsBalance points(final String customerId, final String cardId)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(GET_CUSTOMER_CARD_POINTS_PATH, this.getMerchantId(), customerId, cardId);
        return this.getJsonClient().get(path, PointsBalance.class);
    }

    public void delete(final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_MERCHANT_CARD_PATH, this.getMerchantId(), cardId);
        this.getJsonClient().delete(path);
    }

    public void delete(final String customerId, final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_CUSTOMER_CARD_PATH, this.getMerchantId(), customerId, cardId);
        this.getJsonClient().delete(path);
    }

    @Deprecated
    public Card create(final String customerId, final String cardNumber, final String holderName,
            final String cvv2, final String expMonth, final String expYear, final Address address)
            throws ServiceUnavailableException, OpenpayServiceException {
        Card card = new Card()
                .cardNumber(cardNumber)
                .cvv2(cvv2)
                .holderName(holderName)
                .address(address);
        card.setExpirationMonth(expMonth);
        card.setExpirationYear(expYear);
        return this.create(customerId, card);
    }
}
