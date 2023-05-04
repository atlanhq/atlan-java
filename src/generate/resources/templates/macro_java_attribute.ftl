<#macro render className attribute>
<#if attribute.details.type.type == "ENUM">
??? type-enum "${attribute.details.renamed}"
<#elseif attribute.details.type.type == "STRUCT">
??? type-struct "${attribute.details.renamed}"
<#else>
??? type-${attribute.details.type.originalBase} "${attribute.details.renamed}"
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

    1. Set the `${attribute.details.renamed}` for a `${className}`.
    2. Retrieve the `${attribute.details.renamed}` from a `${className}`.
</#macro>
