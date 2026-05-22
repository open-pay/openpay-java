package mx.openpay.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Taxes {

    @SerializedName("base_amount")
    private BigDecimal baseAmount;

    @SerializedName("iva_amount")
    private BigDecimal ivaAmount;

    @SerializedName("consumption_tax_amount")
    private BigDecimal consumptionTaxAmount;

    @SerializedName("airport_tax")
    private BigDecimal airportTax;

    public Taxes baseAmount(final BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public Taxes ivaAmount(final BigDecimal ivaAmount) {
        this.ivaAmount = ivaAmount;
        return this;
    }

    public Taxes consumptionTaxAmount(final BigDecimal consumptionTaxAmount) {
        this.consumptionTaxAmount = consumptionTaxAmount;
        return this;
    }

    public Taxes airportTax(final BigDecimal airportTax) {
        this.airportTax = airportTax;
        return this;
    }
}
