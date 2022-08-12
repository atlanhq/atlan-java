
# Updating an asset through the Java SDK

All objects in the SDK that you can update within Atlan implement the builder pattern. This allows you to progressively build-up the object you want to update. In addition, each object provides a `toUpdate()` method that takes the minimal set of required fields to *update* that asset, when it already exists in Atlan.

## Build minimal object needed

For example, to update a glossary term we need to provide the `qualifiedName` and name of the term, and the GUID of the glossary in which it exists:

```java linenums="1" title="Build minimal asset necessary for update"
GlossaryTerm term = GlossaryTerm
					.toUpdate("gsNccqJraDZqM6WyGP3ea@FzCMyPR2LxkPFgr8eNGrq", // (1)
						      "Example Term", // (2)
							  "b4113341-251b-4adc-81fb-2420501c30e6"); // (3)
```

1. The `qualifiedName` of the existing term, which must match exactly (case-sensitive). Note that for some assets (like terms), this may be a strange-looking Atlan-internal string.
2. The name of the existing term. This must match exactly (case-sensitive).
3. The GUID of the glossary in which the term exists.

## Enrich before updating

The `term` object now has the minimal required information for Atlan to update it. Without any additional enrichment, though, there isn't really anything to update...

So first you should enrich the object:

```java linenums="5" title="Enrich the asset before updating it"
term = term.toBuilder() // (1)
		.certificateStatus(AtlanCertificateStatus.VERIFIED) // (2)
		.announcementType(AtlanAnnouncementType.INFORMATION)
		.announcementTitle("Imported");
		.announcementMessage("This term was imported from ...")
		.build(); // (3)
```

1. The `toBuilder()` method can be called on any object to create a chainable builder for further enriching the object.
2. In this example, we're adding a certificate and announcement to the object.
3. To persist the enrichment back to the object, we must `build()` the builder.

	!!! warning "Assign the result back"
		Remember to assign the result of the `build()` operation back to your original object. Otherwise the result is not persisted back into any variable! (In this case we're assigning to the `term` variable back on line 5.)

When enriching the object for update, you only need to specify the information you want to change. Any information you do not include in your update will be left untouched on the asset in Atlan. This way you do not need to try to reproduce the complete asset in your request to do targeted updates to specific attributes.

??? details "More information"
	Note that this does create some challenges. For example, what if you *want* to remove some information that already exists on the asset in Atlan? To do that, we provide helper methods that inject the correct content into your object to handle removals. For example, `removeCertificate()` can be used to remove any existing certificate details from an asset.

	These helper methods should *always* be called on the built object itself (not the builder), and only as the final step (after all other enrichment has been done to the object).

## Update the asset from the object

You can then actually update the object in Atlan by calling the `upsert()` method on the object itself:

```java linenums="11" title="Create the asset"
EntityMutationResponse response = term.upsert(); // (1)
Entity updated = response.getUpdatedEntities().get(0); // (2)
GlossaryTerm term;
if (updated instanceof GlossaryTerm) {
	term = (GlossaryTerm) updated; // (3)
}
```

1. The `upsert()` method will either:

	- Update an existing entity, if Atlan already has a term with the same name and `qualifiedName` in the same glossary.
	- Create a new entity, if Atlan does not have a term with the same name in the same glossary.

2. You can distinguish what was created or updated:

	- `getCreatedEntities()` lists entities that were created
	- `getUpdatedEntities()` lists entities that were updated

	Note that the `upsert()` method always returns objects of type `Entity`, though.

3. The `Entity` class is a superclass of all assets. So we need to cast to more specific types (like `GlossaryTerm`) after verifying the object that was actually returned.

4. In this example, creating the `GlossaryTerm` actually also updates the parent `Glossary`. This is why the `response` contains generic `Entity` objects rather than specific types — any operation could side-effect a number of different assets.

5. Like with the `GlossaryTerm`, we can check and cast the generic `Entity` returned by the response into its more specific type (`Glossary`).

!!! warning "Case-sensitive, exact match"
	If you use a different capitalization or spelling for the name or `qualifiedName`, you may accidentally *create* a new asset rather than updating the existing one.
