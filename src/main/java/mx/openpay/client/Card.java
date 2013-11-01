package mx.openpay.client;

import java.util.Date;

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

    private String type;

    private String brand;
    
    private String cvv2;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("bank_code")
    private String bankCode;

    private Address address;
    
    @SerializedName("creation_date")
    private Date creationDate;
}
