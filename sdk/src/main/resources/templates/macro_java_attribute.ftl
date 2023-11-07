<#macro search className attribute>
<#if attribute.details.searchType?? && attribute.details.searchType != "RelationField">
<#assign commentCount = 3>
<#if attribute.details.searchType?starts_with("Keyword") || attribute.details.searchType == "BooleanField">
    client.assets.select().where(${className}.${attribute.details.enumForAttr}.eq(${attribute.values?first})); // (${commentCount})
<#assign commentCount = commentCount + 1>
</#if>
<#if attribute.details.searchType?contains("Text")>
    client.assets.select().where(${className}.${attribute.details.enumForAttr}.match(${attribute.values?first})); // (${commentCount})
<#assign commentCount = commentCount + 1>
</#if>
<#if attribute.details.searchType?starts_with("Numeric")>
    client.assets.select().where(${className}.${attribute.details.enumForAttr}.gt(${attribute.values?first})); // (${commentCount})
<#assign commentCount = commentCount + 1>
</#if>
</#if>
</#macro>
<#macro searchDescription attribute>
<#if attribute.details.searchType?? && attribute.details.searchType != "RelationField">
<#assign commentCount = 3>
<#if attribute.details.searchType?starts_with("Keyword") || attribute.details.searchType == "BooleanField">
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` exactly matching the provided value.

        !!! details "For more details"
            For more information, see the searching section on [term queries](../../search/queries/terms.md#term).
<#assign commentCount = commentCount + 1>
</#if>
<#if attribute.details.searchType?contains("Text")>
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` textually matching the provided value.

        !!! details "For more details"
            For more information, see the searching section on [full text queries](../../search/queries/text.md).
<#assign commentCount = commentCount + 1>
</#if>
<#if attribute.details.searchType?starts_with("Numeric")>
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` greater than the provided value.

        !!! details "For more details"
            For more information, see the searching section on [range queries](../../search/queries/terms.md#range).
<#assign commentCount = commentCount + 1>
</#if>
</#if>
</#macro>
<#macro render className attribute>
<#if attribute.details.type.type == "ENUM">
??? type-enum "${attribute.details.renamed}"
<#elseif attribute.details.type.type == "STRUCT">
??? type-struct "${attribute.details.renamed}"
<#elseif attribute.details.type.container?has_content && attribute.details.type.container == "Map<">
??? type-map "${attribute.details.renamed}"
<#else>
??? type-${attribute.details.type.originalBase} "${attribute.details.renamed}"
</#if>

    ```java linenums="1" title="${attribute.details.description}"
    <#if attribute.values?size == 1>
    builder.${attribute.builderMethod}(${attribute.values?first}); // (1)
    <#else>
    builder // (1)
    <#list attribute.values as value>
        .${attribute.builderMethod}(${value})<#if value?is_last>;</#if>
    </#list>
    </#if>
    ${className?uncap_first}.get${attribute.details.renamed?cap_first}(); // (2)
    <@search className=className attribute=attribute />
    ```

    1. Set the `${attribute.details.renamed}` for a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on either [creating](../../snippets/advanced-examples/create.md) or [updating](../../snippets/advanced-examples/update.md) assets.

    2. Retrieve the `${attribute.details.renamed}` from a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on [retrieving](../../snippets/advanced-examples/read.md) assets.

    <@searchDescription attribute=attribute />
</#macro>
