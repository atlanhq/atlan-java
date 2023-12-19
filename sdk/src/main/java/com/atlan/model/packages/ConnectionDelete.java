/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import com.atlan.model.enums.AtlanPackageType;
import com.atlan.model.workflow.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConnectionDelete extends AbstractPackage {

    public static final String PREFIX = AtlanPackageType.CONNECTION_DELETE.getValue();

    /**
     * Create the base configuration for a new connection delete workflow.
     *
     * @param qualifiedName unique name of the connection whose assets should be deleted
     * @param purge if true, permanently delete the connection and its assets, otherwise only archive (soft-delete) them
     * @return the builder for the base configuration of a connection delete
     */
    public static ConnectionDeleteBuilder<?, ?> creator(String qualifiedName, boolean purge) {
        return _internal()
                .metadata()
                .parameter("connection-qualified-name", qualifiedName)
                .parameter("delete-assets", "" + true)
                .parameter("delete-type", purge ? "PURGE" : "SOFT");
    }

    public abstract static class ConnectionDeleteBuilder<
                    C extends ConnectionDelete, B extends ConnectionDeleteBuilder<C, B>>
            extends AbstractPackageBuilder<C, B> {

        /**
         * Set all the metadata for the package (labels, annotations, etc).
         *
         * @return the builder, with metadata set
         */
        protected ConnectionDeleteBuilder<C, B> metadata() {
            String epoch = getEpoch();
            return this.prefix(PREFIX)
                    .name("@atlan/connection-delete")
                    .runName(PREFIX + "-" + epoch)
                    .label("orchestration.atlan.com/certified", "true")
                    .label("orchestration.atlan.com/type", "utility")
                    .label("orchestration.atlan.com/verified", "true")
                    .label("package.argoproj.io/installer", "argopm")
                    .label("package.argoproj.io/name", "a-t-ratlans-l-a-s-hconnection-delete")
                    .label("package.argoproj.io/registry", "httpsc-o-l-o-ns-l-a-s-hs-l-a-s-hpackages.atlan.com")
                    .label("orchestration.atlan.com/atlan-ui", "true")
                    .annotation("orchestration.atlan.com/allowSchedule", "false")
                    .annotation("orchestration.atlan.com/categories", "utility,admin,connection,delete")
                    .annotation("orchestration.atlan.com/dependentPackage", "")
                    .annotation(
                            "orchestration.atlan.com/docsUrl", "https://ask.atlan.com/hc/en-us/articles/6755306791697")
                    .annotation("orchestration.atlan.com/emoji", "🗑️")
                    .annotation("orchestration.atlan.com/icon", "https://assets.atlan.com/assets/connection-delete.svg")
                    .annotation("orchestration.atlan.com/logo", "https://assets.atlan.com/assets/connection-delete.svg")
                    .annotation(
                            "orchestration.atlan.com/marketplaceLink",
                            "https://packages.atlan.com/-/web/detail/@atlan/connection-delete")
                    .annotation("orchestration.atlan.com/name", "Connection Delete")
                    .annotation("package.argoproj.io/author", "Atlan")
                    .annotation("package.argoproj.io/description", "Deletes a connection and all its related assets")
                    .annotation(
                            "package.argoproj.io/homepage",
                            "https://packages.atlan.com/-/web/detail/@atlan/connection-delete")
                    .annotation("package.argoproj.io/keywords", "[\"delete\",\"admin\",\"utility\"]")
                    .annotation("package.argoproj.io/name", "@atlan/connection-delete")
                    .annotation("package.argoproj.io/registry", "https://packages.atlan.com")
                    .annotation(
                            "package.argoproj.io/repository", "git+https://github.com/atlanhq/marketplace-packages.git")
                    .annotation("package.argoproj.io/support", "support@atlan.com")
                    .annotation("orchestration.atlan.com/atlanName", PREFIX + "-" + epoch);
        }
    }
}
