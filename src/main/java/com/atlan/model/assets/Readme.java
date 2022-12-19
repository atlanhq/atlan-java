/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a README in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Readme extends Resource {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Readme";

    /** Fixed typeName for Readmes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Asset to which the README is linked. */
    @Attribute
    Asset asset;

    /** TBC */
    @Attribute
    @Singular("seeAlsoOne")
    SortedSet<Readme> seeAlso;

    /**
     * Reference to a Readme by GUID.
     *
     * @param guid the GUID of the Readme to reference
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByGuid(String guid) {
        return Readme.builder().guid(guid).build();
    }

    /**
     * Reference to a Readme by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Readme to reference
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByQualifiedName(String qualifiedName) {
        return Readme.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a README.
     *
     * @param reference a reference, by GUID, to the asset to which the README should be attached
     * @param assetName name of the asset to which the README should be attached
     * @param content the HTML content to use for the README
     * @return the minimal object necessary to create the README and attach it to the asset, as a builder
     */
    public static ReadmeBuilder<?, ?> creator(Asset reference, String assetName, String content) {
        return Readme.builder()
                .qualifiedName(generateQualifiedName(reference.getGuid()))
                .name(generateName(assetName))
                .description(content)
                .asset(reference);
    }

    /**
     * Builds the minimal object necessary to update a Readme.
     *
     * @param assetGuid the GUID of the asset to which the README is attached
     * @param assetName name of the asset to which the README is attached
     * @return the minimal request necessary to update the Readme, as a builder
     */
    public static ReadmeBuilder<?, ?> updater(String assetGuid, String assetName) {
        return Readme.builder().qualifiedName(generateQualifiedName(assetGuid)).name(generateName(assetName));
    }

    /**
     * Builds the minimal object necessary to apply an update to a Readme, from a potentially
     * more-complete Readme object.
     *
     * @return the minimal object necessary to update the Readme, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Readme are not found in the initial object
     */
    @Override
    public ReadmeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating Readme is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return Readme.builder().qualifiedName(this.getQualifiedName()).name(this.getName());
    }

    /**
     * Generate a unique README name.
     *
     * @param assetGuid GUID of the asset to which the README should be attached
     * @return a unique name for the README
     */
    private static String generateQualifiedName(String assetGuid) {
        return assetGuid + "/readme";
    }

    /**
     * Generate a readable README name (although this does not appear anywhere in the UI).
     *
     * @param assetName name of the asset to which the README should be attached
     * @return a readable name for the README
     */
    private static String generateName(String assetName) {
        return assetName + " Readme";
    }

    /**
     * Retrieves a Readme by its GUID, complete with all of its relationships.
     *
     * @param guid of the Readme to retrieve
     * @return the requested full Readme, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Readme does not exist or the provided GUID is not a Readme
     */
    public static Readme retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Readme) {
            return (Readme) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Readme.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Readme by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Readme to retrieve
     * @return the requested full Readme, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Readme does not exist
     */
    public static Readme retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Readme) {
            return (Readme) entity;
        } else {
            throw new NotFoundException(
                    "No Readme found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Restore the archived (soft-deleted) Readme to active.
     *
     * @param qualifiedName for the Readme
     * @return true if the Readme is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a Readme.
     *
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Readme)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a Readme.
     *
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Readme) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }
}
