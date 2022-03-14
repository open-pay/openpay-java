package mx.openpay.client.utils;



import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchOpenCheckoutParams extends PaginationParams {


    private static final int DEFAULT_LIMIT_SIZE = 10;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public static SearchOpenCheckoutParams search() {
        return new SearchOpenCheckoutParams();
    }

    public SearchOpenCheckoutParams() {
        this.limit(DEFAULT_LIMIT_SIZE);
    }


    public SearchOpenCheckoutParams startDate(final Date date) {
        this.params.put("startDate", this.format.format(date));
        return this;
    }

    public SearchOpenCheckoutParams endDate(final Date date) {
        this.params.put("endDate", this.format.format(date));
        return this;
    }

    @Override
    public SearchOpenCheckoutParams limit(final int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public SearchOpenCheckoutParams offset(final int offset) {
        super.offset(offset);
        return this;
    }


    /**
     * Search by amount, for charges, payouts, fees and transfers.
     */
    public SearchOpenCheckoutParams name(final String name) {
        this.params.put("name", name);
        return this;
    }


}
