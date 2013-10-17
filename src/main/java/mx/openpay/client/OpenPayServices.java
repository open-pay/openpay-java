package mx.openpay.client;

import java.util.Date;
import java.util.List;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

public interface OpenPayServices {

    public Transaction collectFee(String customerId, Double amount, String desc, String orderID)
            throws ServiceUnavailable, HttpError;

    public List<Customer> getCustomers(Integer offset, Integer limit) throws ServiceUnavailable, HttpError;

    public List<Transaction> getCollectedFees(Date fromDate, final Date toDate) throws ServiceUnavailable, HttpError;

    // CUSTOMER
    public Customer createCustomer(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws ServiceUnavailable, HttpError;

    public Customer updateCustomer(final Customer customer) throws ServiceUnavailable, HttpError;

    public Customer getCustomer(final String customerId) throws ServiceUnavailable, HttpError;

    public Customer activateCustomer(final String customerId) throws ServiceUnavailable, HttpError;

    public Customer inactivateCustomer(final String customerId) throws ServiceUnavailable, HttpError;

    public Double getBalance(final String customerId) throws ServiceUnavailable, HttpError;

    public Transaction sendFunds(String customerId, String destinationId, Double amount, String description,
            String orderID) throws ServiceUnavailable, HttpError;

    public Transaction collectFunds(String customerId, String sourceId, Double amount, String description, String orderID)
            throws ServiceUnavailable, HttpError;

    // CARD
    public Card createCard(final String customerId, final String number, String holderName, final String cvv2,
            final String expMonth, final String expYear, final Address address) throws ServiceUnavailable, HttpError;
    
    public Card updateCard(String customerId, final String carId, final String holderName, String expirationMonth, String expirationYear, Address address, String cvv2) throws ServiceUnavailable, HttpError;

    public Card getCard(final String customerId, final String cardId) throws ServiceUnavailable, HttpError;

    public Card activateCard(final String customerId, final String cardId) throws ServiceUnavailable, HttpError;

    public Card inactivateCard(final String customerId, final String cardId) throws ServiceUnavailable, HttpError;

    public List<Card> getCards(final String customerId, int offset, final int limit) throws ServiceUnavailable,
            HttpError;

    // BANK
    public BankAccount createBank(final String customerId, final String clabe) throws ServiceUnavailable, HttpError;

    public BankAccount getBank(final String customerId, final String bankId) throws ServiceUnavailable, HttpError;

    public BankAccount activateBank(final String customerId, final String bankId) throws ServiceUnavailable, HttpError;

    public BankAccount inactivateBank(final String customerId, final String bankId) throws ServiceUnavailable, HttpError;

    public List<BankAccount> getBankAccounts(String customerId, int offset, int limit) throws ServiceUnavailable,
            HttpError;
    
    //TRANSACTION
    
    public Transaction getTransaction(final String transactionId) throws ServiceUnavailable, HttpError;
}
