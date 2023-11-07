<#macro all subTypes>
    /** Glossary in which the category is contained, searchable by the qualifiedName of the glossary. */
    KeywordField ANCHOR = new KeywordField("anchor", "__glossary");

    /** Parent category in which a subcategory is contained, searchable by the qualifiedName of the category. */
    KeywordField PARENT_CATEGORY = new KeywordField("parentCategory", "__parentCategory");
</#macro>
