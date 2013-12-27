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

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import mx.openpay.client.core.JsonSerializer;
import mx.openpay.client.serialization.DateFormatDeserializer;
import mx.openpay.client.serialization.SubscriptionAdapterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Serializes and deserializes the values using Gson.
 * @author elopez
 * @see JsonSerializer
 */
public class DefaultSerializer implements JsonSerializer {

    private final Gson gson;

    public DefaultSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateFormatDeserializer())
                .registerTypeAdapterFactory(new SubscriptionAdapterFactory())
                .create();
    }

    @Override
    public String serialize(final Object values) {
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
