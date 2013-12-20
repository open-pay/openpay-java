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

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.core.impl.DefaultHttpServiceClient;
import mx.openpay.client.core.impl.DefaultSerializer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author Heber Lazcano
 */
@Slf4j
public class JsonServiceClient {

    private final String root;

    @Getter
    private JsonSerializer serializer;

    @Getter
    private HttpServiceClient httpClient;

    public JsonServiceClient(final String location, final String key) throws GeneralSecurityException {
        this.root = location;
        this.serializer = new DefaultSerializer();
        this.httpClient = new DefaultHttpServiceClient();
        this.httpClient.setKey(key);
    }

    public <T> T get(final String path, final Class<T> clazz) throws OpenpayServiceException,
            ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.get(this.buildUri(path));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> List<T> list(final String path, final Map<String, String> params, final Class<T> clazz)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.get(this.buildUri(path), params);
        this.checkForErrors(response);
        return this.deserializeList(response, clazz);
    }

    public void delete(final String path) throws OpenpayServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.delete(this.buildUri(path));
        this.checkForErrors(response);
    }

    public <T> T put(final String path, final Map<String, Object> params, final Class<T> clazz)
            throws OpenpayServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.put(this.buildUri(path), this.serializer.serialize(params));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    public <T> T post(final String path, final Map<String, Object> params, final Class<T> clazz)
            throws OpenpayServiceException, ServiceUnavailableException {
        HttpServiceResponse response = this.httpClient.post(this.buildUri(path), this.serializer.serialize(params));
        this.checkForErrors(response);
        return this.deserializeObject(response, clazz);
    }

    private String buildUri(final String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.root);
        sb.append(path);
        return sb.toString();
    }

    private void checkForErrors(final HttpServiceResponse response) throws OpenpayServiceException {
        if (response.getStatusCode() >= 300) {
            if (response.isJson()) {
                OpenpayServiceException error = this.serializer.deserialize(response.getBody(),
                        OpenpayServiceException.class);
                throw error;
            } else {
                log.error("Not a Json response: {} ", response.getBody());
                OpenpayServiceException openpayServiceException = new OpenpayServiceException("["
                        + response.getStatusCode() + "] Internal server error");
                openpayServiceException.setHttpCode(response.getStatusCode());
                openpayServiceException.setErrorCode(1000);
                throw openpayServiceException;
            }
        }
    }

    private <T> T deserializeObject(final HttpServiceResponse response, final Class<T> clazz)
            throws OpenpayServiceException {
        if (response.isJson()) {
            return this.serializer.deserialize(response.getBody(), clazz);
        } else if (response.getBody() != null) {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

    private <T> List<T> deserializeList(final HttpServiceResponse response, final Class<T> clazz)
            throws OpenpayServiceException {
        if (response.isJson()) {
            return this.serializer.deserializeList(response.getBody(), clazz);
        } else {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

}
