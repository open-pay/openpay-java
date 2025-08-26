package mx.openpay.core.client.full;

import mx.openpay.client.Bin;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.BinesOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BinesUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private BinesOperations binService;

    /**
    // Asegúrate de que este import sea: import org.junit.Before;
    @BeforeEach
    public void setUp() {
        // Mensaje para confirmar que este método se está ejecutando
        System.out.println("--- Ejecutando el método setUp() ---");

        // 1. Inicializamos los mocks
        MockitoAnnotations.openMocks(this);

        // 2. Hacemos una verificación INMEDIATA para ver si el mock se creó.
        if (this.jsonServiceClientMock == null) {
            System.err.println("¡ERROR CRÍTICO! jsonServiceClientMock es nulo DESPUÉS de openMocks(this).");
            // Esto podría indicar un problema con tus dependencias de Mockito.
            throw new IllegalStateException("El mock no fue inicializado por MockitoAnnotations.");
        }
        System.out.println("Diagnóstico en setUp(): El mock se inicializó correctamente.");

        // 3. Creamos el servicio
        this.binService = new BinesOperations(this.jsonServiceClientMock);
    } */

    @Test
    void testGetBinInfo() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        String binNumber = "415231";
        Bin binResponse = new Bin();
        binResponse.setBank("BANAMEX");
        binResponse.setBrand("VISA");

        // Preparamos el mock para ambas llamadas.
        when(this.jsonServiceClientMock.getMerchantId()).thenReturn("fake_merchant_id");
        when(this.jsonServiceClientMock.get(anyString(), eq(Bin.class))).thenReturn(binResponse);

        // --- ACT ---
        Bin result = this.binService.get(binNumber);

        // --- ASSERT ---
        assertThat(result).isNotNull();
        assertThat(result.getBank()).isEqualTo("BANAMEX");
        assertThat(result.getBrand()).isEqualTo("VISA");
    }
}
