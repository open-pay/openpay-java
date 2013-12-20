/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds requests adding parameters to a hashmap. This class could be used by itself to make requests, but it should be
 * extended to create classes that list the allowed parameters for the request.
 * @author elopez
 */
public abstract class RequestBuilder {

    private Map<String, Object> parameters = new HashMap<String, Object>();

    /**
     * Adds the given parameter to the map and returns this same object.
     * @param jsonParam JSON name of the parameter to add
     * @param obj Object to add to the map
     * @return This same object
     */
    @SuppressWarnings("unchecked")
    public <T extends RequestBuilder> T with(final String jsonParam, final Object obj) {
        this.parameters.put(jsonParam, obj);
        return (T) this;
    }

    /**
     * Removes the given parameter from the request.
     * @param jsonParam The JSON name of the parameter.
     */
    public void without(final String jsonParam) {
        this.parameters.remove(jsonParam);
    }

    /**
     * Returns an unmodifiable map containing the builder's parameters.
     * @return
     */
    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(this.parameters);
    }

}
