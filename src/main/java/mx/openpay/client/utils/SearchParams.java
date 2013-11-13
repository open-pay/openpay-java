/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.utils;

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

    public Map<String, String> asMap() {
        return this.params;
    }
}
