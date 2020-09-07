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
