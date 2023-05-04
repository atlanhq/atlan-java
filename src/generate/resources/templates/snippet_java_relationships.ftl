<#import "macro_java_relationship.ftl" as java>
<#list testAttributes as attribute>
<#if !attribute.inherited && attribute.relationship>
    <@java.render className=className attribute=attribute />

</#if>
</#list>
