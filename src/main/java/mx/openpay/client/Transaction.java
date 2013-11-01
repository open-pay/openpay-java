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

    @SerializedName("creation_date")
    private Date creationDate;
    
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
