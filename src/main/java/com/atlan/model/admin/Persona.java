/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.api.PersonasEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AssetSidebarTab;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Persona extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the persona. */
    String id;

    /** Name of the persona. */
    String name;

    /** Name of the persona as it appears in the UI. */
    String displayName;

    /** Description of the persona. */
    String description;

    /** Unique identifiers (GUIDs) of groups that are associated with the persona. */
    @Singular
    @JsonInclude(JsonInclude.Include.ALWAYS)
    SortedSet<String> groups;

    /** Unique identifiers (GUIDs) of users that are associated with the persona. */
    @Singular
    @JsonInclude(JsonInclude.Include.ALWAYS)
    SortedSet<String> users;

    /** TBC */
    @Singular
    SortedSet<String> apikeys;

    /** Fixed value. */
    @Builder.Default
    String personaType = "persona";

    /** Set of metadata policies defined for this persona. */
    @Singular
    SortedSet<PersonaMetadataPolicy> metadataPolicies;

    /** Set of data policies defined for this persona. */
    @Singular
    SortedSet<PersonaDataPolicy> dataPolicies;

    /** Set of glossary policies defined for this persona. */
    @Singular
    SortedSet<GlossaryPolicy> glossaryPolicies;

    /** Whether this persona is currently active (true) or deactivated (false). */
    Boolean enabled;

    /** Time (epoch) at which this persona was created, in milliseconds. */
    final Long createdAt;

    /** Username of the user who created this persona. */
    final String createdBy;

    /** Time (formatted string) at which this persona was last updated, in milliseconds. */
    final String updatedAt;

    /** Username of the user who last updated this persona. */
    final String updatedBy;

    /** Fixed value. */
    final String type;

    /** Fixed value. */
    final String level;

    /** TBC */
    String version;

    /** README content for the persona. */
    @JsonIgnore // TODO
    String readme;

    @JsonIgnore // TODO
    String resources;

    /** Details associated with the persona. */
    PersonaAttributes attributes;

    /**
     * Builds the minimal object necessary to create a persona.
     *
     * @param name of the persona, as it should appear in the UI
     * @return the minimal request necessary to update the persona, as a builder
     */
    public static PersonaBuilder<?, ?> creator(String name) {
        return Persona.builder().name(name).displayName(name);
    }

    /**
     * Builds the minimal object necessary to update a persona.
     *
     * @param id unique identifier (GUID) of the persona
     * @return the minimal request necessary to update the persona, as a builder
     */
    public static PersonaBuilder<?, ?> updater(String id, String name) {
        return Persona.builder().id(id).name(name);
    }

    /**
     * Send this persona to Atlan to create the persona in Atlan.
     *
     * @return the persona that was created
     * @throws AtlanException on any error during API invocation
     */
    public Persona create() throws AtlanException {
        return PersonasEndpoint.createPersona(this);
    }

    /**
     * Send this persona to Atlan to update the persona in Atlan.
     *
     * @throws AtlanException on any error during API invocation
     */
    public void update() throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException(ErrorCode.MISSING_PERSONA_ID);
        }
        PersonasEndpoint.updatePersona(this.id, this);
    }

    /**
     * Add the provided policy to this persona in Atlan.
     *
     * @param policy to add
     * @return the policy that was added
     * @throws AtlanException on any error during API invocation
     */
    public AbstractPolicy addPolicy(AbstractPolicy policy) throws AtlanException {
        if (this.id == null || this.id.length() == 0) {
            throw new InvalidRequestException(ErrorCode.MISSING_PERSONA_ID);
        }
        return PersonasEndpoint.addPolicyToPersona(this.id, policy);
    }

    /**
     * Delete a persona from Atlan.
     *
     * @param id unique identifier (GUID) of the persona to delete
     * @throws AtlanException on any error during API invocation
     */
    public static void delete(String id) throws AtlanException {
        PersonasEndpoint.deletePersona(id);
    }

    /**
     * Retrieves all personas currently defined in Atlan.
     * @return the list of personas currently defined in Atlan
     * @throws AtlanException on any error during API invocation
     */
    public static List<Persona> retrieveAll() throws AtlanException {
        PersonaResponse response = PersonasEndpoint.getAllPersonas();
        if (response != null && response.getRecords() != null) {
            return response.getRecords();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves the persona with a name that exactly matches the provided string.
     *
     * @param displayName name (as it appears in the UI) by which to retrieve the persona
     * @return the persona whose name (in the UI) contains the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public static Persona retrieveByName(String displayName) throws AtlanException {
        PersonaResponse response = PersonasEndpoint.getPersonas("{\"displayName\":\"" + displayName + "\"}");
        if (response != null && response.getRecords() != null) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class PersonaAttributes extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Preferences associated with the persona. */
        PersonaPreferences preferences;
    }

    @Getter
    @Setter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class PersonaPreferences extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Asset sidebar tabs that should be hidden from this persona. */
        @Singular("assetTabDeny")
        SortedSet<AssetSidebarTab> assetTabsDenyList;

        /** Unique identifiers (GUIDs) of custom metadata that should be hidden from this persona. */
        @Singular("customMetadataDeny")
        SortedSet<String> customMetadataDenyList;
    }
}
