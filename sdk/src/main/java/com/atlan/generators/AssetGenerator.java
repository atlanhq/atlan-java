/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.*;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator implements Comparable<AssetGenerator> {

    public static final String DIRECTORY = "assets";

    // Sort attribute definitions in a set based purely on their name (two attributes
    // in the same set with the same name should be a conflict / duplicate)
    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<AssetGenerator> assetComparator =
            Comparator.comparing(AssetGenerator::getOriginalName, stringComparator);

    private final EntityDef entityDef;
    private String parentClassName;
    private SortedSet<SearchableAttribute<?>> interfaceAttributes;
    private SortedSet<SearchableAttribute<?>> classAttributes;
    private SortedSet<SearchableAttribute<?>> nonInheritedAttributes;
    private List<AssetGenerator> originalSuperTypes = null;
    private List<AssetGenerator> fullSubTypes = null;
    private List<String> originalSubTypes = null;
    private List<String> subTypes = null;
    private List<String> mapContainers = null;
    private final Set<String> superTypes;

    @SuppressWarnings("this-escape")
    public AssetGenerator(AtlanClient client, EntityDef entityDef, GeneratorConfig cfg) {
        super(client, entityDef, cfg);
        this.entityDef = entityDef;
        resolveClassName();
        super.description = cache.getTypeDescription(originalName);
        this.superTypes = cache.getAllSuperTypesForType(getOriginalName());
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(originalName);
    }

    public String resolveSuperTypeName(String name) {
        if (name.equals("Referenceable")) {
            // Don't rename Referenceable, since we have an actual interface defined for it
            return name;
        }
        return cfg.resolveClassName(name);
    }

    public void resolveDetails() {
        resolveParentClassName();
        resolveSubTypes();
        resolveAttributes();
        resolveRelationships();
    }

    public String getInterfaceTemplateFile() {
        try {
            return cfg.getFreemarkerConfig()
                    .getTemplate("I" + className + ".ftl")
                    .getSourceName();
        } catch (TemplateNotFoundException e) {
            // Do nothing - no template to load or otherwise handle
        } catch (IOException e) {
            log.error("Error reading template: I{}.ftl", className, e);
        }
        return null;
    }

    public boolean isAbstract() {
        return (originalSubTypes != null && !originalSubTypes.isEmpty()) && !cfg.forceNonAbstract(getOriginalName());
    }

    public void resolveParentClassName() {
        if (getOriginalName().equals("Asset")) {
            this.parentClassName = "Reference";
        } else {
            this.parentClassName = "Asset";
        }
    }

    public SortedSet<String> getAllSubTypes(String originalTypeName) {
        SortedSet<String> localSubTypes = new TreeSet<>();
        AssetGenerator assetGen = cache.getCachedAssetType(originalTypeName);
        if (assetGen != null) {
            assetGen.resolveSubTypes();
            if (!assetGen.isAbstract()) {
                log.info("Adding concrete subtype {} to: {}", assetGen.getClassName(), originalTypeName);
                localSubTypes.add(assetGen.getClassName());
            }
            List<String> subTypes = assetGen.getOriginalSubTypes();
            if (subTypes != null && !subTypes.isEmpty()) {
                for (String subType : subTypes) {
                    SortedSet<String> further = getAllSubTypes(subType);
                    localSubTypes.addAll(further);
                }
            }
        }
        return localSubTypes;
    }

    private void resolveSubTypes() {
        originalSubTypes = entityDef.getSubTypes();
        if (originalSubTypes != null && !originalSubTypes.isEmpty()) {
            subTypes = new ArrayList<>();
            fullSubTypes = new ArrayList<>();
            for (String originalSubType : originalSubTypes) {
                AssetGenerator sub = cache.getCachedAssetType(originalSubType);
                if (sub != null) {
                    fullSubTypes.add(sub);
                }
                if (cfg.includeTypedef(sub.getEntityDef())) {
                    MappedType subType = cache.getCachedType(originalSubType);
                    if (subType != null) {
                        subTypes.add(subType.getName());
                    } else {
                        log.warn("Mapped subType was not found: {}", originalSubType);
                    }
                }
            }
        }
        List<String> superTypes = entityDef.getSuperTypes();
        if (superTypes != null && !superTypes.isEmpty()) {
            originalSuperTypes = new ArrayList<>();
            for (String superType : superTypes) {
                AssetGenerator parent = cache.getCachedAssetType(superType);
                if (parent != null) {
                    originalSuperTypes.add(parent);
                }
            }
        }
    }

    private void resolveAttributes() {
        Set<AttributeDef> allAttributes;
        if (getOriginalName().equals("Asset")) {
            allAttributes = cache.getAllAttributesForType(getOriginalName());
        } else {
            allAttributes = cache.getAllNonAssetAttributesForType(getOriginalName());
        }
        nonInheritedAttributes = new TreeSet<>();
        for (AttributeDef attributeDef :
                cache.getEntityDefCache().get(getOriginalName()).getAttributeDefs()) {
            SearchableAttribute<?> attribute = new SearchableAttribute<>(client, className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                nonInheritedAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        classAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : allAttributes) {
            SearchableAttribute<?> attribute = new SearchableAttribute<>(client, className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        interfaceAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : cache.getAllAttributesForType(getOriginalName())) {
            SearchableAttribute<?> attribute = new SearchableAttribute<>(client, className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                interfaceAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void resolveRelationships() {
        Set<RelationshipAttributeDef> allRelationships;
        if (getOriginalName().equals("Asset")) {
            allRelationships = cache.getAllRelationshipsForType(getOriginalName());
        } else {
            allRelationships = cache.getAllNonAssetRelationshipsForType(getOriginalName());
        }
        Set<String> uniqueRelationships = cache.getUniqueRelationshipsForType(getOriginalName());
        for (RelationshipAttributeDef relationshipAttributeDef :
                cache.getEntityDefCache().get(getOriginalName()).getRelationshipAttributeDefs()) {
            if (uniqueRelationships.contains(relationshipAttributeDef.getName())) {
                SearchableAttribute<?> attribute =
                        new SearchableAttribute<>(client, className, relationshipAttributeDef, cfg);
                if (!attribute.getType().getName().equals("Internal")) {
                    nonInheritedAttributes.add(attribute);
                    checkAndAddMapContainer(attribute);
                }
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : allRelationships) {
            SearchableAttribute<?> attribute =
                    new SearchableAttribute<>(client, className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : cache.getAllRelationshipsForType(getOriginalName())) {
            SearchableAttribute<?> attribute =
                    new SearchableAttribute<>(client, className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                interfaceAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
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
    public int compareTo(AssetGenerator o) {
        return assetComparator.compare(this, o);
    }
}
