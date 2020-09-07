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
package mx.openpay.client.enums;

/**
 * Affects the transactions shown on the details of the Openpay Fees for a given month.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public enum FeeDetailsType {

    /** Shows transactions that had their fees charged. */
    CHARGED,

    /** Shows transactions that had their fees refunded. */
    REFUNDED,

    /** Shows transactions that added adjustments or other charges. */
    CHARGED_ADJUSTMENTS,

    /** Shows transactions that refunded adjustments or other refunds. */
    REFUNDED_ADJUSTMENTS

}
