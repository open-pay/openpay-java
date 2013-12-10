openpay-java
===============

Java client for Openpay services

This is a client implementing the payment services for Openpay at openpay.mx

Compatibility
----------------

As of now Java 6 is required.

Examples
----------------

#### Starting the API ####

		OpenpayAPI api = new OpenpayAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
	
- - -

#### Creating a customer ####

		Address address = new Address();
		address.setLine1("Calle Morelos #12 - 11");
		address.setLine2("Colonia Centro");				// Optional
		address.setLine3("Cuauhtémoc");					// Optional
		address.setCity("Distrito Federal");
        address.setPostalCode("12345");	
        address.setState("Queretaro");
        address.setCountryCode("MX");					// ISO 3166-1 two-letter code
        		    
		Customer customer = api.customers().create("John", "Doe", "johndoe@example.com", "000-000-0000", address);

- - -

#### Charging a customer ####

		Address address = new Address();
		address.setLine1("Ayuntamiento #111");
		address.setLine2("Colonia Centro");				// Optional
		address.setLine3("Cuauhtémoc");					// Optional
		address.setCity("Distrito Federal");
        address.setPostalCode("12345");	
        address.setState("Queretaro");
        address.setCountryCode("MX");
		
		Card card = new Card();
		card.setCardNumber("0000000000000000");			// No dashes or spaces
        card.setHolderName("Juan Pérez Nuñez");
        card.setCvv2("000");
        card.setExpirationMonth("09");					// Month to two digits
        card.setExpirationYear("14");
        card.setAddress(address);
		
		Charge charge = api.charges().create(customer.getId(), card, amount, description, orderId);
	    
- - -

#### Payout ####

Currently Payouts are only allowed to bank accounts within Mexico.

		Address address = new Address();
		address.setLine1("Ayuntamiento #111");
		address.setLine2("Colonia Centro");				// Optional
		address.setLine3("Cuauhtémoc");					// Optional
		address.setCity("Distrito Federal");
        address.setPostalCode("12345");	
        address.setState("Queretaro");
        address.setCountryCode("MX");
		
		BankAccount bankAccount = new BankAccount();
	  	bankAccount.setClabe("012298026516924616");		// Clave Bancaria Estandarizada
        bankAccount.setHolderName("Juan Pérez");
        bankAccount.setAlias("Juan's deposit account");	// Optional
				
		Payout transaction = this.payouts.createForCustomer(customerId, bankAccount, amount, desc, orderId);


Installation
----------------

To install, add the following dependency to your pom.xml:

	<dependency>
		<groupId>mx.openpay</groupId>
		<artifactId>openpay-api-client</artifactId>
		<version>1.0.0</version>
	</dependency>



