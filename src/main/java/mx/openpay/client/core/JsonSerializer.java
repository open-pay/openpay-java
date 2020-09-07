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

import java.util.List;

/**
 * Serializes the parameters for the requests, and deserializes the server response.
 * @author elopez
 */
public interface JsonSerializer {

    /**
     * Serialize the given object into a JSON string. If an object is {@code null} it should be ignored and ommited from
     * the serialized string.
     * @param values The parameters to serialize.
     * @return The serialized string
     */
    public String serialize(final Object values);

    /**
     * Deserializes the JSON string into a list of objects of the given class. The list may be empty but it must never
     * be null. The list is not required to be modifiable.
     * @param json The JSON String
     * @param clazz The expected object class
     * @return The list of objects.
     */
    public <T> List<T> deserializeList(final String json, final Class<T> clazz);

    /**
     * Deserializes the JSON string into an object of the given class. The returned object must never be null.
     * @param json The JSON String
     * @param type Type of the expected object.
     * @return The deserialized object.
     */
    public <T> T deserialize(final String json, final Class<T> type);

}
