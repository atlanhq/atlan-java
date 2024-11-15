/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SchemaRegistrySchemaCompatibility;
import com.atlan.model.enums.SchemaRegistrySchemaType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a schema registry subject in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SchemaRegistrySubject extends Asset
        implements ISchemaRegistrySubject, ISchemaRegistry, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SchemaRegistrySubject";

    /** Fixed typeName for SchemaRegistrySubjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ApplicationContainer asset containing this Catalog asset. */
    @Attribute
    IApplicationContainer applicationContainer;

    /** Qualified name of the Application Container that contains this asset. */
    @Attribute
    String assetApplicationQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAsset> assets;

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

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

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

    /** Unique identifier for schema definition set by the schema registry. */
    @Attribute
    String schemaRegistrySchemaId;

    /** Type of language or specification used to define the schema, for example: JSON, Protobuf, etc. */
    @Attribute
    SchemaRegistrySchemaType schemaRegistrySchemaType;

    /** Base name of the subject, without -key, -value prefixes. */
    @Attribute
    String schemaRegistrySubjectBaseName;

    /** List of asset qualified names that this subject is governing/validating. */
    @Attribute
    @Singular
    SortedSet<String> schemaRegistrySubjectGoverningAssetQualifiedNames;

    /** Whether the subject is a schema for the keys of the messages (true) or not (false). */
    @Attribute
    Boolean schemaRegistrySubjectIsKeySchema;

    /** Definition of the latest schema in the subject. */
    @Attribute
    String schemaRegistrySubjectLatestSchemaDefinition;

    /** Latest schema version of the subject. */
    @Attribute
    String schemaRegistrySubjectLatestSchemaVersion;

    /** Compatibility of the schema across versions. */
    @Attribute
    SchemaRegistrySchemaCompatibility schemaRegistrySubjectSchemaCompatibility;

    /**
     * Builds the minimal object necessary to create a relationship to a SchemaRegistrySubject, from a potentially
     * more-complete SchemaRegistrySubject object.
     *
     * @return the minimal object necessary to relate to the SchemaRegistrySubject
     * @throws InvalidRequestException if any of the minimal set of required properties for a SchemaRegistrySubject relationship are not found in the initial object
     */
    @Override
    public SchemaRegistrySubject trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SchemaRegistrySubject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SchemaRegistrySubject assets will be included.
     *
     * @return a fluent search that includes all SchemaRegistrySubject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all SchemaRegistrySubject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SchemaRegistrySubject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SchemaRegistrySubject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SchemaRegistrySubject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SchemaRegistrySubjects will be included
     * @return a fluent search that includes all SchemaRegistrySubject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all SchemaRegistrySubject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SchemaRegistrySubjects will be included
     * @return a fluent search that includes all SchemaRegistrySubject assets
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
     * Reference to a SchemaRegistrySubject by GUID. Use this to create a relationship to this SchemaRegistrySubject,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SchemaRegistrySubject to reference
     * @return reference to a SchemaRegistrySubject that can be used for defining a relationship to a SchemaRegistrySubject
     */
    public static SchemaRegistrySubject refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SchemaRegistrySubject by GUID. Use this to create a relationship to this SchemaRegistrySubject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SchemaRegistrySubject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SchemaRegistrySubject that can be used for defining a relationship to a SchemaRegistrySubject
     */
    public static SchemaRegistrySubject refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SchemaRegistrySubject._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SchemaRegistrySubject by qualifiedName. Use this to create a relationship to this SchemaRegistrySubject,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SchemaRegistrySubject to reference
     * @return reference to a SchemaRegistrySubject that can be used for defining a relationship to a SchemaRegistrySubject
     */
    public static SchemaRegistrySubject refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SchemaRegistrySubject by qualifiedName. Use this to create a relationship to this SchemaRegistrySubject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SchemaRegistrySubject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SchemaRegistrySubject that can be used for defining a relationship to a SchemaRegistrySubject
     */
    public static SchemaRegistrySubject refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SchemaRegistrySubject._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SchemaRegistrySubject by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SchemaRegistrySubject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SchemaRegistrySubject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SchemaRegistrySubject does not exist or the provided GUID is not a SchemaRegistrySubject
     */
    @JsonIgnore
    public static SchemaRegistrySubject get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SchemaRegistrySubject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SchemaRegistrySubject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SchemaRegistrySubject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SchemaRegistrySubject does not exist or the provided GUID is not a SchemaRegistrySubject
     */
    @JsonIgnore
    public static SchemaRegistrySubject get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a SchemaRegistrySubject by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SchemaRegistrySubject to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SchemaRegistrySubject, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SchemaRegistrySubject does not exist or the provided GUID is not a SchemaRegistrySubject
     */
    @JsonIgnore
    public static SchemaRegistrySubject get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SchemaRegistrySubject) {
                return (SchemaRegistrySubject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SchemaRegistrySubject) {
                return (SchemaRegistrySubject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SchemaRegistrySubject to active.
     *
     * @param qualifiedName for the SchemaRegistrySubject
     * @return true if the SchemaRegistrySubject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SchemaRegistrySubject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SchemaRegistrySubject
     * @return true if the SchemaRegistrySubject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the minimal request necessary to update the SchemaRegistrySubject, as a builder
     */
    public static SchemaRegistrySubjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return SchemaRegistrySubject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SchemaRegistrySubject, from a potentially
     * more-complete SchemaRegistrySubject object.
     *
     * @return the minimal object necessary to update the SchemaRegistrySubject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SchemaRegistrySubject are not found in the initial object
     */
    @Override
    public SchemaRegistrySubjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SchemaRegistrySubject's owners
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SchemaRegistrySubject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant on which to update the SchemaRegistrySubject's certificate
     * @param qualifiedName of the SchemaRegistrySubject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SchemaRegistrySubject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SchemaRegistrySubject)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SchemaRegistrySubject's certificate
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant on which to update the SchemaRegistrySubject's announcement
     * @param qualifiedName of the SchemaRegistrySubject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SchemaRegistrySubject)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan client from which to remove the SchemaRegistrySubject's announcement
     * @param qualifiedName of the SchemaRegistrySubject
     * @param name of the SchemaRegistrySubject
     * @return the updated SchemaRegistrySubject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SchemaRegistrySubject.
     *
     * @param qualifiedName for the SchemaRegistrySubject
     * @param name human-readable name of the SchemaRegistrySubject
     * @param terms the list of terms to replace on the SchemaRegistrySubject, or null to remove all terms from the SchemaRegistrySubject
     * @return the SchemaRegistrySubject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SchemaRegistrySubject's assigned terms
     * @param qualifiedName for the SchemaRegistrySubject
     * @param name human-readable name of the SchemaRegistrySubject
     * @param terms the list of terms to replace on the SchemaRegistrySubject, or null to remove all terms from the SchemaRegistrySubject
     * @return the SchemaRegistrySubject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SchemaRegistrySubject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SchemaRegistrySubject, without replacing existing terms linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SchemaRegistrySubject
     * @param terms the list of terms to append to the SchemaRegistrySubject
     * @return the SchemaRegistrySubject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SchemaRegistrySubject, without replacing existing terms linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SchemaRegistrySubject
     * @param qualifiedName for the SchemaRegistrySubject
     * @param terms the list of terms to append to the SchemaRegistrySubject
     * @return the SchemaRegistrySubject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SchemaRegistrySubject, without replacing all existing terms linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SchemaRegistrySubject
     * @param terms the list of terms to remove from the SchemaRegistrySubject, which must be referenced by GUID
     * @return the SchemaRegistrySubject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SchemaRegistrySubject, without replacing all existing terms linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SchemaRegistrySubject
     * @param qualifiedName for the SchemaRegistrySubject
     * @param terms the list of terms to remove from the SchemaRegistrySubject, which must be referenced by GUID
     * @return the SchemaRegistrySubject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SchemaRegistrySubject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SchemaRegistrySubject, without replacing existing Atlan tags linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SchemaRegistrySubject
     */
    public static SchemaRegistrySubject appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SchemaRegistrySubject, without replacing existing Atlan tags linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SchemaRegistrySubject
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SchemaRegistrySubject
     */
    public static SchemaRegistrySubject appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SchemaRegistrySubject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SchemaRegistrySubject, without replacing existing Atlan tags linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SchemaRegistrySubject
     */
    public static SchemaRegistrySubject appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SchemaRegistrySubject, without replacing existing Atlan tags linked to the SchemaRegistrySubject.
     * Note: this operation must make two API calls — one to retrieve the SchemaRegistrySubject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SchemaRegistrySubject
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SchemaRegistrySubject
     */
    public static SchemaRegistrySubject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SchemaRegistrySubject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SchemaRegistrySubject.
     *
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SchemaRegistrySubject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SchemaRegistrySubject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SchemaRegistrySubject
     * @param qualifiedName of the SchemaRegistrySubject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SchemaRegistrySubject
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
