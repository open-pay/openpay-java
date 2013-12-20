/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core;

import java.util.Map;

import mx.openpay.client.exceptions.ServiceUnavailableException;

/**
 * @author elopez
 */
public interface HttpServiceClient {

    public void setKey(final String key);

    public HttpServiceResponse get(final String url) throws ServiceUnavailableException;

    public void setConnectionTimeout(final long timeoutMillis);

    public HttpServiceResponse get(final String url, final Map<String, String> queryParams)
            throws ServiceUnavailableException;

    public HttpServiceResponse delete(final String url) throws ServiceUnavailableException;

    public HttpServiceResponse put(final String url, final String json) throws ServiceUnavailableException;

    public HttpServiceResponse post(final String url, final String json) throws ServiceUnavailableException;

}
