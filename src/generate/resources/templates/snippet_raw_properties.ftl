<#import "macro_raw_attribute.ftl" as raw>
<#list testAttributes as attribute>
<#if !attribute.inherited && !attribute.relationship>
    <@raw.render originalName=originalName attribute=attribute />
</#if>
</#list>
