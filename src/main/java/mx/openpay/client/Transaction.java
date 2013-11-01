package mx.openpay.client;

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
    
    private String description;
    
    @SerializedName("transaction_type")
    private String transactionType;
    
    private String status;
    
    private String id;
    
    private Customer customer;
    
    @SerializedName("bank_account")
    private BankAccount bankAccount;
    
    private Card card;
}
