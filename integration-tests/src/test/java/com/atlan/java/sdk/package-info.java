/**
 * Functional tests that run against a live environment. These will side-effect the environment, and in particular can
 * leave behind artifacts that are not automatically removed. They should therefore NOT be used in any environment
 * other than internal Atlan test environments.
 * We intend to use them primarily to:
 * 1. Regression test the SDK through any changes, to ensure the code still operates as intended.
 * 2. Provide some deeper examples of "working code" to anyone looking for such examples.
 */
package com.atlan.java.sdk;
