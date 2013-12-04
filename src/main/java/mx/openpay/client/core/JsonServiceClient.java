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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.serialization.BankAccountAdapterFactory;
import mx.openpay.client.serialization.CardAdapterFactory;
import mx.openpay.client.serialization.CustomerAdapterFactory;
import mx.openpay.client.serialization.DateFormatDeserializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Heber Lazcano
 */
@Slf4j
public class JsonServiceClient {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 90000;

    private static final String AGENT = "openpay-java/";

    private final String root;

    private final String key;

    private final Gson gson;

    @Getter
    private final String userAgent;

    private final HttpClient httpClient;

    public JsonServiceClient(final String location, final String key) throws GeneralSecurityException {
        this.root = location;
        this.key = key;
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "1.0-UNKNOWN";
        }
        this.userAgent = AGENT + version;
        this.httpClient = this.initHttpClient();
        this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateFormatDeserializer())
                .registerTypeAdapterFactory(new CustomerAdapterFactory())
                .registerTypeAdapterFactory(new CardAdapterFactory())
                .registerTypeAdapterFactory(new BankAccountAdapterFactory())
                .create();
    }

    private HttpClient initHttpClient() throws GeneralSecurityException {
        PoolingClientConnectionManager connMgr = new PoolingClientConnectionManager();
        HttpClient httpClient = new DefaultHttpClient(connMgr);
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        return httpClient;
    }

    public void setConnectionTimeout(final int timeout) {
        this.httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        this.httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    public <T> T get(final String path, final Class<T> clazz) throws OpenpayServiceException,
            ServiceUnavailableException {
        URI uri = this.buildUri(path);
        HttpGet request = new HttpGet(uri);
        return this.executeOperation(request, clazz, null);
    }

    public <T> T get(final String path, final Map<String, String> map, final Class<T> clazz)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        URI uri = this.buildUri(path, map);
        HttpGet request = new HttpGet(uri);
        return this.executeOperation(request, clazz, null);
    }

    public <T> T list(final String path, final Map<String, String> params, final Type type)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        URI uri = this.buildUri(path, params);
        HttpGet request = new HttpGet(uri);
        return this.executeOperation(request, null, type);
    }

    public void delete(final String path) throws OpenpayServiceException, ServiceUnavailableException {
        URI uri = this.buildUri(path);
        HttpDelete request = new HttpDelete(uri);
        this.executeOperation(request, null, null);
    }

    public <T> T put(final String path, final Object payload, final Class<T> returnClazz)
            throws OpenpayServiceException,
            ServiceUnavailableException {
        URI uri = this.buildUri(path);
        HttpPut request = new HttpPut(uri);
        request.setEntity(new StringEntity(this.serialize(payload), ContentType.APPLICATION_JSON));
        return this.executeOperation(request, returnClazz, null);
    }

    public <T> T post(final String path, final Object payload, final Class<T> clazz) throws OpenpayServiceException,
            ServiceUnavailableException {
        URI uri = this.buildUri(path);
        HttpPost request = new HttpPost(uri);
        request.setEntity(new StringEntity(this.serialize(payload), ContentType.APPLICATION_JSON));
        return this.executeOperation(request, clazz, null);
    }

    private URI buildUri(final String path) {
        return this.buildUri(path, null);
    }

    private URI buildUri(final String path, final Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.root);
        sb.append(path);
        if (params != null && params.size() > 0) {
            sb.append("?");
            sb.append(this.buildQueryString(params));
        }
        String url = sb.toString();
        log.debug("Calling URL: {}", url);
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI can't be parsed: " + url, e);
        }
    }

    private String buildQueryString(final Map<String, String> params) {
        ArrayList<NameValuePair> nvs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            NameValuePair nv = new BasicNameValuePair(entry.getKey(), entry.getValue());
            nvs.add(nv);
        }
        String queryString = URLEncodedUtils.format(nvs, "UTF-8");
        return queryString;
    }

    private void addHeaders(final HttpUriRequest request) {
        request.addHeader(new BasicHeader("User-Agent", this.userAgent));
        request.addHeader(new BasicHeader("Accept", "application/json"));
    }

    private <T> T executeOperation(final HttpUriRequest request, final Class<T> clazz, final Type type)
            throws OpenpayServiceException, ServiceUnavailableException {
        this.addHeaders(request);
        this.addAuthentication(request);
        long init = System.currentTimeMillis();
        HttpResponse response;
        try {
            response = this.httpClient.execute(request);
        } catch (ClientProtocolException e) {
            throw new ServiceUnavailableException(e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(e);
        }
        String body = null;
        Header contentType = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                contentType = entity.getContentType();
                body = EntityUtils.toString(entity);
            } catch (IOException e) {
                throw new ServiceUnavailableException(e);
            }
        }
        StatusLine status = response.getStatusLine();
        log.debug("Request Time: {}", (System.currentTimeMillis() - init));
        init = System.currentTimeMillis();
        T payload = this.getDeserializedObject(status, contentType, body, clazz, type);
        log.debug("Parse Time: {}", (System.currentTimeMillis() - init));
        return payload;

    }

    private <T> T getDeserializedObject(final StatusLine status, final Header contentType, final String body,
            final Class<T> clazz, final Type type) throws OpenpayServiceException {
        boolean isJsonResponse = contentType != null && contentType.getValue() != null
                && contentType.getValue().startsWith(ContentType.APPLICATION_JSON.getMimeType());
        if (status.getStatusCode() >= 299) {
            if (isJsonResponse) {
                OpenpayServiceException error = this.deserialize(body, OpenpayServiceException.class, null);
                throw error;
            } else {
                log.error("Not a Json response: {} ", body);
                OpenpayServiceException openpayServiceException = new OpenpayServiceException("["
                        + status.getStatusCode() + "] Internal server error");
                openpayServiceException.setHttpCode(status.getStatusCode());
                openpayServiceException.setErrorCode(1000);
                throw openpayServiceException;
            }
        } else if (isJsonResponse) {
            return this.deserialize(body, clazz, type);
        } else if (body != null) {
            log.debug("Body wasn't returned as JSON: {}", body);
        }
        return null;
    }

    private void addAuthentication(final HttpUriRequest request) {
        if (this.key != null) {
            String authEncoding = this.getBase64Auth();
            request.setHeader("Authorization", "Basic " + authEncoding);
        }
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    private String getBase64Auth() {
        // Impossible throw UnsuportedEncodingException
        byte[] auth = (this.key + ":").getBytes("UTF-8");
        return Base64.encodeBase64String(auth);
    }

    private String serialize(final Object payload) {
        String data = this.gson.toJson(payload);
        log.debug("Request: {}", data);
        return data;
    }

    private <T> T deserialize(final String body, final Class<T> clazz, final Type type) {
        log.debug("Response: {}", body);
        if (type == null) {
            return this.gson.fromJson(body, clazz);
        } else {
            return this.gson.fromJson(body, type);
        }
    }
}
