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
package co.openpay.client.core.operations.groups;

import static co.openpay.client.utils.OpenpayPathComponents.CARDS;
import static co.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static co.openpay.client.utils.OpenpayPathComponents.GROUPS;
import static co.openpay.client.utils.OpenpayPathComponents.ID;

import java.util.List;
import java.util.Map;

import co.openpay.client.Card;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.core.operations.ServiceOperations;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class GroupCardOperations extends ServiceOperations {

    private static final String GROUP_CUSTOMER_CARDS_PATH = GROUPS + ID + CUSTOMERS + ID + CARDS;

    private static final String GET_GROUP_CUSTOMER_CARD_PATH = GROUP_CUSTOMER_CARDS_PATH + ID;

    public GroupCardOperations(final JsonServiceClient client) {
        super(client);
    }

    public Card create(final String customerId, final Card card) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GROUP_CUSTOMER_CARDS_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, card, Card.class);
    }

    public List<Card> list(final String customerId, final SearchParams params)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(GROUP_CUSTOMER_CARDS_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Card.class);
    }

    public Card get(final String customerId, final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_GROUP_CUSTOMER_CARD_PATH, this.getMerchantId(), customerId, cardId);
        return this.getJsonClient().get(path, Card.class);
    }

    public void delete(final String customerId, final String cardId) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(GET_GROUP_CUSTOMER_CARD_PATH, this.getMerchantId(), customerId, cardId);
        this.getJsonClient().delete(path);
    }
}
