/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.Config
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.pkl.core.PklException
import org.testng.annotations.Test
import kotlin.test.assertFailsWith

object DuplicateInputsTest {

    @Test
    fun modelEvalFails() {
        val source = ModuleSource.path("src/test/resources/DuplicateInputsTest.pkl")
        assertFailsWith(
            exceptionClass = PklException::class,
            block = {
                ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
                    evaluator.evaluate(source).to<Config>()
                }
            },
        )
    }
}
