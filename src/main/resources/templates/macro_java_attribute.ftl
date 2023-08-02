<#macro search attribute>
<#if attribute.searchFields??>
<#list attribute.searchFields as field>
<#assign commentCount = field?counter + 2>
<#if field.enumClassName == "TextFields" || field.enumClassName == "StemmedFields">
    QueryFactory.must(have(${field.enumClassName}.${field.enumName}).match(${attribute.values?first})); // (${commentCount})
<#elseif field.enumClassName == "NumericFields">
    QueryFactory.must(have(${field.enumClassName}.${field.enumName}).gt(${attribute.values?first})); // (${commentCount})
<#else>
    QueryFactory.must(have(${field.enumClassName}.${field.enumName}).eq(${attribute.values?first})); // (${commentCount})
</#if>
</#list>
</#if>
</#macro>
<#macro searchDescription attribute>
<#if attribute.searchFields??>
<#list attribute.searchFields as field>
<#assign commentCount = field?counter + 2>
<#if field.enumClassName == "TextFields" || field.enumClassName == "StemmedFields">
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` textually matching the provided value.

        !!! details "For more details"
            For more information, see the searching section on [full text queries](../../search/queries/text.md).

<#elseif field.enumClassName == "NumericFields">
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` greater than the provided value.

        !!! details "For more details"
            For more information, see the searching section on [range queries](../../search/queries/terms.md#range).

<#else>
    ${commentCount}. Find all assets in Atlan with their `${attribute.details.renamed}` exactly matching the provided value.

        !!! details "For more details"
            For more information, see the searching section on [term queries](../../search/queries/terms.md#term).

</#if>
</#list>
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
    <@search attribute=attribute />
    ```

    1. Set the `${attribute.details.renamed}` for a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on either [creating](../../snippets/advanced-examples/create.md) or [updating](../../snippets/advanced-examples/update.md) assets.

    2. Retrieve the `${attribute.details.renamed}` from a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on [retrieving](../../snippets/advanced-examples/read.md) assets.

    <@searchDescription attribute=attribute />
</#macro>
