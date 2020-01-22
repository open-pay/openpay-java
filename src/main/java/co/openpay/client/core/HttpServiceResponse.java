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
package co.openpay.client.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author elopez
 */
@Getter
@Setter
public class HttpServiceResponse {

    private static final String JSON_MIME_TYPE = "application/json";

    private String contentType;

    private int statusCode;

    private String body;

    public boolean isJson() {
        return this.contentType != null && this.contentType.startsWith(JSON_MIME_TYPE);
    }
}
