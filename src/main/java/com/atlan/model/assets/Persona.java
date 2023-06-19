/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.enums.PersonaGlossaryAction;
import com.atlan.model.enums.PersonaMetadataAction;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Persona access control object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
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
    @Singular
    SortedSet<AssetSidebarTab> denyAssetTabs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyCustomMetadataGuids;

    /** TBC */
    @Attribute
    Boolean isAccessControlEnabled;

    /** Groups for whom this persona is accessible. */
    @Attribute
    @Singular
    SortedSet<String> personaGroups;

    /** Users for whom this persona is accessible. */
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
     * Reference to a Persona by GUID.
     *
     * @param guid the GUID of the Persona to reference
     * @return reference to a Persona that can be used for defining a relationship to a Persona
     */
    public static Persona refByGuid(String guid) {
        return Persona.builder().guid(guid).build();
    }

    /**
     * Reference to a Persona by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Persona to reference
     * @return reference to a Persona that can be used for defining a relationship to a Persona
     */
    public static Persona refByQualifiedName(String qualifiedName) {
        return Persona.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Persona by its GUID, complete with all of its relationships.
     *
     * @param guid of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist or the provided GUID is not a Persona
     */
    public static Persona retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Persona) {
            return (Persona) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Persona");
        }
    }

    /**
     * Retrieves a Persona by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Persona to retrieve
     * @return the requested full Persona, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Persona does not exist
     */
    public static Persona retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Persona) {
            return (Persona) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Persona");
        }
    }

    /**
     * Restore the archived (soft-deleted) Persona to active.
     *
     * @param qualifiedName for the Persona
     * @return true if the Persona is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Persona.
     *
     * @param name of the Persona
     * @return the minimal request necessary to create the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> creator(String name) {
        return Persona.builder()
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
        return Persona.builder().qualifiedName(qualifiedName).name(name).isAccessControlEnabled(isEnabled);
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
     * Find a Persona by its human-readable name.
     *
     * @param name of the Persona
     * @param attributes an optional collection of attributes to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(String name, Collection<String> attributes) throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(filter).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        List<Persona> personas = new ArrayList<>();
        if (response != null) {
            List<Asset> results = response.getAssets();
            while (results != null) {
                for (Asset result : results) {
                    if (result instanceof Persona) {
                        personas.add((Persona) result);
                    }
                }
                response = response.getNextPage();
                results = response.getAssets();
            }
        }
        if (personas.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        } else {
            return personas;
        }
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
        return (Persona) Asset.removeDescription(updater(qualifiedName, name, isEnabled));
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
        return (Persona) Asset.removeUserDescription(updater(qualifiedName, name, isEnabled));
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
        return (Persona) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        return (Persona) Asset.appendAtlanTags(
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
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        Asset.addAtlanTags(
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
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
