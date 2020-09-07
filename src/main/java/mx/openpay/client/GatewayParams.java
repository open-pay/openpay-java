package mx.openpay.client;

import java.util.HashMap;
import java.util.Map;

public class GatewayParams {

    public Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    public GatewayParams addData(String gateway, String param, String value) {
        Map<String, String> map = data.get(gateway.trim().toLowerCase());
        if (map == null) {
            map = new HashMap<String, String>();
            data.put(gateway.trim().toLowerCase(), map);
        }
        map.put(param, value);
        return this;
    }

}
