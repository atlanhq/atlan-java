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
 * Instance of an element within a DetectiData Dossier in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DetectiDataDossierElement extends Asset
        implements IDetectiDataDossierElement, IDetectiData, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DetectiDataDossierElement";

    /** Fixed typeName for DetectiDataDossierElements. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Score of this individual component of the Dossier. */
    @Attribute
    Long detectiDataComponentScore;

    /** DetectiData Dossier in which this element is combined with others. */
    @Attribute
    IDetectiDataDossier detectiDataDossier;

    /** Simple name of the dossier in which this asset exists, or empty if it is itself a dossier. */
    @Attribute
    String detectiDataDossierName;

    /** Unique name of the dossier in which this asset exists, or empty if it is itself a dossier. */
    @Attribute
    String detectiDataDossierQualifiedName;

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

    /**
     * Builds the minimal object necessary to create a relationship to a DetectiDataDossierElement, from a potentially
     * more-complete DetectiDataDossierElement object.
     *
     * @return the minimal object necessary to relate to the DetectiDataDossierElement
     * @throws InvalidRequestException if any of the minimal set of required properties for a DetectiDataDossierElement relationship are not found in the initial object
     */
    @Override
    public DetectiDataDossierElement trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DetectiDataDossierElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DetectiDataDossierElement assets will be included.
     *
     * @return a fluent search that includes all DetectiDataDossierElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DetectiDataDossierElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DetectiDataDossierElement assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DetectiDataDossierElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DetectiDataDossierElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DetectiDataDossierElements will be included
     * @return a fluent search that includes all DetectiDataDossierElement assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DetectiDataDossierElement assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DetectiDataDossierElements will be included
     * @return a fluent search that includes all DetectiDataDossierElement assets
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
     * Reference to a DetectiDataDossierElement by GUID. Use this to create a relationship to this DetectiDataDossierElement,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DetectiDataDossierElement to reference
     * @return reference to a DetectiDataDossierElement that can be used for defining a relationship to a DetectiDataDossierElement
     */
    public static DetectiDataDossierElement refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DetectiDataDossierElement by GUID. Use this to create a relationship to this DetectiDataDossierElement,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DetectiDataDossierElement to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DetectiDataDossierElement that can be used for defining a relationship to a DetectiDataDossierElement
     */
    public static DetectiDataDossierElement refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DetectiDataDossierElement._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DetectiDataDossierElement by qualifiedName. Use this to create a relationship to this DetectiDataDossierElement,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DetectiDataDossierElement to reference
     * @return reference to a DetectiDataDossierElement that can be used for defining a relationship to a DetectiDataDossierElement
     */
    public static DetectiDataDossierElement refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DetectiDataDossierElement by qualifiedName. Use this to create a relationship to this DetectiDataDossierElement,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DetectiDataDossierElement to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DetectiDataDossierElement that can be used for defining a relationship to a DetectiDataDossierElement
     */
    public static DetectiDataDossierElement refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DetectiDataDossierElement._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DetectiDataDossierElement by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DetectiDataDossierElement to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DetectiDataDossierElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DetectiDataDossierElement does not exist or the provided GUID is not a DetectiDataDossierElement
     */
    @JsonIgnore
    public static DetectiDataDossierElement get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DetectiDataDossierElement by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DetectiDataDossierElement to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DetectiDataDossierElement, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DetectiDataDossierElement does not exist or the provided GUID is not a DetectiDataDossierElement
     */
    @JsonIgnore
    public static DetectiDataDossierElement get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DetectiDataDossierElement by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DetectiDataDossierElement to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DetectiDataDossierElement, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DetectiDataDossierElement does not exist or the provided GUID is not a DetectiDataDossierElement
     */
    @JsonIgnore
    public static DetectiDataDossierElement get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DetectiDataDossierElement) {
                return (DetectiDataDossierElement) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DetectiDataDossierElement) {
                return (DetectiDataDossierElement) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DetectiDataDossierElement to active.
     *
     * @param qualifiedName for the DetectiDataDossierElement
     * @return true if the DetectiDataDossierElement is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DetectiDataDossierElement to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DetectiDataDossierElement
     * @return true if the DetectiDataDossierElement is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DetectiDataDossierElement.
     *
     * @param name of the DetectiDataDossierElement
     * @param dossier in which the DetectiDataDossierElement should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the DetectiDataDossierElement, as a builder
     * @throws InvalidRequestException if the dossier provided is without any required attributes
     */
    public static DetectiDataDossierElementBuilder<?, ?> creator(String name, DetectiDataDossier dossier)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", dossier.getQualifiedName());
        map.put("name", dossier.getName());
        validateRelationship(DetectiDataDossier.TYPE_NAME, map);
        return creator(name, dossier.getQualifiedName(), dossier.getName())
                .detectiDataDossier(dossier.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DetectiDataDossierElement.
     *
     * @param name of the DetectiDataDossierElement (must be unique within the dossier)
     * @param dossierQualifiedName unique name of the DetectiDataDossier in which the DetectiDataDossierElement exists
     * @param dossierName simple human-readable name of the DetectiDataDossier in which the DetectiDataDossierElement exists
     * @return the minimal object necessary to create the DetectiDataDossierElement, as a builder
     */
    public static DetectiDataDossierElementBuilder<?, ?> creator(
            String name, String dossierQualifiedName, String dossierName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(dossierQualifiedName);
        return DetectiDataDossierElement._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, dossierQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .detectiDataDossierQualifiedName(dossierQualifiedName)
                .detectiDataDossierName(dossierName)
                .detectiDataDossier(DetectiDataDossier.refByQualifiedName(dossierQualifiedName));
    }

    /**
     * Generate a unique DetectiDataDossierElement name.
     *
     * @param name of the DetectiDataDossierElement
     * @param dossierQualifiedName unique name of the DetectiDataDossier in which this DetectiDataDossierElement exists
     * @return a unique name for the DetectiDataDossierElement
     */
    public static String generateQualifiedName(String name, String dossierQualifiedName) {
        return dossierQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the minimal request necessary to update the DetectiDataDossierElement, as a builder
     */
    public static DetectiDataDossierElementBuilder<?, ?> updater(String qualifiedName, String name) {
        return DetectiDataDossierElement._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DetectiDataDossierElement, from a potentially
     * more-complete DetectiDataDossierElement object.
     *
     * @return the minimal object necessary to update the DetectiDataDossierElement, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DetectiDataDossierElement are not found in the initial object
     */
    @Override
    public DetectiDataDossierElementBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DetectiDataDossierElement's owners
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DetectiDataDossierElement, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant on which to update the DetectiDataDossierElement's certificate
     * @param qualifiedName of the DetectiDataDossierElement
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DetectiDataDossierElement, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DetectiDataDossierElement)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DetectiDataDossierElement's certificate
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant on which to update the DetectiDataDossierElement's announcement
     * @param qualifiedName of the DetectiDataDossierElement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DetectiDataDossierElement)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeAnnouncement(String qualifiedName, String name)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan client from which to remove the DetectiDataDossierElement's announcement
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the updated DetectiDataDossierElement, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DetectiDataDossierElement.
     *
     * @param qualifiedName for the DetectiDataDossierElement
     * @param name human-readable name of the DetectiDataDossierElement
     * @param terms the list of terms to replace on the DetectiDataDossierElement, or null to remove all terms from the DetectiDataDossierElement
     * @return the DetectiDataDossierElement that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DetectiDataDossierElement's assigned terms
     * @param qualifiedName for the DetectiDataDossierElement
     * @param name human-readable name of the DetectiDataDossierElement
     * @param terms the list of terms to replace on the DetectiDataDossierElement, or null to remove all terms from the DetectiDataDossierElement
     * @return the DetectiDataDossierElement that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DetectiDataDossierElement) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DetectiDataDossierElement, without replacing existing terms linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DetectiDataDossierElement
     * @param terms the list of terms to append to the DetectiDataDossierElement
     * @return the DetectiDataDossierElement that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DetectiDataDossierElement, without replacing existing terms linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DetectiDataDossierElement
     * @param qualifiedName for the DetectiDataDossierElement
     * @param terms the list of terms to append to the DetectiDataDossierElement
     * @return the DetectiDataDossierElement that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DetectiDataDossierElement) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DetectiDataDossierElement, without replacing all existing terms linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DetectiDataDossierElement
     * @param terms the list of terms to remove from the DetectiDataDossierElement, which must be referenced by GUID
     * @return the DetectiDataDossierElement that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DetectiDataDossierElement, without replacing all existing terms linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DetectiDataDossierElement
     * @param qualifiedName for the DetectiDataDossierElement
     * @param terms the list of terms to remove from the DetectiDataDossierElement, which must be referenced by GUID
     * @return the DetectiDataDossierElement that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DetectiDataDossierElement removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DetectiDataDossierElement) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DetectiDataDossierElement, without replacing existing Atlan tags linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DetectiDataDossierElement
     */
    public static DetectiDataDossierElement appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DetectiDataDossierElement, without replacing existing Atlan tags linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DetectiDataDossierElement
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DetectiDataDossierElement
     */
    public static DetectiDataDossierElement appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DetectiDataDossierElement) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DetectiDataDossierElement, without replacing existing Atlan tags linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DetectiDataDossierElement
     */
    public static DetectiDataDossierElement appendAtlanTags(
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
     * Add Atlan tags to a DetectiDataDossierElement, without replacing existing Atlan tags linked to the DetectiDataDossierElement.
     * Note: this operation must make two API calls — one to retrieve the DetectiDataDossierElement's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DetectiDataDossierElement
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DetectiDataDossierElement
     */
    public static DetectiDataDossierElement appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DetectiDataDossierElement) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DetectiDataDossierElement
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DetectiDataDossierElement.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DetectiDataDossierElement
     * @param qualifiedName of the DetectiDataDossierElement
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DetectiDataDossierElement
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
