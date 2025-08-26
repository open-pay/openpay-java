package mx.openpay.core.client.full;

import mx.openpay.client.BankAccount;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.BankAccountOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerBankAccountUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private BankAccountOperations bankAccountService;

    private final String FAKE_CUSTOMER_ID = "cus_test_123";
    private final String FAKE_BANK_ACCOUNT_ID = "ba_test_456";

    @Test
    void testCreateBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        BankAccount bankAccountRequest = new BankAccount().clabe("012298026516924616").holderName("Mi nombre");
        BankAccount bankAccountResponse = new BankAccount();
        bankAccountResponse.setId(FAKE_BANK_ACCOUNT_ID);

        when(jsonServiceClientMock.post(
                anyString(),
                any(BankAccount.class), // <-- ¡El cambio está aquí!
                eq(BankAccount.class)))
                .thenReturn(bankAccountResponse);

        // --- ACT ---
        BankAccount result = bankAccountService.create(FAKE_CUSTOMER_ID, bankAccountRequest);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(FAKE_BANK_ACCOUNT_ID, result.getId());
    }

    @Test
    void testGetBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        BankAccount bankAccountResponse = new BankAccount();
        bankAccountResponse.setId(FAKE_BANK_ACCOUNT_ID);
        bankAccountResponse.setClabe("012XXXXXXXXXX27260"); // Simula la clabe enmascarada

        when(jsonServiceClientMock.get(anyString(), eq(BankAccount.class))).thenReturn(bankAccountResponse);

        // --- ACT ---
        BankAccount result = bankAccountService.get(FAKE_CUSTOMER_ID, FAKE_BANK_ACCOUNT_ID);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals("012XXXXXXXXXX27260", result.getClabe());
    }

    @Test
    void testListBankAccounts() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        BankAccount ba1 = new BankAccount();
        ba1.setId("ba_1");
        BankAccount ba2 = new BankAccount();
        ba2.setId("ba_2");
        List<BankAccount> mockedList = Arrays.asList(ba1, ba2);

        when(jsonServiceClientMock.list(anyString(), anyMap(), eq(BankAccount.class))).thenReturn(mockedList);

        // --- ACT ---
        List<BankAccount> result = bankAccountService.list(FAKE_CUSTOMER_ID, SearchParams.search());

        // --- ASSERT ---
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("ba_1");
    }

    @Test
    void testListBankAccounts_Empty() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        when(jsonServiceClientMock.list(anyString(), any(), eq(BankAccount.class))).thenReturn(Collections.emptyList());

        // --- ACT ---
        List<BankAccount> result = bankAccountService.list(FAKE_CUSTOMER_ID, null);

        // --- ASSERT ---
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        doNothing().when(jsonServiceClientMock).delete(anyString());

        // --- ACT ---
        bankAccountService.delete(FAKE_CUSTOMER_ID, FAKE_BANK_ACCOUNT_ID);

        // --- ASSERT ---
        verify(jsonServiceClientMock, times(1)).delete(contains(FAKE_BANK_ACCOUNT_ID));
    }

    @Test
    void testOperationOnNonExistentResource_ShouldThrowNotFoundException() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        OpenpayServiceException notFoundException = new OpenpayServiceException("Resource not found");
        notFoundException.setHttpCode(404);

        // Simulamos el error 404 para los métodos GET, LIST y DELETE
        when(jsonServiceClientMock.get(anyString(), eq(BankAccount.class))).thenThrow(notFoundException);
        when(jsonServiceClientMock.list(anyString(), any(), eq(BankAccount.class))).thenThrow(notFoundException);
        doThrow(notFoundException).when(jsonServiceClientMock).delete(anyString());

        // --- ACT & ASSERT ---
        assertThrows(OpenpayServiceException.class, () -> {
            bankAccountService.get("customer_id_fake", "ba_fake");
        });

        assertThrows(OpenpayServiceException.class, () -> {
            bankAccountService.list("customer_id_fake", null);
        });

        assertThrows(OpenpayServiceException.class, () -> {
            bankAccountService.delete("customer_id_fake", "ba_fake");
        });
    }

    @Test
    void testCreateBankAccount_WhenClabeAlreadyExists_ShouldThrowConflictException() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        OpenpayServiceException conflictException = new OpenpayServiceException("Bank account already exists");
        conflictException.setHttpCode(409);

        when(jsonServiceClientMock.post(
                anyString(),
                any(BankAccount.class), // <-- ¡El cambio está aquí!
                eq(BankAccount.class)))
                .thenThrow(conflictException);

        // --- ACT & ASSERT ---
        assertThrows(OpenpayServiceException.class, () -> {
            bankAccountService.create(FAKE_CUSTOMER_ID, new BankAccount());
        });
    }

}
