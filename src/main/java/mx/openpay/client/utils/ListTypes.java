package mx.openpay.client.utils;

import java.lang.reflect.Type;
import java.util.Collection;

import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.Fee;
import mx.openpay.client.Payout;
import mx.openpay.client.Transfer;

import com.google.gson.reflect.TypeToken;

/**
 * @author elopez
 */
public class ListTypes {

    public static final Type BANK_ACCOUNT = new TypeToken<Collection<BankAccount>>() {
    }.getType();

    public static final Type CARD = new TypeToken<Collection<Card>>() {
    }.getType();

    public static final Type CUSTOMER = new TypeToken<Collection<Customer>>() {
    }.getType();

    public static final Type CHARGE = new TypeToken<Collection<Charge>>() {
    }.getType();

    public static final Type FEE = new TypeToken<Collection<Fee>>() {
    }.getType();

    public static final Type TRANSFER = new TypeToken<Collection<Transfer>>() {
    }.getType();

    public static final Type PAYOUT = new TypeToken<Collection<Payout>>() {
    }.getType();

}
