<#import "macro_java_attribute_struct.ftl" as java>
<#list attributes as attribute>
    <@java.render className=className attribute=attribute />

</#list>
