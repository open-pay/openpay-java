/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static mx.openpay.client.utils.SearchParams.search;
import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Withdrawal;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
public class WithdrawalOperationsTest {

    String customerId = "afk4csrazjp1udezj1po";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Before
    public void setUp() throws Exception {
        OpenpayAPI.configure(ENDPOINT, API_KEY, MERCHANT_ID);
    }

    @Test
    public void testGetList_Customer() throws ServiceUnavailable, OpenpayServiceException {
        List<Withdrawal> transactions = Withdrawal.getList(search().limit(2));
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGetList_Merchant() throws ServiceUnavailable, OpenpayServiceException {
        List<Withdrawal> transactions = Withdrawal.getList(this.customerId, search().limit(2));
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGet_Merchant() throws ServiceUnavailable, OpenpayServiceException {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Withdrawal transaction = Withdrawal.get(transactionId);
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testGet_Customer() throws ServiceUnavailable, OpenpayServiceException {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Withdrawal transaction = Withdrawal.get(this.customerId, transactionId);
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testCreate_Customer_BankAccountId() throws ServiceUnavailable, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        List<BankAccount> bankAccounts = BankAccount.getList(customerId, search().offset(0).limit(10));
        Assert.assertNotNull(bankAccounts);

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForCustomer(customerId, bankAccounts.get(0).getId(),
                amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Customer_BankAccount() throws ServiceUnavailable, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClabe("012298026516924616");
        bankAccount.setAccountOwner("Cuenta");

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForCustomer(customerId, bankAccount, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Customer_Card() throws ServiceUnavailable, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        Card card = this.getCard();
        card.setBankCode("012");

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForCustomer(customerId, card, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Merchant_BankAccount() throws ServiceUnavailable, OpenpayServiceException {

        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClabe("012298026516924616");
        bankAccount.setAccountOwner("Cuenta");

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForMerchant(bankAccount, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(null, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Merchant_Card() throws ServiceUnavailable, OpenpayServiceException {

        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        Card card = this.getCard();
        card.setBankCode("012");

        String orderId = this.dateFormat.format(new Date());
        Withdrawal transaction = Withdrawal.createForMerchant(card, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(null, transaction.getCustomerId());
    }

    private Card getCard() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setExteriorNumber("11");
        address.setInteriorNumber("01");
        address.setPostalCode("76090");
        address.setRegion("Corregidora");
        address.setStreet("Camino");
        address.setState("Queretaro");

        Card card = new Card();
        card.setCardNumber("5243385358972033");
        card.setHolderName("Holder");
        card.setExpirationMonth("12");
        card.setExpirationYear("15");
        card.setCvv2("123");
        card.setAddress(address);
        return card;
    }

}
