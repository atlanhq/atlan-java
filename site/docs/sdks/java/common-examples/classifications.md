
# Classify assets using the Java SDK

!!! details "Cannot add classifications when creating assets"
	Currently it is not possible to add classifications when creating assets.

## Add classifications to existing assets

To add classifications to existing assets:

```java linenums="1" title="Add classifications to existing assets"
final String termGuid1 = "b4113341-251b-4adc-81fb-2420501c30e6";
final String termGuid2 = "b267858d-8316-4c41-a56a-6e9b840cef4a";
List<Entity> assets = new ArrayList<>();
assets.add(GlossaryTerm // (1)
		.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (2)
				  "Example Term",
				  termGuid1)
		.toBuilder() // (3)
		.classification(Classification.of("PII", termGuid1)) // (4)
		.classification(Classification.of("Finance", termGuid1) // (5)
		.build()); // (6)
assets.add(GlossaryTerm
		.toUpdate("sduw38sCas83Ca8sdf982@FzCMyPR2LxkPFgr8eNGrq", // (7)
				  "Another Term",
				  termGuid2)
		.toBuilder() // (8)
		.classification(Classification.of("PII", termGuid2))
		.classification(Classification.of("HR", termGuid2))
		.build()); // (9)
EntityMutationResponse response = EntityBulkEndpoint.upsert(assets, true, false); // (10)
response.getUpdatedEntities().size == 2 // (11)
```

1. Define our object directly into an element of a `List`, rather than managing a separate object.
2. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../basic-operations/update/#build-minimal-object-needed).
3. Call the `toBuilder()` method on the resulting object to [enrich it further](../../basic-operations/update/#enrich-before-updating).
4. Directly chain our enrichment methods to add a classification.
5. Note that this `classification` method can be chained even to itself, to add multiple classifications.
6. Call the `build()` method to build the enriched object. (No need to assign it back to an original object, as we've chained all the operations together!)
6. Use the `toUpdate()` method to initialize the object for another asset.
7. Chain the `toBuilder()`, enrichment and `build()` methods like we did for the previous asset.
8. Build the result back into the list again, like we did for the previous asset.
9. Optimize our update by bundling all the assets into a single API call.

	!!! warning "Replace classifications"
		Note that we also send `true` as the second parameter to the upsert, to *overwrite* any existing classifications on these assets with the ones we have provided. (Without this, the classifications we send will be ignored.)

10. The response will include all *n* assets that were updated.

???+ warning "Be aware of how much you're updating per request"
	While this is great for reducing the number of API calls for better performance, do be aware of how many objects you're trying to update per request. There will be a limit beyond which you are trying to send too much information through a single API call and you could see other impacts such as failed requests due to network timeouts.

## Remove classifications existing assets

### Removing some, but not all

To remove some classifications from existing assets, you need to provide the classifications you want to keep in your object:

```java linenums="1" title="Remove all classifications from an existing asset"
final String termGuid = "b4113341-251b-4adc-81fb-2420501c30e6";
GlossaryTerm term = GlossaryTerm
		.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
				  "Example Term",
				  termGuid)
		.toBuilder()
		.classification(Classification.of("PII", termGuid)) // (2)
		.build();
EntityMutationResponse response = term.upsert(true, false); // (3)
response.getUpdatedEntities().size() == 1; // (4)
```

1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../basic-operations/update/#build-minimal-object-needed). (Removing the classifications is still an update to the asset, we are not deleting the asset itself.)
2. Enrich the object with the classification(s) you want to *keep* on the asset.
3. Call the `upsert()` method to actually update the asset, using `true` as the second argument to overwrite classifications. This will *replace* the existing classifications on the asset with the limited ones we have enriched on the object. (In other words, it will remove any of the classifications from the asset that we did *not* include in our enrichment.)
4. The response will include that single asset that was updated (again, removing classifications is an update to the asset — we are not deleting the asset itself).

### Removing all classifications

To remove all classifications from an existing asset, you need to specify no classifications in your object:

```java linenums="1" title="Remove all classifications from an existing asset"
GlossaryTerm term = GlossaryTerm
		.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
				  "Example Term",
				  "b4113341-251b-4adc-81fb-2420501c30e6");
EntityMutationResponse response = term.upsert(true, false); // (2)
response.getUpdatedEntities().size() == 1; // (3)
```

1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../basic-operations/update/#build-minimal-object-needed). (Removing the classifications is still an update to the asset, we are not deleting the asset itself.)
2. Call the `upsert()` method to actually update the asset, using `true` as the second argument to overwrite classifications. Since we have not provided any classifications in our object, this will *replace* the existing classifications on the asset with no classifications. (In other words, it will remove all classifications from the asset.)
3. The response will include that single asset that was updated (again, removing classifications is an update to the asset — we are not deleting the asset itself).
