![Openpay Java](http://www.openpay.mx/img/github/java.jpg)[![Build Status](https://travis-ci.org/open-pay/openpay-java.png?branch=master)](https://travis-ci.org/open-pay/openpay-java)
===============

Java client for Openpay services

This is a client implementing the payment services for Openpay at openpay.mx


Installation
----------------

To install, add the following dependency to your pom.xml:

```xml
<dependency>
	<groupId>mx.openpay</groupId>
	<artifactId>openpay-api-client</artifactId>
	<version>1.0.5/version>
</dependency>
```

Compatibility
----------------

As of now Java 6 is required.

Examples
----------------

#### Starting the API ####

```java
OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
```

#### Creating a customer ####

```java
Address address = new Address()
		.line1("Calle Morelos #12 - 11")
		.line2("Colonia Centro")             // Optional
		.line3("Cuauhtémoc")                 // Optional
		.city("Distrito Federal")
		.postalCode("12345")	
		.state("Queretaro")
		.countryCode("MX");                  // ISO 3166-1 two-letter code
		    
Customer customer = api.customers().create(new Customer()
        .name("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .phoneNumber("554-170-3567")
        .address(address));
```

#### Charging ####

Charging a credit card:		

```java
Card card = new Card()
		.cardNumber("5555555555554444")          // No dashes or spaces
		.holderName("Juan Pérez Nuñez")         
		.cvv2("422")            
		.expirationMonth(9)
		.expirationYear(14);

Charge charge = api.charges().create(customer.getId(), new CreateCardChargeParams()
		.description("Service charge")
		.amount(new BigDecimal("200.00"))       // Amount is in MXN
		.orderId("Charge0001")                  // Optional transaction identifier
		.card(card));
```

Refunding a card charge:

```java
Charge refundedCharge = api.charges().refund(customer.getId(), new RefundParams()
		.chargeId(charge.getId()));
```

Create a charge to be paid by bank transfer:

```java
Charge charge = api.charges().create(customer.getId(), new CreateBankChargeParams()
		.description("Service charge")
		.amount(new BigDecimal("100.00"))
		.orderId("Charge0002"));
```

#### Payout ####

Currently Payouts are only allowed to accounts in Mexico.

Bank payout:

```java
BankAccount bankAccount = new BankAccount()
		.clabe("032180000118359719")            // CLABE
		.holderName("Juan Pérez")
		.alias("Juan's deposit account");       // Optional

Payout payout = api.payouts().create(customer.getId(), new CreateBankPayoutParams()
	    .bankAccount(bankAccount)
	    .amount(new BigDecimal("150.00"))
	    .description("Payment to Juan")
	    .orderId("Payout00001"));               // Optional transaction identifier
```

Debit card payout:

```java
Card card = new Card()
        .cardNumber("4111111111111111")         // No dashes or spaces
        .holderName("Juan Pérez Nuñez")
        .bankCode("012");

Payout payout = api.payouts().create(customer.getId(), new CreateCardPayoutParams()
        .card(card)
        .amount(new BigDecimal("150.00"))
        .description("Payment to Juan")
        .orderId("Payout00002"));               // Optional transaction identifier
```


#### Subscriptions ####

Subscriptions allow you to make recurrent charges to your customers. First you need to define a subscription plan:

```java
Plan plan = api.plans().create(new Plan()
		.name("Premium Subscriptions")
		.amount(new BigDecimal("1200.00"))       // Amount is in MXN
		.repeatEvery(1, PlanRepeatUnit.MONTH)           
		.retryTimes(100)
		.statusAfterRetry(PlanStatusAfterRetry.UNPAID));
```

After you have your plan created, you can subscribe customers to it:

```java
Card card = new Card()
		.cardNumber("5555555555554444")         
		.holderName("Juan Pérez Nuñez")
		.cvv2("422")
		.expirationMonth(9)                  
		.expirationYear(14);

Subscription subscription = api.subscriptions().create(customer.getId(), new Subscription()
		.planId(plan.getId())
		.card(card));      // You can also use withCardId to use a pre-registered card.
```

To cancel the subscription at the end of the current period, you can update its cancelAtPeriodEnd property to true:

```java
subscription.setCancelAtPeriodEnd(true);
api.subscriptions().update(subscription);
```

You can also cancel the subscription immediately:

```java
api.subscriptions().delete(customer.getId(), subscription.getId());
```


