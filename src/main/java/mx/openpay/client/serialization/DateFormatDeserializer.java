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

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

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
