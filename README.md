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
		    
CreateCustomer params = new CreateCustomer()
        .withName("John")
        .withLastName("Doe")
        .withEmail("johndoe@example.com")
        .withPhoneNumber("554-170-3567")
        .withAddress(address);

Customer customer = this.ops.create(params);
```

#### Charging ####
		
```java
Card card = new Card();
card.setCardNumber("5555555555554444");         // No dashes or spaces
card.setHolderName("Juan Pérez Nuñez");
card.setCvv2("422");
card.setExpirationMonth("09");                  // Month to two digits
card.setExpirationYear("14");

CreateCardCharge params = new CreateCardCharge()
		.withCustomerId(customer.getId())
		.withDescription("Service charge")
		.withAmount(new BigDecimal("200.00"))   // Amount is in MXN
		.withOrderId("Charge0001")              // Optional transaction identifier
		.withCard(card);

Charge charge = api.charges().create(params);
```

#### Payout ####

Currently Payouts are only allowed to bank accounts within Mexico.

```java
BankAccount bankAccount = new BankAccount();
bankAccount.setClabe("032180000118359719");     // CLABE
bankAccount.setHolderName("Juan Pérez");
bankAccount.setAlias("Juan's deposit account"); // Optional

CreatePayout params = new CreatePayout()
	    .withCustomerId(customer.getId())
	    .withBankAccount(bankAccount)
	    .withAmount(new BigDecimal("150.00"))
	    .withDescription("Payment to Juan")
	    .withOrderId("Payout00001");            // Optional transaction identifier

Payout transaction = api.payouts().create(params);
```

#### Subscriptions ####

Subscriptions allow you to make recurrent charges to your customers. First you need to define a subscription plan:

```java
CreatePlan params = new CreatePlan()
		.withName("Premium Subscriptions")
		.withAmount(new BigDecimal("1200.00"))              // Amount is in MXN
		.withRepeatEvery(1, PlanRepeatUnit.MONTH)           
		.withRetryTimes(100)
		.withStatusAfterRetry(PlanStatusAfterRetry.UNPAID); 
		
Plan plan = api.plans().create(params);
```

After you have your plan created, you can subscribe customers to it:

```java
Card card = new Card();
card.setCardNumber("5555555555554444");         
card.setHolderName("Juan Pérez Nuñez");
card.setCvv2("422");
card.setExpirationMonth("09");                  
card.setExpirationYear("14");

CreateSubscription params = new CreateSubscription(customer.getId())
		.withPlanId(plan.getId())
		.withCard(card);             // You can also use withCardId to use a pre-registered card.
		
Subscription subscription = api.subscriptions().create(params);
```

To cancel the subscription at the end of the current period, you can update its cancelAtPeriodEnd property to true:

```java
UpdateSubscription params = new UpdateSubscription(customer.getId(), subscription.getId())
		.withCancelAtPeriodEnd(true);
 		
api.subscriptions().update(params);
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


