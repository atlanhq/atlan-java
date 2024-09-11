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
import com.atlan.model.enums.AtlanConnectorType;
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
 * Instance of an application module in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class AppModule extends Asset implements IAppModule, IApp, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AppModule";

    /** Fixed typeName for AppModules. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Individual modules of the application. */
    @Attribute
    @Singular
    SortedSet<IAppModule> appChildModules;

    /** Application module that is implemented by this asset. */
    @Attribute
    IAppModule appModuleImplemented;

    /** Assets that implement the application module. */
    @Attribute
    @Singular
    SortedSet<ICatalog> appModuleImplementedByAssets;

    /** Type of application module. */
    @Attribute
    String appModuleType;

    /** Application module in which this module exists. */
    @Attribute
    IAppModule appParentModule;

    /** Number of sub-modules in this application module. */
    @Attribute
    Long appSubModuleCount;

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
     * Builds the minimal object necessary to create a relationship to a AppModule, from a potentially
     * more-complete AppModule object.
     *
     * @return the minimal object necessary to relate to the AppModule
     * @throws InvalidRequestException if any of the minimal set of required properties for a AppModule relationship are not found in the initial object
     */
    @Override
    public AppModule trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AppModule assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AppModule assets will be included.
     *
     * @return a fluent search that includes all AppModule assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all AppModule assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AppModule assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AppModule assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AppModule assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) AppModules will be included
     * @return a fluent search that includes all AppModule assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all AppModule assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AppModules will be included
     * @return a fluent search that includes all AppModule assets
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
     * Reference to a AppModule by GUID. Use this to create a relationship to this AppModule,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AppModule to reference
     * @return reference to a AppModule that can be used for defining a relationship to a AppModule
     */
    public static AppModule refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AppModule by GUID. Use this to create a relationship to this AppModule,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AppModule to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AppModule that can be used for defining a relationship to a AppModule
     */
    public static AppModule refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AppModule._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AppModule by qualifiedName. Use this to create a relationship to this AppModule,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AppModule to reference
     * @return reference to a AppModule that can be used for defining a relationship to a AppModule
     */
    public static AppModule refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AppModule by qualifiedName. Use this to create a relationship to this AppModule,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AppModule to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AppModule that can be used for defining a relationship to a AppModule
     */
    public static AppModule refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AppModule._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AppModule by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the AppModule to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AppModule, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AppModule does not exist or the provided GUID is not a AppModule
     */
    @JsonIgnore
    public static AppModule get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a AppModule by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AppModule to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AppModule, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AppModule does not exist or the provided GUID is not a AppModule
     */
    @JsonIgnore
    public static AppModule get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a AppModule by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AppModule to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AppModule, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AppModule does not exist or the provided GUID is not a AppModule
     */
    @JsonIgnore
    public static AppModule get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AppModule) {
                return (AppModule) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AppModule) {
                return (AppModule) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AppModule to active.
     *
     * @param qualifiedName for the AppModule
     * @return true if the AppModule is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) AppModule to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AppModule
     * @return true if the AppModule is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an application module.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which to create the AppModule
     * @return the minimal request necessary to create the module, as a builder
     */
    public static AppModuleBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return creator(name, connectionQualifiedName, null);
    }

    /**
     * Builds the minimal object necessary to create an application sub-module.
     *
     * @param name of the application sub-module
     * @param parent application module in which the sub-module should be created, which must have at least
     *               a qualifiedName and / or connectionQualifiedName
     * @return the minimal request necessary to create the module, as a builder
     * @throws InvalidRequestException if the module provided is without a qualifiedName
     */
    public static AppModuleBuilder<?, ?> creator(String name, AppModule parent) throws InvalidRequestException {
        validateRelationship(
                AppModule.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", parent.getConnectionQualifiedName(),
                        "qualifiedName", parent.getQualifiedName()));
        return creator(name, parent.getConnectionQualifiedName(), parent.getQualifiedName())
                .appParentModule(parent.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an application module.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which to create the AppModule
     * @param parentQualifiedName unique name of the parent application module in which to create the AppModule (as a sub-module)
     * @return the minimal request necessary to create the module, as a builder
     */
    public static AppModuleBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String parentQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        AppModuleBuilder<?, ?> builder = AppModule._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
        if (parentQualifiedName != null) {
            builder.appParentModule(AppModule.refByQualifiedName(parentQualifiedName));
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update an application module.
     *
     * @param qualifiedName of the application module
     * @param name of the application module
     * @return the minimal request necessary to update the application module, as a builder
     */
    public static AppModuleBuilder<?, ?> updater(String qualifiedName, String name) {
        return AppModule._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique application module name.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which this module exists
     * @return a unique name for the module
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to an application module, from a potentially
     * more-complete application module object.
     *
     * @return the minimal object necessary to update the application module, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for application module are not found in the initial object
     */
    @Override
    public AppModuleBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a AppModule.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AppModule) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a AppModule.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AppModule) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a AppModule.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AppModule's owners
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (AppModule) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AppModule, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AppModule updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AppModule.
     *
     * @param client connectivity to the Atlan tenant on which to update the AppModule's certificate
     * @param qualifiedName of the AppModule
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AppModule, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AppModule updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AppModule) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a AppModule.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AppModule's certificate
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AppModule) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AppModule updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AppModule.
     *
     * @param client connectivity to the Atlan tenant on which to update the AppModule's announcement
     * @param qualifiedName of the AppModule
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AppModule updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AppModule)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a AppModule.
     *
     * @param client connectivity to the Atlan client from which to remove the AppModule's announcement
     * @param qualifiedName of the AppModule
     * @param name of the AppModule
     * @return the updated AppModule, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AppModule removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AppModule) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AppModule.
     *
     * @param qualifiedName for the AppModule
     * @param name human-readable name of the AppModule
     * @param terms the list of terms to replace on the AppModule, or null to remove all terms from the AppModule
     * @return the AppModule that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the AppModule.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AppModule's assigned terms
     * @param qualifiedName for the AppModule
     * @param name human-readable name of the AppModule
     * @param terms the list of terms to replace on the AppModule, or null to remove all terms from the AppModule
     * @return the AppModule that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AppModule) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AppModule, without replacing existing terms linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AppModule
     * @param terms the list of terms to append to the AppModule
     * @return the AppModule that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the AppModule, without replacing existing terms linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AppModule
     * @param qualifiedName for the AppModule
     * @param terms the list of terms to append to the AppModule
     * @return the AppModule that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AppModule) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AppModule, without replacing all existing terms linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AppModule
     * @param terms the list of terms to remove from the AppModule, which must be referenced by GUID
     * @return the AppModule that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a AppModule, without replacing all existing terms linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AppModule
     * @param qualifiedName for the AppModule
     * @param terms the list of terms to remove from the AppModule, which must be referenced by GUID
     * @return the AppModule that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AppModule removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AppModule) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AppModule, without replacing existing Atlan tags linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AppModule
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AppModule
     */
    public static AppModule appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AppModule, without replacing existing Atlan tags linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AppModule
     * @param qualifiedName of the AppModule
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AppModule
     */
    public static AppModule appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AppModule) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AppModule, without replacing existing Atlan tags linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AppModule
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AppModule
     */
    public static AppModule appendAtlanTags(
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
     * Add Atlan tags to a AppModule, without replacing existing Atlan tags linked to the AppModule.
     * Note: this operation must make two API calls — one to retrieve the AppModule's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AppModule
     * @param qualifiedName of the AppModule
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AppModule
     */
    public static AppModule appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AppModule) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AppModule.
     *
     * @param qualifiedName of the AppModule
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AppModule
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a AppModule.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AppModule
     * @param qualifiedName of the AppModule
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AppModule
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
