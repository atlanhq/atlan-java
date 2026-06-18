/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_DOMAIN_DELIMITER
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.FieldMismatchStrategy
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/*
 * Regression test for CSA-444: domain hierarchy paths must not be truncated for
 * mid-tier domains. Builds a 3-level domain hierarchy (grandparent > parent > child)
 * with a data product under the deepest child, then verifies the mesh export
 * resolves full level-order identities (parents before children) rather than the
 * 1-level-short paths produced when the domain cache is not preloaded.
 */
class ExportDomainHierarchyTest : PackageTest("edh") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val grandparent = makeUnique("gp")
    private val parent = makeUnique("p")
    private val child = makeUnique("c")
    private val product = makeUnique("prod")

    // Expected, fully-qualified hierarchy identities (parents-before-children).
    private val parentIdentity = "$grandparent$DATA_DOMAIN_DELIMITER$parent"
    private val childIdentity = "$parentIdentity$DATA_DOMAIN_DELIMITER$child"

    private val files =
        listOf(
            "products-export.csv",
            "debug.log",
        )

    private fun createHierarchy() {
        val gp = DataDomain.creator(grandparent).build()
        val gpResult = gp.save(client).getResult(gp)
        val p = DataDomain.creator(parent, gpResult.qualifiedName).build()
        val pResult = p.save(client).getResult(p)
        val c = DataDomain.creator(child, pResult.qualifiedName).build()
        val cResult = c.save(client).getResult(c)
        val dp =
            DataProduct
                .creator(client, product, cResult.qualifiedName, GlossaryTerm.select(client).build())
                .build()
        dp.save(client).getResult(dp)
    }

    override fun setup() {
        createHierarchy()
        // Ensure the freshly-created assets are searchable before exporting.
        retrySearchUntil(DataDomain.select(client).where(DataDomain.NAME.eq(child)).toRequest(), 1)
        retrySearchUntil(DataProduct.select(client).where(DataProduct.NAME.eq(product)).toRequest(), 1)
        runCustomPackage(
            AssetExportBasicCfg(
                exportScope = "PRODUCTS_ONLY",
                includeGlossaries = false,
                includeProducts = true,
            ),
            Exporter::main,
        )
    }

    override fun teardown() {
        removeDomainAndChildren(grandparent)
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @Test
    fun hierarchyNotTruncated() {
        val exportFile = Paths.get(testDirectory, "products-export.csv")
        val records =
            CsvReader
                .builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .missingFieldStrategy(FieldMismatchStrategy.STRICT)
                .extraFieldStrategy(FieldMismatchStrategy.STRICT)
                .ofCsvRecord(exportFile)
                .stream()
                .toList()

        val header = records.first().fields
        val nameIdx = header.indexOf("name")
        val typeIdx = header.indexOf("typeName")
        val parentDomainIdx = header.indexOf("parentDomain")
        val dataDomainIdx = header.indexOf("dataDomain")
        assert(nameIdx != -1) { "Column 'name' not found in export" }
        assert(typeIdx != -1) { "Column 'typeName' not found in export" }
        assert(parentDomainIdx != -1) { "Column 'parentDomain' not found in export" }
        assert(dataDomainIdx != -1) { "Column 'dataDomain' not found in export" }

        val rows = records.drop(1)

        // Mid-tier child domain must carry the full parent path, not a truncated 1-level path.
        val childRow =
            rows.firstOrNull { it.fields[typeIdx] == DataDomain.TYPE_NAME && it.fields[nameIdx] == child }
        assertNotNull(childRow, "Child domain row '$child' missing from export")
        assertEquals(parentIdentity, childRow.fields[parentDomainIdx], "Child domain parent path truncated")

        // Product under the deepest domain must reference the full 3-level domain identity.
        val productRow =
            rows.firstOrNull { it.fields[typeIdx] == DataProduct.TYPE_NAME && it.fields[nameIdx] == product }
        assertNotNull(productRow, "Product row '$product' missing from export")
        assertEquals(childIdentity, productRow.fields[dataDomainIdx], "Product domain path truncated")
    }
}
