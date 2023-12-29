<#macro render originalName attribute>
<#if attribute.details.type.container??>
### ${attribute.details.originalName} :material-circle-multiple-outline:{ title="multiple" } ([${attribute.relatedTypeOriginal}](<#if attribute.relatedTypeOriginal == "Process">../lineage/index<#elseif attribute.relatedTypeOriginal == "AccessControl">../accesscontrol/index<#else>${attribute.relatedTypeOriginal?lower_case}</#if>.md))
<#else>
### ${attribute.details.originalName} :material-circle-outline:{ title="single" } ([${attribute.relatedTypeOriginal}](<#if attribute.relatedTypeOriginal == "Process">../lineage/index<#elseif attribute.relatedTypeOriginal == "AccessControl">../accesscontrol/index<#else>${attribute.relatedTypeOriginal?lower_case}</#if>.md))
</#if>

<#if attribute.details.renamed == attribute.details.originalName>
${attribute.details.description}
{ .card }
<#else>
${attribute.details.description} (1)
{ .card .annotate }

1.  !!! warning "Uses a different name in SDKs"

        :fontawesome-brands-java:{ title="Java SDK" } `${attribute.details.renamed}`<br>
        :material-language-python:{ title="Python SDK" } `${attribute.details.snakeCaseRenamed}`
</#if>
</#macro>
<#macro diagram originalName attributes>
```mermaid
classDiagram
    direction LR
    class ${originalName}
    link ${originalName} "../${originalName?lower_case}"
    <#list attributes as attribute>
    class ${attribute.details.type.originalBase}
    link ${attribute.details.type.originalBase} "../${attribute.details.type.originalBase?lower_case}"
    ${originalName} --> <#if attribute.rawValues?size == 1>"0..1"<#else>"*"</#if> ${attribute.details.type.originalBase} : ${attribute.details.originalName}
    </#list>
```
</#macro>
<#macro renderSuperTypes originalName superTypes>
    <#list superTypes as superType>
    <#if superType.abstract || superType.originalName == "Referenceable">
    class ${superType.originalName} {
        <<abstract>>
    }
    <#else>
    class ${superType.originalName}
    </#if>
    link ${superType.originalName} "../${superType.originalName?lower_case}"
    ${superType.originalName} <|-- ${originalName} : extends
    <#if superType.originalSuperTypes?has_content>
    <@renderSuperTypes originalName=superType.originalName superTypes=superType.originalSuperTypes />
    </#if>
    </#list>
</#macro>
<#macro diagramInheritance originalName superTypes>
```mermaid
classDiagram
    direction RL
    <#if abstract || originalName == "Referenceable">
    class ${originalName} {
        <<abstract>>
    }
    <#else>
    class ${originalName}
    </#if>
    link ${originalName} "../${originalName?lower_case}"
    <#if superTypes?has_content>
    <@renderSuperTypes originalName=originalName superTypes=superTypes />
    </#if>
```
</#macro>
