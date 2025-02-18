/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
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
 * Instance of a Salesforce report in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class SalesforceReport extends Asset
        implements ISalesforceReport, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceReport";

    /** Fixed typeName for SalesforceReports. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of this asset in the Salesforce API. */
    @Attribute
    String apiName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceDashboard> dashboards;

    /** List of column names on the report. */
    @Attribute
    @Singular
    SortedSet<String> detailColumns;

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

    /** Organization in which this report exists. */
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

    /** Type of report in Salesforce. */
    @Attribute
    @Singular("putReportType")
    Map<String, String> reportType;

    /** Identifier of the report in Salesforce. */
    @Attribute
    String sourceId;

    /**
     * Builds the minimal object necessary to create a relationship to a SalesforceReport, from a potentially
     * more-complete SalesforceReport object.
     *
     * @return the minimal object necessary to relate to the SalesforceReport
     * @throws InvalidRequestException if any of the minimal set of required properties for a SalesforceReport relationship are not found in the initial object
     */
    @Override
    public SalesforceReport trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SalesforceReport assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceReport assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SalesforceReport assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SalesforceReport assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SalesforceReports will be included
     * @return a fluent search that includes all SalesforceReport assets
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
     * Reference to a SalesforceReport by GUID. Use this to create a relationship to this SalesforceReport,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceReport by GUID. Use this to create a relationship to this SalesforceReport,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SalesforceReport to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SalesforceReport._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SalesforceReport by qualifiedName. Use this to create a relationship to this SalesforceReport,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceReport by qualifiedName. Use this to create a relationship to this SalesforceReport,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SalesforceReport to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SalesforceReport._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SalesforceReport by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceReport to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    @JsonIgnore
    public static SalesforceReport get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SalesforceReport by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceReport to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SalesforceReport, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    @JsonIgnore
    public static SalesforceReport get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SalesforceReport) {
                return (SalesforceReport) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SalesforceReport) {
                return (SalesforceReport) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SalesforceReport by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceReport to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceReport, including any relationships
     * @return the requested SalesforceReport, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    @JsonIgnore
    public static SalesforceReport get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SalesforceReport by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceReport to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceReport, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SalesforceReport
     * @return the requested SalesforceReport, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    @JsonIgnore
    public static SalesforceReport get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SalesforceReport.select(client)
                    .where(SalesforceReport.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SalesforceReport) {
                return (SalesforceReport) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SalesforceReport.select(client)
                    .where(SalesforceReport.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SalesforceReport) {
                return (SalesforceReport) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceReport to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceReport
     * @return true if the SalesforceReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        return SalesforceReport.creator(
                name, organizationQualifiedName, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceReportBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        return creator(name, organization, UUID.randomUUID().toString()).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceReportBuilder<?, ?> creator(
            String name, SalesforceOrganization organization, String salesforceId) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", organization.getQualifiedName());
        map.put("connectionQualifiedName", organization.getConnectionQualifiedName());
        validateRelationship(SalesforceOrganization.TYPE_NAME, map);
        return creator(name, organization.getConnectionQualifiedName(), organization.getQualifiedName(), salesforceId)
                .organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(
            String name, String organizationQualifiedName, String salesforceId) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName, salesforceId);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceReport
     * @param organizationQualifiedName unique name of the organization in which to create the SalesforceReport
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String organizationQualifiedName, String salesforceId) {
        return SalesforceReport._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .sourceId(salesforceId)
                .qualifiedName(generateQualifiedName(salesforceId, organizationQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.SALESFORCE)
                .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
                .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceReport name.
     *
     * @param salesforceId unique identifier of this report in Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceReport is accessible
     * @return a unique name for the SalesforceReport
     */
    public static String generateQualifiedName(String salesforceId, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + salesforceId;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the minimal request necessary to update the SalesforceReport, as a builder
     */
    public static SalesforceReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceReport._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceReport, from a potentially
     * more-complete SalesforceReport object.
     *
     * @return the minimal object necessary to update the SalesforceReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceReport are not found in the initial object
     */
    @Override
    public SalesforceReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceReport's owners
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceReport's certificate
     * @param qualifiedName of the SalesforceReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceReport)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceReport's certificate
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceReport's announcement
     * @param qualifiedName of the SalesforceReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceReport)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceReport.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceReport's announcement
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceReport) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceReport's assigned terms
     * @param qualifiedName for the SalesforceReport
     * @param name human-readable name of the SalesforceReport
     * @param terms the list of terms to replace on the SalesforceReport, or null to remove all terms from the SalesforceReport
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceReport) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceReport, without replacing existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceReport
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to append to the SalesforceReport
     * @return the SalesforceReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceReport appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceReport) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceReport, without replacing all existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceReport
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to remove from the SalesforceReport, which must be referenced by GUID
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceReport removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceReport) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SalesforceReport appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SalesforceReport) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceReport, without replacing existing Atlan tags linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceReport
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SalesforceReport appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceReport) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SalesforceReport.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceReport
     * @param qualifiedName of the SalesforceReport
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceReport
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
