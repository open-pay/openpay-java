package mx.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Transaction {

    private BigDecimal amount;

    private String id;

    @SerializedName("creation_date")
    private Date creationDate;

    private String status;

    private String description;

    @SerializedName("transaction_type")
    private String transactionType;

    @SerializedName("error_message")
    private String errorMessage;

    private Card card;

    private String authorization;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("bank_account")
    private BankAccount bankAccount;

    @SerializedName("customer_id")
    private String customerId;

    private Transaction refund;

}
