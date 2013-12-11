openpay-java
===============

Java client for Openpay services

This is a client implementing the payment services for Openpay at openpay.mx

Compatibility
----------------

As of now Java 7 is required.

Examples
----------------

#### Starting the API ####

		OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
	
#### Creating a customer ####

		Address address = new Address();
		address.setLine1("Calle Morelos #12 - 11");
		address.setLine2("Colonia Centro");				// Optional
		address.setLine3("Cuauhtémoc");				// Optional
		address.setCity("Distrito Federal");
        address.setPostalCode("12345");	
        address.setState("Queretaro");
        address.setCountryCode("MX");					// ISO 3166-1 two-letter code
        		    
		Customer customer = api.customers()
					.create("John", "Doe", "johndoe@example.com", "554-170-3567", address);

#### Charging ####
		
		Card card = new Card();
		card.setCardNumber("5555555555554444");			// No dashes or spaces
        card.setHolderName("Juan Pérez Nuñez");
        card.setCvv2("422");
        card.setExpirationMonth("09");					// Month to two digits
        card.setExpirationYear("14");
		
		Charge charge = api.charges()
					.create(customer.getId(), card, amount, description, orderId);
	    
#### Payout ####

Currently Payouts are only allowed to bank accounts within Mexico.

		BankAccount bankAccount = new BankAccount();
	  	bankAccount.setClabe("032180000118359719");		// Clave Bancaria Estandarizada
        bankAccount.setHolderName("Juan Pérez");
        bankAccount.setAlias("Juan's deposit account");	// Optional
        
        
		String description = "Service payment";
		String orderId = "OID0001";						// Optional transaction identifier
		BigDecimal amount = new BigDecimal("150.00");
		Payout transaction = api.payouts()
					.createForCustomer(customer.getId(), bankAccount, amount, description, orderId);


Installation
----------------

To install, add the following dependency to your pom.xml:

	<dependency>
		<groupId>mx.openpay</groupId>
		<artifactId>openpay-api-client</artifactId>
		<version>1.0.0</version>
	</dependency>



