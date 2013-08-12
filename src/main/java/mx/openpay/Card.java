package mx.openpay;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
public class Card {

    private String id;
    
    @SerializedName("card_number")
    private String number;
    
    @SerializedName("expiration_year")
    private String expirationYear;
    
    @SerializedName("expiration_month")
    private String expirationMonth;
    
    @SerializedName("holder_name")
    private String holderName;
    
    @SerializedName("allows_withdrawals")
    private boolean allowsWithdrawals;
    
    @SerializedName("allows_deposits")
    private boolean allowsDeposits;
    
    private String status;
    
    private String type;
    
    private String brand;
    
    private Bank bank;
    
    private Address address;
}
