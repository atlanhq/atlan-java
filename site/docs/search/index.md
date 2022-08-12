
# Introduction to search attributes

These sections introduce all the attributes that can be searched in Atlan.

## How it's organized

- The specific attributes available on an entity will vary, by entity type.
- So each section represents types of entity that have common attributes in Atlan.

You can also use the search bar in the upper right to search for any attribute of interest. This is a full-text search across the entire reference site, so it will find:

- The names of search attributes in queries (requests)
- The names of attributes in search results
- Any descriptions about those attributes, their nuances, and so on

## Details about each attribute

- Each attribute has a heading giving the text you should use in a query (request).
- This is followed by a brief description of that attribute's meaning and usage.

???+ details "Details"
	Attributes that have particular nuances in their behavior will also have a collapsible section like this one. By default, these sections are collapsed.

And then there are tabbed examples of:

=== "Requests"

	```
	With JSON example of using the attribute in a query.
	(These are trimmed to bare minimum queries to show
	off the attributes rather than Elastic's query DSL.)
	```

=== "Responses"

	```
	With JSON example of what the attribute looks like
	in results from the query. (These are also trimmed)
	to bare minimum result data to show off the relevant
	attributes in the results rather than all the
	attributes likely to appear on each result.)
	```

We use the following conventions for these requests and responses:

| Icon | Name | Description |
|---|---|---|
| :material-tag: | Exact | These illustrate using attributes that have an Elastic `keyword` index. In these cases, the search is case-insensitive but otherwise looks for exact values — even if there are spaces in the string you're using for the search. |
| :material-tag-text: | Analyzed | These illustrate using attributes that have an Elastic `text` index. In these cases, the case is not only case-insensitive, but also pre-processed for fuzzy matching. For example, spaces in the string you're using for the search create multiple terms that are individually used to look for results. |
| :material-calendar-clock: | | These illustrate using attributes that have an Elastic index on a timestamp (numeric). Rather than looking for exact matches on these attributes, you probably want to use range operations to look for greater than, less than, and so on. |
| ::material-toggle-switch: | | These illustrate using attributes that have an Elastic index on a boolean value. You should only try to match values of `true` and `false` on these attributes. |