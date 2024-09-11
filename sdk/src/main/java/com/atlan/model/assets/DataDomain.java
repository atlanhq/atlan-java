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
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a data domain in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DataDomain extends Asset implements IDataDomain, IDataMesh, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataDomain";

    /** Fixed typeName for DataDomains. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Application module that is implemented by this asset. */
    @Attribute
    IAppModule appModuleImplemented;

    /** Data products that exist within this data domain. */
    @Attribute
    @Singular
    SortedSet<IDataProduct> dataProducts;

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

    /** Parent data domain in which this sub-data domain exists. */
    @Attribute
    IDataDomain parentDomain;

    /** Unique name of the parent domain in which this asset exists. */
    @Attribute
    String parentDomainQualifiedName;

    /** Stakeholder assigned to the Domain */
    @Attribute
    @Singular
    SortedSet<IStakeholder> stakeholders;

    /** Sub-data domains that exist within this data domain. */
    @Attribute
    @Singular
    SortedSet<IDataDomain> subDomains;

    /** Unique name of the top-level domain in which this asset exists. */
    @Attribute
    String superDomainQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a DataDomain, from a potentially
     * more-complete DataDomain object.
     *
     * @return the minimal object necessary to relate to the DataDomain
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataDomain relationship are not found in the initial object
     */
    @Override
    public DataDomain trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataDomain assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataDomain assets will be included.
     *
     * @return a fluent search that includes all DataDomain assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DataDomain assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataDomain assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataDomain assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataDomain assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataDomains will be included
     * @return a fluent search that includes all DataDomain assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DataDomain assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataDomains will be included
     * @return a fluent search that includes all DataDomain assets
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
     * Reference to a DataDomain by GUID. Use this to create a relationship to this DataDomain,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataDomain to reference
     * @return reference to a DataDomain that can be used for defining a relationship to a DataDomain
     */
    public static DataDomain refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataDomain by GUID. Use this to create a relationship to this DataDomain,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataDomain to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataDomain that can be used for defining a relationship to a DataDomain
     */
    public static DataDomain refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataDomain._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataDomain by qualifiedName. Use this to create a relationship to this DataDomain,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataDomain to reference
     * @return reference to a DataDomain that can be used for defining a relationship to a DataDomain
     */
    public static DataDomain refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataDomain by qualifiedName. Use this to create a relationship to this DataDomain,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataDomain to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataDomain that can be used for defining a relationship to a DataDomain
     */
    public static DataDomain refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataDomain._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataDomain by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DataDomain to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataDomain, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataDomain does not exist or the provided GUID is not a DataDomain
     */
    @JsonIgnore
    public static DataDomain get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DataDomain by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataDomain to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataDomain, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataDomain does not exist or the provided GUID is not a DataDomain
     */
    @JsonIgnore
    public static DataDomain get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DataDomain by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataDomain to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataDomain, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataDomain does not exist or the provided GUID is not a DataDomain
     */
    @JsonIgnore
    public static DataDomain get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataDomain) {
                return (DataDomain) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DataDomain) {
                return (DataDomain) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DataDomain to active.
     *
     * @param qualifiedName for the DataDomain
     * @return true if the DataDomain is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataDomain to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataDomain
     * @return true if the DataDomain is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     * @throws InvalidRequestException will never throw but required given signature of called method
     */
    public static DataDomainBuilder<?, ?> creator(String name) throws InvalidRequestException {
        return creator(name, null);
    }

    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @param parentDomainQualifiedName (optional) unique name of the data domain in which to create this subdomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     */
    public static DataDomainBuilder<?, ?> creator(String name, String parentDomainQualifiedName)
            throws InvalidRequestException {
        DataDomainBuilder<?, ?> builder = DataDomain._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name);
        if (parentDomainQualifiedName != null) {
            builder.parentDomain(DataDomain.refByQualifiedName(parentDomainQualifiedName))
                    .parentDomainQualifiedName(parentDomainQualifiedName)
                    .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(parentDomainQualifiedName));
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the minimal request necessary to update the DataDomain, as a builder
     */
    public static DataDomainBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataDomain._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataDomain, from a potentially
     * more-complete DataDomain object.
     *
     * @return the minimal object necessary to update the DataDomain, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataDomain are not found in the initial object
     */
    @Override
    public DataDomainBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a DataDomain by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name) throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name, Collection<String> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(String name, List<AtlanField> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a DataDomain by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<DataDomain> results = new ArrayList<>();
        DataDomain.select(client)
                .where(DataDomain.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataDomain)
                .forEach(d -> results.add((DataDomain) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Find a DataDomain by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataDomain
     * @param name of the DataDomain
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataDomain
     * @return the DataDomain, if found
     * @throws AtlanException on any API problems, or if the DataDomain does not exist
     */
    public static List<DataDomain> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<DataDomain> results = new ArrayList<>();
        DataDomain.select(client)
                .where(DataDomain.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataDomain)
                .forEach(d -> results.add((DataDomain) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Remove the system description from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DataDomain.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataDomain) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DataDomain.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataDomain) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DataDomain.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataDomain's owners
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataDomain) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataDomain, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DataDomain.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataDomain's certificate
     * @param qualifiedName of the DataDomain
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataDomain, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataDomain)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DataDomain.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataDomain's certificate
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataDomain) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DataDomain.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataDomain's announcement
     * @param qualifiedName of the DataDomain
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DataDomain)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DataDomain.
     *
     * @param client connectivity to the Atlan client from which to remove the DataDomain's announcement
     * @param qualifiedName of the DataDomain
     * @param name of the DataDomain
     * @return the updated DataDomain, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataDomain) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataDomain.
     *
     * @param qualifiedName for the DataDomain
     * @param name human-readable name of the DataDomain
     * @param terms the list of terms to replace on the DataDomain, or null to remove all terms from the DataDomain
     * @return the DataDomain that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DataDomain.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataDomain's assigned terms
     * @param qualifiedName for the DataDomain
     * @param name human-readable name of the DataDomain
     * @param terms the list of terms to replace on the DataDomain, or null to remove all terms from the DataDomain
     * @return the DataDomain that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataDomain) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataDomain, without replacing existing terms linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DataDomain
     * @param terms the list of terms to append to the DataDomain
     * @return the DataDomain that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DataDomain, without replacing existing terms linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataDomain
     * @param qualifiedName for the DataDomain
     * @param terms the list of terms to append to the DataDomain
     * @return the DataDomain that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataDomain) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataDomain, without replacing all existing terms linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DataDomain
     * @param terms the list of terms to remove from the DataDomain, which must be referenced by GUID
     * @return the DataDomain that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DataDomain, without replacing all existing terms linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataDomain
     * @param qualifiedName for the DataDomain
     * @param terms the list of terms to remove from the DataDomain, which must be referenced by GUID
     * @return the DataDomain that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataDomain removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataDomain) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataDomain, without replacing existing Atlan tags linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataDomain
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataDomain
     */
    public static DataDomain appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataDomain, without replacing existing Atlan tags linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataDomain
     * @param qualifiedName of the DataDomain
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataDomain
     */
    public static DataDomain appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataDomain) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataDomain, without replacing existing Atlan tags linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataDomain
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataDomain
     */
    public static DataDomain appendAtlanTags(
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
     * Add Atlan tags to a DataDomain, without replacing existing Atlan tags linked to the DataDomain.
     * Note: this operation must make two API calls — one to retrieve the DataDomain's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataDomain
     * @param qualifiedName of the DataDomain
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataDomain
     */
    public static DataDomain appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataDomain) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DataDomain.
     *
     * @param qualifiedName of the DataDomain
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataDomain
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DataDomain.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataDomain
     * @param qualifiedName of the DataDomain
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataDomain
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
