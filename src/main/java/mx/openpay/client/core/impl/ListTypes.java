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
package mx.openpay.client.core.impl;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.GenericTransaction;
import mx.openpay.client.Order;
import mx.openpay.client.PaymentPlan;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.Transfer;

import com.google.gson.reflect.TypeToken;

/**
 * @author elopez
 */
public class ListTypes {

    private static final Map<Class<?>, Type> TYPES_MAP;

    static {
        Map<Class<?>, Type> map = new HashMap<Class<?>, Type>();
        map.put(BankAccount.class, new TypeToken<List<BankAccount>>() {
        }.getType());
        map.put(Card.class, new TypeToken<List<Card>>() {
        }.getType());
        map.put(Customer.class, new TypeToken<List<Customer>>() {
        }.getType());
        map.put(Charge.class, new TypeToken<List<Charge>>() {
        }.getType());
        map.put(Fee.class, new TypeToken<List<Fee>>() {
        }.getType());
        map.put(Transfer.class, new TypeToken<List<Transfer>>() {
        }.getType());
        map.put(Payout.class, new TypeToken<List<Payout>>() {
        }.getType());
        map.put(Plan.class, new TypeToken<List<Plan>>() {
        }.getType());
        map.put(Subscription.class, new TypeToken<List<Subscription>>() {
        }.getType());
        map.put(GenericTransaction.class, new TypeToken<List<GenericTransaction>>() {
        }.getType());
		map.put(Order.class, new TypeToken<List<Order>>() {
		}.getType());
        map.put(PaymentPlan.class, new TypeToken<List<PaymentPlan>>() {
        }.getType());
        
        TYPES_MAP = Collections.unmodifiableMap(map);
    }

    static Type getType(final Class<?> clazz) {
        return TYPES_MAP.get(clazz);
    }

}
