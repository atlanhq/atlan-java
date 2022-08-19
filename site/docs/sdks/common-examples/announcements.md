
# Manage announcements

## Add to an existing asset

To add an announcement to an existing [asset](/concepts/assets), we've provided a helper method. This is probably the most common use case:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add announcement to existing assets"
	Table result = Table.updateAnnouncement( // (1)
						"default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS", // (2)
						AtlanAnnouncementType.WARNING, // (3)
						"Caution!", // (4)
						"This table was changed."); // (5)
	```

	1. Use the `updateAnnouncement()` helper method, which for most objects requires a minimal set of information. This helper method will construct the necessary request, call the necessary API(s), and return with the result of the update operation all-in-one.
	2. The `qualifiedName` of the object.
	3. The type of the announcement (the `AtlanAnnouncementType` enumeration gives the valid values).
	4. A title for the announcement.
	5. A message to include in the announcement.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## When creating an asset

To add an announcement when creating an [asset](/concepts/assets), you can chain the announcement details onto the [`creator()` result](../../java/basic-operations/create/#build-minimal-object-needed):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add announcement when creating asset"
	GlossaryTerm term = GlossaryTerm
			.creator("Example Term", // (1)
					  "b4113341-251b-4adc-81fb-2420501c30e6",
					  null);
			.announcementType(AtlanAnnouncementType.INFORMATION) // (2)
			.announcementTitle("New!") // (3)
			.announcementMessage("This term is newly created.") // (4)
			.build(); // (5)
	EntityMutationResponse response = term.upsert(); // (6)
	response.getCreatedEntities().size() == 1 // (7)
	```
	
	1. Use the `creator()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed).
	2. Set the announcement that should be added (in this example, we're using `INFORMATION`).
	3. Add a title for the announcement.
	4. Add a message for the announcement.
	5. Call the `build()` method to build the enriched object.
	6. Call the `upsert()` method to actually create the [asset](/concepts/assets) with this announcement.
	7. The response will include that single [asset](/concepts/assets) that was created.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Remove from an existing asset

To remove an announcement from an existing asset:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove announcement from existing asset"
	Column column = Column.
			.updater("default/snowflake/1657037873/SAMPLE_DB/FOOD_BEV/TOP_BEVERAGE_USERS/USER_ID", // (1)
					  "USER_ID");
	column.removeAnnouncement(); // (2)
	EntityMutationResponse response = column.upsert(); // (3)
	response.getUpdatedEntities().size() == 1; // (4)
	```

	1. Use the `updater()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the announcement is still an update to the [asset](/concepts/assets), we are not deleting the asset itself.)
	2. Call the `removeAnnouncement()` method on the built object to set it up for removing the announcement details when sent across to Atlan.
	3. Call the `upsert()` method to actually update the [asset](/concepts/assets) (remove its announcement).
	4. The response will include that single [asset](/concepts/assets) that was updated (again, removing an announcement is an update to the [asset](/concepts/assets) — we are not deleting the [asset](/concepts/assets) itself).

=== ":material-language-python: Python"

	!!! construction "Coming soon"
