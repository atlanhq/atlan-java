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
import com.atlan.model.enums.FileType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Instance of a static file in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class File extends Asset implements IFile, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "File";

    /** Fixed typeName for Files. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    IAsset fileAssets;

    /** URL giving the online location where the file can be accessed. */
    @Attribute
    String filePath;

    /** Type (extension) of the file. */
    @Attribute
    FileType fileType;

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

    /** Whether the resource is global (true) or not (false). */
    @Attribute
    Boolean isGlobal;

    /** URL to the resource. */
    @Attribute
    String link;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
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

    /** Reference to the resource. */
    @Attribute
    String reference;

    /** Metadata of the resource. */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /**
     * Builds the minimal object necessary to create a relationship to a File, from a potentially
     * more-complete File object.
     *
     * @return the minimal object necessary to relate to the File
     * @throws InvalidRequestException if any of the minimal set of required properties for a File relationship are not found in the initial object
     */
    @Override
    public File trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all File assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) File assets will be included.
     *
     * @return a fluent search that includes all File assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all File assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) File assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all File assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all File assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Files will be included
     * @return a fluent search that includes all File assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all File assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Files will be included
     * @return a fluent search that includes all File assets
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
     * Reference to a File by GUID. Use this to create a relationship to this File,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the File to reference
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a File by GUID. Use this to create a relationship to this File,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the File to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByGuid(String guid, Reference.SaveSemantic semantic) {
        return File._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a File by qualifiedName. Use this to create a relationship to this File,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the File to reference
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a File by qualifiedName. Use this to create a relationship to this File,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the File to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return File._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a File by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the File to retrieve, either its GUID or its full qualifiedName
     * @return the requested full File, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the File does not exist or the provided GUID is not a File
     */
    @JsonIgnore
    public static File get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a File by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the File to retrieve, either its GUID or its full qualifiedName
     * @return the requested full File, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the File does not exist or the provided GUID is not a File
     */
    @JsonIgnore
    public static File get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a File by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the File to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full File, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the File does not exist or the provided GUID is not a File
     */
    @JsonIgnore
    public static File get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof File) {
                return (File) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof File) {
                return (File) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) File to active.
     *
     * @param qualifiedName for the File
     * @return true if the File is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) File to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the File
     * @return true if the File is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a File.
     *
     * @param name of the File (if multiple files with the same name exist in the connection, also include the path that makes this file unique)
     * @param connectionQualifiedName unique name of the connection in which the file is contained
     * @param type of the File
     * @return the minimal request necessary to update the File, as a builder
     */
    public static FileBuilder<?, ?> creator(String name, String connectionQualifiedName, FileType type) {
        return File._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .connectionQualifiedName(connectionQualifiedName)
                .name(name)
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .fileType(type);
    }

    /**
     * Generate a unique File name.
     *
     * @param connectionQualifiedName unique name of the connection in which the file is contained
     * @param name of the File (including any path details, if necessary to ensure this file is unique within the connection)
     * @return a unique name for the File
     */
    public static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + StringUtils.trimPathDelimiters(name);
    }

    /**
     * Builds the minimal object necessary to update a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the minimal request necessary to update the File, as a builder
     */
    public static FileBuilder<?, ?> updater(String qualifiedName, String name) {
        return File._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a File, from a potentially
     * more-complete File object.
     *
     * @return the minimal object necessary to update the File, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for File are not found in the initial object
     */
    @Override
    public FileBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a File.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a File.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (File) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a File.
     *
     * @param client connectivity to the Atlan tenant from which to remove the File's owners
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a File.
     *
     * @param qualifiedName of the File
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated File, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a File.
     *
     * @param client connectivity to the Atlan tenant on which to update the File's certificate
     * @param qualifiedName of the File
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated File, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (File) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a File.
     *
     * @param client connectivity to the Atlan tenant from which to remove the File's certificate
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a File.
     *
     * @param qualifiedName of the File
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a File.
     *
     * @param client connectivity to the Atlan tenant on which to update the File's announcement
     * @param qualifiedName of the File
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (File) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a File.
     *
     * @param client connectivity to the Atlan client from which to remove the File's announcement
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the File.
     *
     * @param qualifiedName for the File
     * @param name human-readable name of the File
     * @param terms the list of terms to replace on the File, or null to remove all terms from the File
     * @return the File that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static File replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the File.
     *
     * @param client connectivity to the Atlan tenant on which to replace the File's assigned terms
     * @param qualifiedName for the File
     * @param name human-readable name of the File
     * @param terms the list of terms to replace on the File, or null to remove all terms from the File
     * @return the File that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static File replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (File) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the File, without replacing existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the File
     * @param terms the list of terms to append to the File
     * @return the File that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static File appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the File, without replacing existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the File
     * @param qualifiedName for the File
     * @param terms the list of terms to append to the File
     * @return the File that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static File appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (File) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a File, without replacing all existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the File
     * @param terms the list of terms to remove from the File, which must be referenced by GUID
     * @return the File that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static File removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a File, without replacing all existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the File
     * @param qualifiedName for the File
     * @param terms the list of terms to remove from the File, which must be referenced by GUID
     * @return the File that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static File removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (File) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the File
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (File) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(
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
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the File
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (File) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a File.
     *
     * @param qualifiedName of the File
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the File
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a File.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a File
     * @param qualifiedName of the File
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the File
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
