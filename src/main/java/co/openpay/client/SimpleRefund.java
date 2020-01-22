package co.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class SimpleRefund {

    @SerializedName("operation_date")
    private Date operationDate;

    private String authorization;

    private BigDecimal amount;

    private String status;

    private Boolean conciliated;

    private String id;

    private String description;
    
    private TransactionFee fee;

}
