package mx.openpay.client;

import java.util.Date;
import java.util.List;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

public interface OpenPayServices {

    public Transaction collectFee(String ewalletID, Double amount, String desc, String orderID)
            throws ServiceUnavailable, HttpError;

    public List<Ewallet> getEwallets(Integer offset, Integer limit) throws ServiceUnavailable, HttpError;

    public List<Transaction> getCollectedFees(Date fromDate, final Date toDate) throws ServiceUnavailable, HttpError;

    // EWALLET
    public Ewallet createEwallet(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws ServiceUnavailable, HttpError;

    public Ewallet updateEwallet(final Ewallet ewallet) throws ServiceUnavailable, HttpError;

    public Ewallet getEwallet(final String ewalletId) throws ServiceUnavailable, HttpError;

    public Ewallet activateEwallet(final String ewalletId) throws ServiceUnavailable, HttpError;

    public Ewallet inactivateEwallet(final String ewalletId) throws ServiceUnavailable, HttpError;

    public Double getBalance(final String ewalletId) throws ServiceUnavailable, HttpError;

    public Transaction sendFunds(String ewalletId, String destinationId, Double amount, String description,
            String orderID) throws ServiceUnavailable, HttpError;

    public Transaction collectFunds(String ewalletId, String sourceId, Double amount, String description, String orderID)
            throws ServiceUnavailable, HttpError;

    // CARD
    public Card createCard(final String ewalletId, final String number, String holderName, final String cvv2,
            final String expMonth, final String expYear, final Address address) throws ServiceUnavailable, HttpError;

    public Card createDepositCard(String ewalletId, String cardNumber, String holderName, String bankCode)
            throws ServiceUnavailable, HttpError;

    public Card getCard(final String ewalletId, final String cardId) throws ServiceUnavailable, HttpError;

    public Card activateCard(final String ewalletId, final String cardId) throws ServiceUnavailable, HttpError;

    public Card inactivateCard(final String ewalletId, final String cardId) throws ServiceUnavailable, HttpError;

    public List<Card> getCards(final String ewalletId, int offset, final int limit) throws ServiceUnavailable,
            HttpError;

    // BANK
    public BankAccount createBank(final String ewalletId, final String clabe) throws ServiceUnavailable, HttpError;

    public BankAccount getBank(final String ewalletId, final String bankId) throws ServiceUnavailable, HttpError;

    public BankAccount activateBank(final String ewalletId, final String bankId) throws ServiceUnavailable, HttpError;

    public BankAccount inactivateBank(final String ewalletId, final String bankId) throws ServiceUnavailable, HttpError;

    public List<BankAccount> getBankAccounts(String ewalletId, int offset, int limit) throws ServiceUnavailable,
            HttpError;
}
