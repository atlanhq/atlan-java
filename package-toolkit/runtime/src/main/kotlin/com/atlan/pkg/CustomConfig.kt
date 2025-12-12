/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlin.reflect.KProperty1
import kotlin.reflect.full.primaryConstructor

/**
 * Base class that must be extended for any configuration, to define the expected contents
 * of the configuration.
 */
@JsonIgnoreProperties("defaultInstance\$delegate")
abstract class CustomConfig<T : Any> {
    lateinit var runtime: RuntimeConfig

    // An instance of the config class with only default values
    private val defaultInstance: T by lazy {
        val clazz = this::class
        val constructor =
            clazz.primaryConstructor
                ?: error("No primary constructor for ${clazz.qualifiedName}")
        @Suppress("UNCHECKED_CAST")
        constructor.callBy(emptyMap()) as T
    }

    /**
     * Retrieve the effective value of a configuration parameter.
     * If the controller element is set to "default", will always give you the default
     * value for that parameter; otherwise, will use the value of the parameter as-received.
     *
     * @param property for which to retrieve the effective value
     * @param controller element of the config that controls whether to use default values or advanced config values
     * @param advancedOptIn string value that means the advanced config values (non-defaults) should be kept
     * @return the effective value of the provided property
     */
    @Suppress("UNCHECKED_CAST")
    fun <R> getEffectiveValue(
        property: KProperty1<T, R>,
        controller: KProperty1<T, String?>,
        advancedOptIn: String = "advanced",
    ): R {
        val keepActual = (controller.get(this as T) ?: "") == advancedOptIn
        val option = property.get(this as T)
        return if (keepActual) {
            option
        } else {
            val defaultValue = property.get(defaultInstance)
            if (option != defaultValue) {
                val logger = Utils.getLogger(this.javaClass.name)
                logger.warn {
                    "Found non-default value for configuration parameter '${property.name}' -- but '${controller.name}' not set to \"$advancedOptIn\", so falling back to default."
                }
            }
            property.get(defaultInstance)
        }
    }
}
