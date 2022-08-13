
# Change custom metadata on assets

## When creating an asset

To add custom metadata when creating an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add custom metadata when creating asset"
	CustomMetadata cm = CustomMetadata.builder() // (1)
			.withAttribute("RACI", "Responsible", "jsmith") // (2)
			.withAttribute("RACI", "Accountable", "jdoe")
			.withAttribute("RACI", "Consulted", List.of("finance", "risk")) // (3)
			.withAttribute("RACI", "Informed", List.of("operations"))
	GlossaryTerm term = GlossaryTerm
			.toCreate("Example Term", // (4)
					  "b4113341-251b-4adc-81fb-2420501c30e6",
					  null);
	term = term.toBuilder() // (5)
			.customMetadata(cm) // (6)
			.build(); // (7)
	EntityMutationResponse response = term.upsert(false, true); // (8)
	response.getCreatedEntities().size() == 1 // (9)
	```
	
	1. Create a custom metadata object that will contain all the custom metadata you want to add to the [asset](/concepts/assets).
	2. For each attribute, use the `withAttribute()` method and pass:

		1. the name of the custom metadata set
		2. the name of the attribute within that set
		3. the value for that attribute

		The value can be any object valid for the attribute: a string, a boolean, or a number. (Note that dates are sent as `long` (epoch) numbers.)

	3. For any attribute that can be multi-valued, we can send a list of values.
	4. Use the `toCreate()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed).
	5. Call the `toBuilder()` method on the resulting object to [enrich it further](../../java/basic-operations/create/#optional-enrich-before-creating).
	6. Set the custom metadata that should be added (using the custom metadata object you built earlier).
	7. Call the `build()` method to build the enriched object. (Remember this needs to be assigned back to the original object — line 10!)
	8. Call the `upsert()` method to actually create the [asset](/concepts/assets) with this custom metadata. Note that we send a `true` for the second argument to ensure that we update the custom metadata in Atlan.
	9. The response will include that single [asset](/concepts/assets) that was created.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Add to existing assets

### Individually

You can also add custom metadata to existing [assets](/concepts/assets). If you do this individually, you can selectively update individual sets of custom metadata:

=== ":fontawesome-brands-java: Java"

	!!! construction "Coming soon"

=== ":material-language-python: Python"

	!!! construction "Coming soon"

### In bulk

You can also add custom metadata to many existing [assets](/concepts/assets) at the same time.

!!! warning "Replaces any existing custom metadata"
	This bulk approach will replace all existing custom metadata (across all attributes) on the [assets](/concepts/assets). If you have only a few custom metadata attributes defined in the update, this will remove any other custom metadata attributes that are already set on the [assets](/concepts/assets) within Atlan.

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add custom metadata to existing assets"
	CustomMetadata cm = CustomMetadata.builder() // (1)
			.withAttribute("RACI", "Responsible", "jsmith")
			.withAttribute("RACI", "Accountable", "jdoe")
			.withAttribute("RACI", "Consulted", List.of("finance", "risk"))
			.withAttribute("RACI", "Informed", List.of("operations"))
	List<Entity> assets = new ArrayList<>();
	assets.add(GlossaryTerm // (2)
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (3)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6")
			.toBuilder() // (4)
			.customMetadata(cm) // (5)
			.build()); // (6)
	assets.add(GlossaryTerm
			.toUpdate("sduw38sCas83Ca8sdf982@FzCMyPR2LxkPFgr8eNGrq", // (7)
					  "Another Term",
					  "b267858d-8316-4c41-a56a-6e9b840cef4a")
			.toBuilder() // (8)
			.customMetadata(cm)
			.build());
	EntityMutationResponse response = EntityBulkEndpoint.upsert(assets, false, true); // (9)
	response.getUpdatedEntities().size() == 2 // (10)
	```
	
	1. Create the custom metadata object that will contain all the custom metadata you want to add to the [assets](/concepts/assets).
	2. Define your object directly into an element of a `List`, rather than managing a separate object.
	3. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed).
	4. Call the `toBuilder()` method on the resulting object to [enrich it further](../../java/basic-operations/update/#enrich-before-updating).
	5. Directly chain the custom metadata onto the `toBuilder()` method's result.
	6. Call the `build()` method to build the enriched object. (No need to assign it back to an original object, as we've chained all the operations 	together!)
	7. Use the `toUpdate()` method to initialize the object for another [asset](/concepts/assets).
	8. Chain the `toBuilder()`, enrichment and `build()` methods like we did for the previous [asset](/concepts/assets).
	9. To optimize your update, you want to limit the number of API calls we make. If you called `upsert()` against each of *n* [assets](/concepts/assets) individually, you would have *n* API calls. Here you use the API endpoint directly with a list of [assets](/concepts/assets) — this makes 1 API call to update all *n* [assets](/concepts/assets) at the same time.
	10. The response will include all *n* [assets](/concepts/assets) that were updated.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

???+ warning "Be aware of how much you're updating per request"
	While this is great for reducing the number of API calls for better performance, do be aware of how many objects you're trying to update per request. There will be a limit beyond which you are trying to send too much information through a single API call and you could see other impacts such as failed requests due to network timeouts.

## Remove from an existing asset

To remove a custom metadata from an existing [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove all custom metadata from an existing asset"
	GlossaryTerm term = GlossaryTerm
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6");
	EntityMutationResponse response = term.upsert(false, true); // (2)
	response.getUpdatedEntities().size() == 1; // (3)
	```
	
	1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the custom metadata is still an update to the [asset](/concepts/assets), we are not deleting the [asset](/concepts/assets) itself.)
	2. Call the `upsert()` method to actually update the [asset](/concepts/assets), using `true` as the second argument to overwrite custom metadata. Since we have not provided any custom metadata in our object, this will *replace* the existing custom metadata on the [asset](/concepts/assets) with no custom metadata. (In other words, it will remove all custom metadata from the [asset](/concepts/assets).)
	3. The response will include that single [asset](/concepts/assets) that was updated (again, removing custom metadata is an update to the [asset](/concepts/assets) — we are not deleting the [asset](/concepts/assets) itself).

=== ":material-language-python: Python"

	!!! construction "Coming soon"
