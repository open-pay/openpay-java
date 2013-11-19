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
import mx.openpay.client.Payout;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

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
    public void testList_Customer() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = Payout.list(search().limit(2));
        assertEquals(2, transactions.size());
    }

    @Test
    public void testList_Customer_Empty() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = Payout.list(search().limit(2).offset(10000));
        assertEquals(0, transactions.size());
    }

    @Test
    public void testList_Merchant() throws ServiceUnavailableException, OpenpayServiceException {
        List<Payout> transactions = Payout.list(this.customerId, search().limit(2));
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGet_Merchant() throws ServiceUnavailableException, OpenpayServiceException {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Payout transaction = Payout.get(transactionId);
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testGet_Customer() throws ServiceUnavailableException, OpenpayServiceException {
        String transactionId = "tf1wjucai0gj7awz0uvf";
        Payout transaction = Payout.get(this.customerId, transactionId);
        Assert.assertNotNull(transaction.getId());
        Assert.assertNotNull(transaction.getAmount());
    }

    @Test
    public void testCreate_Customer_BankAccountId() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        List<BankAccount> bankAccounts = BankAccount.list(customerId, search().offset(0).limit(10));
        Assert.assertNotNull(bankAccounts);

        String orderId = this.dateFormat.format(new Date());
        Payout transaction = Payout.createForCustomer(customerId, bankAccounts.get(0).getId(),
                amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Customer_BankAccount() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClabe("012298026516924616");
        bankAccount.setHolderName("Cuenta");

        String orderId = this.dateFormat.format(new Date());
        Payout transaction = Payout.createForCustomer(customerId, bankAccount, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Customer_Card() throws ServiceUnavailableException, OpenpayServiceException {
        String customerId = "afk4csrazjp1udezj1po";
        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        Card card = this.getCard();
        card.setBankCode("012");

        String orderId = this.dateFormat.format(new Date());
        Payout transaction = Payout.createForCustomer(customerId, card, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(customerId, transaction.getCustomerId());
    }

    @Test
    public void testCreate_Merchant_BankAccount() throws Exception {

        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClabe("012298026516924616");
        bankAccount.setHolderName("Cuenta");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2012-10-10");
        bankAccount.setCreationDate(date);

        String orderId = this.dateFormat.format(new Date());
        Payout transaction = Payout.createForMerchant(bankAccount, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(null, transaction.getCustomerId());
        Assert.assertFalse(transaction.getCreationDate().equals(date));
    }

    @Test
    public void testCreate_Merchant_Card() throws ServiceUnavailableException, OpenpayServiceException {

        BigDecimal amount = new BigDecimal("1.00");
        String desc = "Ganancias";

        Card card = this.getCard();
        card.setBankCode("012");

        String orderId = this.dateFormat.format(new Date());
        Payout transaction = Payout.createForMerchant(card, amount, desc, orderId);
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getCreationDate());
        Assert.assertEquals(amount, transaction.getAmount());
        Assert.assertEquals(desc, transaction.getDescription());
        Assert.assertEquals(null, transaction.getCustomerId());
    }

    private Card getCard() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 - 01");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
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
