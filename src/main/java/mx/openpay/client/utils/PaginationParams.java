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
package mx.openpay.client.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters used to paginate transaction lists.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class PaginationParams {

    Map<String, String> params = new HashMap<String, String>();

    public static PaginationParams pagination() {
        return new PaginationParams();
    }

    public PaginationParams limit(final int limit) {
        this.params.put("limit", String.valueOf(limit));
        return this;
    }

    public PaginationParams offset(final int offset) {
        this.params.put("offset", String.valueOf(offset));
        return this;
    }

    public Map<String, String> asMap() {
        return this.params;
    }

}
