<#macro render className attribute>
<#if attribute.type.type == "ENUM">
??? type-enum "${attribute.renamed}"
<#elseif attribute.type.type == "STRUCT">
??? type-struct "${attribute.renamed}"
<#elseif attribute.type.container?has_content && attribute.type.container == "Map<">
??? type-map "${attribute.renamed}"
<#else>
??? type-${attribute.type.originalBase} "${attribute.renamed}"
</#if>

    ```java linenums="1" title="${attribute.description}"
    builder.${attribute.renamed}(...); // (1)
    ${className?uncap_first}.get${attribute.renamed?cap_first}(); // (2)
    ```

    1. Set the `${attribute.renamed}` for a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on either [creating](../../snippets/advanced-examples/create.md) or [updating](../../snippets/advanced-examples/update.md) assets.

    2. Retrieve the `${attribute.renamed}` from a `${className}`.

        !!! details "For more details"
            For more information, see the asset CRUD snippets on [retrieving](../../snippets/advanced-examples/read.md) assets.

</#macro>
