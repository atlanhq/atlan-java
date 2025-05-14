<#macro all subTypes>
    /** Unique fully-qualified name of the asset in Atlan. */
    KeywordTextField QUALIFIED_NAME =
            new KeywordTextField("qualifiedName", "qualifiedName", "qualifiedName.text");

    /** Built-in connector type through which this asset is accessible. */
    KeywordField CONNECTOR_TYPE = new KeywordField("connectorName", "connectorName");

    /** Custom connector type through which this asset is accessible. */
    KeywordField CUSTOM_CONNECTOR_TYPE = new KeywordField("connectorName", "connectorName");
</#macro>
