package mx.openpay.client;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
public class Card {

    @SerializedName("creation_date")
    private Date creationDate;

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("allows_deposits")
    private boolean allowsDeposits;

    @SerializedName("holder_name")
    private String holderName;

    @SerializedName("expiration_month")
    private String expirationMonth;

    private Address address;

    @SerializedName("card_number")
    private String cardNumber;

    private String brand;

    @SerializedName("expiration_year")
    private String expirationYear;

    @SerializedName("allows_withdrawals")
    private boolean allowsWithdrawals;

    @SerializedName("bank_code")
    private String bankCode;

    private String type;

    private String cvv2;

}
