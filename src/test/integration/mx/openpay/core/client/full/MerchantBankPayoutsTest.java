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

import static mx.openpay.client.utils.SearchParams.search;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Payout;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantBankPayoutsTest extends BaseTest {

    private BankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        this.api.charges().create(new CreateCardChargeParams()
                .amount(new BigDecimal("5"))
                .description("Cargo")
                .card(new Card()
                        .cardNumber("5555555555554444")
                        .holderName("Juanito Pérez Nuñez")
                        .cvv2("111")
                        .expirationMonth(9)
                        .expirationYear(14)));

        this.bankAccount = this.api.bankAccounts().create(
                new BankAccount().clabe("012298026516924616").holderName("Mi nombre"));
    }

    @After
    public void tearDown() throws Exception {
        if (this.bankAccount != null) {
            this.api.bankAccounts().delete(this.bankAccount.getId());
        }
    }

    @Test
    public void testListCustomerPayouts() throws ServiceUnavailableException, OpenpayServiceException {
        Date date = new Date();
        BigDecimal amount = new BigDecimal(String.format("%tI%<td.%<tM", date));
        CreateCardChargeParams charge = new CreateCardChargeParams()
                .amount(amount).description("Cargo")
                .card(new Card().cardNumber("5555555555554444").holderName("Juanito Pérez Nuñez").cvv2("111")
                        .expirationMonth(9).expirationYear(14));
        this.api.charges().create(charge);
        this.api.charges().create(charge);
        this.api.charges().create(charge);
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 1"));
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 2"));
        this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(amount).description("desc 3"));
        List<Payout> transactions = this.api.payouts().list(search().amount(amount));
        assertEquals(3, transactions.size());
    }

    @Test
    public void testListCustomerPayouts_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = this.api.payouts().list(
                search().creation(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))));
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetCustomerPayout() throws ServiceUnavailableException, OpenpayServiceException {
        Payout payout = this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId()).amount(BigDecimal.ONE).description("desc 1"));
        Payout transaction = this.api.payouts().get(payout.getId());
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testCreateCustomerBankPayout_WithId() throws ServiceUnavailableException, OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";
        String orderId = String.valueOf(System.currentTimeMillis());
        CreateBankPayoutParams createPayout = new CreateBankPayoutParams()
                .bankAccountId(this.bankAccount.getId())
                .amount(amount)
                .description(desc)
                .orderId(orderId);
        Payout transaction = this.api.payouts().create(createPayout);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCreateCustomerBankPayout_WithBankAccount_Old() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().createForMerchant(
                new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta")
                , amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }

    @Test
    public void testCreateCustomerBankPayout_WithBankAccount() throws ServiceUnavailableException,
            OpenpayServiceException {
        BigDecimal amount = BigDecimal.ONE;
        String desc = "Ganancias";

        String orderId = String.valueOf(System.currentTimeMillis());
        Payout transaction = this.api.payouts().create(new CreateBankPayoutParams()
                .bankAccount(new BankAccount()
                        .clabe("012298026516924616")
                        .holderName("Cuenta"))
                .amount(amount)
                .description(desc)
                .orderId(orderId));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertNull(transaction.getCustomerId());
    }
}
