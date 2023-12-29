<#macro render originalName attribute>
### ${attribute.originalName} <#if attribute.type.type == "ENUM">:material-format-list-group:{ title="enum" }<#elseif attribute.type.type == "STRUCT">:material-code-json:{ title="object" }<#elseif attribute.type.container?has_content && attribute.type.container == "Map<">:material-code-braces:{ title="map / dict" }<#elseif attribute.type.originalBase == "string">:material-alphabetical-variant:{ title="string" }<#elseif attribute.type.originalBase == "date">:material-calendar-clock:{ title="timestamp" }<#elseif attribute.type.originalBase == "boolean">:material-toggle-switch:{ title="boolean" }<#elseif attribute.type.originalBase == "float">:material-decimal:{ title="float" }<#elseif attribute.type.originalBase == "long">:material-numeric:{ title="long" }<#elseif attribute.type.originalBase == "int">:material-numeric:{ title="integer" }<#else>:material-progress-question:{ title="${attribute.type.originalBase}" }</#if>

${attribute.description}
{ .card }
</#macro>
