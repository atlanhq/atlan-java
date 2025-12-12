/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.AssetSmusMetadataFormStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * The details of MetadataForms attached to an AWS SMUS Asset
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AssetSmusMetadataFormDetails extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetSmusMetadataFormDetails";

    /** Fixed typeName for AssetSmusMetadataFormDetails. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Technical name of the Metadata Form */
    String assetMetadataFormName;

    /** Description of the Metadata Form */
    String assetMetadataFormDescription;

    /** Domain ID of the MetadataForm to which it belongs */
    String assetMetadataFormDomainId;

    /** Owning Project ID of the MetadataForm to which it belongs */
    String assetMetadataFormProjectId;

    /** Usability status of the MetadataForm */
    AssetSmusMetadataFormStatus assetMetadataFormStatus;

    /** Version of the MetadatForm published */
    String assetMetadataFormRevision;

    /** List of Key: Value pairs defining the fields of the MetadataForm */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Map<String, String>> assetMetadataFormFields;

    /**
     * Quickly create a new AssetSmusMetadataFormDetails.
     * @param assetMetadataFormName Technical name of the Metadata Form
     * @param assetMetadataFormDescription Description of the Metadata Form
     * @param assetMetadataFormDomainId Domain ID of the MetadataForm to which it belongs
     * @param assetMetadataFormProjectId Owning Project ID of the MetadataForm to which it belongs
     * @param assetMetadataFormStatus Usability status of the MetadataForm
     * @param assetMetadataFormRevision Version of the MetadatForm published
     * @param assetMetadataFormFields List of Key: Value pairs defining the fields of the MetadataForm
     * @return a AssetSmusMetadataFormDetails with the provided information
     */
    public static AssetSmusMetadataFormDetails of(
            String assetMetadataFormName,
            String assetMetadataFormDescription,
            String assetMetadataFormDomainId,
            String assetMetadataFormProjectId,
            AssetSmusMetadataFormStatus assetMetadataFormStatus,
            String assetMetadataFormRevision,
            List<Map<String, String>> assetMetadataFormFields) {
        return AssetSmusMetadataFormDetails.builder()
                .assetMetadataFormName(assetMetadataFormName)
                .assetMetadataFormDescription(assetMetadataFormDescription)
                .assetMetadataFormDomainId(assetMetadataFormDomainId)
                .assetMetadataFormProjectId(assetMetadataFormProjectId)
                .assetMetadataFormStatus(assetMetadataFormStatus)
                .assetMetadataFormRevision(assetMetadataFormRevision)
                .assetMetadataFormFields(assetMetadataFormFields)
                .build();
    }

    public abstract static class AssetSmusMetadataFormDetailsBuilder<
                    C extends AssetSmusMetadataFormDetails, B extends AssetSmusMetadataFormDetailsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
