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
package mx.openpay.client.enums;

/**
 * Methods to charge a transaction in Openpay.
 * @author elopez
 */
public enum ChargeMethods {

    /** Charges done to credit and debit cards. */
    CARD,

    /** Charges done through bank transfers. */
    BANK_ACCOUNT,

    /** Charges paid in a convenience store. */
    STORE,

    /** Charges paid through Bitcoin. Discontinued. */
    BITCOIN,

    /**
     * Charges paid through Alipay.
     * @since 1.0.10
     */
    ALIPAY,

    /**
     * Charges paid through Checkout Lending.
     */
    LENDING;
}
