<#macro all subTypes>
    /** Glossary in which the term is contained, searchable by the qualifiedName of the glossary. */
    KeywordField ANCHOR = new KeywordField("anchor", "__glossary");

    /** Categories in which the term is organized, searchable by the qualifiedName of the category. */
    KeywordField CATEGORIES = new KeywordField("categories", "__categories");
</#macro>
