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
package mx.openpay.client.serialization;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Formats the JSON-serialized dates.
 * @author elopez
 */
public class DateFormatDeserializer implements JsonDeserializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    // Synchronized becase SimpleDateFormat is not thread-safe.
    public synchronized Date deserialize(final JsonElement json, final Type paramType,
            final JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
        try {
            return this.dateFormat.parse(json.getAsJsonPrimitive().getAsString());
        } catch (ParseException e) {
            return null;
        }
    }

}
