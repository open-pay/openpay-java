package mx.openpay.client;

import lombok.Getter;
import lombok.Setter;

/**
 * Data returned in the response regarding the Gateway used during the charge. This data is
 * optionally returned for card transactions.
 */
@Getter
@Setter
public class GatewayResponse {

   /** Affiliation number used for the transaction, if there is one. */
   private String affiliation;

}
