
# Manage announcements

## When creating an asset

To add an announcement when creating an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add announcement when creating asset"
	GlossaryTerm term = GlossaryTerm
			.toCreate("Example Term", // (1)
					  "b4113341-251b-4adc-81fb-2420501c30e6",
					  null);
	term = term.toBuilder() // (2)
			.announcementType(AtlanAnnouncementType.INFORMATION) // (3)
			.announcementTitle("New!") // (4)
			.announcementMessage("This term is newly created.") // (5)
			.build(); // (6)
	EntityMutationResponse response = term.upsert(); // (7)
	response.getCreatedEntities().size() == 1 // (8)
	```
	
	1. Use the `toCreate()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed).
	2. Call the `toBuilder()` method on the resulting object to [enrich it further](../../java/basic-operations/create/#optional-enrich-before-creating).
	3. Set the announcement that should be added (in this example, we're using `INFORMATION`).
	4. Add a title for the announcement.
	5. Add a message for the announcement.
	6. Call the `build()` method to build the enriched object. (Remember this needs to be assigned back to the original object — line 5!)
	7. Call the `upsert()` method to actually create the [asset](/concepts/assets) with this announcement.
	8. The response will include that single [asset](/concepts/assets) that was created.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Add to existing assets

You can also add an announcement to existing [assets](/concepts/assets). To illustrate a bit more flexibility, let's imagine we want to update the announcement on multiple assets at the same time:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add announcement to existing assets"
	List<Entity> assets = new ArrayList<>();
	assets.add(GlossaryTerm // (1)
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (2)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6")
			.toBuilder() // (3)
			.announcementType(AtlanAnnouncementType.WARNING) // (4)
			.announcementTitle("Caution!")
			.announcementMessage("This term was changed.")
			.build()); // (5)
	assets.add(GlossaryTerm
			.toUpdate("sduw38sCas83Ca8sdf982@FzCMyPR2LxkPFgr8eNGrq", // (6)
					  "Another Term",
					  "b267858d-8316-4c41-a56a-6e9b840cef4a")
			.toBuilder() // (7)
			.announcementType(AtlanAnnouncementType.WARNING)
			.announcementTitle("Caution!")
			.announcementMessage("This term was changed.")
			.build()); // (8)
	EntityMutationResponse response = EntityBulkEndpoint.upsert(assets, false, false); // (9)
	response.getUpdatedEntities().size() == 2 // (10)
	```
	
	1. Define our object directly into an element of a `List`, rather than managing a separate object.
	2. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed).
	3. Call the `toBuilder()` method on the resulting object to [enrich it further](../../java/basic-operations/update/#enrich-before-updating).
	4. Directly chain our enrichment methods to add the announcement details onto the `toBuilder()` method's result.
	5. Call the `build()` method to build the enriched object. (No need to assign it back to an original object, as we've chained all the operations 	together!)
	6. Use the `toUpdate()` method to initialize the object for another [asset](/concepts/assets).
	7. Chain the `toBuilder()`, enrichment and `build()` methods like we did for the previous [asset](/concepts/assets).
	8. Build the result back into the list again, like we did for the previous [asset](/concepts/assets).
	9. To optimize our update, we want to limit the number of API calls we make. If we called `upsert()` against each of *n* [assets](/concepts/assets) individually, we would 	have *n* API calls. Here we use the API endpoint directly with a list of [assets](/concepts/assets) — this makes 1 API call to update all *n* [assets](/concepts/assets) at the same time.
	10. The response will include all *n* [assets](/concepts/assets) that were updated.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

???+ warning "Be aware of how much you're updating per request"
	While this is great for reducing the number of API calls for better performance, do be aware of how many objects you're trying to update per request. There will be a limit beyond which you are trying to send too much information through a single API call and you could see other impacts such as failed requests due to network timeouts.

## Remove from an existing asset

To remove an announcement from an existing asset:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Remove announcement from existing asset"
	GlossaryTerm term = GlossaryTerm
			.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6");
	term.removeAnnouncement(); // (2)
	EntityMutationResponse response = term.upsert(); // (3)
	response.getUpdatedEntities().size() == 1; // (4)
	```
	
	1. Use the `toUpdate()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed). (Removing the announcement is still an update to the [asset](/concepts/assets), we are not deleting the asset itself.)
	2. Call the `removeAnnouncement()` method on the built object to set it up for removing the announcement details when sent across to Atlan.
	3. Call the `upsert()` method to actually update the [asset](/concepts/assets) (remove its announcement).
	4. The response will include that single [asset](/concepts/assets) that was updated (again, removing an announcement is an update to the [asset](/concepts/assets) — we are not deleting the [asset](/concepts/assets) itself).

=== ":material-language-python: Python"

	!!! construction "Coming soon"