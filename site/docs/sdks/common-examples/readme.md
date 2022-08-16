
# Add asset READMEs

READMEs can only be added to [assets](/concepts/assets) after an asset exists. (The asset itself must be created first.)

!!! recommendation "README content is written in HTML"
	The content of a README needs to be HTML. The HTML should be everything that would be *inside* the `<body></body>` tags, but not include the `<body></body>` tags themselves. (So it should also exclude the outer `<html></html>` tags.)

## Add to an existing asset

Each README can be assigned to only a single [asset](/concepts/assets). To create a README and assign it to an [asset](/concepts/assets):

=== ":fontawesome-brands-java: Java"

	```java linenums="1" title="Add to an existing asset"
	final String readmeContent = "<h1>Overview</h1><p>Details about this term...</p>"; // (1)
	Readme readme = Readme 
			.toCreate(GlossaryTerm.TYPE_NAME, // (2)
				"b4113341-251b-4adc-81fb-2420501c30e6", // (3)
				"Example Term", // (4)
				readmeContent); // (5)
	EntityMutationResponse response = readme.upsert(); // (6)
	response.getCreatedEntities().size() == 1 // (7)
	response.getUpdatedEntities().size() == 1 // (8)
	```
	
	1. Pick up your HTML content from somewhere (here it is defined directly in the code).
	2. Use the `toCreate()` method to initialize the object with all [necessary attributes for creating it](../../java/basic-operations/create/#build-minimal-object-needed).
	3. In the case of a README, we need to give the GUID of the asset to which we want to attach the README.
	4. And the name of the asset to which we want to attach the README.
	5. And finally the content for the README itself (the HTML).
	6. Call the `upsert()` method to actually create the README and attach it to the [asset](/concepts/assets).
	7. The response will include that single [asset](/concepts/assets) that was created (the README).
	8. The response will also include a single [asset](/concepts/assets) that was updated (the asset to which we've attached the README).

=== ":material-language-python: Python"

	!!! construction "Coming soon"

## Remove from an existing asset

To remove a README from an existing [asset](/concepts/assets) you only need to delete the README itself. (The README is itself an asset.)

=== ":fontawesome-brands-java: Java"

	See [Deleting an asset through the Java SDK](../../java/basic-operations/delete).

	!!! warning "The README will have its own GUID, separate from the asset to which it is attached"
		When deleting the README, you need to use the README's GUID, not the GUID of the asset to which it is attached.

=== ":material-language-python: Python"

	See [Deleting an asset through the Python SDK](../../python/basic-operations/delete).

	!!! warning "The README will have its own GUID, separate from the asset to which it is attached"
		When deleting the README, you need to use the README's GUID, not the GUID of the asset to which it is attached.
