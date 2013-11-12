/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.enums;

/**
 * @author elopez
 */
public enum TransactionType {

    /** Cuando se recibe un pago por SPEI */
    SPEI,

    /** Cargo a tarjeta para agregar a account. */
    CARD_DEBIT,

    /** Deposito del account hacia un banco. */
    BANK_DEPOSIT,

    /** Transaccion del account origen en un traspaso entre accounts. */
    TRANSFER_FROM,

    /** Transaccion del account destino en un traspaso entre accounts. */
    TRANSFER_TO,

    /** Transaccion del account al que se le esta cobrando una comision. */
    FEE_FROM,

    /** Cancelacion de cargo a tarjeta */
    CARD_REFUND,

    /** Transferencia de un account a una tarjeta */
    CARD_DEPOSIT,

    /** Transaccion del account al que se le esta depositando el cobro de una comision. */
    FEE_TO,

    /** Cancelacion de un deposito a tarjeta o cuenta de banco */
    CANCEL_DEPOSIT;

}
