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
package mx.openpay.client.core.requests.bank;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateBankAccountParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateBankAccountParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    /**
     * Name of the bank account's owner. Required.
     */
    public CreateBankAccountParams holderName(final String holderName) {
        return this.with("holder_name", holderName);
    }

    /**
     * Bank account's CLABE. Required. See <a href="http://es.wikipedia.org/wiki/CLABE">this</a> for an explanation.
     */
    public CreateBankAccountParams clabe(final String clabe) {
        return this.with("clabe", clabe);
    }

    /**
     * An alias to identify the bank account. Optional.
     */
    public CreateBankAccountParams alias(final String alias) {
        return this.with("alias", alias);
    }

}
