/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.SourceTagAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Snowflake Tag in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class SnowflakeTag extends Asset implements ISnowflakeTag, ITag, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeTag";

    /** Fixed typeName for SnowflakeTags. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
    @Attribute
    String databaseQualifiedName;

    /** dbt Models to which this tag is applied. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** dbt Sources to which this tag is applied. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @JsonProperty("mappedClassificationName")
    String mappedAtlanTagName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Database schemas to which this tag is applied. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> tagAllowedValues;

    /** TBC */
    @Attribute
    @Singular
    List<SourceTagAttribute> tagAttributes;

    /** TBC */
    @Attribute
    String tagId;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Reference to a SnowflakeTag by GUID.
     *
     * @param guid the GUID of the SnowflakeTag to reference
     * @return reference to a SnowflakeTag that can be used for defining a relationship to a SnowflakeTag
     */
    public static SnowflakeTag refByGuid(String guid) {
        return SnowflakeTag.builder().guid(guid).build();
    }

    /**
     * Reference to a SnowflakeTag by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeTag to reference
     * @return reference to a SnowflakeTag that can be used for defining a relationship to a SnowflakeTag
     */
    public static SnowflakeTag refByQualifiedName(String qualifiedName) {
        return SnowflakeTag.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SnowflakeTag by its GUID, complete with all of its relationships.
     *
     * @param guid of the SnowflakeTag to retrieve
     * @return the requested full SnowflakeTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeTag does not exist or the provided GUID is not a SnowflakeTag
     */
    public static SnowflakeTag retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SnowflakeTag) {
            return (SnowflakeTag) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SnowflakeTag");
        }
    }

    /**
     * Retrieves a SnowflakeTag by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SnowflakeTag to retrieve
     * @return the requested full SnowflakeTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeTag does not exist
     */
    public static SnowflakeTag retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SnowflakeTag) {
            return (SnowflakeTag) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SnowflakeTag");
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeTag to active.
     *
     * @param qualifiedName for the SnowflakeTag
     * @return true if the SnowflakeTag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the minimal request necessary to update the SnowflakeTag, as a builder
     */
    public static SnowflakeTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeTag.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeTag, from a potentially
     * more-complete SnowflakeTag object.
     *
     * @return the minimal object necessary to update the SnowflakeTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakeTag are not found in the initial object
     */
    @Override
    public SnowflakeTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SnowflakeTag", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the updated SnowflakeTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeTag) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the updated SnowflakeTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeTag) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the updated SnowflakeTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeTag) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeTag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeTag) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the updated SnowflakeTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeTag) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SnowflakeTag) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the updated SnowflakeTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SnowflakeTag) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeTag.
     *
     * @param qualifiedName for the SnowflakeTag
     * @param name human-readable name of the SnowflakeTag
     * @param terms the list of terms to replace on the SnowflakeTag, or null to remove all terms from the SnowflakeTag
     * @return the SnowflakeTag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeTag) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeTag, without replacing existing terms linked to the SnowflakeTag.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeTag's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SnowflakeTag
     * @param terms the list of terms to append to the SnowflakeTag
     * @return the SnowflakeTag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeTag) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeTag, without replacing all existing terms linked to the SnowflakeTag.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeTag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SnowflakeTag
     * @param terms the list of terms to remove from the SnowflakeTag, which must be referenced by GUID
     * @return the SnowflakeTag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeTag removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeTag) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeTag, without replacing existing Atlan tags linked to the SnowflakeTag.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeTag
     */
    public static SnowflakeTag appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SnowflakeTag) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeTag, without replacing existing Atlan tags linked to the SnowflakeTag.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeTag
     */
    public static SnowflakeTag appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeTag) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SnowflakeTag
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SnowflakeTag
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
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeTag
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
