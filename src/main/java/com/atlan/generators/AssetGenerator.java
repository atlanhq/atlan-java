/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.typedefs.*;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetGenerator extends TypeGenerator {

    public static final String DIRECTORY = "assets";

    private final EntityDef entityDef;
    private String parentClassName;
    private SortedSet<Attribute<?>> interfaceAttributes;
    private SortedSet<Attribute<?>> classAttributes;
    private SortedSet<Attribute<?>> nonInheritedAttributes;
    private List<AssetGenerator> originalSuperTypes = null;
    private List<AssetGenerator> fullSubTypes = null;
    private List<String> originalSubTypes = null;
    private List<String> subTypes = null;
    private List<String> mapContainers = null;
    private final Set<String> superTypes;

    public AssetGenerator(EntityDef entityDef, GeneratorConfig cfg) {
        super(entityDef, cfg);
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

    public String getClassTemplateFile() {
        try {
            return cfg.getFreemarkerConfig().getTemplate(className + ".ftl").getSourceName();
        } catch (TemplateNotFoundException e) {
            // Do nothing - no template to load or otherwise handle
        } catch (IOException e) {
            log.error("Error reading template: {}.ftl", className, e);
        }
        return null;
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

    public boolean isBuiltIn(String orgName, String reTyped) {
        if (orgName != null) {
            EntityDef entity = cache.getEntityDefCache().get(orgName);
            if (entity != null) {
                return (entity.getServiceType() != null
                        && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(entity.getServiceType()));
            } else {
                StructDef struct = cache.getStructDefCache().get(orgName);
                if (struct != null) {
                    return (struct.getServiceType() != null
                                    && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(struct.getServiceType()))
                            || GeneratorConfig.BUILT_IN_STRUCTS.contains(orgName);
                } else {
                    EnumDef enumDef = cache.getEnumDefCache().get(orgName);
                    return (enumDef != null
                                    && enumDef.getServiceType() != null
                                    && TypeDefsEndpoint.RESERVED_SERVICE_TYPES.contains(enumDef.getServiceType()))
                            || GeneratorConfig.BUILT_IN_ENUMS.contains(orgName)
                            || (orgName.equals("string") && GeneratorConfig.BUILT_IN_ENUMS.contains(reTyped));
                }
            }
        }
        return false;
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
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                nonInheritedAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        classAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : allAttributes) {
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        interfaceAttributes = new TreeSet<>();
        for (AttributeDef attributeDef : cache.getAllAttributesForType(getOriginalName())) {
            Attribute<?> attribute = new Attribute<>(className, attributeDef, cfg);
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
                Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
                if (!attribute.getType().getName().equals("Internal")) {
                    nonInheritedAttributes.add(attribute);
                    checkAndAddMapContainer(attribute);
                }
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : allRelationships) {
            Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                classAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
        for (RelationshipAttributeDef relationshipAttributeDef : cache.getAllRelationshipsForType(getOriginalName())) {
            Attribute<?> attribute = new Attribute<>(className, relationshipAttributeDef, cfg);
            if (!attribute.getType().getName().equals("Internal")) {
                interfaceAttributes.add(attribute);
                checkAndAddMapContainer(attribute);
            }
        }
    }

    private void checkAndAddMapContainer(Attribute<?> attribute) {
        if (attribute.getType().getContainer() != null
                && attribute.getType().getContainer().contains("Map")) {
            if (mapContainers == null) {
                mapContainers = new ArrayList<>();
            }
            mapContainers.add(attribute.getRenamed());
        }
    }

    @Getter
    public static class Attribute<T extends Attribute<?>> extends AttributeGenerator implements Comparable<T> {

        // Sort attribute definitions in a set based purely on their name (two attributes
        // in the same set with the same name should be a conflict / duplicate)
        private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
        private static final Comparator<Attribute<?>> attributeComparator =
                Comparator.comparing(Attribute::getRenamed, stringComparator);

        protected Attribute(GeneratorConfig cfg) {
            super(cfg);
        }

        public Attribute(String className, AttributeDef attributeDef, GeneratorConfig cfg) {
            super(className, attributeDef, cfg);
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
            String assetTypeOverride = cfg.resolveAttributeToTypeOverride(className, getOriginalName());
            if (assetTypeOverride != null) {
                setType(getType().toBuilder()
                        .name(assetTypeOverride)
                        .type(MappedType.Type.ASSET)
                        .build());
                setRetyped(true);
            }
        }

        public String getSingular() {
            if (getType().getContainer() != null) {
                return cfg.resolveSingular(getOriginalName());
            }
            return null;
        }

        /** {@inheritDoc} */
        @Override
        @SuppressWarnings("rawtypes")
        public int compareTo(Attribute o) {
            return attributeComparator.compare(this, o);
        }
    }
}
