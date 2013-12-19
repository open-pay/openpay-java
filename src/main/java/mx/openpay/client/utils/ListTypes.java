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
package mx.openpay.client.utils;

import java.lang.reflect.Type;
import java.util.Collection;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.Transfer;

import com.google.gson.reflect.TypeToken;

/**
 * @author elopez
 */
public class ListTypes {

    public static final Type BANK_ACCOUNT = new TypeToken<Collection<BankAccount>>() {
    }.getType();

    public static final Type CARD = new TypeToken<Collection<Card>>() {
    }.getType();

    public static final Type CUSTOMER = new TypeToken<Collection<Customer>>() {
    }.getType();

    public static final Type CHARGE = new TypeToken<Collection<Charge>>() {
    }.getType();

    public static final Type FEE = new TypeToken<Collection<Fee>>() {
    }.getType();

    public static final Type TRANSFER = new TypeToken<Collection<Transfer>>() {
    }.getType();

    public static final Type PAYOUT = new TypeToken<Collection<Payout>>() {
    }.getType();

    public static final Type PLANS = new TypeToken<Collection<Plan>>() {
    }.getType();

    public static final Type SUBSCRIPTIONS = new TypeToken<Collection<Subscription>>() {
    }.getType();;

}
