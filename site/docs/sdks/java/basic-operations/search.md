---
icon: fontawesome/brands-java
---

# Searching for assets using the Java SDK

Searching is a very flexible operation in Atlan. This also makes it more complex to understand than the other operations. To encapsulate the full flexibility of Atlan's search, the SDK provides a dedicated `IndexSearchRequest` object.

## Build the query

Atlan uses ElasticSearch to power its search. For ultimate flexibility, we have also adopted [Elastic's query DSL :material-dock-window:](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html){ target=es }.

So to run a search in Atlan, you need to define the query using Elastic's structures. For example, to find all active terms we could build up a query like the following:

```java linenums="1" title="Build the query"
Query byState = MatchQuery.of(m -> m.
					field("__state")
					.query("ACTIVE"))
				._toQuery(); // (1)

Query byType = MatchQuery.of(m -> m.
					field("__typeName.keyword")
					.query(GlossaryTerm.TYPE_NAME))
				._toQuery(); // (2)

Query combined = BoolQuery.of(b -> b
					.must(byState)
					.must(byType))
				._toQuery(); // (3)
```

1. Create an Elastic query that matches the `__state` of an [asset](/concepts/assets) being `ACTIVE`.
2. Create an Elastic query that matches the `__typeName` of an [asset](/concepts/assets) being a glossary term.
3. Create an Elastic query that combines the first two queries, asserting that both conditions must be met.

## Build the request

Once the query is defined, we can then build up the search request. The request includes not only the query, but also parameters like paging and which attributes to include in the response:

```java linenums="15" title="Build the request"
IndexSearchRequest index = IndexSearchRequest.builder()
        .dsl(IndexSearchDSL.builder()
                .from(0) // (1)
                .size(100) // (2)
                .query(combined) // (3)
                .build())
        .attributes(Collections.singletonList("anchor")) // (4)
        .relationAttributes(Collections.singletonList("certificateStatus")) // (5)
        .build(); // (6)
```

1. The starting point for paging of results.
2. The number of results to include (per page).
3. The query to run, that we built-up in the previous step.
4. The list of attributes to include in each result. In this case we will return the `anchor` attribute for terms, which gives the [relationship](/concepts/relationships) from the term to its parent glossary.
5. The list of attributes to include on each [relationship](/concepts/relationships) that is included in each result. Since we are returning `anchor` [relationships](/concepts/relationships), this will ensure that the `certificateStatus` of those related glossaries is also included in each result.
6. Since we're progressively building the request up, we must remember to `build()` it into the request object as the final step.

## Run the search

To now run the search, we call the `search()` method against our request object:

```java linenums="24" title="Run the search"
IndexSearchResponse response = index.search();
log.info(response.getApproximateCount()); // (1)
List<Entity> results = response.getEntities(); // (2)
for (Entity result : results) { // (3)
	if (result instanceof GlossaryTerm) {
		GlossaryTerm term = (GlossaryTerm) result; // (4)
	}
}
```

1. The `getApproximateCount()` method gives the total number of results overall (not resticted by page).
2. The results themselves can be accessed through the `getEntities()` method on the response.
3. You can then iterate through these results.
4. Remember that each result is a generic `Entity`. In our example we searched for a specific type, but another example may search for any [asset](/concepts/assets) with a given name (or classification) — so each result could be a different type. So again we should check and cast the results as-needed.

!!! recommendation "Details of searchable attributes"
	The [Search attributes](/search/) section of this reference material provides details on all attributes that can be searched in Atlan.
