/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sigma data element in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SigmaDataElement extends Asset
        implements ISigmaDataElement, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataElement";

    /** Fixed typeName for SigmaDataElements. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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

    /** Number of fields in this data element. */
    @Attribute
    Long sigmaDataElementFieldCount;

    /** Data element fields that exist in this data element. */
    @Attribute
    @Singular
    SortedSet<ISigmaDataElementField> sigmaDataElementFields;

    /** Simple name of the data element in which this asset exists. */
    @Attribute
    String sigmaDataElementName;

    /** Unique name of the data element in which this asset exists. */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** TBC */
    @Attribute
    String sigmaDataElementQuery;

    /** TBC */
    @Attribute
    String sigmaDataElementType;

    /** Page on which this data element exists. */
    @Attribute
    ISigmaPage sigmaPage;

    /** Simple name of the page on which this asset exists. */
    @Attribute
    String sigmaPageName;

    /** Unique name of the page on which this asset exists. */
    @Attribute
    String sigmaPageQualifiedName;

    /** Simple name of the workbook in which this asset exists. */
    @Attribute
    String sigmaWorkbookName;

    /** Unique name of the workbook in which this asset exists. */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a SigmaDataElement, from a potentially
     * more-complete SigmaDataElement object.
     *
     * @return the minimal object necessary to relate to the SigmaDataElement
     * @throws InvalidRequestException if any of the minimal set of required properties for a SigmaDataElement relationship are not found in the initial object
     */
    @Override
    public SigmaDataElement trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElement assets will be included.
     *
     * @return a fluent search that includes all SigmaDataElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElement assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SigmaDataElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElements will be included
     * @return a fluent search that includes all SigmaDataElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElements will be included
     * @return a fluent search that includes all SigmaDataElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElement assets will be included.
     *
     * @return an asset filter that includes all SigmaDataElement assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElement assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all SigmaDataElement assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElements will be included
     * @return an asset filter that includes all SigmaDataElement assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all SigmaDataElement assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElements will be included
     * @return an asset filter that includes all SigmaDataElement assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a SigmaDataElement by GUID. Use this to create a relationship to this SigmaDataElement,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SigmaDataElement to reference
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SigmaDataElement by GUID. Use this to create a relationship to this SigmaDataElement,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SigmaDataElement to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SigmaDataElement._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SigmaDataElement by qualifiedName. Use this to create a relationship to this SigmaDataElement,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataElement to reference
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SigmaDataElement by qualifiedName. Use this to create a relationship to this SigmaDataElement,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SigmaDataElement to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SigmaDataElement that can be used for defining a relationship to a SigmaDataElement
     */
    public static SigmaDataElement refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SigmaDataElement._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SigmaDataElement by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SigmaDataElement to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     */
    @JsonIgnore
    public static SigmaDataElement get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SigmaDataElement by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataElement to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     */
    @JsonIgnore
    public static SigmaDataElement get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a SigmaDataElement by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataElement to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SigmaDataElement, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     */
    @JsonIgnore
    public static SigmaDataElement get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SigmaDataElement) {
                return (SigmaDataElement) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SigmaDataElement) {
                return (SigmaDataElement) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SigmaDataElement by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SigmaDataElement retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a SigmaDataElement by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist or the provided GUID is not a SigmaDataElement
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SigmaDataElement retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a SigmaDataElement by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SigmaDataElement retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a SigmaDataElement by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the SigmaDataElement to retrieve
     * @return the requested full SigmaDataElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElement does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SigmaDataElement retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElement to active.
     *
     * @param qualifiedName for the SigmaDataElement
     * @return true if the SigmaDataElement is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElement to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SigmaDataElement
     * @return true if the SigmaDataElement is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the minimal request necessary to update the SigmaDataElement, as a builder
     */
    public static SigmaDataElementBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataElement._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataElement, from a potentially
     * more-complete SigmaDataElement object.
     *
     * @return the minimal object necessary to update the SigmaDataElement, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataElement are not found in the initial object
     */
    @Override
    public SigmaDataElementBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDataElement", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataElement's owners
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElement, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataElement's certificate
     * @param qualifiedName of the SigmaDataElement
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElement, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaDataElement)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataElement's certificate
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataElement's announcement
     * @param qualifiedName of the SigmaDataElement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SigmaDataElement)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan client from which to remove the SigmaDataElement's announcement
     * @param qualifiedName of the SigmaDataElement
     * @param name of the SigmaDataElement
     * @return the updated SigmaDataElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDataElement.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param name human-readable name of the SigmaDataElement
     * @param terms the list of terms to replace on the SigmaDataElement, or null to remove all terms from the SigmaDataElement
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SigmaDataElement's assigned terms
     * @param qualifiedName for the SigmaDataElement
     * @param name human-readable name of the SigmaDataElement
     * @param terms the list of terms to replace on the SigmaDataElement, or null to remove all terms from the SigmaDataElement
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataElement) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataElement, without replacing existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to append to the SigmaDataElement
     * @return the SigmaDataElement that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SigmaDataElement, without replacing existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SigmaDataElement
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to append to the SigmaDataElement
     * @return the SigmaDataElement that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElement) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElement, without replacing all existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to remove from the SigmaDataElement, which must be referenced by GUID
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElement, without replacing all existing terms linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SigmaDataElement
     * @param qualifiedName for the SigmaDataElement
     * @param terms the list of terms to remove from the SigmaDataElement, which must be referenced by GUID
     * @return the SigmaDataElement that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElement removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElement) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataElement
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SigmaDataElement) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(
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
     * Add Atlan tags to a SigmaDataElement, without replacing existing Atlan tags linked to the SigmaDataElement.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataElement
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElement
     */
    public static SigmaDataElement appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDataElement) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SigmaDataElement
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SigmaDataElement
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElement
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SigmaDataElement.
     *
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataElement
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SigmaDataElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SigmaDataElement
     * @param qualifiedName of the SigmaDataElement
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataElement
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
