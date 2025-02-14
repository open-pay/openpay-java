package mx.openpay.client.serialization;

import com.google.gson.*;
import mx.openpay.client.exceptions.OpenpayServiceException;

import java.lang.reflect.Type;

public class OpenpayServiceExceptionAdapter implements JsonDeserializer<OpenpayServiceException>, JsonSerializer<OpenpayServiceException> {

    @Override
    public OpenpayServiceException deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Extrae los campos del JSON
        int httpCode = jsonObject.has("http_code") ? jsonObject.get("http_code").getAsInt() : 0;
        int errorCode = jsonObject.has("error_code") ? jsonObject.get("error_code").getAsInt() : 0;
        String category = jsonObject.has("category") ? jsonObject.get("category").getAsString() : null;
        String description = jsonObject.has("description") ? jsonObject.get("description").getAsString() : null;
        String requestId = jsonObject.has("request_id") ? jsonObject.get("request_id").getAsString() : null;

        // Construye una instancia de OpenpayServiceException
        OpenpayServiceException exception = new OpenpayServiceException(description); // Usa 'description' como mensaje
        exception.setHttpCode(httpCode);
        exception.setErrorCode(errorCode);
        exception.setCategory(category);
        exception.setRequestId(requestId);
        return exception;
    }

    @Override
    public JsonElement serialize(OpenpayServiceException src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", src.getMessage());
        return jsonObject;
    }
}