package mx.openpay.core.client.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds the API Credentials for a Merchant or a Group.
 * @author elopez
 */
@Getter
@Setter
@NoArgsConstructor
public class OpenpayTestCredentials {

    /** Merchant ID or Group ID to which the credentials belong. */
    private String id;

    /** Private Key, used to authenticate the user for back-end operations. */
    private String privateKey;

    /** Sets the {@link #id} and returns the same object. */
    public OpenpayTestCredentials id(String id) {
        this.id = id;
        return this;
    }

    /** Sets the {@link #privateKey} and returns the same object. */
    public OpenpayTestCredentials privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

}
