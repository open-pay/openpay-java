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

import static mx.openpay.client.utils.OpenpayPathComponents.VERSION;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.core.impl.DefaultHttpServiceClient;
import mx.openpay.client.core.impl.DefaultSerializer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * Calls the HTTP Service and parses the response, delegating to a HttpServiceClient and a JsonSerializer. Custom
 * implementations can be used if the defaults are not appropiate.
 * @author Heber Lazcano
 * @author elopez
 */
@Slf4j
public class JsonServiceClient {

    private static final String HTTP_RESOURCE_SEPARATOR = "/";

    private final String root;

    @Getter
    private final JsonSerializer serializer;

    @Getter
    private final HttpServiceClient httpClient;

    @Getter
    private final String merchantId;

    /**
     * Initializes a JsonServiceClient with the default JsonSerializer and HttpServiceClient.
     * @param location Base URL of the Webservice.
     * @param merchantId Merchant's Id.
     * @param key Public or private key. Public Key may have limited permissions.
     */
    public JsonServiceClient(final String location, final String merchantId, final String key) {
        this(location, merchantId, key, new DefaultSerializer(), new DefaultHttpServiceClient(true));
    }

    /**
     * Initializes a JsonServiceClient using a custom implementation of a serializer and http client. Useful if the
     * defaults need to be changed or a different Http Client library needs to be used.
     * @param location
     * @param merchantId
     * @param key
     * @param serializer
     * @param httpClient
     */
    public JsonServiceClient(final String location, final String merchantId, final String key,
            final JsonSerializer serializer, final HttpServiceClient httpClient) {
        this.validateParameters(location, merchantId);
        String url = this.getUrl(location);
        this.root = url;
        this.merchantId = merchantId;
        this.serializer = serializer;
        this.httpClient = httpClient;
        this.httpClient.setKey(key);
    }

    private void validateParameters(final String location, final String merchantId) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID can't be null");
        }
    }

    private String getUrl(final String location) {
        StringBuilder baseUri = new StringBuilder();
        if (location.contains("http") || location.contains("https")) {
            baseUri.append(location.replace("http:", "https:"));
        } else {
            baseUri.append("https://").append(location);
        }
        if (!location.endsWith(HTTP_RESOURCE_SEPARATOR)) {
            baseUri.append(HTTP_RESOURCE_SEPARATOR);
        }
        baseUri.append(VERSION);
        return baseUri.toString();
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

    public <T> T put(final String path, final T params, final Class<T> clazz)
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

    public <T> T post(final String path, final T params, final Class<T> clazz) throws OpenpayServiceException,
            ServiceUnavailableException {
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
                error.setBody(response.getBody());
                throw error;
            } else {
                log.error("Not a Json response: {} ", response.getBody());
                OpenpayServiceException openpayServiceException = new OpenpayServiceException("["
                        + response.getStatusCode() + "] Internal server error");
                openpayServiceException.setHttpCode(response.getStatusCode());
                openpayServiceException.setErrorCode(1000);
                openpayServiceException.setBody(response.getBody());
                throw openpayServiceException;
            }
        }
    }

    private <T> T deserializeObject(final HttpServiceResponse response, final Class<T> clazz) {
        if (response.isJson()) {
            return this.serializer.deserialize(response.getBody(), clazz);
        } else if (response.getBody() != null) {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

    private <T> List<T> deserializeList(final HttpServiceResponse response, final Class<T> clazz) {
        if (response.isJson()) {
            return this.serializer.deserializeList(response.getBody(), clazz);
        } else {
            log.debug("Body wasn't returned as JSON: {}", response.getBody());
        }
        return null;
    }

}
