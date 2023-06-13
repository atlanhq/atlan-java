/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator {

    public static final String DIRECTORY = "assets";

    private final EntityDef entityDef;
    private String parentClassName;
    private List<Attribute> attributes;
    private List<AssetGenerator> originalSuperTypes = null;
    private List<AssetGenerator> fullSubTypes = null;
    private List<String> originalSubTypes = null;
    private List<String> subTypes = null;
    private List<String> mapContainers = null;

    public AssetGenerator(EntityDef entityDef, GeneratorConfig cfg) {
        super(entityDef, cfg);
        this.entityDef = entityDef;
        resolveClassName();
        super.description = AttributeCSVCache.getTypeDescription(originalName);
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(originalName);
    }

    public void resolveDetails() {
        resolveParentClassName();
        resolveSubTypes();
        resolveAttributes();
        resolveRelationships();
    }

    public String getTemplateFile() {
        try {
            return cfg.getFreemarkerConfig().getTemplate(className + ".ftl").getSourceName();
        } catch (TemplateNotFoundException e) {
            // Do nothing - no template to load or otherwise handle
        } catch (IOException e) {
            log.error("Error reading template: {}.ftl", className, e);
        }
        return null;
    }

    public boolean isAbstract() {
        return (originalSubTypes != null && !originalSubTypes.isEmpty()) && !cfg.forceNonAbstract(getOriginalName());
    }

    public void resolveParentClassName() {
        String parentOriginalName = cfg.getSingleTypeToExtend(originalName, entityDef.getSuperTypes());
        this.parentClassName = cfg.resolveClassName(parentOriginalName);
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
                        String flattened = cfg.getSingleTypeToExtend(
                                originalSubType, sub.getEntityDef().getSuperTypes());
                        // Only output the subtype if that subtype still considers this type its parent,
                        // after polymorphic flattening...
                        if (flattened == null || flattened.equals(getOriginalName())) {
                            subTypes.add(subType.getName());
                        }
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
        attributes = new ArrayList<>();
        for (AttributeDef attributeDef : entityDef.getAttributeDefs()) {
            Attribute attribute = new Attribute(className, attributeDef);
            if (!attribute.getType().getName().equals("Internal")) {
                attributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void resolveRelationships() {
        Set<String> uniqueRelationships = cache.getUniqueRelationshipsForType(getOriginalName());
        for (RelationshipAttributeDef relationshipAttributeDef : entityDef.getRelationshipAttributeDefs()) {
            Attribute attribute = new Attribute(className, relationshipAttributeDef);
            if (uniqueRelationships.contains(attribute.getOriginalName())
                    && !attribute.getType().getName().equals("Internal")) {
                boolean duplicate = false;
                for (Attribute existing : attributes) {
                    if (existing.getRenamed().equals(attribute.getRenamed())) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    log.warn(
                            "Found duplicate relationship defined in {} for {} ({}) - skipping.",
                            className,
                            attribute.getRenamed(),
                            attribute.getFullType());
                } else {
                    attributes.add(attribute);
                    checkAndAddMapContainer(attribute);
                }
            }
        }
    }

    private void checkAndAddMapContainer(Attribute attribute) {
        if (attribute.getType().getContainer() != null
                && attribute.getType().getContainer().contains("Map")) {
            if (mapContainers == null) {
                mapContainers = new ArrayList<>();
            }
            mapContainers.add(attribute.getRenamed());
        }
    }

    @Getter
    public static class Attribute extends AttributeGenerator {

        // Override these properties that would normally be SortedSet<> with List<>,
        // as ordering is crucial to their proper operation.
        private static final Set<String> LIST_OVERRIDES = Set.of("policyResources");

        protected Attribute() {}

        public Attribute(String className, AttributeDef attributeDef) {
            super(className, attributeDef);
            if (LIST_OVERRIDES.contains(attributeDef.getName())) {
                setType(getType().toBuilder().container("List<").build());
            }
        }

        @Override
        protected void resolveName() {
            super.resolveName();
            setRenamed(cfg.resolveAttributeName(getOriginalName()));
        }

        @Override
        protected void resolveType(AttributeDef attributeDef) {
            super.resolveType(attributeDef);
            String enumName = cfg.resolveAttributeToEnumeration(getOriginalName());
            if (enumName != null) {
                setType(getType().toBuilder()
                        .name(enumName)
                        .type(MappedType.Type.ENUM)
                        .build());
            }
        }

        public String getSingular() {
            if (getType().getContainer() != null) {
                return cfg.resolveSingular(getOriginalName());
            }
            return null;
        }
    }
}
