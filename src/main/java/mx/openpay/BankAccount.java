package mx.openpay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BankAccount {

    private String id;
    
    private String clabe;
    
    private String status;
    
    private Bank bank;
    
}
