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
package mx.openpay.client.core;

import java.util.Map;

import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * Calls the web service with the given parameters and returns the response information necessary to deserialize the
 * object.
 * @author elopez
 */
public interface HttpServiceClient {

    public void setKey(final String key);

    public void setPublicIp(final String publicIp);

    /**
     * Optional method to set connection timeout. Should do nothing if not implemented.
     * @param timeoutMillis
     */
    public void setConnectionTimeout(final int timeoutMillis);

    /**
     * Optional method to set socket timeout. Should do nothing if not implemented.
     * @param timeoutMillis
     */
    public void setSocketTimeout(final int timeoutMillis);

    public HttpServiceResponse get(final String url) throws ServiceUnavailableException;

    public HttpServiceResponse get(final String url, final Map<String, String> queryParams)
            throws ServiceUnavailableException;

    public HttpServiceResponse delete(final String url) throws ServiceUnavailableException;

    public HttpServiceResponse put(final String url, final String json) throws ServiceUnavailableException;

    public HttpServiceResponse post(final String url, final String json) throws ServiceUnavailableException;

}
