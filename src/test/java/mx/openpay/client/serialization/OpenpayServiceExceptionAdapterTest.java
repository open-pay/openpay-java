package mx.openpay.client.serialization;

import com.google.gson.JsonObject;
import junit.framework.TestCase;
import mx.openpay.client.exceptions.OpenpayServiceException;

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
}