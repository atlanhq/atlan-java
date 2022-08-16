
# Link terms and assets

## Link assets to a term

To link [assets](/concepts/assets) to a term:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Link assets to a term"
	GlossaryTerm term = GlossaryTerm
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6");
			.toBuilder() // (2)
			.assignedEntity(Reference.to(Column.TYPE_NAME, "47a40a54-9f3f-4e96-85a9-0ef62ef1ba9f")) // (3)
			.assignedEntity(Reference.to(Column.TYPE_NAME, "e0747784-c799-4055-a4c0-df465f3856f6"))
			.build(); // (4)
	EntityMutationResponse response = term.upsert(); // (5)
	response.getCreatedEntities().size() == 3 // (6)
	```
	
	1. Use the `toUpdate()` method to initialize the term with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed).
	2. Call the `toBuilder()` method on the resulting term to [enrich it further](../../java/basic-operations/update/#enrich-before-updating).
	3. Directly chain our enrichment methods to add the linked [assets](/concepts/assets) onto the `toBuilder()` method's result.
	4. Call the `build()` method to build the enriched term.
	5. Call the `upsert()` method to actually update the term with these linked [assets](/concepts/assets) (assigned entities).
	6. The response will include that single term that was updated along with the two linked assets that were updated (3 total updates).

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Link terms to an asset

You can also link the two in the other direction. To link terms to an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Link terms to an asset"
	Column column = Column
			.toUpdate("default/snowflake/1657037873/SAMPLE_DATA/FOOD_BEVERAGE/ORDER_ANALYSIS/CUSTOMER_NAME",
					  "CUSTOMER_NAME") // (1)
			.toBuilder() // (2)
			.meaning(Reference.to(GlossaryTerm.TYPE_NAME, "b4113341-251b-4adc-81fb-2420501c30e6")) // (3)
			.meaning(Reference.to(GlossaryTerm.TYPE_NAME, "b267858d-8316-4c41-a56a-6e9b840cef4a"))
			.build(); // (4)
	EntityMutationResponse response = column.upsert(); // (5)
	response.getUpdatedEntities().size() == 3 // (6)
	```
	
	1. Use the `toUpdate()` method to initialize the [asset](/concepts/assets) with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed).
	2. Call the `toBuilder()` method on the resulting [asset](/concepts/assets) to [enrich it further](../../java/basic-operations/update/#enrich-before-updating).
	3. Directly chain our enrichment methods to add the linked terms onto the `toBuilder()` method's result.
	4. Call the `build()` method to build the enriched [asset](/concepts/assets).
	5. Call the `upsert()` method to actually update the [asset](/concepts/assets) with these linked terms (meanings).
	6. The response will include that single [asset](/concepts/assets) that was updated along with the two linked terms that were updated (3 total updates).

=== ":material-language-python: Python"

	!!! construction "Coming soon"

!!! recommendation "You only need to link in one direction or the other"
	[Relationships](/concepts/relationships) in Atlan are always set up in both directions, regardless of which direction you use to add them. So there is no need to do *both* directions for the same asset and term linkages. Use whichever is easiest for your scenario, and the reverse relationship is automatically set up as well.

## Remove terms from an asset

To remove terms from an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove terms from an asset"
	Column column = Column
			.toUpdate("default/snowflake/1657037873/SAMPLE_DATA/FOOD_BEVERAGE/ORDER_ANALYSIS/CUSTOMER_NAME",
					  "CUSTOMER_NAME") // (1)
	column.removeMeanings(); // (2)
	EntityMutationResponse response = term.upsert(); // (3)
	response.getUpdatedEntities().size() == 3; // (4)
	```
	
	1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the terms is still an update to the [asset](/concepts/assets), we are not deleting the [asset](/concepts/assets) itself.)
	2. Call the `removeMeanings()` method on the built object to set it up for removing the linked terms when sent across to Atlan.
	3. Call the `upsert()` method to actually update the [asset](/concepts/assets) (remove its linked terms).
	4. The response will include that single [asset](/concepts/assets) that was updated and the two linked terms that were updated (again, removing linked terms is an update to the [asset](/concepts/assets) and terms — we are not deleting the [entities](/concepts/entities) themselves).

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Remove assets from a term

To remove terms from an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove assets from a term"
	GlossaryTerm term = GlossaryTerm
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6");
	term.removeAssignedEntities(); // (2)
	EntityMutationResponse response = term.upsert(); // (3)
	response.getUpdatedEntities().size() == 3; // (4)
	```
	
	1. Use the `toUpdate()` method to initialize the term with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the linked assets is still an update to the term, we are not deleting the term or the [assets](/concepts/assets) themselves.)
	2. Call the `removeAssignedEntities()` method on the built object to set it up for removing the linked [assets](/concepts/assets) (assigned entities) when sent across to Atlan.
	3. Call the `upsert()` method to actually update the term (remove its linked [assets](/concepts/assets)).
	4. The response will include that single term that was updated and the two linked [assets](/concepts/assets) that were updated (again, removing linked [assets](/concepts/assets) is an update to the term and [assets](/concepts/assets) — we are not deleting any of the [entities](/concepts/entities) themselves).

=== ":material-language-python: Python"

	!!! construction "Coming soon"

!!! recommendation "You only need to unlink in one direction or the other"
	As with the linking, since [relationships](/concepts/relationships) in Atlan are always set up in both directions you only need to remove the links from one direction. Use whichever is easiest for your scenario, and the reverse relationship is automatically removed as well.
