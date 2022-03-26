package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OpenCheckoutConfigurationResponseLight {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    }

