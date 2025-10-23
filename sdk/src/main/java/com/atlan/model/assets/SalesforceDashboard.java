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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Salesforce dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SalesforceDashboard extends Asset
        implements ISalesforceDashboard, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceDashboard";

    /** Fixed typeName for SalesforceDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of this asset in the Salesforce API. */
    @Attribute
    String apiName;

    /** Type of dashboard in Salesforce. */
    @Attribute
    String dashboardType;

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

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Organization in which this dashboard exists. */
    @Attribute
    ISalesforceOrganization organization;

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

    /** Number of reports linked to the dashboard in Salesforce. */
    @Attribute
    Long reportCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceReport> reports;

    /** Identifier of the dashboard in Salesforce. */
    @Attribute
    String sourceId;

    /**
     * Builds the minimal object necessary to create a relationship to a SalesforceDashboard, from a potentially
     * more-complete SalesforceDashboard object.
     *
     * @return the minimal object necessary to relate to the SalesforceDashboard
     * @throws InvalidRequestException if any of the minimal set of required properties for a SalesforceDashboard relationship are not found in the initial object
     */
    @Override
    public SalesforceDashboard trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SalesforceDashboard assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SalesforceDashboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SalesforceDashboards will be included
     * @return a fluent search that includes all SalesforceDashboard assets
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
     * Reference to a SalesforceDashboard by GUID. Use this to create a relationship to this SalesforceDashboard,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceDashboard by GUID. Use this to create a relationship to this SalesforceDashboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SalesforceDashboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SalesforceDashboard._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SalesforceDashboard by qualifiedName. Use this to create a relationship to this SalesforceDashboard,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceDashboard by qualifiedName. Use this to create a relationship to this SalesforceDashboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SalesforceDashboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SalesforceDashboard._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SalesforceDashboard, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceDashboard, including any relationships
     * @return the requested SalesforceDashboard, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SalesforceDashboard by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceDashboard to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceDashboard, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SalesforceDashboard
     * @return the requested SalesforceDashboard, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    @JsonIgnore
    public static SalesforceDashboard get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SalesforceDashboard.select(client)
                    .where(SalesforceDashboard.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SalesforceDashboard.select(client)
                    .where(SalesforceDashboard.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SalesforceDashboard) {
                return (SalesforceDashboard) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceDashboard
     * @return true if the SalesforceDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        return SalesforceDashboard.creator(
                name, organizationQualifiedName, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceDashboardBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        return creator(name, organization, UUID.randomUUID().toString()).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
            String name, SalesforceOrganization organization, String salesforceId) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", organization.getQualifiedName());
        map.put("connectionQualifiedName", organization.getConnectionQualifiedName());
        validateRelationship(SalesforceOrganization.TYPE_NAME, map);
        return creator(name, organization.getConnectionQualifiedName(), organization.getQualifiedName(), salesforceId)
                .organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
            String name, String organizationQualifiedName, String salesforceId) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName, salesforceId);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceDashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String organizationQualifiedName, String salesforceId) {
        return SalesforceDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .sourceId(salesforceId)
                .qualifiedName(generateQualifiedName(salesforceId, organizationQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
                .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceDashboard name.
     *
     * @param salesforceId unique identifier of this dashboard in Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceDashboard is accessible
     * @return a unique name for the SalesforceDashboard
     */
    public static String generateQualifiedName(String salesforceId, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + salesforceId;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the minimal request necessary to update the SalesforceDashboard, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceDashboard, from a potentially
     * more-complete SalesforceDashboard object.
     *
     * @return the minimal object necessary to update the SalesforceDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceDashboard are not found in the initial object
     */
    @Override
    public SalesforceDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SalesforceDashboardBuilder<
                    C extends SalesforceDashboard, B extends SalesforceDashboardBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceDashboard's owners
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceDashboard's certificate
     * @param qualifiedName of the SalesforceDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceDashboard)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceDashboard's certificate
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceDashboard's announcement
     * @param qualifiedName of the SalesforceDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceDashboard)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceDashboard's announcement
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceDashboard's assigned terms
     * @param qualifiedName for the SalesforceDashboard
     * @param name human-readable name of the SalesforceDashboard
     * @param terms the list of terms to replace on the SalesforceDashboard, or null to remove all terms from the SalesforceDashboard
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceDashboard, without replacing existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceDashboard
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to append to the SalesforceDashboard
     * @return the SalesforceDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceDashboard, without replacing all existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceDashboard
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to remove from the SalesforceDashboard, which must be referenced by GUID
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SalesforceDashboard appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SalesforceDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceDashboard, without replacing existing Atlan tags linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SalesforceDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SalesforceDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceDashboard
     * @param qualifiedName of the SalesforceDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
