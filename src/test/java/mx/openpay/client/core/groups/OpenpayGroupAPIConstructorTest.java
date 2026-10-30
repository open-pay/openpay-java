package mx.openpay.client.core.groups;

import java.lang.reflect.Field;

import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.impl.DefaultHttpServiceClient;
import mx.openpay.client.core.operations.ServiceOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class OpenpayGroupAPIConstructorTest {

    @Test
    void shouldWireJsonClientAndOperationsFromStringConstructor() throws Exception {
        String location = "sandbox-api.openpay.mx";
        String apiKey = "sk_test_123";
        String merchantId = "mch_456";

        OpenpayGroupAPI api = new OpenpayGroupAPI(location, apiKey, merchantId);

        JsonServiceClient jsonClient = getField(api, OpenpayGroupAPI.class, "jsonClient", JsonServiceClient.class);
        assertNotNull(jsonClient);
        assertEquals(merchantId, jsonClient.getMerchantId());

        DefaultHttpServiceClient httpClient = (DefaultHttpServiceClient) jsonClient.getHttpClient();
        assertEquals(apiKey, getField(httpClient, DefaultHttpServiceClient.class, "key", String.class));

        assertSame(jsonClient, getServiceClient(api.groupCustomers()));
        assertSame(jsonClient, getServiceClient(api.groupCards()));
        assertSame(jsonClient, getServiceClient(api.groupCharges()));
        assertSame(jsonClient, getServiceClient(api.groupSubscriptions()));
    }

    private static JsonServiceClient getServiceClient(final Object operations) throws Exception {
        return getField(operations, ServiceOperations.class, "client", JsonServiceClient.class);
    }

    private static <T> T getField(final Object target, final Class<?> declaringClass, final String fieldName,
            final Class<T> type) throws Exception {
        Field field = declaringClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(target));
    }
}
