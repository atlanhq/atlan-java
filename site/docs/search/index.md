
# Introduction to search attributes

These sections introduce all the attributes that can be searched in Atlan.

## How it's organized

- The specific attributes available on an entity will vary, by entity type.
- So each section represents types of entity that have common attributes in Atlan.

You can also use the search bar in the upper right to search for any attribute of interest. This is a full-text search across the entire reference site, so it will find:

- The names of search attributes in queries (requests)
- The names of attributes in search results (responses)
- Any descriptions about those attributes, their nuances, and so on

## Details about each attribute

- Each attribute has a heading giving the text you should use in a query (request).
- This is followed by a brief description of that attribute's meaning and usage.

???+ details "Details"
	Attributes that have particular nuances in their behavior will also have a collapsible section like this one. By default, these sections are collapsed.

And then there are tabbed examples of:

=== "Requests"

	```json linenums="1" title="Annotated request"
	{
	  "dsl": {
	    "query": { // (1)
	      "term": { "__guid": "25638e8c-0225-46fd-a70c-304117370c4c" } // (2)
	    }
	  },
	  "attributes": [ "__guid" ] // (3)
	}
	```

	1. Only the attribute being discussed appears in the query. You could of course combine this with many other conditions in a more complex query.
	2. Only the simplest form of query is shown. But of course you could use other Elastic mechanisms (match, prefix, etc) depending on your needs.
	3. Only the attribute being discussed appears in the attributes list. You could of course list any number of other attributes as well.

=== "Responses"

	```json linenums="1" title="Annotated response"
	{
	  "entities": [
	    { // (1)
	      "attributes": {
	        "__guid": "25638e8c-0225-46fd-a70c-304117370c4c" // (2)
	      },
	      "guid": "25638e8c-0225-46fd-a70c-304117370c4c" // (3)
	    }
	  ]
	}
	```

	1. Only the attribute being discussed appears in the result, for clarity. There will of course be many more attributes in an actual response.
	2. The value sent in the request's `attributes` list is included in the response.
	3. In case the `attributes` list in the request does not include the attribute being discussed, any default attribute that would appear in the response that represents the same information is also shown.

We use the following conventions for these requests and responses:

| Icon | Name | Description |
|---|---|---|
| :material-tag: | (exact) | These illustrate using attributes that have an Elastic `keyword` index. In these cases, the search is case-insensitive but otherwise looks for exact values — even if there are spaces in the string you're using for the search. |
| :material-tag-text: | (analyzed) | These illustrate using attributes that have an Elastic `text` index. In these cases, the case is not only case-insensitive, but also pre-processed for fuzzy matching. For example, spaces in the string you're using for the search create multiple terms that are individually used to look for results. |
| :material-calendar-clock: | | These illustrate using attributes that have an Elastic index on a timestamp (numeric). Rather than looking for exact matches on these attributes, you probably want to use range operations to look for greater than, less than, and so on. |
| :material-toggle-switch: | | These illustrate using attributes that have an Elastic index on a boolean value. You should only try to match values of `true` and `false` on these attributes. |