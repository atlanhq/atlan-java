<#macro render originalName attribute>
<#if attribute.details.type.type == "ENUM">
??? type-enum "${attribute.details.originalName}"
<#elseif attribute.details.type.type == "STRUCT">
??? type-struct "${attribute.details.originalName}"
<#else>
??? type-${attribute.details.type.originalBase} "${attribute.details.originalName}"
</#if>

    ```json title="${attribute.details.description}"
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
</#macro>
