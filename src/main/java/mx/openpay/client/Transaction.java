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
package mx.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

@Getter
@Setter
@ToString
public class Transaction {

    private BigDecimal amount;

    private String id;

    @SerializedName("creation_date")
    private Date creationDate;

    private String status;

    private String description;

    @SerializedName("transaction_type")
    private String transactionType;

    @SerializedName("operation_type")
    private String operationType;

    @SerializedName("method")
    private String method;

    @SerializedName("error_message")
    private String errorMessage;

    private Card card;

    private String authorization;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("bank_account")
    private BankAccount bankAccount;

    @SerializedName("customer_id")
    private String customerId;

	@SerializedName("due_date")
	private Date dueDate;
	
	private TransactionFee fee;

}
