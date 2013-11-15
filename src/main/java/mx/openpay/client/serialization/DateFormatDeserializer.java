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
public class DateFormatSerializer implements JsonDeserializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

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
