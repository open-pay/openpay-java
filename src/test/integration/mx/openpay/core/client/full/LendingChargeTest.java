package mx.openpay.core.client.full;

import mx.openpay.client.*;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.CreateLendingChargeParams;
import mx.openpay.client.enums.LendingShippingType;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LendingChargeTest extends BaseTest {

    @Before
    public void setUp() throws Exception {
        this.api = new OpenpayAPI("https://dev-api.openpay.mx", "sk_902afcb096104425b43a80337788955b", "mtd4dwopalmf9ags3eqs");
    }
    @Test
    public void merchantLendingCharge() throws OpenpayServiceException, ServiceUnavailableException {
        CreateLendingChargeParams request = new CreateLendingChargeParams();
        LendingData lendingData = new LendingData();
        lendingData.setPrivacyTermsAccepted(true);
        LendingCallbacks lendingCallbacks = new LendingCallbacks();
        lendingCallbacks.setOnSuccess("https://merchantdomain.com/success");
        lendingCallbacks.setOnReject("https://merchantdomain.com/reject");
        lendingCallbacks.setOnCanceled("https://merchantdomain.com/canceled");
        lendingCallbacks.setOnFailed("https://merchantdomain.com/failed");
        lendingData.setCallbacks(lendingCallbacks);
        AmountDetails amountDetails = new AmountDetails();
        amountDetails.setSubtotal(new BigDecimal(2000.00));
        amountDetails.setShipping(new BigDecimal(0));
        amountDetails.setHandlingFee(new BigDecimal(0));
        amountDetails.setTax(new BigDecimal(0));
        amountDetails.setDiscount(new BigDecimal(0));
        lendingData.setAmountDetails(amountDetails);

        LendingShipping lendingShipping = new LendingShipping();
        lendingShipping.setName("Jose");
        lendingShipping.setLastName("Martinez");
        LendingAddress lendingAddress = new LendingAddress();
        lendingAddress.setAddress("Insurgentes 98");
        lendingAddress.setInterior("Interior 2");
        lendingAddress.setNeighborhood("Colonia Centro");
        lendingAddress.setState("Queretaro");
        lendingAddress.setCity("Queretaro");
        lendingAddress.setZipcode("12345");
        lendingAddress.setCountry("MX");
        lendingShipping.setAddress(lendingAddress);
        lendingShipping.setPhoneNumber("4158688276");
        lendingShipping.setEmail("jose.lopez+1@openpay.com");
        lendingShipping.setType(LendingShippingType.HOME.name());
        lendingData.setShipping(lendingShipping);

        LendingBilling lendingBilling = new LendingBilling();
        lendingBilling.setName("Jose Lopez");
        lendingBilling.setRfc("LOMJ800117E21");
        lendingBilling.setAddress(lendingAddress);
        lendingBilling.setPhoneNumber("4428767865");
        lendingBilling.setEmail("jose.lopez+1@openpay.com");
        lendingData.setBilling(lendingBilling);

        List<LendingItems> lendingItemsList = new ArrayList<LendingItems>();
        LendingItems lendingItems =  new LendingItems();
        lendingItems.setName("Tenis zoom 5");
        lendingItems.setDescription("Nike Air Zoom Wild Horse 5, Color Blanco Talla 32");
        lendingItems.setQuantity(1);
        lendingItems.setPrice(new BigDecimal(2000));
        lendingItems.setTax(new BigDecimal(0));
        lendingItems.setSku("1");
        lendingItems.setDiscount(new BigDecimal(0));
        lendingItems.setCurrency("MXN");
        lendingData.setItems(lendingItemsList);

        Customer customer = new Customer();
        customer.setName("Juan");
        customer.setLastName("Vazquez Juarez");
        customer.setPhoneNumber("4423456723");
        customer.setEmail("juan.vazquez@empresa.com.mx");

        request.amount(new BigDecimal("2000.00"));
        request.currency("MXN");
        request.description("Cargo desde cliente Java test");
        request.orderId("Order" + String.format("%f", Math.random()));
        request.customer(customer);
        request.lendingData(lendingData);

        Charge charge = this.api.charges().createCharge(request);
        Assert.assertNotNull(charge);
        Assert.assertNotNull(charge.getId());
        Assert.assertEquals("in_progress", charge.getStatus());
        Assert.assertEquals("lending", charge.getMethod());

    }

    @Test
    public void customerLendingCharge() throws OpenpayServiceException, ServiceUnavailableException {
        CreateLendingChargeParams request = new CreateLendingChargeParams();
        LendingData lendingData = new LendingData();
        lendingData.setPrivacyTermsAccepted(true);
        LendingCallbacks lendingCallbacks = new LendingCallbacks();
        lendingCallbacks.setOnSuccess("https://merchantdomain.com/success");
        lendingCallbacks.setOnReject("https://merchantdomain.com/reject");
        lendingCallbacks.setOnCanceled("https://merchantdomain.com/canceled");
        lendingCallbacks.setOnFailed("https://merchantdomain.com/failed");
        lendingData.setCallbacks(lendingCallbacks);
        AmountDetails amountDetails = new AmountDetails();
        amountDetails.setSubtotal(new BigDecimal(2000.00));
        amountDetails.setShipping(new BigDecimal(0));
        amountDetails.setHandlingFee(new BigDecimal(0));
        amountDetails.setTax(new BigDecimal(0));
        amountDetails.setDiscount(new BigDecimal(0));
        lendingData.setAmountDetails(amountDetails);

        LendingShipping lendingShipping = new LendingShipping();
        lendingShipping.setName("Jose");
        lendingShipping.setLastName("Martinez");
        LendingAddress lendingAddress = new LendingAddress();
        lendingAddress.setAddress("Insurgentes 98");
        lendingAddress.setInterior("Interior 2");
        lendingAddress.setNeighborhood("Colonia Centro");
        lendingAddress.setState("Queretaro");
        lendingAddress.setCity("Queretaro");
        lendingAddress.setZipcode("12345");
        lendingAddress.setCountry("MX");
        lendingShipping.setAddress(lendingAddress);
        lendingShipping.setPhoneNumber("4158688276");
        lendingShipping.setEmail("jose.lopez+1@openpay.com");
        lendingShipping.setType(LendingShippingType.HOME.name());
        lendingData.setShipping(lendingShipping);

        LendingBilling lendingBilling = new LendingBilling();
        lendingBilling.setName("Jose Lopez");
        lendingBilling.setRfc("LOMJ800117E21");
        lendingBilling.setAddress(lendingAddress);
        lendingBilling.setPhoneNumber("4428767865");
        lendingBilling.setEmail("jose.lopez+1@openpay.com");
        lendingData.setBilling(lendingBilling);

        List<LendingItems> lendingItemsList = new ArrayList<LendingItems>();
        LendingItems lendingItems =  new LendingItems();
        lendingItems.setName("Tenis zoom 5");
        lendingItems.setDescription("Nike Air Zoom Wild Horse 5, Color Blanco Talla 32");
        lendingItems.setQuantity(1);
        lendingItems.setPrice(new BigDecimal(2000));
        lendingItems.setTax(new BigDecimal(0));
        lendingItems.setSku("1");
        lendingItems.setDiscount(new BigDecimal(0));
        lendingItems.setCurrency("MXN");
        lendingData.setItems(lendingItemsList);

        request.amount(new BigDecimal("2000.00"));
        request.currency("MXN");
        request.description("Cargo desde cliente Java test");
        request.orderId("Order" + String.format("%f", Math.random()));
        request.lendingData(lendingData);

        Charge charge = this.api.charges().createCharge("a3chshllsuzlf8dcnwas",request);
        Assert.assertNotNull(charge);
        Assert.assertNotNull(charge.getId());
        Assert.assertEquals("in_progress", charge.getStatus());
        Assert.assertEquals("lending", charge.getMethod());

    }
}
