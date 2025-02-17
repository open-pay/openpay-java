package mx.openpay.client.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import junit.framework.TestCase;
import mx.openpay.client.exceptions.OpenpayServiceException;
import org.junit.Test;

public class OpenpayServiceExceptionAdapterTest extends TestCase {

    public void testDeserializeValidJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("http_code", 400);
        jsonObject.addProperty("error_code", 1001);
        jsonObject.addProperty("category", "request");
        jsonObject.addProperty("description", "Invalid request");
        jsonObject.addProperty("request_id", "req_123");

        OpenpayServiceExceptionAdapter adapter = new OpenpayServiceExceptionAdapter();
        OpenpayServiceException exception = adapter.deserialize(jsonObject, OpenpayServiceException.class, null);

        assertNotNull(exception);
        assertEquals(400, (int) exception.getHttpCode());
        assertEquals(1001, (int) exception.getErrorCode());
        assertEquals("request", exception.getCategory());
        assertEquals("Invalid request", exception.getMessage());
        assertEquals("req_123", exception.getRequestId());
    }

    public void testDeserializeMissingOptionalFields() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("http_code", 400);
        jsonObject.addProperty("description", "Invalid request");

        OpenpayServiceExceptionAdapter adapter = new OpenpayServiceExceptionAdapter();
        OpenpayServiceException exception = adapter.deserialize(jsonObject, OpenpayServiceException.class, null);

        assertNotNull(exception);
        assertEquals(400, (int) exception.getHttpCode());
        assertEquals(0,(int)exception.getErrorCode());
        assertNull(exception.getCategory());
        assertEquals("Invalid request", exception.getMessage());
        assertNull(exception.getRequestId());
    }
    

    @Test
    public void testSerialize() {
        OpenpayServiceExceptionAdapter adapter = new OpenpayServiceExceptionAdapter();

        // Crear una instancia de excepción con un mensaje de prueba
        OpenpayServiceException exception = new OpenpayServiceException("Error de prueba");

        // Serializar la instancia de excepción manualmente
        JsonElement result = adapter.serialize(exception, null, null);

        // Crear un objeto JSON esperado
        JsonObject expected = new JsonObject();
        expected.addProperty("message", "Error de prueba");

        // Validar que el resultado no sea nulo
        assertNotNull(result);

        // Validar que el resultado sea igual al esperado
        assertEquals(expected, result);
    }
}