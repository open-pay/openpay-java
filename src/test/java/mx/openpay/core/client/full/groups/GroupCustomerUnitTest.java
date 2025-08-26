package mx.openpay.core.client.full.groups;

import mx.openpay.client.Customer;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.groups.GroupCustomerOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupCustomerUnitTest {

    private List<Customer> customersToDelete;

    // 1. Mockeamos JsonServiceClient. Esta es la dependencia directa de GroupCustomerOperations
    // y es la responsable de hacer las llamadas HTTP.
    @Mock
    private JsonServiceClient jsonServiceClientMock;

    // 2. Creamos una instancia REAL de la clase que queremos probar (la clase de operaciones)
    // y le inyectamos el mock de arriba.
    @InjectMocks
    private GroupCustomerOperations groupCustomerOperations;

    // 1. Declaramos los "capturadores" para cada argumento del método 'get'.
    @Captor
    private ArgumentCaptor<String> pathCaptor;
    @Captor
    private ArgumentCaptor<Map<String, Object>> mapCaptor;
    @Captor
    private ArgumentCaptor<Type> typeCaptor;


    @Test
    void testCreateCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE (Preparar) ---
        // 1. El objeto que enviamos en la petición.
        Customer customerToCreate = new Customer()
                .name("Juan")
                .email("juan.perez@gmail.com");

        // 2. El objeto que SIMULAMOS que la API nos devuelve.
        // Este es el objeto que nuestro mock va a retornar. Debe ser un objeto Customer completo.
        Customer expectedCustomerResponse = new Customer();
        expectedCustomerResponse.setId("cus_test_123456");
        expectedCustomerResponse.setName("Juan");
        expectedCustomerResponse.setEmail("juan.perez@gmail.com");
        expectedCustomerResponse.setCreationDate(new Date()); // Asignamos los campos que devolvería la API

        // 3. Configuramos el mock para que coincida con la firma del método post.
        // El método que probablemente se llama internamente es: post(path, params, clazz)
        // Usamos los "matchers" de Mockito para mayor flexibilidad.
        when(jsonServiceClientMock.post(
                anyString(),              // Coincide con cualquier path (ej. "customers")
                any(Customer.class),      // Coincide con cualquier objeto de tipo Customer
                eq(Customer.class)        // Coincide con el argumento Class<Customer>
        )).thenReturn(expectedCustomerResponse); // Devuelve el OBJETO Java, no un String.

        // --- ACT (Actuar) ---
        Customer createdCustomer = groupCustomerOperations.create(customerToCreate);

        // --- ASSERT (Verificar) ---
        // Verificamos que el objeto devuelto por nuestro servicio es el mismo que configuramos en el mock.
        assertNotNull(createdCustomer);
        assertEquals("cus_test_123456", createdCustomer.getId());
        assertEquals(expectedCustomerResponse.getName(), createdCustomer.getName());
    }

    /**
     * Conversión de: testUpdateCustomer
     * Lógica: Simulamos la respuesta del método 'put' y verificamos que el servicio
     * devuelve el objeto con los datos actualizados.
     */
    @Test
    void testUpdateCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Creamos el objeto a actualizar directamente en memoria.
        // Simulamos que este cliente ya existe dándole un ID.
        Customer customerToUpdate = new Customer();
        customerToUpdate.setId("cus_test_12345");
        customerToUpdate.setName("Juanito 2"); // Asignamos el nombre ya actualizado

        // 2. No necesitamos llamar a create() ni usar la lista customersToDelete.

        // 3. Configuramos el mock para que, cuando reciba una llamada PUT,
        // devuelva el mismo objeto que le pasamos.
        when(jsonServiceClientMock.put(
                anyString(),
                any(Customer.class),
                eq(Customer.class)))
                .thenReturn(customerToUpdate);

        // --- ACT ---
        // Llamamos solo al método que queremos probar: update.
        Customer result = groupCustomerOperations.update(customerToUpdate);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals("cus_test_12345", result.getId());
        assertEquals("Juanito 2", result.getName());
    }

    /**
     * Conversión de: testDeleteCustomer
     * Lógica: Verificamos que al llamar a 'delete' en nuestro servicio,
     * se invoca el método 'delete' en el cliente HTTP. No necesitamos verificar
     * el 'get' posterior, ya que eso se prueba en 'testGet_DoesNotExist'.
     */
    @Test
    void testDeleteCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        String customerId = "cus_test_123";
        // Para métodos 'void', la configuración del mock es así:
        doNothing().when(jsonServiceClientMock).delete(anyString());

        // --- ACT ---
        groupCustomerOperations.delete(customerId);

        // --- ASSERT ---
        // Verificamos que el método delete del mock fue llamado 1 vez con el ID correcto.
        verify(jsonServiceClientMock, times(1)).delete(contains(customerId));
    }

    /**
     * Conversión de: testDelete_DoesNotExist y testGet_DoesNotExist
     * Lógica: Son esencialmente el mismo caso de prueba. Simulamos que el cliente HTTP
     * lanza una excepción (como un 404) y verificamos que nuestro servicio
     * la propaga correctamente.
     */
    @Test
    void testOperationOnNonExistentCustomer_ShouldThrowException() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        String nonExistentId = "blahblahblah";

        // 1. Creamos la excepción con un constructor válido.
        OpenpayServiceException exceptionToThrow = new OpenpayServiceException(
                "The resource does not exist");

        // 2. Usamos los setters para añadir los detalles que necesitamos probar.
        exceptionToThrow.setErrorCode(1005);
        exceptionToThrow.setHttpCode(404);

        // El resto del test permanece igual...

        // Simulamos la excepción para el método GET.
        when(jsonServiceClientMock.get(anyString(), any(Class.class)))
                .thenThrow(exceptionToThrow);

        // Simulamos la excepción para el método DELETE.
        doThrow(exceptionToThrow).when(jsonServiceClientMock).delete(anyString());

        // --- ACT & ASSERT ---
        OpenpayServiceException getException = assertThrows(OpenpayServiceException.class, () -> {
            groupCustomerOperations.get(nonExistentId);
        });

        // Las aserciones ahora funcionarán porque el objeto tiene los datos correctos.
        assertEquals(404, getException.getHttpCode());
        assertEquals("1005", getException.getErrorCode().toString());

        OpenpayServiceException deleteException = assertThrows(OpenpayServiceException.class, () -> {
            groupCustomerOperations.delete(nonExistentId);
        });

        assertEquals(404, deleteException.getHttpCode());
        assertEquals("1005", deleteException.getErrorCode().toString());
    }

    /**
     * Conversión de: testList
     * Lógica: Simulamos una respuesta del 'list' con una lista de clientes
     * y verificamos que el servicio la devuelve correctamente. No necesitamos
     * probar la paginación de la API aquí, solo que nuestro código maneja
     * una lista de respuesta.
     */
    @Test
    void testList() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Preparamos la respuesta que queremos que el mock devuelva.
        Customer customer1 = new Customer();
        customer1.setId("cus_aaa");
        Customer customer2 = new Customer();
        customer2.setId("cus_bbb");
        List<Customer> customerListResponse = Arrays.asList(customer1, customer2);

        // 2. ¡LA LÍNEA CORRECTA! Mockeamos el método 'list' con los argumentos correctos.
        when(jsonServiceClientMock.list(
                anyString(),            // Coincide con el path "/groups/null/customers"
                anyMap(),               // Coincide con el mapa {"limit" = "2"}
                any(Class.class)        // Coincide con class mx.openpay.client.Customer
        )).thenReturn(customerListResponse); // Devuelve la lista que preparamos.

        // --- ACT ---
        List<Customer> result = groupCustomerOperations.list(SearchParams.search().limit(2));

        // --- ASSERT ---
        // Esta aserción finalmente debe pasar.
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("cus_aaa", result.get(0).getId());
    }

    /**
     * Conversión de: testList_OnlyWithoutAccount
     * Lógica: Similar a 'testList', creamos una lista simulada que cumple
     * con las condiciones del test (algunos con balance nulo, etc.)
     * y corremos las mismas aserciones.
     */
    @Test
    void testList_CheckCustomerProperties() throws ServiceUnavailableException, OpenpayServiceException {
        // --- ARRANGE ---
        Customer c1 = new Customer();
        c1.setBalance(null); // Sin balance
        c1.setStatus(null);

        Customer c2 = new Customer();
        c2.setBalance(new BigDecimal("0.00")); // Con balance 0
        c2.setStatus("active");

        List<Customer> mockedResponseList = Arrays.asList(c1, c2);
        when(jsonServiceClientMock.list(
                anyString(),            // Coincide con el path "/groups/null/customers"
                anyMap(),               // Coincide con el mapa {"limit" = "2"}
                any(Class.class)        // Coincide con class mx.openpay.client.Customer
        )).thenReturn(mockedResponseList); // Devuelve la lista que preparamos.

        // --- ACT ---
        List<Customer> customers = groupCustomerOperations.list(SearchParams.search().limit(2));

        // --- ASSERT ---
        // Las mismas aserciones del test original, pero sobre datos simulados.
        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertNull(customers.get(0).getBalance());
        assertEquals(customers.get(1).getBalance(), new BigDecimal("0.00"));
        assertNotNull(customers.get(1).getStatus());
    }
}
