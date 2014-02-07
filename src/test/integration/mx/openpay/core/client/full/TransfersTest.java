/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Transfer;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateTransferParams;
import mx.openpay.client.exceptions.OpenpayServiceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class TransfersTest extends BaseTest {

    private Customer customerFrom;

    private Customer customerTo;

    @Before
    public void setUp() throws Exception {
        this.customerFrom = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.api.charges().create(this.customerFrom.getId(), new CreateCardChargeParams()
                .amount(new BigDecimal("100"))
                .description("Some funds")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .expirationMonth(12)
                        .expirationYear(20)
                        .cvv2("123")
                        .holderName("Someone")));
        this.customerTo = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
    }

    @After
    public void tearDown() throws Exception {
        this.api.customers().delete(this.customerFrom.getId());
        this.api.customers().delete(this.customerTo.getId());
    }

    @Test
    public void testTransfer() throws Exception {
        Transfer transfer = this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                .amount(BigDecimal.ONE)
                .description("Some description")
                .toCustomerId(this.customerTo.getId()));
        assertThat(transfer.getStatus(), is("completed"));

    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateTransfer_Old() throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        Transfer transfer = this.api.transfers().create(this.customerFrom.getId(), this.customerTo.getId(),
                new BigDecimal("10.0"),
                "Una descripcion", orderId);
        assertNotNull(transfer.getId());
        assertNotNull(transfer.getCreationDate());
        assertNull(transfer.getCard());
        assertNull(transfer.getBankAccount());
        assertEquals(orderId, transfer.getOrderId());
    }

    @Test
    public void testCreate() throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        Transfer transfer = this.api.transfers().create(this.customerFrom.getId(),
                new CreateTransferParams()
                        .toCustomerId(this.customerTo.getId())
                        .amount(BigDecimal.TEN)
                        .description("Una descripcion")
                        .orderId(orderId));
        assertNotNull(transfer.getId());
        assertNotNull(transfer.getCreationDate());
        assertNull(transfer.getCard());
        assertNull(transfer.getBankAccount());
        assertEquals(orderId, transfer.getOrderId());
    }

    @Test
    public void testCreate_NoDestination() throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        try {
            this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                    .amount(new BigDecimal("10.0"))
                    .description("Una descripcion")
                    .orderId(orderId));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(400, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreate_InvalidDestination() throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        try {
            this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                    .toCustomerId("")
                    .amount(new BigDecimal("10.0"))
                    .description("Una descripcion")
                    .orderId(orderId));
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testGet_Customer() throws Exception {
        String id = this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                .amount(BigDecimal.ONE)
                .description("Some description")
                .orderId("20131203174909")
                .toCustomerId(this.customerTo.getId())).getId();
        Transfer transfer = this.api.transfers().get(this.customerFrom.getId(), id);
        assertEquals(id, transfer.getId());
        assertEquals("20131203174909", transfer.getOrderId());
    }

    @Test
    public void testGet_Customer_NotFound() throws Exception {
        try {
            this.api.transfers().get(this.customerFrom.getId(), "estonoexiste");
            fail();
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testListEmpty_Customer() throws Exception {
        List<Transfer> transfers = this.api.transfers().list(this.customerFrom.getId(), null);
        assertTrue(transfers.isEmpty());
    }

    @Test
    public void testList_Customer() throws Exception {
        this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                .amount(BigDecimal.ONE)
                .description("Some description 1")
                .toCustomerId(this.customerTo.getId())).getId();
        this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                .amount(BigDecimal.ONE)
                .description("Some description 2")
                .toCustomerId(this.customerTo.getId())).getId();
        this.api.transfers().create(this.customerFrom.getId(), new CreateTransferParams()
                .amount(BigDecimal.ONE)
                .description("Some description 3")
                .toCustomerId(this.customerTo.getId())).getId();
        List<Transfer> transfers = this.api.transfers().list(this.customerFrom.getId(), null);
        assertEquals(3, transfers.size());
    }

}
