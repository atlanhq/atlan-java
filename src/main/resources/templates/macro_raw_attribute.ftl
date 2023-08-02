<#macro render originalName attribute>
<#if attribute.details.type.type == "ENUM">
??? type-enum "${attribute.details.originalName}"
<#elseif attribute.details.type.type == "STRUCT">
??? type-struct "${attribute.details.originalName}"
<#elseif attribute.details.type.container?has_content && attribute.details.type.container == "Map<">
??? type-map "${attribute.details.originalName}"
<#else>
??? type-${attribute.details.type.originalBase} "${attribute.details.originalName}"
</#if>

    ```json linenums="1" title="${attribute.details.description}"
    {
      "attributes": {
      <#if attribute.rawValues?size == 1>
        "${attribute.details.originalName}": ${attribute.rawValues?first} // (1)
      <#else>
        "${attribute.details.originalName}": [ // (1)
        <#list attribute.rawValues as rawValue>
          ${rawValue}<#sep>,</#sep>
        </#list>
        ]
      </#if>
      }
    }
    ```

    1. Set the `${attribute.details.originalName}` for a `${originalName}`.

        !!! details "For more details"
            For more information, see the [asset CRUD](../snippets/advanced-examples/index.md) snippets.
</#macro>
