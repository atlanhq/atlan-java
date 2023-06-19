/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.IconType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a README template in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class ReadmeTemplate extends Asset implements IReadmeTemplate, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ReadmeTemplate";

    /** Fixed typeName for ReadmeTemplates. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String icon;

    /** TBC */
    @Attribute
    IconType iconType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isGlobal;

    /** TBC */
    @Attribute
    String link;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    String reference;

    /** TBC */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /**
     * Reference to a ReadmeTemplate by GUID.
     *
     * @param guid the GUID of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByGuid(String guid) {
        return ReadmeTemplate.builder().guid(guid).build();
    }

    /**
     * Reference to a ReadmeTemplate by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByQualifiedName(String qualifiedName) {
        return ReadmeTemplate.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ReadmeTemplate by its GUID, complete with all of its relationships.
     *
     * @param guid of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     */
    public static ReadmeTemplate retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ReadmeTemplate) {
            return (ReadmeTemplate) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ReadmeTemplate");
        }
    }

    /**
     * Retrieves a ReadmeTemplate by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist
     */
    public static ReadmeTemplate retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ReadmeTemplate) {
            return (ReadmeTemplate) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ReadmeTemplate");
        }
    }

    /**
     * Restore the archived (soft-deleted) ReadmeTemplate to active.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @return true if the ReadmeTemplate is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the minimal request necessary to update the ReadmeTemplate, as a builder
     */
    public static ReadmeTemplateBuilder<?, ?> updater(String qualifiedName, String name) {
        return ReadmeTemplate.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ReadmeTemplate, from a potentially
     * more-complete ReadmeTemplate object.
     *
     * @return the minimal object necessary to update the ReadmeTemplate, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ReadmeTemplate are not found in the initial object
     */
    @Override
    public ReadmeTemplateBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ReadmeTemplate", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeUserDescription(updater(qualifiedName, name));
    }
}
