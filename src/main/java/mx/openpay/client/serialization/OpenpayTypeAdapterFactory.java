/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.serialization;

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
