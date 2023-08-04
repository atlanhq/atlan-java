/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
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
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a category in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class GlossaryCategory extends Asset implements IGlossaryCategory, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryCategory";

    /** Fixed typeName for GlossaryCategorys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** TBC */
    @Attribute
    IGlossary anchor;

    /** TBC */
    @Attribute
    @Singular("childCategory")
    @Setter(AccessLevel.PACKAGE)
    SortedSet<IGlossaryCategory> childrenCategories;

    /** TBC */
    @Attribute
    String longDescription;

    /** TBC */
    @Attribute
    IGlossaryCategory parentCategory;

    /** TBC */
    @Attribute
    String shortDescription;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> terms;

    /**
     * Start an asset filter that will return all GlossaryCategory assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GlossaryCategory assets will be included.
     *
     * @return an asset filter that includes all GlossaryCategory assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all GlossaryCategory assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GlossaryCategory assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all GlossaryCategory assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all GlossaryCategory assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) GlossaryCategorys will be included
     * @return an asset filter that includes all GlossaryCategory assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all GlossaryCategory assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GlossaryCategorys will be included
     * @return an asset filter that includes all GlossaryCategory assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a GlossaryCategory by GUID.
     *
     * @param guid the GUID of the GlossaryCategory to reference
     * @return reference to a GlossaryCategory that can be used for defining a relationship to a GlossaryCategory
     */
    public static GlossaryCategory refByGuid(String guid) {
        return GlossaryCategory._internal().guid(guid).build();
    }

    /**
     * Reference to a GlossaryCategory by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GlossaryCategory to reference
     * @return reference to a GlossaryCategory that can be used for defining a relationship to a GlossaryCategory
     */
    public static GlossaryCategory refByQualifiedName(String qualifiedName) {
        return GlossaryCategory._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a GlossaryCategory by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the GlossaryCategory to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     */
    @JsonIgnore
    public static GlossaryCategory get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a GlossaryCategory by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryCategory to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     */
    @JsonIgnore
    public static GlossaryCategory get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a GlossaryCategory by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryCategory to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GlossaryCategory, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     */
    @JsonIgnore
    public static GlossaryCategory get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GlossaryCategory) {
                return (GlossaryCategory) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof GlossaryCategory) {
                return (GlossaryCategory) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a GlossaryCategory by its GUID, complete with all of its relationships.
     *
     * @param guid of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static GlossaryCategory retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a GlossaryCategory by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist or the provided GUID is not a GlossaryCategory
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static GlossaryCategory retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a GlossaryCategory by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static GlossaryCategory retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a GlossaryCategory by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the GlossaryCategory to retrieve
     * @return the requested full GlossaryCategory, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryCategory does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static GlossaryCategory retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) GlossaryCategory to active.
     *
     * @param qualifiedName for the GlossaryCategory
     * @return true if the GlossaryCategory is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) GlossaryCategory to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GlossaryCategory
     * @return true if the GlossaryCategory is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary for creating a GlossaryCategory. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique identifier of the GlossaryCategory's glossary
     * @param glossaryQualifiedName unique name of the GlossaryCategory's glossary
     * @return the minimal object necessary to create the GlossaryCategory, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> creator(
            String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryCategory._internal()
                .qualifiedName(name)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, glossaryQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique identifier of the GlossaryCategory's glossary
     * @return the minimal request necessary to update the GlossaryCategory, as a builder
     */
    public static GlossaryCategoryBuilder<?, ?> updater(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a category requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryCategory._internal()
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, null));
    }

    /**
     * Builds the minimal object necessary to apply an update to a GlossaryCategory, from a potentially
     * more-complete GlossaryCategory object.
     *
     * @return the minimal object necessary to update the GlossaryCategory, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GlossaryCategory are not found in the initial object
     */
    @Override
    public GlossaryCategoryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getAnchor() == null || !this.getAnchor().isValidReferenceByGuid()) {
            missing.add("anchor.guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GlossaryCategory", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(String name, String glossaryName) throws AtlanException {
        return findByName(name, glossaryName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, glossaryName, attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(AtlanClient client, String name, String glossaryName)
            throws AtlanException {
        return findByName(client, name, glossaryName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryName name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByName(
            AtlanClient client, String name, String glossaryName, Collection<String> attributes) throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName, null);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(String name, String glossaryQualifiedName)
            throws AtlanException {
        return findByNameFast(name, glossaryQualifiedName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            String name, String glossaryQualifiedName, Collection<String> attributes) throws AtlanException {
        return findByNameFast(Atlan.getDefaultClient(), name, glossaryQualifiedName, attributes);
    }

    /**
     * Find a GlossaryCategory by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the category, if found.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(AtlanClient client, String name, String glossaryQualifiedName)
            throws AtlanException {
        return findByNameFast(client, name, glossaryQualifiedName, null);
    }

    /**
     * Find a GlossaryCategory by its human-readable name.
     * Note that categories are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryCategory
     * @return the GlossaryCategory, if found
     * @throws AtlanException on any API problems, or if the GlossaryCategory does not exist
     */
    public static List<GlossaryCategory> findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, Collection<String> attributes)
            throws AtlanException {
        List<GlossaryCategory> results = new ArrayList<>();
        GlossaryCategory.all(client)
                .filter(QueryFactory.have(KeywordFields.NAME).eq(name))
                .filter(QueryFactory.have(KeywordFields.GLOSSARY).eq(glossaryQualifiedName))
                .attributes(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof GlossaryCategory)
                .forEach(c -> results.add((GlossaryCategory) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Remove the system description from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
    }

    /**
     * Remove the system description from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's description
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeDescription(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryCategory) Asset.removeDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeUserDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
    }

    /**
     * Remove the user's description from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's description
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeUserDescription(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryCategory) Asset.removeUserDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeOwners(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
    }

    /**
     * Remove the owners from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's owners
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeOwners(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryCategory) Asset.removeOwners(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            String qualifiedName, String name, String glossaryGuid, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid, certificate, message);
    }

    /**
     * Update the certificate on a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryCategory's certificate
     * @param qualifiedName of the GlossaryCategory
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateCertificate(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            CertificateStatus certificate,
            String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateCertificate(client, updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
    }

    /**
     * Remove the certificate from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's certificate
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeCertificate(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryCategory) Asset.removeCertificate(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateAnnouncement(
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid, type, title, message);
    }

    /**
     * Update the announcement on a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryCategory's announcement
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryCategory, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryCategory)
                Asset.updateAnnouncement(client, updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, glossaryGuid);
    }

    /**
     * Remove the announcement from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryCategory's announcement
     * @param qualifiedName of the GlossaryCategory
     * @param name of the GlossaryCategory
     * @param glossaryGuid unique ID (GUID) of the GlossaryCategory's glossary
     * @return the updated GlossaryCategory, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryCategory removeAnnouncement(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryCategory) Asset.removeAnnouncement(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Add Atlan tags to a GlossaryCategory, without replacing existing Atlan tags linked to the GlossaryCategory.
     * Note: this operation must make two API calls — one to retrieve the GlossaryCategory's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GlossaryCategory
     */
    public static GlossaryCategory appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GlossaryCategory, without replacing existing Atlan tags linked to the GlossaryCategory.
     * Note: this operation must make two API calls — one to retrieve the GlossaryCategory's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GlossaryCategory
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GlossaryCategory
     */
    public static GlossaryCategory appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GlossaryCategory) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GlossaryCategory, without replacing existing Atlan tags linked to the GlossaryCategory.
     * Note: this operation must make two API calls — one to retrieve the GlossaryCategory's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GlossaryCategory
     */
    public static GlossaryCategory appendAtlanTags(
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
     * Add Atlan tags to a GlossaryCategory, without replacing existing Atlan tags linked to the GlossaryCategory.
     * Note: this operation must make two API calls — one to retrieve the GlossaryCategory's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GlossaryCategory
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GlossaryCategory
     */
    public static GlossaryCategory appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GlossaryCategory) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GlossaryCategory
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the GlossaryCategory
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GlossaryCategory
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GlossaryCategory
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
     * Add Atlan tags to a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the GlossaryCategory
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GlossaryCategory
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
     * Remove an Atlan tag from a GlossaryCategory.
     *
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GlossaryCategory
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a GlossaryCategory.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GlossaryCategory
     * @param qualifiedName of the GlossaryCategory
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GlossaryCategory
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
