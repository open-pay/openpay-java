/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.Payout;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.Transfer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.CreateBankChargeParams;
import mx.openpay.client.core.requests.transactions.CreateBankPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.CreateCardPayoutParams;
import mx.openpay.client.core.requests.transactions.CreateFeeParams;
import mx.openpay.client.core.requests.transactions.CreateStoreChargeParams;
import mx.openpay.client.core.requests.transactions.CreateTransferParams;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test creating all kinds of objects using an empty merchant account.
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
@Ignore
public class FullApiTest {

    private OpenpayAPI api;

    Customer customer;

    Customer recipientCustomer;

    BankAccount merchantBankAccount;

    BankAccount customerBankAccount;

    private Card customerCreditCard;

    private Card merchantCreditCard;

    private Card customerDebitCard;

    private Card merchantDebitCard;

    private Plan planWithTrial;

    private Plan planWithoutTrial;

    @Before
    public void setUp() throws Exception {
        String merchantId = "mi93pk0cjumoraf08tqt";
        String apiKey = "sk_88ab47ebc710472d91488cc4f3009080";
        String endpoint = "https://sandbox-api.openpay.mx/";
        this.api = new OpenpayAPI(endpoint, apiKey, merchantId);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testFullApi() throws Exception {
        this.testCustomers();
        this.testBankAccounts();
        this.testCards();
        this.testCardCharges();
        this.testBankCharges();
        this.testStoreCharges();
        this.testBankPayouts();
        this.testCardPayouts();
        this.testFees();
        this.testTransfers();
        this.testPlans();
        this.testSubscriptions();
    }

    private void testCustomers() throws OpenpayServiceException, ServiceUnavailableException {
        this.customer = this.api.customers().create(new Customer()
                .name("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phoneNumber("525541703567")
                .address(getAddress()));
        log.info("Customer: {}", this.customer.getId());

        this.recipientCustomer = this.api.customers().create(new Customer()
                .name("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phoneNumber("554-170-3567"));
        log.info("Recipient Customer: {}", this.recipientCustomer.getId());
    }

    private void testBankAccounts() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateMerchantBankAccount();
        this.testCreateCustomerBankAccount();
    }

    private void testCreateMerchantBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantBankAccount = this.api.bankAccounts().create(new BankAccount()
                .clabe("032180000118359719") // CLABE
                .holderName("Juan Pérez"));
        log.info("Merchant bank account: {}", this.merchantBankAccount.getId());

    }

    private void testCreateCustomerBankAccount() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerBankAccount = this.api.bankAccounts().create(this.customer.getId(),
                new BankAccount()
                        .clabe("032180000118359719") // CLABE
                        .holderName("Juan Pérez"));
        log.info("Customer bank account: {}", this.customerBankAccount.getId());
    }

    private void testCards() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreateMerchantCard();
        this.testCreateCustomerCard();
        this.testCreateMerchantDebitCard();
        this.testCreateCustomerDebitCard();
    }

    private void testCreateMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantCreditCard = this.api.cards().create(this.createCard("345678000000007"));
        log.info("Merchant credit card: {}", this.merchantCreditCard.getId());

    }

    private void testCreateCustomerCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerCreditCard = this.api.cards().create(this.customer.getId(), this.createCard("5555555555554444"));
        log.info("Customer credit card: {}", this.customerCreditCard.getId());
    }

    private void testCreateMerchantDebitCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.merchantDebitCard = this.api.cards().create(this.createCard("4111111111111111"));
        log.info("Merchant debit card: {}", this.merchantDebitCard.getId());

    }

    private void testCreateCustomerDebitCard() throws OpenpayServiceException, ServiceUnavailableException {
        this.customerDebitCard = this.api.cards().create(this.customer.getId(), this.createCard("4111111111111111"));
        log.info("Customer debit card: {}", this.customerDebitCard.getId());
    }

    private void testCardCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantCard();
        this.testChargeCustomerCard();
        this.testChargeMerchantDirectCard();
        this.testChargeCustomerDirectCard();
    }

    private void testChargeMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .description("Service charge")
                .amount(new BigDecimal("200.00")) // Amount is in MXN
                .orderId(String.valueOf(System.currentTimeMillis()))
                .cardId(this.merchantCreditCard.getId()));
        log.info("Merchant card charge: {}", charge.getId());
    }

    private void testChargeCustomerCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .description("Service charge")
                .amount(new BigDecimal("200.00")) // Amount is in MXN
                .cardId(this.customerCreditCard.getId()));
        log.info("Customer card charge: {}", charge.getId());
    }

    private void testChargeMerchantDirectCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(new CreateCardChargeParams()
                .description("Service charge")
                .amount(new BigDecimal("200.00")) // Amount is in MXN
                .card(this.createCard("345678000000007")));
        log.info("Merchant direct card charge: {}", charge.getId());

    }

    private void testChargeCustomerDirectCard() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateCardChargeParams()
                .description("Service charge")
                .amount(new BigDecimal("200.00")) // Amount is in MXN
                .card(this.createCard("5555555555554444")));
        log.info("Customer direct card charge: {}", charge.getId());
    }

    private void testBankCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantBank();
        this.testChargeCustomerBank();
    }

    private void testChargeMerchantBank() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(new CreateBankChargeParams()
                .amount(new BigDecimal("100"))
                .description("Cargo por banco"));
        log.info("Merchant bank Charge: {}", charge.getId());
    }

    private void testChargeCustomerBank() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateBankChargeParams()
                .amount(new BigDecimal("100"))
                .description("Cargo por banco"));
        log.info("Customer bank Charge: {}", charge.getId());
    }

    private void testStoreCharges() throws OpenpayServiceException, ServiceUnavailableException {
        this.testChargeMerchantStore();
        this.testChargeCustomerStore();
    }

    private void testChargeMerchantStore() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(new CreateStoreChargeParams()
                .amount(new BigDecimal("100"))
                .description("Cargo por tienda"));
        log.info("Merchant store Charge: {}", charge.getId());
    }

    private void testChargeCustomerStore() throws OpenpayServiceException, ServiceUnavailableException {
        Charge charge = this.api.charges().create(this.customer.getId(), new CreateStoreChargeParams()
                .amount(new BigDecimal("100"))
                .description("Cargo por tienda"));
        log.info("Customer store Charge: {}", charge.getId());
    }

    private void testBankPayouts() throws OpenpayServiceException, ServiceUnavailableException {
        this.testBankPayoutMerchant();
        this.testBankPayoutCustomer();
        this.testBankPayoutMerchantDirect();
        this.testBankPayoutCustomerDirect();
    }

    private void testBankPayoutMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .bankAccountId(this.merchantBankAccount.getId()));
        log.info("Merchant bank payout: {}", payout.getId());
    }

    private void testBankPayoutCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .bankAccountId(this.customerBankAccount.getId()));
        log.info("Customer bank payout: {}", payout.getId());

    }

    private void testBankPayoutMerchantDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Merchant payout")
                .bankAccount(new BankAccount()
                        .clabe("032180000118359719") // CLABE
                        .holderName("Juan Pérez")));
        log.info("Merchant direct bank payout: {}", payout.getId());
    }

    private void testBankPayoutCustomerDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateBankPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .bankAccount(new BankAccount()
                        .clabe("032180000118359719") // CLABE
                        .holderName("Juan Pérez")));
        log.info("Customer direct bank payout: {}", payout.getId());
    }

    private void testCardPayouts() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCardPayoutMerchant();
        this.testCardPayoutCustomer();
        this.testCardPayoutMerchantDirect();
        this.testCardPayoutCustomerDirect();
    }

    private void testCardPayoutMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(new CreateCardPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Merchant payout")
                .cardId(this.merchantDebitCard.getId()));
        log.info("Merchant card payout: {}", payout.getId());
    }

    private void testCardPayoutCustomer() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateCardPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .cardId(this.customerDebitCard.getId()));
        log.info("Customer card payout: {}", payout.getId());
    }

    private void testCardPayoutMerchantDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(new CreateCardPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Merchant payout")
                .card(this.createCard("4111111111111111").cvv2(null)));
        log.info("Merchant direct card payout: {}", payout.getId());
    }

    private void testCardPayoutCustomerDirect() throws OpenpayServiceException, ServiceUnavailableException {
        Payout payout = this.api.payouts().create(this.customer.getId(), new CreateCardPayoutParams()
                .amount(new BigDecimal("20"))
                .description("Customer payout")
                .card(this.createCard("4111111111111111").cvv2(null)));
        log.info("Customer direct card payout: {}", payout.getId());
    }

    private void testFees() throws OpenpayServiceException, ServiceUnavailableException {
        Fee fee = this.api.fees().create(new CreateFeeParams()
                .amount(new BigDecimal("5"))
                .customerId(this.customer.getId())
                .description("Use fee"));
        log.info("Fee: {}", fee.getId());

    }

    private void testTransfers() throws OpenpayServiceException, ServiceUnavailableException {
        Transfer transfer = this.api.transfers().create(this.customer.getId(), new CreateTransferParams()
                .toCustomerId(this.recipientCustomer.getId())
                .amount(new BigDecimal("100"))
                .description("Transfer to another customer"));
        log.info("Transfer: {}", transfer.getId());
    }

    private void testPlans() throws OpenpayServiceException, ServiceUnavailableException {
        this.testCreatePlanNoTrial();
        this.testCreatePlanTrial();
    }

    private void testCreatePlanNoTrial() throws OpenpayServiceException, ServiceUnavailableException {
        this.planWithoutTrial = this.api.plans().create(new Plan()
                .name("Plan without trial")
                .amount(new BigDecimal("100"))
                .repeatEvery(1, PlanRepeatUnit.WEEK)
                .statusAfterRetry(PlanStatusAfterRetry.CANCELLED));
        log.info("Plan without trial: {}", this.planWithoutTrial.getId());
    }

    private void testCreatePlanTrial() throws OpenpayServiceException, ServiceUnavailableException {
        this.planWithTrial = this.api.plans().create(new Plan()
                .name("Plan with trial")
                .amount(new BigDecimal("100"))
                .trialDays(30)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID));
        log.info("Plan with trial: {}", this.planWithTrial.getId());
    }

    private void testSubscriptions() throws OpenpayServiceException, ServiceUnavailableException {
        this.testSubscribeCustomerCardExists();
        this.testSubscribeCustomerNewCard();
        this.testSubscribeCustomerAddTrialDate();
        this.testSubscribeCustomerRemoveTrial();
    }

    private void testSubscribeCustomerCardExists() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithoutTrial.getId())
                .cardId(this.customerCreditCard.getId()));
        log.info("Subscription existing card: {}", subscription.getId());
    }

    private void testSubscribeCustomerNewCard() throws OpenpayServiceException, ServiceUnavailableException {
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(),
                new Subscription()
                        .planId(this.planWithTrial.getId())
                        .card(this.createCard("5555555555554444")));
        log.info("Subscription new card: {}", subscription.getId());
    }

    private void testSubscribeCustomerAddTrialDate() throws OpenpayServiceException, ServiceUnavailableException {
        Date oneDayAfter = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .cardId(this.customerCreditCard.getId())
                .planId(this.planWithoutTrial.getId())
                .trialEndDate(oneDayAfter));
        log.info("Subscription adding trial: {}", subscription.getId());
        assertThat(subscription.getStatus(), is("trial"));
    }

    private void testSubscribeCustomerRemoveTrial() throws OpenpayServiceException, ServiceUnavailableException {
        Date oneDayBefore = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .cardId(this.customerCreditCard.getId())
                .planId(this.planWithTrial.getId())
                .trialEndDate(oneDayBefore));
        log.info("Subscription removing trial: {}", subscription.getId());
        assertThat(subscription.getStatus(), is("active"));
    }

    private Card createCard(final String number) {
        return new Card()
                .cardNumber(number) // No dashes or spaces
                .holderName("Juan Pérez Nuñez")
                .cvv2("422")
                .expirationMonth(9)
                .expirationYear(14);
    }

    private static final Address getAddress() {
        return new Address()
                .line1("Calle Morelos #12 - 11")
                .city("Distrito Federal")
                .postalCode("12345")
                .state("Queretaro")
                .countryCode("MX");
    }

}
