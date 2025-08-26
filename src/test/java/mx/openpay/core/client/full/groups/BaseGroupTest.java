package mx.openpay.core.client.full.groups;

import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.groups.OpenpayGroupAPI;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BaseGroupTest {

    @Mock // Crea un mock de la API de Grupo
    protected OpenpayGroupAPI groupApi;

    @Mock // Crea un mock para la API del primer merchant
    protected OpenpayAPI firstMerchantApi;

    @Mock // Crea un mock para la API del segundo merchant
    protected OpenpayAPI secondMerchantApi;

    // Nota: El método @BeforeEach (antes @Before) ya no es necesario para
    // inicializar los mocks. @ExtendWith(MockitoExtension.class) se encarga de eso.
    // Podrías usar @BeforeEach para definir comportamientos comunes de los mocks si lo necesitaras.
}
