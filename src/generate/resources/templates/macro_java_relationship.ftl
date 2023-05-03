<#macro render attribute className>
<#if attribute.details.type.container??>
??? reln-multiple "${attribute.details.renamed}"
<#else>
??? reln-single "${attribute.details.renamed}"
</#if>

    ```java title="${attribute.details.description}"
    builder // (1)
    <#list attribute.values as value>
        .${attribute.builderMethod}(${value})
    </#list>
    ${className?uncap_first}.get${attribute.details.renamed?cap_first}(); // (2)
    <#if attribute.searchFields??>
    <#list attribute.searchFields as field>
        QueryFactory.must(have(${field.enumName}).eq(${attribute.values}));
    </#list>
    </#if>
    ```

    1. Add a `${attribute.details.renamed}` relationship<#if attribute.rawValues?size == 2>s</#if> from `${className}` to `${attribute.details.type.name}`<#if attribute.rawValues?size == 2>s</#if>.
    2. Retrieve the `${attribute.details.renamed}` from a `${className}`.
</#macro>
