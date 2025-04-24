/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.generators.lombok.Singulars;
import com.atlan.model.typedefs.*;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RelationshipGenerator extends TypeGenerator implements Comparable<RelationshipGenerator> {

    public static final String DIRECTORY = "relations";

    // Sort attribute definitions in a set based purely on their name (two attributes
    // in the same set with the same name should be a conflict / duplicate)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<RelationshipGenerator> relationshipComparator =
            Comparator.comparing(RelationshipGenerator::getOriginalName, stringComparator);

    private final RelationshipDef relationshipDef;
    private SortedSet<SearchableAttribute<?>> nonInheritedAttributes;
    private List<String> mapContainers = null;

    private String endDef1TypeName;
    private String endDef2TypeName;
    private String endDef1AttrName;
    private String endDef2AttrName = null;

    @SuppressWarnings("this-escape")
    public RelationshipGenerator(AtlanClient client, RelationshipDef relationshipDef, GeneratorConfig cfg) {
        super(client, relationshipDef, cfg);
        this.relationshipDef = relationshipDef;
        resolveClassName();
        super.description = cache.getTypeDescription(originalName);
        resolveAttributes();
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(originalName);
    }

    private void resolveAttributes() {
        nonInheritedAttributes = new TreeSet<>();
        for (AttributeDef attributeDef :
                cache.getRelationshipDefCache().get(getOriginalName()).getAttributeDefs()) {
            SearchableAttribute<?> attribute = new SearchableAttribute<>(client, className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                nonInheritedAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        endDef1TypeName = cfg.resolveClassName(relationshipDef.getEndDef1().getType());
        endDef2TypeName = cfg.resolveClassName(relationshipDef.getEndDef2().getType());
        String name1 = cfg.resolveAttributeName(relationshipDef.getEndDef1().getName());
        String name2 = cfg.resolveAttributeName(relationshipDef.getEndDef2().getName());
        String name1s = Singulars.autoSingularize(name1);
        String name2s = Singulars.autoSingularize(name2);
        endDef1AttrName = name1s == null ? name1 : name1s;
        // When the name is the same, avoid generating multiple embedded classes with the same name
        if (!name1.equals(name2)) {
            endDef2AttrName = name2s == null ? name2 : name2s;
        }
    }

    private void checkAndAddMapContainer(SearchableAttribute<?> attribute) {
        if (attribute.getType().getContainer() != null
                && attribute.getType().getContainer().contains("Map")) {
            if (mapContainers == null) {
                mapContainers = new ArrayList<>();
            }
            mapContainers.add(attribute.getRenamed());
        }
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(RelationshipGenerator o) {
        return relationshipComparator.compare(this, o);
    }
}
