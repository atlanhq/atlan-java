<#import "macro_raw_relationship.ftl" as raw>

# ${originalName}

${description}

!!! warning "Complete reference"
    This is a complete reference for the `${originalName}` object in Atlan, showing every possible property and relationship that can exist for these objects. For an introduction, you probably want to start with:

    - [Snippets](../../snippets/index.md) — small, atomic examples of single-step use cases.
    - [Patterns](../../patterns/index.md) — walkthroughs of common multi-step implementation patterns.

`${originalName}` inherits its attributes and relationships from these other types:

<#if originalSuperTypes?has_content>
<@raw.diagramInheritance originalName=originalName superTypes=originalSuperTypes />
</#if>

## Properties

??? details "Inherited properties"

    These attributes are inherited from `${originalName}`'s supertypes (shown above):

    <div class="grid" markdown>

    --8<-- "snippets/model/core-properties.md"
    <#if inheritedProperties?has_content>
    <#list superTypes?reverse as superType>
    --8<-- "snippets/model/${superType?lower_case}-properties.md"
    </#list>
    </#if>

    </div>
<#if typeSpecificProperties?has_content>

These attributes are specific to instances of `${originalName}` (and all of its subtypes).

<div class="grid" markdown>

--8<-- "snippets/model/${originalName?lower_case}-properties.md"

</div>
</#if>

## Relationships

<#if inheritedRelationships?has_content>
??? details "Inherited relationships"

    These relationships are inherited from `${originalName}`'s supertypes:

    <div class="grid" markdown>

    <#list superTypes?reverse as superType>
    --8<-- "snippets/model/${superType?lower_case}-relationships.md"
    </#list>

    </div>
</#if>
<#if typeSpecificRelationships?has_content>

These relationships are specific to instances of `${originalName}` (and all of its subtypes).

<div class="grid" markdown>

--8<-- "snippets/model/${originalName?lower_case}-relationships.md"

</div>
</#if>
