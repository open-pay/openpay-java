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
 */
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.DeferralPayments;
import mx.openpay.client.GatewayParams;
import mx.openpay.client.HttpContext;
import mx.openpay.client.PaymentOptions;
import mx.openpay.client.ShipTo;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;
import mx.openpay.client.enums.Currency;
import mx.openpay.client.enums.OriginChannel;
import mx.openpay.client.enums.UseCardPointsType;

/**
 * Parameters to charge a credit or debit card.
 * @author elopez
 */
public class CreateCardChargeParams extends RequestBuilder {

    public CreateCardChargeParams() {
        this.with("method", ChargeMethods.CARD.name().toLowerCase());
    }

    /**
     * A new card to use only for this charge. Required if no card Id is given.
     */
    public CreateCardChargeParams card(final Card card) {
        return this.with("card", card);
    }

    /**
     * The ID of a card to use for this charge. Required if no new card is given.
     */
    public CreateCardChargeParams cardId(final String cardId) {
        return this.with("source_id", cardId);
    }

    /**
     * The amount to charge to the card. Required.
     */
    public CreateCardChargeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A description to give to the charge. Optional.
     */
    public CreateCardChargeParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * An unique custom identifier for the charge. Optional.
     */
    public CreateCardChargeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

    /**
     * If false, a preathorization will be made and the card won't be charged until the capture method is called.
     */
    public CreateCardChargeParams capture(final Boolean capture) {
        return this.with("capture", capture);
    }

    /**
     * If false, a charge intention will be registered and the card won't be charged until the confirm method is called.
     */
    public CreateCardChargeParams confirm(final Boolean confirm) {
        return this.with("confirm", confirm);
    }

    /**
     * Device Session ID generated by the front user, used for fraud detection.
     * @param deviceSessionId
     * @return
     */
    public CreateCardChargeParams deviceSessionId(final String deviceSessionId) {
        return this.with("device_session_id", deviceSessionId);
    }

    /**
     * A currency to give to the charge. Optional.<br>
     * Default value is MXN<br>
     * @param currency
     * @return
     */
    public CreateCardChargeParams currency(final Currency currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency.name());
    }

    /**
     * A currency to give to the charge in ISO 4217 alphanumeric code. Optional.<br>
     * Default value is MXN<br>
     * @param currency
     * @return
     */
    public CreateCardChargeParams currency(final String currency) {
        return this.with("currency", currency == null ? Currency.MXN.name() : currency);
    }

    /**
     * Customer Information when you want to send info but not create the resource
     */
    public CreateCardChargeParams customer(final Customer customer) {
        return this.with("customer", customer);
    }

    /**
     * Is Phone Order Flag indicate the transaction was sent from call center.
     */
    public CreateCardChargeParams isPhoneOrder(final boolean isPhoneOrder) {
        return this.with("is_phone_order", isPhoneOrder);
    }

    /**
     * Due Date when you create an unconfirmed charge you may want to define the date limit to confirm.
     */
    public CreateCardChargeParams dueDate(final Date dueDate) {
        return this.with("due_date", dueDate);
    }

    /**
     * Personalized metadata.
     */
    public CreateCardChargeParams metadata(final Map<String, String> metadata) {
        return this.with("metadata", metadata);
    }

    /**
     * If true, the charge will use the points available in the card. <br>
     * The request will fail if the card does not accept points, so check the card response first.
     */
    public CreateCardChargeParams useCardPoints(final UseCardPointsType useCardPoints) {
        return this.with("use_card_points", useCardPoints);
    }
    
    public CreateCardChargeParams paymentOptions(final String paymentOptions) {
    	PaymentOptions po = new PaymentOptions(); 
    	po.setPayments(paymentOptions);
    	return this.with("payment_options", po);
    }

    /**
     * If true, the charge will use the points available in the card. <br>
     * The request will fail if the card does not accept points, so check the card response first.
     */
    public CreateCardChargeParams deferralPayments(final DeferralPayments deferralPayments) {
        return this.with("payment_plan", deferralPayments);
    }

    /**
     * Send Email Flag indicate the transaction will sent to email.
     */
    public CreateCardChargeParams sendEmail(final boolean sendEmail) {
        return this.with("send_email", sendEmail);
    }

    /**
     * Redirect Url indicate the url to redirect after completed the transaction.
     */
    public CreateCardChargeParams redirectUrl(final String redirectUrl) {
        return this.with("redirect_url", redirectUrl);
    }

    /**
     * indicates if the charge must be done using 3D Secure.
     * @since 1.0.10
     */
    public CreateCardChargeParams use3dSecure(final Boolean use3dSecure) {
        return this.with("use_3d_secure", use3dSecure);
    }

    /**
     * Sends cvv or cvc for those cases when the card is stored in Openpay in order to improve the acceptance
     */
    public CreateCardChargeParams cvv2(final String cvv2) {
        return this.with("cvv2", cvv2);
    }

    /**
     * Sends shipping information.
     */
    public CreateCardChargeParams shipTo(final ShipTo shipTo) {
        return this.with("ship_to", shipTo);
    }

    /**
     * Sends customer browser information.
     */
    public CreateCardChargeParams httpContext(final HttpContext httpContext) {
        return this.with("http_context", httpContext);
    }
    
    /** Additional Gateway Options. */
    public CreateCardChargeParams gateway(final GatewayParams gatewayParams) {
        return this.with("gateway", gatewayParams);
    }

    /**
     * Sends iva 
     */
    public CreateCardChargeParams iva(final String iva) {
    	return this.with("iva", iva);
    }
    
    /**
     * Origin channel.
     *
     * @param originChannel the origin channel
     * @return the creates the card charge params
     */
    public CreateCardChargeParams originChannel(OriginChannel originChannel) {
       return this.with("origin_channel", originChannel.name().toLowerCase());
    }
}
