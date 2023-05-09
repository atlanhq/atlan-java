/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for catalog assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EventStore.class, name = EventStore.TYPE_NAME),
    @JsonSubTypes.Type(value = ObjectStore.class, name = ObjectStore.TYPE_NAME),
    @JsonSubTypes.Type(value = DataQuality.class, name = DataQuality.TYPE_NAME),
    @JsonSubTypes.Type(value = BI.class, name = BI.TYPE_NAME),
    @JsonSubTypes.Type(value = SaaS.class, name = SaaS.TYPE_NAME),
    @JsonSubTypes.Type(value = Dbt.class, name = Dbt.TYPE_NAME),
    @JsonSubTypes.Type(value = Resource.class, name = Resource.TYPE_NAME),
    @JsonSubTypes.Type(value = Insight.class, name = Insight.TYPE_NAME),
    @JsonSubTypes.Type(value = API.class, name = API.TYPE_NAME),
    @JsonSubTypes.Type(value = Tag.class, name = Tag.TYPE_NAME),
    @JsonSubTypes.Type(value = SQL.class, name = SQL.TYPE_NAME),
})
@Slf4j
public abstract class Catalog extends Asset {

    public static final String TYPE_NAME = "Catalog";

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<LineageProcess> inputToProcesses;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<LineageProcess> outputFromProcesses;

    /**
     * Reference to an asset by its qualifiedName.
     *
     * @param typeName the type of the asset being referenced
     * @param qualifiedName the qualifiedName of the asset to reference
     * @return reference to an asset that can be used for defining a lineage relationship to the asset
     */
    public static Catalog getLineageReference(String typeName, String qualifiedName) {
        Catalog ref = null;
        switch (typeName) {
            case ADLSAccount.TYPE_NAME:
                ref = ADLSAccount.refByQualifiedName(qualifiedName);
                break;
            case ADLSContainer.TYPE_NAME:
                ref = ADLSContainer.refByQualifiedName(qualifiedName);
                break;
            case ADLSObject.TYPE_NAME:
                ref = ADLSObject.refByQualifiedName(qualifiedName);
                break;
            case APIPath.TYPE_NAME:
                ref = APIPath.refByQualifiedName(qualifiedName);
                break;
            case APISpec.TYPE_NAME:
                ref = APISpec.refByQualifiedName(qualifiedName);
                break;
            case AtlanQuery.TYPE_NAME:
                ref = AtlanQuery.refByQualifiedName(qualifiedName);
                break;
            case Column.TYPE_NAME:
                ref = Column.refByQualifiedName(qualifiedName);
                break;
            case DataStudioAsset.TYPE_NAME:
                ref = DataStudioAsset.refByQualifiedName(qualifiedName);
                break;
            case Database.TYPE_NAME:
                ref = Database.refByQualifiedName(qualifiedName);
                break;
            case DbtMetric.TYPE_NAME:
                ref = DbtMetric.refByQualifiedName(qualifiedName);
                break;
            case DbtModel.TYPE_NAME:
                ref = DbtModel.refByQualifiedName(qualifiedName);
                break;
            case DbtModelColumn.TYPE_NAME:
                ref = DbtModelColumn.refByQualifiedName(qualifiedName);
                break;
            case DbtSource.TYPE_NAME:
                ref = DbtSource.refByQualifiedName(qualifiedName);
                break;
            case GCSBucket.TYPE_NAME:
                ref = GCSBucket.refByQualifiedName(qualifiedName);
                break;
            case GCSObject.TYPE_NAME:
                ref = GCSObject.refByQualifiedName(qualifiedName);
                break;
            case Insight.TYPE_NAME:
                ref = Insight.refByQualifiedName(qualifiedName);
                break;
            case Link.TYPE_NAME:
                ref = Link.refByQualifiedName(qualifiedName);
                break;
            case LookerDashboard.TYPE_NAME:
                ref = LookerDashboard.refByQualifiedName(qualifiedName);
                break;
            case LookerExplore.TYPE_NAME:
                ref = LookerExplore.refByQualifiedName(qualifiedName);
                break;
            case LookerField.TYPE_NAME:
                ref = LookerField.refByQualifiedName(qualifiedName);
                break;
            case LookerFolder.TYPE_NAME:
                ref = LookerFolder.refByQualifiedName(qualifiedName);
                break;
            case LookerLook.TYPE_NAME:
                ref = LookerLook.refByQualifiedName(qualifiedName);
                break;
            case LookerModel.TYPE_NAME:
                ref = LookerModel.refByQualifiedName(qualifiedName);
                break;
            case LookerProject.TYPE_NAME:
                ref = LookerProject.refByQualifiedName(qualifiedName);
                break;
            case LookerQuery.TYPE_NAME:
                ref = LookerQuery.refByQualifiedName(qualifiedName);
                break;
            case LookerTile.TYPE_NAME:
                ref = LookerTile.refByQualifiedName(qualifiedName);
                break;
            case LookerView.TYPE_NAME:
                ref = LookerView.refByQualifiedName(qualifiedName);
                break;
            case MaterializedView.TYPE_NAME:
                ref = MaterializedView.refByQualifiedName(qualifiedName);
                break;
            case MetabaseCollection.TYPE_NAME:
                ref = MetabaseCollection.refByQualifiedName(qualifiedName);
                break;
            case MetabaseDashboard.TYPE_NAME:
                ref = MetabaseDashboard.refByQualifiedName(qualifiedName);
                break;
            case MetabaseQuestion.TYPE_NAME:
                ref = MetabaseQuestion.refByQualifiedName(qualifiedName);
                break;
            case ModeChart.TYPE_NAME:
                ref = ModeChart.refByQualifiedName(qualifiedName);
                break;
            case ModeCollection.TYPE_NAME:
                ref = ModeCollection.refByQualifiedName(qualifiedName);
                break;
            case ModeQuery.TYPE_NAME:
                ref = ModeQuery.refByQualifiedName(qualifiedName);
                break;
            case ModeReport.TYPE_NAME:
                ref = ModeReport.refByQualifiedName(qualifiedName);
                break;
            case ModeWorkspace.TYPE_NAME:
                ref = ModeWorkspace.refByQualifiedName(qualifiedName);
                break;
            case PowerBIColumn.TYPE_NAME:
                ref = PowerBIColumn.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDashboard.TYPE_NAME:
                ref = PowerBIDashboard.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDataflow.TYPE_NAME:
                ref = PowerBIDataflow.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDataset.TYPE_NAME:
                ref = PowerBIDataset.refByQualifiedName(qualifiedName);
                break;
            case PowerBIDatasource.TYPE_NAME:
                ref = PowerBIDatasource.refByQualifiedName(qualifiedName);
                break;
            case PowerBIMeasure.TYPE_NAME:
                ref = PowerBIMeasure.refByQualifiedName(qualifiedName);
                break;
            case PowerBIPage.TYPE_NAME:
                ref = PowerBIPage.refByQualifiedName(qualifiedName);
                break;
            case PowerBIReport.TYPE_NAME:
                ref = PowerBIReport.refByQualifiedName(qualifiedName);
                break;
            case PowerBITable.TYPE_NAME:
                ref = PowerBITable.refByQualifiedName(qualifiedName);
                break;
            case PowerBITile.TYPE_NAME:
                ref = PowerBITile.refByQualifiedName(qualifiedName);
                break;
            case PowerBIWorkspace.TYPE_NAME:
                ref = PowerBIWorkspace.refByQualifiedName(qualifiedName);
                break;
            case PresetChart.TYPE_NAME:
                ref = PresetChart.refByQualifiedName(qualifiedName);
                break;
            case PresetDashboard.TYPE_NAME:
                ref = PresetDashboard.refByQualifiedName(qualifiedName);
                break;
            case PresetDataset.TYPE_NAME:
                ref = PresetDataset.refByQualifiedName(qualifiedName);
                break;
            case PresetWorkspace.TYPE_NAME:
                ref = PresetWorkspace.refByQualifiedName(qualifiedName);
                break;
            case Procedure.TYPE_NAME:
                ref = Procedure.refByQualifiedName(qualifiedName);
                break;
            case Readme.TYPE_NAME:
                ref = Readme.refByQualifiedName(qualifiedName);
                break;
            case ReadmeTemplate.TYPE_NAME:
                ref = ReadmeTemplate.refByQualifiedName(qualifiedName);
                break;
            case S3Bucket.TYPE_NAME:
                ref = S3Bucket.refByQualifiedName(qualifiedName);
                break;
            case S3Object.TYPE_NAME:
                ref = S3Object.refByQualifiedName(qualifiedName);
                break;
            case SalesforceDashboard.TYPE_NAME:
                ref = SalesforceDashboard.refByQualifiedName(qualifiedName);
                break;
            case SalesforceField.TYPE_NAME:
                ref = SalesforceField.refByQualifiedName(qualifiedName);
                break;
            case SalesforceObject.TYPE_NAME:
                ref = SalesforceObject.refByQualifiedName(qualifiedName);
                break;
            case SalesforceOrganization.TYPE_NAME:
                ref = SalesforceOrganization.refByQualifiedName(qualifiedName);
                break;
            case SalesforceReport.TYPE_NAME:
                ref = SalesforceReport.refByQualifiedName(qualifiedName);
                break;
            case Schema.TYPE_NAME:
                ref = Schema.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataElement.TYPE_NAME:
                ref = SigmaDataElement.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataElementField.TYPE_NAME:
                ref = SigmaDataElementField.refByQualifiedName(qualifiedName);
                break;
            case SigmaDataset.TYPE_NAME:
                ref = SigmaDataset.refByQualifiedName(qualifiedName);
                break;
            case SigmaDatasetColumn.TYPE_NAME:
                ref = SigmaDatasetColumn.refByQualifiedName(qualifiedName);
                break;
            case SigmaPage.TYPE_NAME:
                ref = SigmaPage.refByQualifiedName(qualifiedName);
                break;
            case SigmaWorkbook.TYPE_NAME:
                ref = SigmaWorkbook.refByQualifiedName(qualifiedName);
                break;
            case SnowflakePipe.TYPE_NAME:
                ref = SnowflakePipe.refByQualifiedName(qualifiedName);
                break;
            case SnowflakeStream.TYPE_NAME:
                ref = SnowflakeStream.refByQualifiedName(qualifiedName);
                break;
            case Table.TYPE_NAME:
                ref = Table.refByQualifiedName(qualifiedName);
                break;
            case TablePartition.TYPE_NAME:
                ref = TablePartition.refByQualifiedName(qualifiedName);
                break;
            case TableauCalculatedField.TYPE_NAME:
                ref = TableauCalculatedField.refByQualifiedName(qualifiedName);
                break;
            case TableauDashboard.TYPE_NAME:
                ref = TableauDashboard.refByQualifiedName(qualifiedName);
                break;
            case TableauDatasource.TYPE_NAME:
                ref = TableauDatasource.refByQualifiedName(qualifiedName);
                break;
            case TableauDatasourceField.TYPE_NAME:
                ref = TableauDatasourceField.refByQualifiedName(qualifiedName);
                break;
            case TableauFlow.TYPE_NAME:
                ref = TableauFlow.refByQualifiedName(qualifiedName);
                break;
            case TableauMetric.TYPE_NAME:
                ref = TableauMetric.refByQualifiedName(qualifiedName);
                break;
            case TableauProject.TYPE_NAME:
                ref = TableauProject.refByQualifiedName(qualifiedName);
                break;
            case TableauSite.TYPE_NAME:
                ref = TableauSite.refByQualifiedName(qualifiedName);
                break;
            case TableauWorkbook.TYPE_NAME:
                ref = TableauWorkbook.refByQualifiedName(qualifiedName);
                break;
            case TableauWorksheet.TYPE_NAME:
                ref = TableauWorksheet.refByQualifiedName(qualifiedName);
                break;
            case View.TYPE_NAME:
                ref = View.refByQualifiedName(qualifiedName);
                break;
            default:
                // Do nothing — not a supported Catalog subtype
                break;
        }
        return ref;
    }
}
