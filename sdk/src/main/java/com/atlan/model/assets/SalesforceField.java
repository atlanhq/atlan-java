/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Salesforce field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SalesforceField extends Asset
        implements ISalesforceField, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceField";

    /** Fixed typeName for SalesforceFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of this asset in the Salesforce API. */
    @Attribute
    String apiName;

    /** Data type of values in this field. */
    @Attribute
    String dataType;

    /** Formula for the default value for this field. */
    @Attribute
    String defaultValueFormula;

    /** Formula for this field, if it is a calculated field. */
    @Attribute
    String formula;

    /** Help text for this field. */
    @Attribute
    String inlineHelpText;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Whether this field is calculated (true) or not (false). */
    @Attribute
    Boolean isCalculated;

    /** Whether this field is case sensitive (true) or in-sensitive (false). */
    @Attribute
    Boolean isCaseSensitive;

    /** Whether this field is encrypted (true) or not (false). */
    @Attribute
    Boolean isEncrypted;

    /** Whether this field allows null values (true) or not (false). */
    @Attribute
    Boolean isNullable;

    /** Whether this field references a record of multiple objects (true) or not (false). */
    @Attribute
    Boolean isPolymorphicForeignKey;

    /** Whether this field must have unique values (true) or not (false). */
    @Attribute
    Boolean isUnique;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceObject> lookupObjects;

    /** Maximum length of this field. */
    @Attribute
    Long maxLength;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Number of digits allowed to the right of the decimal point. */
    @Attribute
    Double numericScale;

    /** Object in which this field exists. */
    @Attribute
    ISalesforceObject object;

    /** Unique name of the object in which this field exists. */
    @Attribute
    String objectQualifiedName;

    /** Order (position) of this field within the object. */
    @Attribute
    Integer order;

    /** Fully-qualified name of the organization in Salesforce. */
    @Attribute
    String organizationQualifiedName;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /** List of values from which a user can pick while adding a record. */
    @Attribute
    @Singular
    SortedSet<String> picklistValues;

    /** Total number of digits allowed. */
    @Attribute
    Integer precision;

    /**
     * Builds the minimal object necessary to create a relationship to a SalesforceField, from a potentially
     * more-complete SalesforceField object.
     *
     * @return the minimal object necessary to relate to the SalesforceField
     * @throws InvalidRequestException if any of the minimal set of required properties for a SalesforceField relationship are not found in the initial object
     */
    @Override
    public SalesforceField trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all SalesforceField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SalesforceField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SalesforceField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SalesforceFields will be included
     * @return a fluent search that includes all SalesforceField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a SalesforceField by GUID. Use this to create a relationship to this SalesforceField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SalesforceField to reference
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceField by GUID. Use this to create a relationship to this SalesforceField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SalesforceField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SalesforceField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SalesforceField by qualifiedName. Use this to create a relationship to this SalesforceField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SalesforceField to reference
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceField by qualifiedName. Use this to create a relationship to this SalesforceField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SalesforceField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SalesforceField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SalesforceField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist or the provided GUID is not a SalesforceField
     */
    @JsonIgnore
    public static SalesforceField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SalesforceField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SalesforceField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist or the provided GUID is not a SalesforceField
     */
    @JsonIgnore
    public static SalesforceField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SalesforceField) {
                return (SalesforceField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SalesforceField) {
                return (SalesforceField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SalesforceField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceField, including any relationships
     * @return the requested SalesforceField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist or the provided GUID is not a SalesforceField
     */
    @JsonIgnore
    public static SalesforceField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SalesforceField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SalesforceField
     * @return the requested SalesforceField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist or the provided GUID is not a SalesforceField
     */
    @JsonIgnore
    public static SalesforceField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SalesforceField.select(client)
                    .where(SalesforceField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SalesforceField) {
                return (SalesforceField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SalesforceField.select(client)
                    .where(SalesforceField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SalesforceField) {
                return (SalesforceField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceField
     * @return true if the SalesforceField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceField asset.
     *
     * @param name of the field
     * @param object Salesforce object through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided object does not have a qualifiedName
     */
    public static SalesforceFieldBuilder<?, ?> creator(String name, SalesforceObject object)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", object.getQualifiedName());
        map.put("connectionQualifiedName", object.getConnectionQualifiedName());
        map.put("organizationQualifiedName", object.getOrganizationQualifiedName());
        validateRelationship(SalesforceObject.TYPE_NAME, map);
        return creator(
                        name,
                        object.getConnectionQualifiedName(),
                        object.getOrganizationQualifiedName(),
                        object.getQualifiedName())
                .object(object.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceField asset.
     *
     * @param name of the field
     * @param objectQualifiedName unique name of the object through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> creator(String name, String objectQualifiedName) {
        String organizationQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(objectQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName, objectQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceField asset.
     *
     * @param name of the field
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceField
     * @param organizationQualifiedName unique name of the SalesforceOrganization in which to create the SalesforceField
     * @param objectQualifiedName unique name of the object through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String organizationQualifiedName, String objectQualifiedName) {
        return SalesforceField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, objectQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .organizationQualifiedName(organizationQualifiedName)
                .object(SalesforceObject.refByQualifiedName(objectQualifiedName))
                .objectQualifiedName(objectQualifiedName);
    }

    /**
     * Generate a unique SalesforceField name.
     *
     * @param name unique name of the object within Salesforce
     * @param objectQualifiedName unique name of the object through which the asset is accessible
     * @return a unique name for the SalesforceField
     */
    public static String generateQualifiedName(String name, String objectQualifiedName) {
        return objectQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the minimal request necessary to update the SalesforceField, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceField, from a potentially
     * more-complete SalesforceField object.
     *
     * @return the minimal object necessary to update the SalesforceField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceField are not found in the initial object
     */
    @Override
    public SalesforceFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SalesforceFieldBuilder<
                    C extends SalesforceField, B extends SalesforceFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceField's owners
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceField's certificate
     * @param qualifiedName of the SalesforceField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceField's certificate
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceField's announcement
     * @param qualifiedName of the SalesforceField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceField.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceField's announcement
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceField's assigned terms
     * @param qualifiedName for the SalesforceField
     * @param name human-readable name of the SalesforceField
     * @param terms the list of terms to replace on the SalesforceField, or null to remove all terms from the SalesforceField
     * @return the SalesforceField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceField, without replacing existing terms linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceField
     * @param qualifiedName for the SalesforceField
     * @param terms the list of terms to append to the SalesforceField
     * @return the SalesforceField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceField, without replacing all existing terms linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceField
     * @param qualifiedName for the SalesforceField
     * @param terms the list of terms to remove from the SalesforceField, which must be referenced by GUID
     * @return the SalesforceField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceField, without replacing existing Atlan tags linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceField
     * @param qualifiedName of the SalesforceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SalesforceField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SalesforceField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceField, without replacing existing Atlan tags linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceField
     * @param qualifiedName of the SalesforceField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SalesforceField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SalesforceField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceField
     * @param qualifiedName of the SalesforceField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
