<#macro render originalName attribute>
<#if attribute.type.type == "ENUM">
??? type-enum "${attribute.originalName}"
<#elseif attribute.type.type == "STRUCT">
??? type-struct "${attribute.originalName}"
<#elseif attribute.type.container?has_content && attribute.type.container == "Map<">
??? type-map "${attribute.originalName}"
<#else>
??? type-${attribute.type.originalBase} "${attribute.originalName}"
</#if>

    ```json linenums="1" title="${attribute.description}"
    {
      "attributes": {
        "${attribute.originalName}": ... // (1)
      }
    }
    ```

    1. Set the `${attribute.originalName}` for a `${originalName}`.

        !!! details "For more details"
            For more information, see the [asset CRUD](../../snippets/advanced-examples/index.md) snippets.
</#macro>
