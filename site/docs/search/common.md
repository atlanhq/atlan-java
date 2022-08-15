
# Common attributes

These attributes exist on *all* entities in Atlan. You can therefore use them to search *all* entities in Atlan.

## `__guid`

The globally unique identifier (GUID) of any object in Atlan.

The identifier has no meaning, and is randomly generated, but is guaranteed to uniquely identify only a single entity.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "term": { "__guid": "25638e8c-0225-46fd-a70c-304117370c4c" }
        }
      },
      "attributes": [ "__guid" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__guid": "25638e8c-0225-46fd-a70c-304117370c4c"
          },
          "guid": "25638e8c-0225-46fd-a70c-304117370c4c"
        }
      ]
    }
    ```

## `__createdBy`

The Atlan user who created this entity.

If created via API, this will be a unique identifier for the API token used. Otherwise, this will be the username of the user that created the entity through the Atlan UI.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "term": { "__createdBy": "jdoe" }
        }
      },
      "attributes": [ "__createdBy" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__createdBy": "jdoe"
          },
          "createdBy": "jdoe"
        }
      ]
    }
    ```

## `__modifiedBy`

The Atlan user who last updated the entity.

If updated via API, this will be a unique identifier for the API token used. Otherwise, this will be the username of the user that made the change through the Atlan UI.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "term": { "__modifiedBy": "jdoe" }
        }
      },
      "attributes": [ "__modifiedBy" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__modifiedBy": "jdoe"
          },
          "updatedBy": "jdoe"
        }
      ]
    }
    ```

## `__timestamp`

The time (in milliseconds) when the entity was created.

This is stored as an epoch: the milliseconds since January 1, 1970 (UTC).

=== ":material-calendar-clock: Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "range": { "__timestamp": { "gte": 1640995200000 }}
        }
      },
      "attributes": [ "__timestamp" ]
    }
    ```

=== ":material-calendar-clock: Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__timestamp": 1654992094524
          },
          "createTime": 1654992094524
        }
      ]
    }
    ```

## `__modificationTimestamp`

The time (in milliseconds) when the entity was last updated.

This is stored as an epoch: the milliseconds since January 1, 1970 (UTC).

=== ":material-calendar-clock: Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "range": { "__modificationTimestamp": { "gte": 1640995200000 }}
        }
      },
      "attributes": [ "__modificationTimestamp" ]
    }
    ```

=== ":material-calendar-clock: Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__modificationTimestamp": 1654905667786
          },
          "updateTime": 1654905667786
        }
      ]
    }
    ```

## `__state`

The entity status in Atlan. The expected values are:

- `ACTIVE` for entities that are available in Atlan.
- `DELETED` for entities that are (soft-)deleted in Atlan. These will not appear in the UI or API responses unless explicitly requested.

(Note that hard-deleted, or "purged" entities are fully erased, and therefore no status exists for them.)

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "term": { "__state": "DELETED" }
        }
      },
      "attributes": [ "__state" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7"
    {
      "entities": [
        {
          "attributes": {
            "__state": "DELETED"
          },
          "status": "DELETED"
        }
      ]
    }
    ```

## `__traitNames`

All directly-assigned classifications that exist on an entity.

**Note**: the classification names in the index are an Atlan-internal hashed string, *not* the human-readable name you see in the UI. Both the value you search for and what you see in the response must be this Atlan-internal hashed string.

??? details "Details"
    - `attributes.__classificationNames` in the response is a single string of all classifications, pipe-delimited.
    - `classificationNames` in the response is a set (unordered) of strings. Note that its order may or may not match that of the pipe-delimited `attributes.__classificationNames` string.
    - When searching, the `__traitNames` field provides more reliable results than searching `__classificationNames`. The latter can return results that have no classifications (but previously did), while the former returns only those that currently have classifications.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "exists": { "field": "__traitNames" }
        }
      },
      "attributes": [ "__classificationNames" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7-10"
    {
      "entities": [
        {
          "attributes": {
            "__classificationNames": "|E4FUqA9JFgb0VHRZWRAq95|I0oabU4LhZ69Nb0FKBGKfS|"
          },
          "classificationNames": [
            "I0oabU4LhZ69Nb0FKBGKfS",
            "E4FUqA9JFgb0VHRZWRAq95"
          ]
        }
      ]
    }
    ```

## `__propagatedTraitNames`

All propagated classifications that exist on an entity. This includes classifications propagated by:

- Upstream entities in lineage (from source to target)
- Parent entities (for example, from tables to columns)
- Linked terms

**Note**: the classification names in the index are an Atlan-internal hashed string, *not* the human-readable name you see in the UI. Both the value you search for and what you see in the response must be this Atlan-internal hashed string.

??? details "Details"
    - `attributes.__propagatedClassificationNames` in the response is a single string of all classifications, pipe-delimited.
    - `classificationNames` in the response is a set (unordered) of strings. Note that its order may or may not match that of the pipe-delimited `attributes.__propagatedClassificationNames` string.
    - When searching, the `__propagatedTraitNames` field provides more reliable results than searching `__propagatedClassificationNames`. The latter can return results that have no classifications (but previously did), while the former returns only those that currently have classifications.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "exists": { "field": "__propagatedTraitNames" }
        }
      },
      "attributes": [ "__propagatedClassificationNames" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="5 7-10"
    {
      "entities": [
        {
          "attributes": {
            "__propagatedClassificationNames": "|E4FUqA9JFgb0VHRZWRAq95|I0oabU4LhZ69Nb0FKBGKfS|"
          },
          "classificationNames": [
            "I0oabU4LhZ69Nb0FKBGKfS",
            "E4FUqA9JFgb0VHRZWRAq95"
          ]
        }
      ]
    }
    ```

## `__classificationsText`

All classifications that exist on an entity, whether directly assigned or propagated.

**Note**: the classification names in the index are an Atlan-internal hashed string, *not* the human-readable name you see in the UI. Both the value you search for and what you see in the response must be this Atlan-internal hashed string.

??? details "Details"
    - `attributes.__classificationsText` in the response is a single string of all classifications, space-delimited.
    - `classificationNames` in the response is a set (unordered) of strings. Note that its order may or may not match that of the space-delimited `attributes.__classificationsText` string.
    - When searching for existence (as in the example), the `__classificationsText` field may provide false positives. These are typically where a classification previously existed on an entity but no longer does.

=== ":material-tag-text: Analyzed-Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "exists": { "field": "__classificationsText" }
        }
      },
      "attributes": [ "__classificationsText" ]
    }
    ```

=== ":material-tag-text: Analyzed-Response"

    ```json hl_lines="5 7-10"
    {
      "entities": [
        {
          "attributes": {
            "__classificationsText": "I0oabU4LhZ69Nb0FKBGKfS E4FUqA9JFgb0VHRZWRAq95 "
          },
          "classificationNames": [
            "I0oabU4LhZ69Nb0FKBGKfS",
            "E4FUqA9JFgb0VHRZWRAq95"
          ]
        }
      ]
    }
    ```

## `__meanings`

All terms attached to an entity.

??? details "Details"
    - `__meanings` is a keyword array in Elastic, so cannot be searched by simple matching.
    - `__meanings` has no separate response — the default `meanings` appears with or without `__meanings` in the attribute list of the request.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "exists": { "field": "__meanings" }
        }
      },
      "attributes": [ "__meanings" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="4-11"
    {
      "entities": [
        {
          "meanings": [
            {
              "termGuid": "b4113341-251b-4adc-81fb-2420501c30e6",
              "relationGuid": "10df06a1-5b7c-492f-b827-bf4f46931c3e",
              "displayText": "Example Term",
              "confidence": 0
            }
          ]
        }
      ]
    }
    ```

## `__meaningsText`

All terms attached to an entity, as a single comma-separated string.

??? details "Details"
    - `__meaningsText` has no separate response — the default `meaningNames` appears with or without `__meaningsText` in the attribute list of the request.
    - `meaningNames` is an (unordered) set of term names, rather than a single string.

=== ":material-tag-text: Analyzed-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "match": { "__meaningsText": "example term" }
        }
      },
      "attributes": [ "__meaningsText" ]
    }
    ```

=== ":material-tag-text: Analyzed-Response"

    ```json hl_lines="4-6"
    {
      "entities": [
        {
          "meaningNames": [
            "Example Term"
          ]
        }
      ]
    }
    ```

## `__typeName`

The type of entity. For example, `Table`, `Column`, and so on.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "term": { "__typeName.keyword": "AtlasGlossaryTerm" }
        }
      },
      "attributes": [ "__typeName" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="4 6"
    {
      "entities": [
        {
          "typeName": "AtlasGlossaryTerm",
          "attributes": {
            "__typeName": "AtlasGlossaryTerm"
          }
        }
      ]
    }
    ```

=== ":material-tag-text: Analyzed-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "match": { "__typeName": "atlasglossaryterm table" }
        }
      },
      "attributes": [ "__typeName" ]
    }
    ```

=== ":material-tag-text: Analyzed-Response"

    ```json hl_lines="4 6 10 12"
    {
      "entities": [
        {
          "typeName": "AtlasGlossaryTerm",
          "attributes": {
            "__typeName": "AtlasGlossaryTerm"
          }
        },
        {
          "typeName": "Table",
          "attributes": {
            "__typeName": "Table"
          }
        }
      ]
    }
    ```

## `__superTypeNames`

All super types of an entity.

For example:

- `Table` has super types of `SQL`, `Catalog`, `Asset` and `Referenceable`.
- `LookerField` has super types of `Looker`, `BI`, `Catalog`, `Asset` and `Referenceable`.

??? details "Details"
    - `__superTypeNames` has no separate response — the default `typeName` appears with or without `__superTypeNames` in the attribute list of the request.

=== ":material-tag: Exact-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "match": { "__superTypeNames.keyword": "SQL" }
        }
      },
      "attributes": [ "__superTypeNames" ]
    }
    ```

=== ":material-tag: Exact-Response"

    ```json hl_lines="4 7 10 13"
    {
      "entities": [
        {
          "typeName": "Query"
        },
        {
          "typeName": "Table"
        },
        {
          "typeName": "Database"
        },
        {
          "typeName": "Column"
        }
      ]
    }
    ```

=== ":material-tag-text: Analyzed-Request"

    ```json hl_lines="4"
    {
      "dsl": {
        "query": {
          "match": { "__superTypeNames": "sql bi" }
        }
      },
      "attributes": [ "__superTypeNames" ]
    }
    ```

=== ":material-tag-text: Analyzed-Response"

    ```json hl_lines="4 7 10 13 16 19"
    {
      "entities": [
        {
          "typeName": "Query"
        },
        {
          "typeName": "Table"
        },
        {
          "typeName": "Database"
        },
        {
          "typeName": "Column"
        },
        {
          "typeName": "LookerQuery"
        },
        {
          "typeName": "LookerField"
        }
      ]
    }
    ```

## `__hasLineage`

Flag that is true if an entity has at least one process upstream or downstream. Otherwise, it will be false.

**Note**: `Process` entities themselves will also be included in the `true` results, unless excluded by some other search criteria.

=== ":material-toggle-switch: Request"

    ```json hl_lines="4 7"
    {
      "dsl": {
        "query": {
          "term": { "__hasLineage": true }
        }
      },
      "attributes": [ "__hasLineage" ]
    }
    ```

=== ":material-toggle-switch: Response"

    ```json hl_lines="5"
    {
      "entities": [
        {
          "attributes": {
            "__hasLineage": true
          }
        }
      ]
    }
    ```
