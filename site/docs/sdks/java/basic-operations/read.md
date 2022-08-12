
# Retrieving an asset through the Java SDK

Retrieving an asset uses a slightly different pattern from the other operations. For this we use static methods provided by the `Entity` class:

## By GUID

To retrieve an asset using its GUID, we only need to pass that GUID.

```java linenums="1" title="Retrieve an asset by its GUID"
Entity read = Entity
				.retrieveFull("b4113341-251b-4adc-81fb-2420501c30e6"); // (1)
Glossary glossary;
if (read instanceof Glossary) {
	glossary = (Glossary) read; // (2)
}
```

1. Retrieve the asset by its GUID. Since GUIDs are globally unique, we do not need to specify a type. (And this is why the operation returns a generic `Entity`, since the SDK can only determine the type at runtime, once it has a response back from Atlan.)
2. Since the operation returns a generic `Entity`, we need to check and cast it to a more specific type if we want to access the more specific attributes of that type.

## By `qualifiedName`

To retrieve an asset using its `qualifiedName`, we need to pass both the type of asset and its `qualifiedName`:

```java linenums="1" title="Retrieve an asset by its qualifiedName"
Entity read = Entity
				.retrieveFull(Glossary.TYPE_NAME, // (1)
							  "FzCMyPR2LxkPFgr8eNGrq"); // (2)
Glossary glossary;
if (read instanceof Glossary) {
	glossary = (Glossary) read; // (3)
}
```

1. The type of the asset to retrieve. Note that each object in the SDK provides a static `TYPE_NAME` member, so you do not need to remember the exact type names in Atlan.
2. The `qualifiedName` of the asset, which must match exactly (case-sensitive). Note that for some assets (like glossaries), this may be a strange-looking Atlan-internal string.
2. Since the operation returns a generic `Entity`, we need to check and cast it to a more specific type if we want to access the more specific attributes of that type.

## Full vs minimal assets

The examples above illustrate how to use the `retrieveFull()` method. `Entity` also exposes a `retrieveMinimal()` method for each of these scenarios. (To use it, you'd only need to replace the `retrieveFull` on line 2 of the examples wtih `retrieveMinimal` — the parameters and response object are identical.)

- `retrieveFull()` retrieves the details of the entity along with all of its relationships
- `retrieveMinimal()` retrieves only the details of the entity, without any of its relationships

!!! recommendation "Use `retrieveMinimal()` where possible"
	You should use `retrieveMinimal()` for better performance in cases where you do not need *all* of the relationships of the entity. Be aware, though, that both still return `Entity` objects that can be cast to more specific types. The relationships will not be present in the object in Java after doing a `retrieveMinimal()`, but this does *not* mean that there are no relationships on that asset (in Atlan).
