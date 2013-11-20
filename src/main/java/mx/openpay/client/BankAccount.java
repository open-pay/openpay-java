package mx.openpay.client;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class BankAccount {

    private String id;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("holder_name")
    private String holderName;

    private String clabe;

    private String alias;

    @SerializedName("bank_code")
    private String bankCode;

    @SerializedName("creation_date")
    private Date creationDate;

}
