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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchParams {

    private static final int DEFAULT_LIMIT_SIZE = 10;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private Map<String, String> params = new HashMap<String, String>();

    public static SearchParams search() {
        return new SearchParams();
    }

    public SearchParams() {
        this.limit(DEFAULT_LIMIT_SIZE);
    }

    public SearchParams creation(final Date date) {
        this.params.put("creation", this.format.format(date));
        return this;
    }

    public SearchParams creationLte(final Date date) {
        this.params.put("creation[lte]", this.format.format(date));
        return this;
    }

    public SearchParams creationGte(final Date date) {
        this.params.put("creation[gte]", this.format.format(date));
        return this;
    }

    /**
     * Shorthand for creationGte(start).creationLte(end).
     */
    public SearchParams between(final Date start, final Date end) {
        return this.creationGte(start).creationLte(end);
    }

    public SearchParams limit(final int limit) {
        this.params.put("limit", String.valueOf(limit));
        return this;
    }

    public SearchParams offset(final int offset) {
        this.params.put("offset", String.valueOf(offset));
        return this;
    }

    /**
     * Search by amount, for charges, payouts, fees and transfers.
     */
    public SearchParams amount(final BigDecimal amount) {
        this.params.put("amount", amount.toPlainString());
        return this;
    }

    public Map<String, String> asMap() {
        return this.params;
    }
}
