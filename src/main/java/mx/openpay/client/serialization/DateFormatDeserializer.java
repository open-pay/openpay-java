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

/**
 * Formats the JSON-serialized dates.
 * @author elopez
 */
public class DateFormatDeserializer implements JsonDeserializer<Date> {

    public Date deserialize(final JsonElement json, final Type paramType,
            final JsonDeserializationContext paramJsonDeserializationContext) {
        String data = json.getAsJsonPrimitive().getAsString();
        try {
            return this.parse(data);
        } catch (Exception e) {
            return null;
        }
    }

    private Date parse(final String data) throws java.text.ParseException {
        if (data.length() <= 10) {
            return this.parseDateOnly(data);
        } else {
            return this.parseISO8601(data);
        }
    }

    private Date parseDateOnly(final String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    /**
     * Parse the date using the ISO8601DateParser from Apache. Synchronized since it's not thread-safe.
     */
    private synchronized Date parseISO8601(final String date) throws ParseException {
        return ISO8601DateParser.parse(date);
    }
}
