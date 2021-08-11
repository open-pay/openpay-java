package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LendingData {

    private LendingCallbacks callbacks;

    private AmountDetails amountDetails;

    private LendingShipping shipping;

    private LendingBilling billing;

    private List<LendingItems> items;

    @SerializedName("is_privacy_terms_accepted")
    private boolean isPrivacyTermsAccepted;

    private boolean isDigitalProduct;
}
