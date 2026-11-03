package mx.openpay.client.core.impl;

import junit.framework.TestCase;
import mx.openpay.client.*;

import java.util.List;

public class ListTypesTest extends TestCase {

    public void testGetType() {
        assertEquals(new com.google.gson.reflect.TypeToken<List<BankAccount>>() {
        }.getType(), ListTypes.getType(BankAccount.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Card>>() {
        }.getType(), ListTypes.getType(Card.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Customer>>() {
        }.getType(), ListTypes.getType(Customer.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Charge>>() {
        }.getType(), ListTypes.getType(Charge.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Fee>>() {
        }.getType(), ListTypes.getType(Fee.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Transfer>>() {
        }.getType(), ListTypes.getType(Transfer.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Payout>>() {
        }.getType(), ListTypes.getType(Payout.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Plan>>() {
        }.getType(), ListTypes.getType(Plan.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Subscription>>() {
        }.getType(), ListTypes.getType(Subscription.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<GenericTransaction>>() {
        }.getType(), ListTypes.getType(GenericTransaction.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Order>>() {
        }.getType(), ListTypes.getType(Order.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<PaymentPlan>>() {
        }.getType(), ListTypes.getType(PaymentPlan.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<Webhook>>() {
        }.getType(), ListTypes.getType(Webhook.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<OpenCheckoutConfigurationResponseLight>>() {
        }.getType(), ListTypes.getType(OpenCheckoutConfigurationResponseLight.class));
        assertEquals(new com.google.gson.reflect.TypeToken<List<CheckoutResponse>>() {
        }.getType(), ListTypes.getType(CheckoutResponse.class));
    }
}