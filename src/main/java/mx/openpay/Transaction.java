package mx.openpay;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Transaction {

    private Date date;
    
    private Double amount;
    
    private String memo;
    
    @SerializedName("transaction_type")
    private String transactionType;
    
    private String status;
    
    private String id;
    
    @SerializedName("ewallet_id")
    private String ewalletId;
    
    @SerializedName("bank_id")
    private String bankId;
    
    @SerializedName("card_id")
    private String cardId;
}
