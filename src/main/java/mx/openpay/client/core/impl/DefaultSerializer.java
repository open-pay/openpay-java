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

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.openpay.client.core.JsonSerializer;
import mx.openpay.client.serialization.BankAccountAdapterFactory;
import mx.openpay.client.serialization.CardAdapterFactory;
import mx.openpay.client.serialization.CustomerAdapterFactory;
import mx.openpay.client.serialization.DateFormatDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author elopez
 */
public class DefaultSerializer implements JsonSerializer {

    private final Gson gson;

    public DefaultSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateFormatDeserializer())
                .registerTypeAdapterFactory(new CustomerAdapterFactory())
                .registerTypeAdapterFactory(new CardAdapterFactory())
                .registerTypeAdapterFactory(new BankAccountAdapterFactory())
                .create();
    }

    @Override
    public String serialize(final Map<String, Object> values) {
        return this.gson.toJson(values);
    }

    @Override
    public <T> List<T> deserializeList(final String json, final Class<T> clazz) {
        Type type = ListTypes.getType(clazz);
        return this.gson.fromJson(json, type);
    }

    @Override
    public <T> T deserialize(final String json, final Class<T> clazz) {
        return this.gson.fromJson(json, clazz);
    }

}
