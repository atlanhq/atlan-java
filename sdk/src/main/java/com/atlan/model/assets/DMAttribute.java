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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an attribute within a data model entity in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DMAttribute extends Asset implements IDMAttribute, IDM, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DMAttribute";

    /** Fixed typeName for DMAttributes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Application module that is implemented by this asset. */
    @Attribute
    IAppModule appModuleImplemented;

    /** Business date for the data model. */
    @Attribute
    @Date
    Long dMDataModelBusinessDate;

    /** A domain of the datam model in which this asset exists. */
    @Attribute
    String dMDataModelDomain;

    /** Business expiration date for the data model. */
    @Attribute
    @Date
    Long dMDataModelExpiredAtBusinessDate;

    /** System expiration date for the data model. */
    @Attribute
    @Date
    Long dMDataModelExpiredAtSystemDate;

    /** Simple name of the model in which this asset exists, or empty if it is itself a data model. */
    @Attribute
    String dMDataModelName;

    /** A namespace of the data model in which this asset exists. */
    @Attribute
    String dMDataModelNamespace;

    /** Unique name of the model in which this asset exists, or empty if it is itself a data model. */
    @Attribute
    String dMDataModelQualifiedName;

    /** System date for the data model. */
    @Attribute
    @Date
    Long dMDataModelSystemDate;

    /** Whether this attribute is derived indicator or not. */
    @Attribute
    Boolean dMDerivedIndicator;

    /** Entity in which this attribute exists. */
    @Attribute
    IDMEntity dMEntity;

    /** Simple name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String dMEntityName;

    /** Unique name of the entity in which this asset exists, or empty if it is itself a data model entity. */
    @Attribute
    String dMEntityQualifiedName;

    /** Whether this attribute is foreign key indicator or not. */
    @Attribute
    Boolean dMForeignKeyIndicator;

    /** Whether this attribute is nullable or not. */
    @Attribute
    Boolean dMIsNullable;

    /** Attributes from which this attribute is mapped. */
    @Attribute
    @Singular
    SortedSet<IDMAttribute> dMMappedFromAttributes;

    /** Attributes to which this attribute is mapped. */
    @Attribute
    @Singular
    SortedSet<IDMAttribute> dMMappedToAttributes;

    /** Precision of the attribute. */
    @Attribute
    Long dMPrecision;

    /** Whether this attribute is primary key indicator or not. */
    @Attribute
    Boolean dMPrimaryKeyIndicator;

    /** Association from this attribute is related. */
    @Attribute
    @Singular
    SortedSet<IDMAttributeAssociation> dMRelatedFromAttributes;

    /** Association to which this attribute is related. */
    @Attribute
    @Singular
    SortedSet<IDMAttributeAssociation> dMRelatedToAttributes;

    /** Scale of the attribute. */
    @Attribute
    Long dMScale;

    /** Type of the attribute. */
    @Attribute
    String dMType;

    /** Simple name of the version in which this asset exists, or empty if it is itself a data model version. */
    @Attribute
    String dMVersionName;

    /** Unique name of the version in which this asset exists, or empty if it is itself a data model version. */
    @Attribute
    String dMVersionQualifiedName;

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

    /**
     * Builds the minimal object necessary to create a relationship to a DMAttribute, from a potentially
     * more-complete DMAttribute object.
     *
     * @return the minimal object necessary to relate to the DMAttribute
     * @throws InvalidRequestException if any of the minimal set of required properties for a DMAttribute relationship are not found in the initial object
     */
    @Override
    public DMAttribute trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DMAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DMAttribute assets will be included.
     *
     * @return a fluent search that includes all DMAttribute assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DMAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DMAttribute assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DMAttribute assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DMAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DMAttributes will be included
     * @return a fluent search that includes all DMAttribute assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DMAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DMAttributes will be included
     * @return a fluent search that includes all DMAttribute assets
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
     * Reference to a DMAttribute by GUID. Use this to create a relationship to this DMAttribute,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DMAttribute to reference
     * @return reference to a DMAttribute that can be used for defining a relationship to a DMAttribute
     */
    public static DMAttribute refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DMAttribute by GUID. Use this to create a relationship to this DMAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DMAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DMAttribute that can be used for defining a relationship to a DMAttribute
     */
    public static DMAttribute refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DMAttribute._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DMAttribute by qualifiedName. Use this to create a relationship to this DMAttribute,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DMAttribute to reference
     * @return reference to a DMAttribute that can be used for defining a relationship to a DMAttribute
     */
    public static DMAttribute refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DMAttribute by qualifiedName. Use this to create a relationship to this DMAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DMAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DMAttribute that can be used for defining a relationship to a DMAttribute
     */
    public static DMAttribute refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DMAttribute._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DMAttribute by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DMAttribute to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DMAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DMAttribute does not exist or the provided GUID is not a DMAttribute
     */
    @JsonIgnore
    public static DMAttribute get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DMAttribute by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DMAttribute to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DMAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DMAttribute does not exist or the provided GUID is not a DMAttribute
     */
    @JsonIgnore
    public static DMAttribute get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DMAttribute by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DMAttribute to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DMAttribute, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DMAttribute does not exist or the provided GUID is not a DMAttribute
     */
    @JsonIgnore
    public static DMAttribute get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DMAttribute) {
                return (DMAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DMAttribute) {
                return (DMAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DMAttribute to active.
     *
     * @param qualifiedName for the DMAttribute
     * @return true if the DMAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DMAttribute to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DMAttribute
     * @return true if the DMAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the minimal request necessary to update the DMAttribute, as a builder
     */
    public static DMAttributeBuilder<?, ?> updater(String qualifiedName, String name) {
        return DMAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DMAttribute, from a potentially
     * more-complete DMAttribute object.
     *
     * @return the minimal object necessary to update the DMAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DMAttribute are not found in the initial object
     */
    @Override
    public DMAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DMAttribute) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DMAttribute) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DMAttribute's owners
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DMAttribute) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DMAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the DMAttribute's certificate
     * @param qualifiedName of the DMAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DMAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DMAttribute)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DMAttribute's certificate
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DMAttribute) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the DMAttribute's announcement
     * @param qualifiedName of the DMAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DMAttribute)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DMAttribute.
     *
     * @param client connectivity to the Atlan client from which to remove the DMAttribute's announcement
     * @param qualifiedName of the DMAttribute
     * @param name of the DMAttribute
     * @return the updated DMAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DMAttribute) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DMAttribute.
     *
     * @param qualifiedName for the DMAttribute
     * @param name human-readable name of the DMAttribute
     * @param terms the list of terms to replace on the DMAttribute, or null to remove all terms from the DMAttribute
     * @return the DMAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DMAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DMAttribute's assigned terms
     * @param qualifiedName for the DMAttribute
     * @param name human-readable name of the DMAttribute
     * @param terms the list of terms to replace on the DMAttribute, or null to remove all terms from the DMAttribute
     * @return the DMAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DMAttribute) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DMAttribute, without replacing existing terms linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DMAttribute
     * @param terms the list of terms to append to the DMAttribute
     * @return the DMAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DMAttribute, without replacing existing terms linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DMAttribute
     * @param qualifiedName for the DMAttribute
     * @param terms the list of terms to append to the DMAttribute
     * @return the DMAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DMAttribute) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DMAttribute, without replacing all existing terms linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DMAttribute
     * @param terms the list of terms to remove from the DMAttribute, which must be referenced by GUID
     * @return the DMAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DMAttribute, without replacing all existing terms linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DMAttribute
     * @param qualifiedName for the DMAttribute
     * @param terms the list of terms to remove from the DMAttribute, which must be referenced by GUID
     * @return the DMAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DMAttribute removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DMAttribute) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DMAttribute, without replacing existing Atlan tags linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DMAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DMAttribute
     */
    public static DMAttribute appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DMAttribute, without replacing existing Atlan tags linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DMAttribute
     * @param qualifiedName of the DMAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DMAttribute
     */
    public static DMAttribute appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DMAttribute) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DMAttribute, without replacing existing Atlan tags linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DMAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DMAttribute
     */
    public static DMAttribute appendAtlanTags(
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
     * Add Atlan tags to a DMAttribute, without replacing existing Atlan tags linked to the DMAttribute.
     * Note: this operation must make two API calls — one to retrieve the DMAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DMAttribute
     * @param qualifiedName of the DMAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DMAttribute
     */
    public static DMAttribute appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DMAttribute) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DMAttribute.
     *
     * @param qualifiedName of the DMAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DMAttribute
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DMAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DMAttribute
     * @param qualifiedName of the DMAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DMAttribute
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}