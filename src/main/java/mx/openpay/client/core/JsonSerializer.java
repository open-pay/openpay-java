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

import java.util.List;
import java.util.Map;

/**
 * @author elopez
 */
public interface JsonSerializer {

    public String serialize(final Map<String, Object> values);

    public <T> List<T> deserializeList(final String json, final Class<T> clazz);

    public <T> T deserialize(final String json, final Class<T> type);

}
