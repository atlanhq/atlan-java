---
icon: fontawesome/brands-java
---

# Creating an asset through the Java SDK

All objects in the SDK that you can create within Atlan implement the builder pattern. This allows you to progressively build-up the object you want to create. In addition, each object provides a `toCreate()` method that takes the minimal set of required fields to create that [asset](/concepts/assets).

## Build minimal object needed

For example, to create a glossary term we need to provide the name of the term and either the GUID or `qualifiedName` of the glossary in which to create the term:

```java linenums="1" title="Build minimal asset necessary for creation"
GlossaryTerm term = GlossaryTerm
		.toCreate("Example Term", // (1)
				  "b4113341-251b-4adc-81fb-2420501c30e6", // (2)
				  null); // (3)
```

1. A name for the new term
2. The GUID of the glossary in which to create the term
3. Since we've provided the glossary GUID, the `qualifiedName` for the glossary can be `null`

## Create the asset from the object

This `term` object now has the minimal required information for Atlan to create it.
You can then actually create the object in Atlan by calling the `upsert()` method on the object itself:

```java linenums="5" title="Create the asset"
EntityMutationResponse response = term.upsert(); // (1)
Entity created = response.getCreatedEntities().get(0); // (2)
GlossaryTerm term;
if (created instanceof GlossaryTerm) {
	term = (GlossaryTerm) created; // (3)
}
Entity updated = response.getUpdatedEntities().get(0); // (4)
Glossary glossary;
if (updated instanceof Glossary) {
	glossary = (Glossary) updated; // (5)
}
```

1. The `upsert()` method will either:

	- create a new entity, if Atlan does not have a term with the same name in the same glossary
	- update an existing entity, if Atlan already has a term with the same name in the same glossary

2. You can distinguish what was created or updated:

	- `getCreatedEntities()` lists entities that were created
	- `getUpdatedEntities()` lists entities that were updated

	Note that the `upsert()` method always returns objects of type `Entity`, though.

3. The `Entity` class is a superclass of all [assets](/concepts/assets). So we need to cast to more specific types (like `GlossaryTerm`) after verifying the object that was actually returned.

4. In this example, creating the `GlossaryTerm` actually also updates the parent `Glossary`. This is why the `response` contains generic `Entity` objects rather than specific types — any operation could side-effect a number of different [assets](/concepts/assets).

5. Like with the `GlossaryTerm`, we can check and cast the generic `Entity` returned by the response into its more specific type (`Glossary`).

## (Optional) Enrich before creating

If you want to further enrich the [asset](/concepts/assets) before creating it, you can do this using the builder pattern:

```java linenums="5" title="Alternatively, further enrich the asset before creating it"
term = term.toBuilder() // (1)
		.certificateStatus(AtlanCertificateStatus.VERIFIED) // (2)
		.announcementType(AtlanAnnouncementType.INFORMATION)
		.announcementTitle("Imported");
		.announcementMessage("This term was imported from ...")
		.build(); // (3)
EntityMutationResponse response = term.upsert(); // (4)
```

1. The `toBuilder()` method can be called on any object to create a chainable builder for further enriching the object.
2. In this example, we're adding a certificate and announcement to the object.
3. To persist the enrichment back to the object, we must `build()` the builder.
4. We can call the `upsert()` operation against this enriched object, the same as we showed earlier.

!!! warning "Assign the result back"
	Remember to assign the result of the `build()` operation back to your original object. Otherwise the result is not persisted back into any variable! (In this case we're assigning to the `term` variable back on line 5.)
