package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class OpenCheckoutConfigurationSearchResponse {
    @SerializedName("configurations")
    private List<OpenCheckoutConfigurationResponse> configurations;
    @SerializedName("search_details")
    private SearchDetails searchDetails;





}
