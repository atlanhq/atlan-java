<#macro render originalName attribute>
<#if attribute.details.type.container??>
??? reln-multiple "${attribute.details.originalName}"
<#else>
??? reln-single "${attribute.details.originalName}"
</#if>

    ```json title="${attribute.details.description}"
    {
      "relationshipAttributes": {
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

    1. Set the `${attribute.details.originalName}` relationship<#if attribute.rawValues?size == 2>s</#if> from `${originalName}` to `${attribute.details.type.originalBase}`<#if attribute.rawValues?size == 2>s</#if>.
</#macro>
<#macro diagram originalName attributes>
```mermaid
classDiagram
    direction LR
    class ${originalName}
    link ${originalName} "../${originalName?lower_case}"
    <#list attributes as attribute>
    <#if attribute.relationship && !attribute.inherited>
    class ${attribute.details.type.originalBase}
    link ${attribute.details.type.originalBase} "../${attribute.details.type.originalBase?lower_case}"
    ${originalName} --> <#if attribute.rawValues?size == 1>"0..1"<#else>"*"</#if> ${attribute.details.type.originalBase} : ${attribute.details.originalName}
    </#if>
    </#list>
```
</#macro>
<#macro diagramInherited originalName attributes>
```mermaid
classDiagram
    direction LR
    class ${originalName}
    link ${originalName} "../${originalName?lower_case}"
    <#list attributes as attribute>
    <#if attribute.relationship && attribute.inherited>
    class ${attribute.details.type.originalBase}
    link ${attribute.details.type.originalBase} "../${attribute.details.type.originalBase?lower_case}"
    ${originalName} --> <#if attribute.rawValues?size == 1>"0..1"<#else>"*"</#if> ${attribute.details.type.originalBase} : ${attribute.details.originalName}
    </#if>
    </#list>
```
</#macro>
