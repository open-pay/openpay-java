package co.openpay.client;

import lombok.Getter;
import lombok.Setter;

/**
 * Customer's browser information.
 */
@Getter
@Setter
public class HttpContext {

    /** Customer's IP. */
    private String ip;

    /** Customer's Browser. */
    private String browser;

    /** Merchant website domain. */
    private String domain;

    public HttpContext ip(String ip) {
        this.ip = ip;
        return this;
    }

    public HttpContext browser(String browser) {
        this.browser = browser;
        return this;
    }

    public HttpContext domain(String domain) {
        this.domain = domain;
        return this;
    }

}
