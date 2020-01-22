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
package co.openpay.client.serialization;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Allows modifying the generated JSON Element before serialization and after deserialization.
 * @author elopez
 */
public abstract class OpenpayTypeAdapterFactory<C> implements TypeAdapterFactory {

    private final Class<C> clazz;

    public OpenpayTypeAdapterFactory(final Class<C> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        if (this.clazz.equals(type.getRawType())) {
            return (TypeAdapter<T>) this.getClassAdapter(gson, (TypeToken<C>) type);
        }
        return null;
    }

    public TypeAdapter<C> getClassAdapter(final Gson gson, final TypeToken<C> type) {
        final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<C>() {

            @Override
            public void write(final JsonWriter out, final C value) throws IOException {
                JsonElement tree = delegate.toJsonTree(value);
                OpenpayTypeAdapterFactory.this.beforeWrite(value, tree);
                elementAdapter.write(out, tree);
            }

            @Override
            public C read(final JsonReader in) throws IOException {
                JsonElement tree = elementAdapter.read(in);
                OpenpayTypeAdapterFactory.this.afterRead(tree);
                return delegate.fromJsonTree(tree);
            }
        };
    }

    protected void afterRead(final JsonElement tree) {
    }

    protected void beforeWrite(final C value, final JsonElement tree) {
    }

}
