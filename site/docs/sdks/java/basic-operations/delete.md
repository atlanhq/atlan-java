---
icon: fontawesome/brands-java
---

# Deleting an asset through the Java SDK

Deleting an [asset](/concepts/assets) uses a similar pattern to the retrieval operations. For this we use static methods provided by the `Entity` class:

## Soft-delete an asset

Soft-deletes are a reversible operation. The status of the [asset](/concepts/assets) is changed to `DELETED` and it no longer appears in the UI, but the [asset](/concepts/assets) is still present in Atlan's back-end.

To soft-delete an [asset](/concepts/assets), we only need to provide the GUID:

```java linenums="1" title="Soft-delete an asset"
EntityMutationResponse response =
		Entity.delete("b4113341-251b-4adc-81fb-2420501c30e6"); // (1)
Entity deleted = response.getDeletedEntities().get(0); // (2)
Glossary glossary;
if (deleted instanceof Glossary) {
	glossary = (Glossary) deleted; // (3)
}
```

1. The `delete()` method returns the deleted form of the [asset](/concepts/assets).
2. You can distinguish what was deleted through the `getDeletedEntities()` method. This lists only the entities deleted by the operation.
3. The `Entity` class is a superclass of all [assets](/concepts/assets). So we need to cast to more specific types (like `Glossary`) after verifying the object that was actually returned.

## Hard-delete an asset

Hard-deletes (also called a *purge*) are irreversible operations. The [asset](/concepts/assets) is removed from Atlan entirely, so no longer appears in the UI and also no longer exists in Atlan's back-end.

To hard-delete (purge) an [asset](/concepts/assets), we only need to provide the GUID:

```java linenums="1" title="Hard-delete (purge) an asset"
EntityMutationResponse response =
		Entity.purge("b4113341-251b-4adc-81fb-2420501c30e6"); // (1)
Entity deleted = response.getDeletedEntities().get(0); // (2)
Glossary glossary;
if (deleted instanceof Glossary) {
	glossary = (Glossary) deleted; // (3)
}
```

1. The `purge()` method returns the purged form of the [asset](/concepts/assets).
2. You can distinguish what was purged through the `getDeletedEntities()` method. This lists only the entities deleted by the operation.
3. The `Entity` class is a superclass of all [assets](/concepts/assets). So we need to cast to more specific types (like `Glossary`) after verifying the object that was actually returned.

!!! warning "You can only purge active assets"
	Currently you can only purge (hard-delete) [assets](/concepts/assets) that are in an active state in Atlan. [Assets](/concepts/assets) that have been soft-deleted cannot be purged.
