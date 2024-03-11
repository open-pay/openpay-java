package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDetails {

    @SerializedName("returned_elements")
    private Integer returnedElements;
    @SerializedName("total_elements")
    private Integer totalElements;
}
