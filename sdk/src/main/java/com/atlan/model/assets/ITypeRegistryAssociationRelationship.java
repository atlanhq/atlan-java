/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.TypeRegistryPropagationType;
import com.atlan.model.enums.TypeRegistryStatus;
import com.atlan.model.fields.BooleanField;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.TypeRegistryEndDef;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Definition of an association (peer-to-peer) relationship in the metamodel.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface ITypeRegistryAssociationRelationship {

    public static final String TYPE_NAME = "TypeRegistryAssociationRelationship";

    /** First end of the relationship. */
    KeywordField TYPE_REGISTRY_END1 = new KeywordField("typeRegistryEnd1", "typeRegistryEnd1");

    /** Second end of the relationship. */
    KeywordField TYPE_REGISTRY_END2 = new KeywordField("typeRegistryEnd2", "typeRegistryEnd2");

    /** Whether this relationship supports many of end1 referencing many of end2 (and vice versa). */
    BooleanField TYPE_REGISTRY_MANY_TO_MANY = new BooleanField("typeRegistryManyToMany", "typeRegistryManyToMany");

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** Unique name for this asset. This is typically a concatenation of the asset's name onto its parent's qualifiedName. This must be unique across all assets of the same type. */
    String getQualifiedName();

    /** Attributes contained within the relationship. */
    SortedSet<ITypeRegistryAttribute> getTypeRegistryAttributes();

    /** Date and time at which this portion of the metamodel was deprecated. */
    Long getTypeRegistryDeprecatedAt();

    /** User who deprecated this portion of the metamodel. */
    String getTypeRegistryDeprecatedBy();

    /** Explanatory definition of this portion of the metamodel. */
    String getTypeRegistryDescription();

    /** First end of the relationship. */
    TypeRegistryEndDef getTypeRegistryEnd1();

    /** Second end of the relationship. */
    TypeRegistryEndDef getTypeRegistryEnd2();

    /** Whether this portion of the metamodel is deprecated (marked for removal). */
    Boolean getTypeRegistryIsDeprecated();

    /** Whether this relationship supports many of end1 referencing many of end2 (and vice versa). */
    Boolean getTypeRegistryManyToMany();

    /** Unique name for this portion of the metamodel. */
    String getTypeRegistryName();

    /** Namespace in which the relationship's definition is contained. */
    ITypeRegistryNamespace getTypeRegistryNamespace();

    /** Metamodel object that replaces this one. */
    ITypeRegistry getTypeRegistryReplacedBy();

    /** Metamodel object(s) that are replaced by this one. */
    SortedSet<ITypeRegistry> getTypeRegistryReplaces();

    /** Status of this portion of the metamodel. */
    TypeRegistryStatus getTypeRegistryStatus();

    /** How tags should propagate through this relationship, if at all. */
    TypeRegistryPropagationType getTypeRegistryTagPropagation();

    /** Version of this relationship's definition. */
    String getTypeRegistryVersion();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** URL of an icon to use for this asset. (Only applies to CustomEntity and Fivetran Catalog assets, currently.) */
    String getIconUrl();

    /** Built-in connector type through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** Custom connector type through which this asset is accessible. */
    String getCustomConnectorType();

    /** Name of the type that defines the asset. */
    String getTypeName();

    /** Globally-unique identifier for the asset. */
    String getGuid();

    /** Human-readable name of the asset. */
    String getDisplayText();

    /** Status of the asset (if this is a related asset). */
    String getEntityStatus();

    /** Type of the relationship (if this is a related asset). */
    String getRelationshipType();

    /** Unique identifier of the relationship (when this is a related asset). */
    String getRelationshipGuid();

    /** Status of the relationship (when this is a related asset). */
    AtlanStatus getRelationshipStatus();

    /** Attributes specific to the relationship (unused). */
    RelationshipAttributes getRelationshipAttributes();

    /**
     * Attribute(s) that uniquely identify the asset (when this is a related asset).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes getUniqueAttributes();

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    boolean isComplete();

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     * @return true if it is a valid GUID reference, false otherwise
     */
    boolean isValidReferenceByGuid();

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    boolean isValidReferenceByQualifiedName();
}
