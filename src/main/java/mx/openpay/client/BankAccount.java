package mx.openpay.client;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BankAccount {

    private String id;
    
    private String clabe;
    
    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("bank_code")
    private String bankCode;
    
    @SerializedName("creation_date")
    private Date creationDate;
    
}
