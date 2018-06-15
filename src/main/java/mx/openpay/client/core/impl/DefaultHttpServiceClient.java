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
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.core.HttpServiceClient;
import mx.openpay.client.core.HttpServiceResponse;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
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

    private final CloseableHttpClient httpClient;

    private final String userAgent;

    private RequestConfig requestConfig;

    @Setter
    private String key;

    public DefaultHttpServiceClient(final boolean requirePoolManager) {
        this.httpClient = this.initHttpClient(requirePoolManager, DEFAULT_CONNECTION_TIMEOUT,
                DEFAULT_CONNECTION_TIMEOUT);
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "1.0.10-UNKNOWN";
        }
        this.userAgent = AGENT + version;
    }

    /**
     * @see mx.openpay.client.core.HttpServiceClient#setConnectionTimeout(int)
     */
    @Override
    public void setConnectionTimeout(final int timeout) {
        this.requestConfig = RequestConfig.copy(this.requestConfig).setConnectTimeout(timeout).build();
    }

    @Override
    public void setSocketTimeout(final int timeout) {
        this.requestConfig = RequestConfig.copy(this.requestConfig).setSocketTimeout(timeout).build();
    }

    protected CloseableHttpClient initHttpClient(final boolean requirePoolManager, final int connectionTimeout,
            final int socketTimeout) {
        CloseableHttpClient httpClient;
        HttpClientConnectionManager manager;
        
        SSLConnectionSocketFactory sslSocketFactory;
        SSLContext tlsContext;
        try {
            try {
                tlsContext = new SSLContextBuilder().useProtocol("TLSv1.2").build();
            } catch (GeneralSecurityException e) {
                log.warn("Could not force protocol TLSv1.2: {}", e.getMessage());
                tlsContext = new SSLContextBuilder().build();
            }
            sslSocketFactory = new SSLConnectionSocketFactory(tlsContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        if (requirePoolManager) {
            manager = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslSocketFactory).build());
        } else {
            manager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslSocketFactory).build());
        }
        
        this.requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout).build();
        ConnectionConfig connnectionConfig = ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).build();
        httpClient = HttpClientBuilder.create()
                .setConnectionManager(manager)
                .setDefaultConnectionConfig(connnectionConfig)
                .setDefaultRequestConfig(this.requestConfig)
                .build();
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

    protected URI createUriWithParams(final String url, final Map<String, String> queryParams)
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
        request.setEntity(new StringEntity(json, Consts.UTF_8.name()));
        return this.executeOperation(request);
    }

    protected HttpServiceResponse executeOperation(final HttpRequestBase request) throws ServiceUnavailableException {
        this.addHeaders(request);
        this.addAuthentication(request);
        long init = System.currentTimeMillis();
        CloseableHttpResponse response = this.callService(request);
        HttpServiceResponse serviceResponse;
        try {
            serviceResponse = this.createResult(response);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        log.trace("Request Time: {}", (System.currentTimeMillis() - init));
        return serviceResponse;
    }

    protected void addHeaders(final HttpRequestBase request) {
        request.addHeader(new BasicHeader("User-Agent", this.userAgent));
        request.addHeader(new BasicHeader("Accept", "application/json"));
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
    }

    protected void addAuthentication(final HttpRequestBase request) {
        if (this.key != null) {
            String authEncoding = this.getBase64Auth();
            request.setHeader("Authorization", "Basic " + authEncoding);
        }
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    protected String getBase64Auth() {
        // Impossible throw UnsuportedEncodingException
        byte[] auth = (this.key + ":").getBytes("UTF-8");
        return StringUtils.newStringUtf8(Base64.encodeBase64(auth, false));
    }

    protected CloseableHttpResponse callService(final HttpRequestBase request) throws ServiceUnavailableException {
        request.setConfig(this.requestConfig);
        CloseableHttpResponse response;
        try {
            response = this.httpClient.execute(request);
        } catch (ClientProtocolException e) {
            throw new ServiceUnavailableException(e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(e);
        }
        return response;
    }

    protected HttpServiceResponse createResult(final HttpResponse response) throws ParseException,
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
