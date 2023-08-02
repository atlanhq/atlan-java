<#import "macro_raw_relationship.ftl" as raw>

# ${originalName}

${description}

!!! warning "Complete reference"
    This is a complete reference for the `${originalName}` object in Atlan, showing every possible property and relationship that can exist for these objects. For an introduction, you probably want to start with:

    - [Snippets](../snippets/index.md) — small, atomic examples of single-step use cases.
    - [Patterns](../patterns/index.md) — walkthroughs of common multi-step implementation patterns.

## Inheritance

Following is the inheritance structure for `${originalName}`. The type structure may be simplified in some of the SDKs, but for search purposes you could still use any of the super types shown below.

<#if originalSuperTypes?has_content>
<@raw.diagramInheritance originalName=originalName superTypes=originalSuperTypes />
</#if>

## Attributes

Following are all the properties available on `${originalName}` assets in Atlan.

### Core properties

For detailed examples of searching these fields, see [common search fields](../search/attributes/common.md).

??? details "Expand for details on each core property"

    === ":fontawesome-brands-java: Java"

        ??? type-string "typeName"

            ```java linenums="1" title="Type of this asset."
            ${className?uncap_first}.getTypeName(); // (1)
            QueryFactory.must(haveSuperType(Asset.TYPE_NAME)); // (2)
            QueryFactory.must(beOfType(${className}.TYPE_NAME)); // (3)
            QueryFactory.must(beOneOfTypes(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME)); // (4)
            ```

            1. Retrieve the `typeName` from an asset.

                !!! recommendation "Use instanceof for type checking"
                    If you are operating on an `Asset` type, chances are it is actually a more concrete type. Rather than using String-based comparisons, you can type-check using Java types: `if (asset instanceof Column)`, for example. This has the added benefit of not needing separate null handling (if null, then `asset` cannot be an `instanceof` any type).

            2. Query for all assets that are sub-types of a particular super-type, in this example all assets that are sub-types of `Asset`.
            3. Query for all assets with a particular type, in this example a `${className}`.
            4. Query for all assets with any one of a number of different types, in this example either a `Table`, `View`, or `MaterializedView`.

        --8<-- "snippets/model/core-java.md"

    === ":material-language-python: Python"

        ??? type-string "type_name"

            ```python title="Type of this asset."
            asset.type_name; // (1)
            ```

            1. Retrieve the `type_name` from an asset.

        --8<-- "snippets/model/core-python.md"

    === ":material-code-json: Raw REST API"

        ??? type-string "typeName"

            ```json title="Type of this asset."
            {
              "typeName": "${className}" // (1)
            }
            ```

            1. The `typeName` of an asset is at the top-level of the payload, and in this example indicates a `${className}`.

        --8<-- "snippets/model/core-json.md"

<#if inheritedProperties?has_content>
### Inherited properties

??? details "Expand for details on each inherited property"

    === ":fontawesome-brands-java: Java"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/java/${superType?lower_case}-properties.md"
    </#list>

    === ":material-language-python: Python"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/python/${superType?lower_case}-properties.md"
    </#list>

    === ":material-code-json: Raw REST API"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/raw/${superType?lower_case}-properties.md"
    </#list>

</#if>
<#if typeSpecificProperties?has_content>
### Type-specific properties

=== ":fontawesome-brands-java: Java"

    --8<-- "snippets/model/java/${originalName?lower_case}-properties.md"

=== ":material-language-python: Python"

    --8<-- "snippets/model/python/${originalName?lower_case}-properties.md"

=== ":material-code-json: Raw REST API"

    --8<-- "snippets/model/raw/${originalName?lower_case}-properties.md"

</#if>
## Relationships

Following are all the relationships available between `${originalName}` assets and other objects in Atlan.

<#if inheritedRelationships?has_content>
### Inherited relationships

<@raw.diagram originalName=originalName attributes=inheritedRelationships />

??? details "Expand for details on each inherited relationship"

    === ":fontawesome-brands-java: Java"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/java/${superType?lower_case}-relationships.md"
    </#list>

    === ":material-language-python: Python"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/python/${superType?lower_case}-relationships.md"
    </#list>

    === ":material-code-json: Raw REST API"

    <#list superTypes?reverse as superType>
        --8<-- "snippets/model/raw/${superType?lower_case}-relationships.md"
    </#list>

</#if>
<#if typeSpecificRelationships?has_content>
### Type-specific relationships

<@raw.diagram originalName=originalName attributes=typeSpecificRelationships />

=== ":fontawesome-brands-java: Java"

    --8<-- "snippets/model/java/${originalName?lower_case}-relationships.md"

=== ":material-language-python: Python"

    --8<-- "snippets/model/python/${originalName?lower_case}-relationships.md"

=== ":material-code-json: Raw REST API"

    --8<-- "snippets/model/raw/${originalName?lower_case}-relationships.md"
</#if>
