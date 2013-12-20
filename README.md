openpay-java
===============

Java client for Openpay services

This is a client implementing the payment services for Openpay at openpay.mx

What's new
----------------

- **API incompatibility**: Removed the option to look a transfer up using only the transactionId.
- **API incompatibility**: Customer charges can't be looked up only with the transactionId anymore, customerId is required. 
- Added management of plans and subscriptions.
- Fixed bug that made Java 7 required.
- Parameters are now set into a request object to reduce method signatures.


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
Address address = new Address();
address.setLine1("Calle Morelos #12 - 11");
address.setLine2("Colonia Centro");             // Optional
address.setLine3("Cuauhtémoc");                 // Optional
address.setCity("Distrito Federal");
address.setPostalCode("12345");	
address.setState("Queretaro");
address.setCountryCode("MX");                   // ISO 3166-1 two-letter code
		    
Customer customer = api.customers().create(new CreateCustomerParams()
        .name("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .phoneNumber("554-170-3567")
        .address(address));
```

#### Charging ####
		
```java
Card card = new Card();
card.setCardNumber("5555555555554444");         // No dashes or spaces
card.setHolderName("Juan Pérez Nuñez");         
card.setCvv2("422");                            
card.setExpirationMonth("09");                  // Month to two digits
card.setExpirationYear("14");

Charge charge = api.charges().create(new CreateCardChargeParams()
		.customerId(customer.getId())
		.description("Service charge")
		.amount(new BigDecimal("200.00"))       // Amount is in MXN
		.orderId("Charge0001")                  // Optional transaction identifier
		.card(card));
```

#### Payout ####

Currently Payouts are only allowed to bank accounts within Mexico.

```java
BankAccount bankAccount = new BankAccount();
bankAccount.setClabe("032180000118359719");     // CLABE
bankAccount.setHolderName("Juan Pérez");
bankAccount.setAlias("Juan's deposit account"); // Optional

CreatePayout params = ;            

Payout transaction = api.payouts().create(new CreatePayoutParams()
	    .customerId(customer.getId())
	    .bankAccount(bankAccount)
	    .amount(new BigDecimal("150.00"))
	    .description("Payment to Juan")
	    .orderId("Payout00001"));               // Optional transaction identifier
```

#### Subscriptions ####

Subscriptions allow you to make recurrent charges to your customers. First you need to define a subscription plan:

```java
Plan plan = api.plans().create(new CreatePlanParams()
		.name("Premium Subscriptions")
		.amount(new BigDecimal("1200.00"))       // Amount is in MXN
		.repeatEvery(1, PlanRepeatUnit.MONTH)           
		.retryTimes(100)
		.statusAfterRetry(PlanStatusAfterRetry.UNPAID));
```

After you have your plan created, you can subscribe customers to it:

```java
Card card = new Card();
card.setCardNumber("5555555555554444");         
card.setHolderName("Juan Pérez Nuñez");
card.setCvv2("422");
card.setExpirationMonth("09");                  
card.setExpirationYear("14");

Subscription subscription = api.subscriptions()
		.create(new CreateSubscriptionParams()
				.customerId(customer.getId())
				.planId(plan.getId())
				.card(card));      // You can also use withCardId to use a pre-registered card.
```

To cancel the subscription at the end of the current period, you can update its cancelAtPeriodEnd property to true:

```java
api.subscriptions().update(new UpdateSubscriptionParams()
		.customerId(customer.getId())
		.subscriptionId(subscription.getId())
		.cancelAtPeriodEnd(true));
```

You can also cancel the subscription immediately:

```java
api.subscriptions().delete(customer.getId(), subscription.getId());
```

_(TODO)_

Installation
----------------

To install, add the following dependency to your pom.xml:

```xml
<dependency>
	<groupId>mx.openpay</groupId>
	<artifactId>openpay-api-client</artifactId>
	<version>1.0.1</version>
</dependency>
```


