
# Certify assets using the Java SDK

## Set certificate when creating asset

To add a certificate when creating an asset:

```java linenums="1" title="Set certificate when creating asset"
GlossaryTerm term = GlossaryTerm
		.toCreate("Example Term", // (1)
				  "b4113341-251b-4adc-81fb-2420501c30e6",
				  null);
term = term.toBuilder() // (2)
		.certificateStatus(AtlanCertificateStatus.VERIFIED) // (3)
		.certificateStatusMessage("Set automatically during creation.") // (4)
		.build(); // (5)
EntityMutationResponse response = term.upsert(); // (6)
response.getCreatedEntities().size == 1 // (7)
```

1. Use the `toCreate()` method to initialize the object with all [necessary attributes for creating it](../../basic-operations/create/#build-minimal-object-needed).
2. Call the `toBuilder()` method on the resulting object to [enrich it further](../../basic-operations/create/#optional-enrich-before-creating).
3. Set the certificate that should be added (in this example, we're using `VERIRIFED`).
4. (Optional) Add a message for the certificate.
5. Call the `build()` method to build the enriched object. (Remember this needs to be assigned back to the original object — line 5!)
6. Call the `upsert()` method to actually create the asset with this certificate.
7. The response will include that single asset that was created.

## Add certificate to existing assets

You can also add a certificate to existing assets. To illustrate a bit more of the builder pattern's flexibility, let's imagine we want to update the certificate on multiple assets at the same time:

```java linenums="1" title="Add certificate to existing assets"
List<Entity> assets = new ArrayList<>();
assets.add(GlossaryTerm // (1)
		.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (2)
				  "Example Term",
				  "b4113341-251b-4adc-81fb-2420501c30e6")
		.toBuilder() // (3)
		.certificateStatus(AtlanCertificateStatus.DEPRECATED) // (4)
		.certificateStatusMessage("This asset should no longer be used.")
		.build()); // (5)
assets.add(GlossaryTerm
		.toUpdate("sduw38sCas83Ca8sdf982@FzCMyPR2LxkPFgr8eNGrq", // (6)
				  "Another Term",
				  "b267858d-8316-4c41-a56a-6e9b840cef4a")
		.toBuilder() // (7)
		.certificateStatus(AtlanCertificateStatus.DEPRECATED)
		.certificateStatusMessage("This asset should no longer be used.")
		.build()); // (8)
EntityMutationResponse response = EntityBulkEndpoint.upsert(assets, false, false); // (9)
response.getUpdatedEntities().size == 2 // (10)
```

1. Define our object directly into an element of a `List`, rather than managing a separate object.
2. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../basic-operations/update/#build-minimal-object-needed).
3. Call the `toBuilder()` method on the resulting object to [enrich it further](../../basic-operations/update/#enrich-before-updating).
4. Directly chain our enrichment methods to add the certificate and message onto the `toBuilder()` method's result.
5. Call the `build()` method to build the enriched object. (No need to assign it back to an original object, as we've chained all the operations together!)
6. Use the `toUpdate()` method to initialize the object for another asset.
7. Chain the `toBuilder()`, enrichment and `build()` methods like we did for the previous asset.
8. Build the result back into the list again, like we did for the previous asset.
9. To optimize our update, we want to limit the number of API calls we make. If we called `upsert()` against each of *n* assets individually, we would have *n* API calls. Here we use the API endpoint directly with a list of assets — this makes 1 API call to update all *n* assets at the same time.
10. The response will include all *n* assets that were updated.

???+ warning "Be aware of how much you're updating per request"
	While this is great for reducing the number of API calls for better performance, do be aware of how many objects you're trying to update per request. There will be a limit beyond which you are trying to send too much information through a single API call and you could see other impacts such as failed requests due to network timeouts.

## Remove certificate from an existing asset

To remove a certificate from an existing asset you need to use a non-builder method:

```java linenums="1" title="Remove certificates from existing assets"
GlossaryTerm term = GlossaryTerm
		.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
				  "Example Term",
				  "b4113341-251b-4adc-81fb-2420501c30e6");
term.removeCertificate(); // (2)
EntityMutationResponse response = term.upsert(); // (3)
response.getUpdatedEntities().size() == 1; // (4)
```

1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../basic-operations/update/#build-minimal-object-needed). (Removing the certificate is still an update to the asset, we are not deleting the asset itself.)
2. Call the `removeCertificate()` method on the built object to set it up for removing the certificate details when sent across to Atlan.
3. Call the `upsert()` method to actually update the asset (remove its certificate).
4. The response will include that single asset that was updated (again, removing a certificate is an update to the asset — we are not deleting the asset itself).
