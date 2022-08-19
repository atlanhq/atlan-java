
# Certify assets

## Add to an existing asset

To add a certificate to an existing [asset](/concepts/assets), we've provided a helper method. This is probably the most common use case:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add certificate to existing assets"
	Table result = Table.updateCertificate( // (1)
						"default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS", // (2)
						AtlanCertificateStatus.VERIFIED, // (3)
						"Verified through automation."); // (4)
	```

	1. Use the `updateCertificate()` helper method, which for most objects requires a minimal set of information. This helper method will construct the necessary request, call the necessary API(s), and return with the result of the update operation all-in-one.
	2. The `qualifiedName` of the object.
	3. The type of certificate (the `AtlanCertificateStatus` enumeration gives the valid values).
	4. (Optional) A message to include in the certificate.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## When creating an asset

To add a certificate when creating an [asset](/concepts/assets), you can chain the certificate details onto the [`creator()` result](../../java/basic-operations/create/#build-minimal-object-needed):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add certificate when creating asset"
	GlossaryTerm term = GlossaryTerm
			.creator("Example Term", // (1)
					  "b4113341-251b-4adc-81fb-2420501c30e6",
					  null);
			.certificateStatus(AtlanCertificateStatus.VERIFIED) // (2)
			.certificateStatusMessage("Verified at creation.") // (3)
			.build(); // (4)
	EntityMutationResponse response = term.upsert(); // (5)
	response.getCreatedEntities().size() == 1 // (6)
	```
	
	1. Use the `creator()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed).
	2. Set the certificate that should be added (in this example, we're using `VERIFIED`).
	3. (Optional) Add a message for the certificate.
	4. Call the `build()` method to build the enriched object.
	5. Call the `upsert()` method to actually create the [asset](/concepts/assets) with this certificate.
	6. The response will include that single [asset](/concepts/assets) that was created.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Remove from an existing asset

To remove a certificate from an existing asset:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove certificate from existing asset"
	Column column = Column.
			.updater("default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS/USER_ID", // (1)
					  "USER_ID");
	column.removeCertificate(); // (2)
	EntityMutationResponse response = column.upsert(); // (3)
	response.getUpdatedEntities().size() == 1; // (4)
	```

	1. Use the `updater()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the certificate is still an update to the [asset](/concepts/assets), we are not deleting the asset itself.)
	2. Call the `removeCertificate()` method on the built object to set it up for removing the certificate details when sent across to Atlan.
	3. Call the `upsert()` method to actually update the [asset](/concepts/assets) (remove its certificate).
	4. The response will include that single [asset](/concepts/assets) that was updated (again, removing an certificate is an update to the [asset](/concepts/assets) — we are not deleting the [asset](/concepts/assets) itself).

=== ":material-language-python: Python"

	!!! construction "Coming soon"
