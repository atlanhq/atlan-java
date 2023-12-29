<#import "macro_attribute_struct.ftl" as raw>
<#list attributes as attribute>
    <@raw.render originalName=originalName attribute=attribute />

</#list>
