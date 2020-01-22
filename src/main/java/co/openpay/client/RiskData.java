package co.openpay.client;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskData {

    private String score;

    private List<String> rules;

}
