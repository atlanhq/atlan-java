
# Advanced examples

For most of the common examples already covered, there is also an approach you can use to:

- Combine multiple operations together
- Create or update multiple [assets](/concepts/assets) at the same time

## Combine multiple operations together

To combine multiple operations together, start with the result of either a `creator()` or `updater()` call. These both return a subclass of an `AssetBuilder<?,?>` object, which you can use to chain together various enrichments.

This is useful when you have many changes to make to an asset. Rather than making a separate API call for each change, with this approach you can make a single API call and include all the information within it.

For example, to create a term complete with a description, parent category, announcement, certificate, several owners, and several linked assets:

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add announcement when creating asset"
	GlossaryTerm term = GlossaryTerm
			.creator("Example Term", // (1)
					  "b4113341-251b-4adc-81fb-2420501c30e6",
					  null);
			.description("This is only an example.") // (2)
			.category(Reference.to(GlossaryCategory.TYPE_NAME, "1b736a83-207b-4269-ab92-44d77e1aca28")) // (3)
			.announcementType(AtlanAnnouncementType.INFORMATION) // (4)
			.announcementTitle("New!") // (5)
			.announcementMessage("This term is newly created.") // (6)
			.certificateStatus(AtlanCertificateStatus.VERIFIED) // (7)
			.certificateMessage("For good measure!") // (8)
			.ownerUser("jdoe") // (9)
			.ownerUser("jsmith") // (10)
			.assignedEntity(Reference.to(Column.TYPE_NAME, "1df0980d-b753-481a-8021-b97357b2a77d")) // (11)
			.assignedEntity(Reference.to(Column.TYPE_NAME, "6c1a2db3-8708-4220-a709-5fdc7577f028")) // (12)
			.build(); // (13)
	EntityMutationResponse response = term.upsert(); // (14)
	response.getCreatedEntities().size() == 1 // (15)
	response.getUpdatedEntities().size() == 3 // (16)
	```
	
	1. Use the `creator()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed). For a term, this is a name and the GUID of the glossary in which to create the term. (The final `null` is for a `qualifiedName` of the glossary, which could be used instead of the GUID.)
	2. Set a description for the term.
	3. Add a category for the term. (This category must already exist, or be created before to this operation.)
	4. Set the announcement that should be added (in this example, we're using `INFORMATION`).
	5. Add a title for the announcement.
	6. Add a message for the announcement.
	7. Set the certificate for the term (in this example, we're using `VERIFIED`).
	8. Add a message for the certificate.
	9. Add an owner.
	10. Add another owner. Note that we can just continue chaining single owners, we do not need to create our own list first.
	11. Add a linked asset (a column).
	12. Like with the owners, we can add further linked assets (another column) without needing to create our own list first.
	13. Call the `build()` method to build the enriched object.
	14. Call the `upsert()` method to actually create the term with all of these initial details.
	15. The response will include that single term that was created.
	16. The response will also include any related objects (the category and the 2 linked columns) that were updated by this term being related to them.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

!!! question "What are the available enrichments?"
	The list of methods that are available to enrich each type of asset in Atlan should be directly visible to you within your favorite IDE.

## Operate on multiple assets at the same time

The example above illustrates how to make many changes at one time to a single asset. But you may also want to make changes to many assets at the same time. After all, if you need to create or update many assets it will be more efficient to do this with fewer API calls than one API call per asset.

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add certificate to existing assets"
	List<Entity> assets = new ArrayList<>();
	assets.add(GlossaryTerm // (1)
			.updater("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (2)
					  "Example Term",
					  "b4113341-251b-4adc-81fb-2420501c30e6")
			.certificateStatus(AtlanCertificateStatus.DEPRECATED) // (3)
			.certificateStatusMessage("This asset should no longer be used.")
			.build()); // (4)
	assets.add(GlossaryTerm
			.updater("sduw38sCas83Ca8sdf982@FzCMyPR2LxkPFgr8eNGrq", // (5)
					  "Another Term",
					  "b267858d-8316-4c41-a56a-6e9b840cef4a")
			.certificateStatus(AtlanCertificateStatus.DEPRECATED)
			.certificateStatusMessage("This asset should no longer be used.")
			.build()); // (6)
	EntityMutationResponse response = EntityBulkEndpoint.upsert(assets, false, false); // (7)
	response.getUpdatedEntities().size() == 2 // (8)
	```
	
	1. Define our object directly into an element of a `List`, rather than managing a separate object.
	2. Use the `updater()` method to initialize the object with all [necessary attributes for updating it](../../java/basic-operations/update/#build-minimal-object-needed).
	3. Directly chain our enrichment methods to add the certificate and message onto the `updater()` method's result.
	4. Call the `build()` method to build the enriched object.
	5. Use the `updater()` method to initialize the object for another [asset](/concepts/assets).
	6. Chain the enrichment and `build()` methods like we did for the previous [asset](/concepts/assets).
	7. To optimize our update, we want to limit the number of API calls we make. If we called `upsert()` against each of *n* [assets](/concepts/assets) individually, we would 	have *n* API calls. Here we use the API endpoint directly with a list of [assets](/concepts/assets) — this makes 1 API call to update all *n* [assets](/concepts/assets) at the same time.
	8. The response will include all *n* [assets](/concepts/assets) that were updated.

=== ":material-language-python: Python"

	!!! construction "Coming soon"

And you can naturally combine these two approaches to both make many changes per asset, and do this against many assets at the same time — all in a single API call.

???+ warning "Be aware of how much you're updating per request"
	While this is great for reducing the number of API calls for better performance, do be aware of how many objects you're trying to update per request. There will be a limit beyond which you are trying to send too much information through a single API call and you could see other impacts such as failed requests due to network timeouts.
