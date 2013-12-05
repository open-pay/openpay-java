openpay-java
===============

Java client for Openpay services

This is a client implementing the payment services for Openpay at openpay.mx

Compatibility
----------------

As of now Java 6 is required.

Examples
----------------

Starting the API:

		OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
	
- - -

Creating a customer:

		Address address = new Address();
		address.setCountryCode("MX");
		
		...
		    
		Customer customer = api.customers().create("John", "Doe", "johndoe@example.com", "000-000-0000", address);

- - -

Charging a customer:
	
		Card card = new Card();
		
		...
		
		Charge charge = api.charges().create(customer.getId(), card, amount, description, orderId);

or

		Charge charge = api.charges().create(customer.getId(), cardId, amount, description, orderId);
	    
- - -

Payout:
		
		BankAccount bankAccount = new BankAccount();
	
		...
		
		Payout transaction = this.payouts.createForCustomer(customerId, bankAccount, amount, desc, orderId);

or

		Payout payout = this.payouts.createForCustomer(customer.getId(), PayoutMethod.BANK_ACCOUNT, bankAccountId,
	                amount, desc, orderId);



Installation
----------------

To install, add the following dependency to your pom.xml:

	<dependency>
		<groupId>mx.openpay</groupId>
		<artifactId>openpay-api-client</artifactId>
		<version>1.0.0</version>
	</dependency>



