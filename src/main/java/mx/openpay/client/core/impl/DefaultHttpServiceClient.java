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
package mx.openpay.client.core.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.core.HttpServiceClient;
import mx.openpay.client.core.HttpServiceResponse;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 * Uses Apache HttpClient to call the web service and retrieve the response information.
 * @author elopez
 * @see HttpServiceClient
 */
@Slf4j
public class DefaultHttpServiceClient implements HttpServiceClient {

    private static final String AGENT = "openpay-java/";

    private static final int DEFAULT_CONNECTION_TIMEOUT = 90000;

    private final HttpClient httpClient;

    private final String userAgent;

    @Setter
    private String key;

    public DefaultHttpServiceClient() {
        this.httpClient = this.initHttpClient();
        this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        this.setSocketTimeout(DEFAULT_CONNECTION_TIMEOUT);
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "1.0.1-UNKNOWN";
        }
        this.userAgent = AGENT + version;
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#setConnectionTimeout(int)
     */
    @Override
    public void setConnectionTimeout(final int timeout) {
        this.httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    @Override
    public void setSocketTimeout(final int timeout) {
        this.httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }

    private HttpClient initHttpClient() {
        PoolingClientConnectionManager connMgr = new PoolingClientConnectionManager();
        HttpClient httpClient = new DefaultHttpClient(connMgr);
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        return httpClient;
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#get(java.lang.String)
     */
    @Override
    public HttpServiceResponse get(final String url) throws ServiceUnavailableException {
        HttpGet request = new HttpGet(url);
        return this.executeOperation(request);
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#get(java.lang.String, java.util.Map)
     */
    @Override
    public HttpServiceResponse get(final String url, final Map<String, String> queryParams)
            throws ServiceUnavailableException {
        URI uri;
        if (queryParams == null) {
            uri = URI.create(url);
        } else {
            uri = this.createUriWithParams(url, queryParams);
        }
        HttpGet request = new HttpGet(uri);
        return this.executeOperation(request);

    }

    private URI createUriWithParams(final String url, final Map<String, String> queryParams)
            throws IllegalArgumentException {
        URIBuilder builder = new URIBuilder(URI.create(url));
        for (Entry<String, String> entry : queryParams.entrySet()) {
            if (entry.getValue() != null) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            return builder.build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#delete(java.lang.String)
     */
    @Override
    public HttpServiceResponse delete(final String url) throws ServiceUnavailableException {
        HttpDelete request = new HttpDelete(URI.create(url));
        return this.executeOperation(request);
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#put(java.lang.String, java.lang.String)
     */
    @Override
    public HttpServiceResponse put(final String url, final String json) throws ServiceUnavailableException {
        HttpPut request = new HttpPut(URI.create(url));
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        return this.executeOperation(request);
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#post(java.lang.String, java.lang.String)
     */
    @Override
    public HttpServiceResponse post(final String url, final String json) throws ServiceUnavailableException {
        HttpPost request = new HttpPost(URI.create(url));
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        return this.executeOperation(request);
    }

    private HttpServiceResponse executeOperation(final HttpUriRequest request) throws ServiceUnavailableException {
        this.addHeaders(request);
        this.addAuthentication(request);
        long init = System.currentTimeMillis();
        HttpResponse response = this.callService(request);
        HttpServiceResponse serviceResponse = this.createResult(response);
        log.trace("Request Time: {}", (System.currentTimeMillis() - init));
        return serviceResponse;
    }

    private void addHeaders(final HttpUriRequest request) {
        request.addHeader(new BasicHeader("User-Agent", this.userAgent));
        request.addHeader(new BasicHeader("Accept", "application/json"));
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

    private HttpResponse callService(final HttpUriRequest request) throws ServiceUnavailableException {
        HttpResponse response;
        try {
            response = this.httpClient.execute(request);
        } catch (ClientProtocolException e) {
            throw new ServiceUnavailableException(e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(e);
        }
        return response;
    }

    private HttpServiceResponse createResult(final HttpResponse response) throws ParseException,
            UnsupportedCharsetException {
        HttpServiceResponse serviceResponse = new HttpServiceResponse();
        serviceResponse.setStatusCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                serviceResponse.setBody(EntityUtils.toString(entity));
            } catch (IOException e) {
                log.error("Could not get body request", e);
            }
            if (entity.getContentType() != null) {
                serviceResponse.setContentType(entity.getContentType().getValue());
            }
        }
        return serviceResponse;
    }

}
