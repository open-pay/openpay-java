package mx.openpay.core.client;

import junit.framework.Assert;
import mx.openpay.client.Address;
import mx.openpay.client.BankAccount;
import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.OpenPayServices;
import mx.openpay.client.Transaction;
import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import org.junit.Before;
import org.junit.Test;

public class STPMexTest {

	private OpenPayServices openPayServices;

    private static String root = "http://localhost:8080/Services/";

    private String merchantId = "m7psroutl8tycqtcmxly";

    private String apiKey = "e97b8bf7728242c0aa97b409a4c59236";

    @Before
    public void setUp() throws Exception {
        this.openPayServices = new OpenPayServices(this.merchantId, this.apiKey, root);
    }
    
    @Test
    public void sendSPEI() throws ServiceUnavailable, HttpError {
    	
    	Address address = null;
        Customer customer = new Customer();
        
        //Organizador
        address = new Address();
        address.setCity("Queretaro");
        address.setExteriorNumber("63");
        
        address.setInteriorNumber("37");
        address.setPostalCode("12345");
        address.setRegion("Corregidora");
        address.setStreet("Av. Camino Real");
//        customer = this.openPayServices.createCustomer("Heber", "Lazcano", "heber.lazcano@yattos.com", "4422581039", address);
//        Assert.assertNotNull(customer);
//        Assert.assertNotNull(customer.getId());
        
        Card card = new Card();
        card.setHolderName("Heber");
        card.setExpirationMonth("09");
        card.setExpirationYear("15");
        card.setNumber("5516361252244977");
        card.setAddress(address);
        card.setCvv2("123");
//        card = this.openPayServices.createCard(customer.getId(), "5516361252244977", "Heber", "123", "10", "13", address);
        
        this.openPayServices.collectFunds("awpdk2ikl0lmadpsnhb8", "knvba6m6yqgzafglfict", 100.00, "Recarga desde tarjeta", "098765432112");
        
//        customer.setId("agt0tslutb7tyz4nu1ce");
        BankAccount bankAccount = this.openPayServices.createBank(customer.getId(), "012298026516924616");
        Transaction transaction = this.openPayServices.sendFunds(customer.getId(), bankAccount.getId(), 10.00, "Prueba STPMEX", "1234567890222");
        Assert.assertNotNull(transaction);
        System.out.println("TRANS: " + transaction);
    }
}
