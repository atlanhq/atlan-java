/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a connection in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Connection extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Connection";

    /** Fixed typeName for column processes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of connection. */
    @Attribute
    AtlanConnectionCategory category;

    /** Unused attributes */
    @JsonIgnore
    String subCategory;

    @JsonIgnore
    Map<String, String> queryPreviewConfig;

    @JsonIgnore
    String queryConfig;

    /** Host name of the connection's source. */
    @Attribute
    String host;

    /** Port number to the connection's source. */
    @Attribute
    Integer port;

    /** When true, allow the source to be queried. */
    @Attribute
    Boolean allowQuery;

    /** When true, allow data previews of the source. */
    @Attribute
    Boolean allowQueryPreview;

    /** TBC */
    @Attribute
    String credentialStrategy;

    /** Maximum number of rows that can be returned for the source. */
    @Attribute
    Long rowLimit;

    /** TBC */
    @Attribute
    String defaultCredentialGuid;

    /** TBC */
    @Attribute
    String connectorIcon;

    /** TBC */
    @Attribute
    String connectorImage;

    /** TBC */
    @Attribute
    String sourceLogo;

    /**
     * Builds the minimal object necessary to create a connection.
     * Note: at least one of {@code #adminRoles}, {@code #adminGroups}, or {@code #adminUsers} must be
     * provided or an InvalidRequestException will be thrown.
     *
     * @param name of the connection
     * @param category type of connection
     * @param connectorName name of the connection's connector (this determines what logo appears for the assets)
     * @param adminRoles the GUIDs of the roles that can administer this connection
     * @param adminGroups the names of the groups that can administer this connection
     * @param adminUsers the names of the users that can administer this connection
     * @return the minimal object necessary to create the connection, as a builder
     * @throws InvalidRequestException if no admin has been defined for the connection
     */
    public static ConnectionBuilder<?, ?> creator(
            String name,
            AtlanConnectionCategory category,
            String connectorName,
            List<String> adminRoles,
            List<String> adminGroups,
            List<String> adminUsers)
            throws InvalidRequestException {
        boolean adminFound = false;
        ConnectionBuilder<?, ?> builder = Connection.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(connectorName))
                .category(category)
                .connectorName(connectorName);
        if (adminRoles != null && !adminRoles.isEmpty()) {
            adminFound = true;
            builder = builder.adminRoles(adminRoles);
        }
        if (adminGroups != null && !adminGroups.isEmpty()) {
            adminFound = true;
            builder = builder.adminGroups(adminGroups);
        }
        if (adminUsers != null && !adminUsers.isEmpty()) {
            adminFound = true;
            builder = builder.adminUsers(adminUsers);
        }
        if (adminFound) {
            return builder;
        } else {
            throw new InvalidRequestException(
                    "No admin provided for the connection, will not attempt to create one.",
                    "adminRoles,adminGroups,adminUsers",
                    "--",
                    "ATLAN-CLIENT-CONNECTION-400-001",
                    400,
                    null);
        }
    }

    /**
     * Builds the minimal object necessary to update a connection.
     *
     * @param qualifiedName of the connection
     * @param name of the connection
     * @return the minimal object necessary to update the connection, as a builder
     */
    public static ConnectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return Connection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a connection, from a potentially
     * more-complete connection object.
     *
     * @return the minimal object necessary to update the connection, as a builder
     */
    @Override
    protected ConnectionBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique connection name.
     * @param connectorName name of the connection's connector
     * @return a unique name for the connection
     */
    private static String generateQualifiedName(String connectorName) {
        long now = System.currentTimeMillis() / 1000;
        return "default/" + connectorName + "/" + now;
    }
}
