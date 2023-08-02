<#macro render attribute className>
<#if attribute.details.type.container??>
??? reln-multiple "${attribute.details.renamed}"
<#else>
??? reln-single "${attribute.details.renamed}"
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
    ```

    1. Add a `${attribute.details.renamed}` relationship<#if attribute.rawValues?size == 2>s</#if> from `${className}` to `${attribute.details.type.name}`<#if attribute.rawValues?size == 2>s</#if>.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on either [creating](../../snippets/advanced-examples/create.md) or [updating](../../snippets/advanced-examples/update.md) assets.

    2. Retrieve the `${attribute.details.renamed}` from a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on [retrieving](../../snippets/advanced-examples/read.md) assets.
</#macro>
