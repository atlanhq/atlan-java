/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.adoption.AdoptionExporter.Section
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests which portions of the export a given configuration produces, and therefore which
 * files are handed to email or object-storage delivery. (CSA-618: in CSV mode nothing was
 * added to the delivery list at all, so emails arrived with no attachments.)
 */
class SectionsToExportTest {
    @Test
    fun csvFileNamesAreStable() {
        assertEquals("views.csv", Section.VIEWS.csvFileName)
        assertEquals("user-views.csv", Section.USER_VIEWS.csvFileName)
        assertEquals("changes.csv", Section.CHANGES.csvFileName)
        assertEquals("user-changes.csv", Section.USER_CHANGES.csvFileName)
        assertEquals("user-searches.csv", Section.USER_SEARCHES.csvFileName)
    }

    @Test
    fun defaultConfigExportsViewsOnly() {
        assertEquals(
            listOf(Section.VIEWS),
            AdoptionExporter.sectionsToExport(AdoptionExportCfg()),
        )
    }

    @Test
    fun changesOnly() {
        assertEquals(
            listOf(Section.CHANGES),
            AdoptionExporter.sectionsToExport(
                AdoptionExportCfg(
                    includeViews = "NONE",
                    includeChanges = "YES",
                    fileFormat = "CSV",
                ),
            ),
        )
    }

    @Test
    fun detailsFollowTheirParentSection() {
        assertEquals(
            listOf(Section.VIEWS, Section.USER_VIEWS, Section.CHANGES, Section.USER_CHANGES, Section.USER_SEARCHES),
            AdoptionExporter.sectionsToExport(
                AdoptionExportCfg(
                    includeViews = "BY_VIEWS",
                    viewsDetails = "YES",
                    includeChanges = "YES",
                    changesDetails = "YES",
                    includeSearches = "YES",
                    fileFormat = "CSV",
                ),
            ),
        )
    }

    @Test
    fun detailsAreSkippedWhenTheirParentSectionIsOff() {
        val sections =
            AdoptionExporter.sectionsToExport(
                AdoptionExportCfg(
                    includeViews = "NONE",
                    viewsDetails = "YES",
                    includeChanges = "NO",
                    changesDetails = "YES",
                    fileFormat = "CSV",
                ),
            )
        assertTrue(sections.isEmpty(), "Expected no sections, but found: $sections")
    }

    @Test
    fun nothingSelectedExportsNothing() {
        assertTrue(
            AdoptionExporter
                .sectionsToExport(
                    AdoptionExportCfg(
                        includeViews = "NONE",
                        includeChanges = "NO",
                        includeSearches = "NO",
                        fileFormat = "CSV",
                    ),
                ).isEmpty(),
        )
    }
}
