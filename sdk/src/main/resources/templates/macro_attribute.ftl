<#macro render originalName attribute>
### ${attribute.details.originalName} <#if attribute.details.type.type == "ENUM">:material-format-list-group:{ title="enum" }<#elseif attribute.details.type.type == "STRUCT">:material-code-json:{ title="object" }<#elseif attribute.details.type.container?has_content && attribute.details.type.container == "Map<">:material-code-braces:{ title="map / dict" }<#elseif attribute.details.type.originalBase == "string">:material-alphabetical-variant:{ title="string" }<#elseif attribute.details.type.originalBase == "date">:material-calendar-clock:{ title="timestamp" }<#elseif attribute.details.type.originalBase == "boolean">:material-toggle-switch:{ title="boolean" }<#elseif attribute.details.type.originalBase == "float">:material-decimal:{ title="float" }<#elseif attribute.details.type.originalBase == "long">:material-numeric:{ title="long" }<#elseif attribute.details.type.originalBase == "int">:material-numeric:{ title="integer" }<#else>:material-progress-question:{ title="${attribute.details.type.originalBase}" }</#if>

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
