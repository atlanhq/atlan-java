---
hide:
  - toc
---

# Full model reference

This area contains a full reference of the different metadata in Atlan. For every [type](../concepts/assets.md#type-definitions), a full list of attributes and relationships is provided.

Some of these models are for types that can only be created through the APIs (or SDKs). For more information on creating these assets, see the [creating assets pattern](../patterns/create/index.md).

<#macro renderSubTypes originalName subTypes>
    <#list subTypes as subType>
    <#if subType.abstract>
    class ${subType.originalName} {
        <<abstract>>
    }
    <#else>
    class ${subType.originalName}
    </#if>
    link ${subType.originalName} "${subType.originalName?lower_case}"
    ${originalName} <|-- ${subType.originalName} : extends
    <#if subType.fullSubTypes?has_content>
    <@renderSubTypes originalName=subType.originalName subTypes=subType.fullSubTypes />
    </#if>
    </#list>
</#macro>

```mermaid
classDiagram
    direction RL
    class Referenceable {
        <<abstract>>
    }
    link Referenceable "referenceable"
    <@renderSubTypes originalName="Referenceable" subTypes=fullSubTypes />
```
