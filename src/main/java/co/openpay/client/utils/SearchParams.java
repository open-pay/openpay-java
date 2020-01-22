/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Class: SearchParams.java
 * 
 * Change control:
 * ---------------------------------------------------------------------------------------
 * Version | Date       | Name                                      | Description
 * ---------------------------------------------------------------------------------------
 *   1.0	2014-01-08	Eli Lopez, eli.lopez@opencard.mx			 Creating Class.
 *   1.1	2014-06-06	Marcos Coronado marcos.coronado@openpay.mx	 Se agregan las propiedades de amountLte y amountGte faltantes.
 *
 */

package co.openpay.client.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.openpay.client.enums.OrderStatusFilter;

/**
 * <p>Clase que permite definir los criterios utilizados para realizar busquedas de algún tipo de entidad.</p>
 *
 * @author Eli Lopez, eli.lopez@opencard.mx
 * @author Marcos Coronado marcos.coronado@openpay.mx
 * @since 2014-06-06
 * @version 1.1
 */
public class SearchParams extends PaginationParams {

    private static final int DEFAULT_LIMIT_SIZE = 10;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static SearchParams search() {
        return new SearchParams();
    }

    public SearchParams() {
        this.limit(DEFAULT_LIMIT_SIZE);
    }

    public SearchParams creation(final Date date) {
        this.params.put("creation", this.format.format(date));
        return this;
    }

    public SearchParams creationLte(final Date date) {
        this.params.put("creation[lte]", this.format.format(date));
        return this;
    }

    public SearchParams creationGte(final Date date) {
        this.params.put("creation[gte]", this.format.format(date));
        return this;
    }

    @Override
    public SearchParams limit(final int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public SearchParams offset(final int offset) {
        super.offset(offset);
        return this;
    }

    /**
     * Shorthand for creationGte(start).creationLte(end).
     */
    public SearchParams between(final Date start, final Date end) {
        return this.creationGte(start).creationLte(end);
    }

    /**
     * Search by amount, for charges, payouts, fees and transfers.
     */
    public SearchParams amount(final BigDecimal amount) {
        this.params.put("amount", amount.toPlainString());
        return this;
    }

    /**
     * <p>Criterio para buscar un monto menor o igual al monto definido como parámetro. </p>
     * @param amountLte monto usado como critero para evaluar
     * @return regresa el objeto {@link SearchParams} con el criterio adjunto
     */
    public SearchParams amountLte(final BigDecimal amountLte) {
        this.params.put("amount[lte]", amountLte.toPlainString());
        return this;
    }
    
    /**
     * <p>Criterio para buscar un monto mayor o igual al monto definido como parámetro. </p>
     * @param amountGte monto usado como critero para evaluar
     * @return regresa el objeto {@link SearchParams} con el criterio adjunto
     */
    public SearchParams amountGte(final BigDecimal amountGte) {
        this.params.put("amount[gte]", amountGte.toPlainString());
        return this;
    }
    
    /**
     * Search transactions by Order ID.
     * @param orderId The Order ID. Unique among successful transactions (Failed transactions may repeat).
     * @return List of transactions with the given Order ID.
     */
    public SearchParams orderId(final String orderId) {
        this.params.put("order_id", orderId);
        return this;
    }
    
    /**
     * Search customer by External Id.
     * @param externalId The External ID. Unique customer external id.
     * @return List of Customers with the given External ID.
     */
    public SearchParams externalId(final String externalId) {
        this.params.put("external_id", externalId);
        return this;
    }
    
    /**
     * Search transactions by Order Status.
     * @param orderStatus The Order Status. Unique among successful transactions (Failed transactions may repeat).
     * @return List of transactions with the given Order ID.
     */
    public SearchParams orderStatus(final OrderStatusFilter orderStatus) {
        this.params.put("order_status", orderStatus.getValue());
        return this;
    }
}
