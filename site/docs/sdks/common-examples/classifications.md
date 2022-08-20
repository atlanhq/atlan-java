
# Classify assets

!!! details "Cannot add classifications when creating assets"
	Currently it is not possible to add classifications when creating [assets](/concepts/assets).

## Add to an existing asset

To add classifications to an existing [asset](/concepts/assets), we've provided a helper method. This is probably the most common use case:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add classifications to an existing asset"
	Table.addClassifications( // (1)
		"default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS", // (2)
		List.of("PII", "Marketing Analysis")); // (3)
	```

	1. Use the `addClassifications()` helper method, which for most objects requires a minimal set of information. This helper method will construct the necessary request and call the necessary API(s) to add the classifications all-in-one.
	2. The `qualifiedName` of the asset.
	3. A list of the classifications (the names as you set them up in the UI) to add to the asset.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Remove from existing assets

### Remove a single classification

To remove a single classification from an existing [asset](/concepts/assets), we've provided another helper method:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove one classification from an existing asset"
	Table.removeClassification( // (1)
		"default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS", // (2)
		"Marketing Analysis"); // (3)
	```

	1. Use the `removeClassification()` helper method, which for most objects requires a minimal set of information. This helper method will construct the necessary request and call the necessary API(s) to remove a classification from an asset, all-in-one.
	2. The `qualifiedName` of the asset.
	3. The classification (the name you set up in the UI) to remove from the asset.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

### Remove all classifications

To remove all classifications from an existing [asset](/concepts/assets), you need to specify no classifications in your object:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove all classifications from an existing asset"
	Table table = Table.updater( // (1)
		"default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS",
		"TOP_BEVERAGE_USERS").build();
	EntityMutationResponse response = term.upsert(true, false); // (2)
	response.getUpdatedEntities().size() == 1; // (3)
	```
	
	1. Use the `updater()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the classifications is still an update to the [asset](/concepts/assets), we are not deleting the [asset](/concepts/assets) itself.)
	2. Call the `upsert()` method to actually update the [asset](/concepts/assets), using `true` as the first argument to overwrite classifications. Since we have not 	provided any classifications in our object, this will *replace* the existing classifications on the [asset](/concepts/assets) with no classifications. (In other words, it 	will remove all classifications from the [asset](/concepts/assets).)
	3. The response will include that single [asset](/concepts/assets) that was updated (again, removing classifications is an update to the [asset](/concepts/assets) — we are not deleting the [asset](/concepts/assets) itself).

=== ":material-language-python: Python"

	!!! construction "Coming soon"
