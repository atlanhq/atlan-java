package com.atlan.pkg.lftag

import com.atlan.pkg.PackageTest
import org.testng.annotations.Test

class CVSProducerTest : PackageTest() {
    @Test
    fun transform() {
        val connectionMap = mapOf("dev" to "default/redshift/1687361820/wide_world_importers")
        val metadataMap = mapOf(
            "business_domain" to "Data Domain::Business Domain",
            "domain" to "Data Domain::Domain",
            "Subdomain" to "Data Domain::Subdomain",
            "bi_layer" to "Data Domain::BI Layer",
            "data_product_owner" to "Data Domain::Data Product Owner",
            "bi_domain" to "Data Domain::BI Domain",
            "privacy_classification" to "Data Privacy::Privacy Classification",
            "security_classification" to "Data Privacy::Security Classification",
            "data_masking" to "Data Privacy::Data Masking",
            "subclassification" to "Data Privacy::SubClassification",
            "score" to "completeness check::Score",
            "missing_metadata" to "completeness check::Missing metadata",
            "score_contributors" to "completeness check::Score contributors",
            "environment" to "Database Details::Environment",
            "zone" to "Database Details::Zone",
            "data_load_method" to "Database Details::Data Load Method",
            "cde" to "Data Governance::CDE",
            "duplication_count" to "Duplication::Duplication Count",
            "other_duplicate_assets" to "Duplication::Other Duplicate Assets",
        )
        val producer = CSVProducer(connectionMap, metadataMap, testDirectory)
        producer.transform("./src/test/resources/sample.json", "data.csv")
    }
}
