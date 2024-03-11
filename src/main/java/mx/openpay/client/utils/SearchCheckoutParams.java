package mx.openpay.client.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchCheckoutParams  extends  SearchParams {

    private static final int DEFAULT_LIMIT_SIZE = 10;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public static SearchCheckoutParams search() {
        return new SearchCheckoutParams();
    }

    public SearchCheckoutParams() {
        this.limit(DEFAULT_LIMIT_SIZE);
    }


    public SearchCheckoutParams startDate(final Date date) {
        this.params.put("startDate", this.format.format(date));
        return this;
    }

    public SearchCheckoutParams endDate(final Date date) {
        this.params.put("endDate", this.format.format(date));
        return this;
    }

    @Override
    public SearchCheckoutParams limit(final int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public SearchCheckoutParams offset(final int offset) {
        super.offset(offset);
        return this;
    }


    /**
     * Search by amount, for charges, payouts, fees and transfers.
     */
    public SearchCheckoutParams nameOrLastName(final String nameOrLastName) {
        this.params.put("nameOrLastName", nameOrLastName);
        return this;
    }
}
