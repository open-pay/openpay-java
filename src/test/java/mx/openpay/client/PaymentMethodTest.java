package mx.openpay.client;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentMethodTest {

    @Test
    void shouldSetAndGetCallbackUrl() {
        PaymentMethod pm = new PaymentMethod();
        pm.setCallbackUrl("https://example.com/callback");
        assertEquals("https://example.com/callback", pm.getCallbackUrl());
    }

    @Test
    void shouldSetAndGetBarcodeBase64() {
        PaymentMethod pm = new PaymentMethod();
        pm.setBarcodeBase64("base64encodedstring==");
        assertEquals("base64encodedstring==", pm.getBarcodeBase64());
    }

    @Test
    void shouldSetAndGetQrType() {
        PaymentMethod pm = new PaymentMethod();
        pm.setQrType("CoDi");
        assertEquals("CoDi", pm.getQrType());
    }

    @Test
    void shouldSetAndGetType() {
        PaymentMethod pm = new PaymentMethod();
        pm.setType("store");
        assertEquals("store", pm.getType());
    }

    @Test
    void shouldSetAndGetBarcodeUrl() {
        PaymentMethod pm = new PaymentMethod();
        pm.setBarcodeUrl("https://example.com/barcode.png");
        assertEquals("https://example.com/barcode.png", pm.getBarcodeUrl());
    }

    @Test
    void shouldSetAndGetReference() {
        PaymentMethod pm = new PaymentMethod();
        pm.setReference("REF-001");
        assertEquals("REF-001", pm.getReference());
    }

    @Test
    void shouldSetAndGetUrl() {
        PaymentMethod pm = new PaymentMethod();
        pm.setUrl("https://payment.example.com");
        assertEquals("https://payment.example.com", pm.getUrl());
    }

    @Test
    void shouldSetAndGetPaymentAddress() {
        PaymentMethod pm = new PaymentMethod();
        pm.setPaymentAddress("1A2B3C4D5E");
        assertEquals("1A2B3C4D5E", pm.getPaymentAddress());
    }

    @Test
    void shouldHaveNullFieldsByDefault() {
        PaymentMethod pm = new PaymentMethod();
        assertNull(pm.getCallbackUrl());
        assertNull(pm.getBarcodeBase64());
        assertNull(pm.getQrType());
        assertNull(pm.getType());
        assertNull(pm.getUrl());
        assertNull(pm.getReference());
        assertNull(pm.getBarcodeUrl());
    }

    @Test
    void shouldSetAndGetAllNewFieldsTogether() {
        PaymentMethod pm = new PaymentMethod();
        pm.setCallbackUrl("https://example.com/cb");
        pm.setBarcodeBase64("abc123==");
        pm.setQrType("CoDi");

        assertEquals("https://example.com/cb", pm.getCallbackUrl());
        assertEquals("abc123==", pm.getBarcodeBase64());
        assertEquals("CoDi", pm.getQrType());
        assertNotNull(pm.toString());
    }
}
