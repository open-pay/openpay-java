/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
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
 * @author elopez
 */
@Slf4j
public class DefaultHttpServiceClient implements HttpServiceClient {

    private static final String AGENT = "openpay-java/";

    private static final int DEFAULT_CONNECTION_TIMEOUT = 90000;

    private final HttpClient httpClient;

    private final String userAgent;

    @Setter
    private String key;

    public DefaultHttpServiceClient() throws GeneralSecurityException {
        this.httpClient = this.initHttpClient();
        this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "1.0-UNKNOWN";
        }
        this.userAgent = AGENT + version;
    }

    public void setConnectionTimeout(final int timeout) {
        this.httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        this.httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    private HttpClient initHttpClient() throws GeneralSecurityException {
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
     * @throws ServiceUnavailableException
     * @see mx.openpay.client.core.HttpServiceClient#get(java.lang.String, java.util.Map)
     */
    @Override
    public HttpServiceResponse get(final String url, final Map<String, String> queryParams)
            throws ServiceUnavailableException {
        URIBuilder builder = new URIBuilder(URI.create(url));
        if (queryParams != null) {
            for (Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getValue() != null) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        try {
            HttpGet request = new HttpGet(builder.build());
            return this.executeOperation(request);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @throws ServiceUnavailableException
     * @see mx.openpay.client.core.HttpServiceClient#delete(java.lang.String)
     */
    @Override
    public HttpServiceResponse delete(final String url) throws ServiceUnavailableException {
        HttpDelete request = new HttpDelete(URI.create(url));
        return this.executeOperation(request);
    }

    /**
     * @throws ServiceUnavailableException
     * @see mx.openpay.client.core.HttpServiceClient#put(java.lang.String, java.lang.String)
     */
    @Override
    public HttpServiceResponse put(final String url, final String json) throws ServiceUnavailableException {
        HttpPut request = new HttpPut(URI.create(url));
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        return this.executeOperation(request);
    }

    /**
     * @throws ServiceUnavailableException
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
        HttpResponse response;
        try {
            response = this.httpClient.execute(request);
        } catch (ClientProtocolException e) {
            throw new ServiceUnavailableException(e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(e);
        }
        HttpServiceResponse serviceResponse = new HttpServiceResponse();
        serviceResponse.setStatusCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                serviceResponse.setBody(EntityUtils.toString(entity));
                serviceResponse.setContentType(ContentType.getOrDefault(entity).getMimeType());
            } catch (IOException e) {
                throw new ServiceUnavailableException(e);
            }
        }
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

    /**
     * @see mx.openpay.client.core.HttpServiceClient#setConnectionTimeout(long)
     */
    @Override
    public void setConnectionTimeout(final long timeoutMillis) {
        // TODO Auto-generated method stub

    }

}
