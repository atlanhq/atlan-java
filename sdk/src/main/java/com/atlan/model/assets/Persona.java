/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.PersonaDomainAction;
import com.atlan.model.enums.PersonaGlossaryAction;
import com.atlan.model.enums.PersonaMetadataAction;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Atlan Type representing a Persona model
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class Persona extends Asset implements IPersona, IAccessControl, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Persona";

    /** Fixed typeName for Personas. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String channelLink;

    /** TBC */
    @Attribute
    String defaultNavigation;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyAssetFilters;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<AssetSidebarTab> denyAssetTabs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyAssetTypes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyCustomMetadataGuids;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyNavigationPages;

    /** TBC */
    @Attribute
    Boolean isAccessControlEnabled;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> personaGroups;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> personaUsers;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAuthPolicy> policies;

    /** TBC */
    @Attribute
    String roleId;

    /**
     * Builds the minimal object necessary to create a relationship to a Persona, from a potentially
     * more-complete Persona object.
     *
     * @return the minimal object necessary to relate to the Persona
     * @throws InvalidRequestException if any of the minimal set of required properties for a Persona relationship are not found in the initial object
     */
    @Override
    public Persona trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Persona assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Persona assets will be included.
     *
     * @return a fluent search that includes all Persona assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all Persona assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Persona assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Persona assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Persona assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Personas will be included
     * @return a fluent search that includes all Persona assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all Persona assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Personas will be included
     * @return a fluent search that includes all Persona assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all Persona assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Persona assets will be included.
     *
     * @return an asset filter that includes all Persona assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all Persona assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Persona assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all Persona assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all Persona assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Personas will be included
     * @return an asset filter that includes all Persona assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all Persona assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Personas will be included
     * @return an asset filter that includes all Persona assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a Persona by GUID.
     *
     * @param guid the GUID of the Persona to reference
     * @return reference to a Persona that can be used for defining a relationship to a Persona
     */
    public static Persona refByGuid(String guid) {
        return Persona._internal().guid(guid).build();
    }

    /**
     * Reference to a Persona by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Persona to reference
     * @return reference to a Persona that can be used for defining a relationship to a Persona
     */
    public static Persona refByQualifiedName(String qualifiedName) {
        return Persona._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Persona by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Persona to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     */
    @JsonIgnore
    public static Persona get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a Persona by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Persona to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     */
    @JsonIgnore
    public static Persona get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a Persona by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Persona to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Persona, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     */
    @JsonIgnore
    public static Persona get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Persona) {
                return (Persona) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof Persona) {
                return (Persona) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Persona by its GUID, complete with all of its relationships.
     *
     * @param guid of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Persona retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Persona by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Persona retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a Persona by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Persona retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Persona by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Persona retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Persona to active.
     *
     * @param qualifiedName for the Persona
     * @return true if the Persona is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Persona to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Persona
     * @return true if the Persona is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Persona.
     *
     * @param name of the Persona
     * @return the minimal request necessary to create the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> creator(String name) {
        return Persona._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName(name)
                .isAccessControlEnabled(true)
                .description("");
    }

    /**
     * Builds the minimal object necessary to update a Persona.
     *
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the minimal request necessary to update the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> updater(String qualifiedName, String name, boolean isEnabled) {
        return Persona._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .isAccessControlEnabled(isEnabled);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Persona, from a potentially
     * more-complete Persona object.
     *
     * @return the minimal object necessary to update the Persona, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Persona are not found in the initial object
     */
    @Override
    public PersonaBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getIsAccessControlEnabled() == null) {
            missing.add("isAccessControlEnabled");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Persona", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Persona by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the persona, if found.
     *
     * @param name of the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(String name) throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param name of the Persona
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(String name, Collection<String> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param name of the Persona
     * @param attributes an optional collection of attributes (checked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(String name, List<AtlanField> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a Persona by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the persona, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<Persona> results = new ArrayList<>();
        Persona.select(client)
                .where(Persona.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Persona)
                .forEach(p -> results.add((Persona) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @param attributes an optional list of attributes (checked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<Persona> results = new ArrayList<>();
        Persona.select(client)
                .where(Persona.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Persona)
                .forEach(p -> results.add((Persona) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Builds the minimal object necessary to create a metadata policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this metadata policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param connectionQualifiedName unique name of the connection whose assets this policy will control
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedNamePrefix}
     * @return the minimal request necessary to create the metadata policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createMetadataPolicy(
            String name,
            String personaId,
            AuthPolicyType policyType,
            Collection<PersonaMetadataAction> actions,
            String connectionQualifiedName,
            Collection<String> resources) {
        return AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(policyType)
                .connectionQualifiedName(connectionQualifiedName)
                .policyResources(resources)
                .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
                .policyServiceName("atlas")
                .policySubCategory("metadata")
                .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a data policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this data policy
     * @param policyType type of policy (for example allow vs deny)
     * @param connectionQualifiedName unique name of the connection whose assets this policy will control
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedNamePrefix}
     * @return the minimal request necessary to create the data policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDataPolicy(
            String name,
            String personaId,
            AuthPolicyType policyType,
            String connectionQualifiedName,
            Collection<String> resources) {
        return AuthPolicy.creator(name)
                .policyAction(DataAction.SELECT)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(policyType)
                .connectionQualifiedName(connectionQualifiedName)
                .policyResources(resources)
                .policyResource("entity-type:*")
                .policyResourceCategory(AuthPolicyResourceCategory.ENTITY)
                .policyServiceName("heka")
                .policySubCategory("data")
                .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a glossary policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this glossary policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedName} of the glossary
     * @return the minimal request necessary to create the glossary policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createGlossaryPolicy(
            String name,
            String personaId,
            AuthPolicyType policyType,
            Collection<PersonaGlossaryAction> actions,
            Collection<String> resources) {
        return AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(policyType)
                .policyResources(resources)
                .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
                .policyServiceName("atlas")
                .policySubCategory("glossary")
                .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a domain policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this metadata policy
     * @param actions to include in the policy
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedName} where the qualifiedName is for a domain or subdomain
     * @return the minimal request necessary to create the metadata policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDomainPolicy(
            String name, String personaId, Collection<PersonaDomainAction> actions, Collection<String> resources) {
        return AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(AuthPolicyType.ALLOW)
                .policyResources(resources)
                .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
                .policyServiceName("atlas")
                .policySubCategory("domain")
                .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Remove the system description from a Persona.
     *
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated Persona, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Persona removeDescription(String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name, isEnabled);
    }

    /**
     * Remove the system description from a Persona.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Persona's description
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated Persona, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Persona removeDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return (Persona) Asset.removeDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Remove the user's description from a Persona.
     *
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated Persona, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Persona removeUserDescription(String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name, isEnabled);
    }

    /**
     * Remove the user's description from a Persona.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Persona's description
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated Persona, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Persona removeUserDescription(
            AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (Persona) Asset.removeUserDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Add Atlan tags to a Persona, without replacing existing Atlan tags linked to the Persona.
     * Note: this operation must make two API calls — one to retrieve the Persona's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Persona
     */
    public static Persona appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Persona, without replacing existing Atlan tags linked to the Persona.
     * Note: this operation must make two API calls — one to retrieve the Persona's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Persona
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Persona
     */
    public static Persona appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Persona) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Persona, without replacing existing Atlan tags linked to the Persona.
     * Note: this operation must make two API calls — one to retrieve the Persona's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Persona
     */
    public static Persona appendAtlanTags(
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
     * Add Atlan tags to a Persona, without replacing existing Atlan tags linked to the Persona.
     * Note: this operation must make two API calls — one to retrieve the Persona's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Persona
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Persona
     */
    public static Persona appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Persona) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Persona.
     *
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Persona
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Persona.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Persona
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Persona
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Persona.
     *
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Persona
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
     * Add Atlan tags to a Persona.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Persona
     * @param qualifiedName of the Persona
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Persona
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
     * Remove an Atlan tag from a Persona.
     *
     * @param qualifiedName of the Persona
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Persona
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Persona.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Persona
     * @param qualifiedName of the Persona
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Persona
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
