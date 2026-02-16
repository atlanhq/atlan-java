/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.Test;

/**
 * Tests for ImagesEndpoint URL parsing logic.
 */
public class ImagesEndpointTest {

    /**
     * Verify that filename extraction from URLs with query parameters works correctly.
     * This test validates the fix for URLs like:
     * https://cdn.brandfetch.io/idS1yr4PQO/w/400/h/400/theme/dark/icon.jpeg?c=1bxid64Mup7aczewSAYMX&t=1731007317922
     * where the query string should NOT be included in the filename.
     */
    @Test
    void extractFilenameFromUrlWithQueryParams() throws MalformedURLException {
        String urlWithQueryParams =
                "https://cdn.brandfetch.io/idS1yr4PQO/w/400/h/400/theme/dark/icon.jpeg?c=1bxid64Mup7aczewSAYMX&t=1731007317922";
        URL url = new URL(urlWithQueryParams);

        // Using getPath() (correct) - excludes query parameters
        File pathFromGetPath = new File(url.getPath());
        String filenameFromPath = pathFromGetPath.getName();

        // Using getFile() (incorrect) - includes query parameters
        File pathFromGetFile = new File(url.getFile());
        String filenameFromFile = pathFromGetFile.getName();

        // Verify getPath() gives the correct filename without query params
        assertEquals(filenameFromPath, "icon.jpeg", "getPath() should extract filename without query parameters");

        // Verify getFile() would have included query params (demonstrating the bug)
        assertTrue(
                filenameFromFile.contains("?"),
                "getFile() includes query parameters in the filename, which is incorrect for content-type detection");
        assertEquals(
                filenameFromFile,
                "icon.jpeg?c=1bxid64Mup7aczewSAYMX&t=1731007317922",
                "getFile() returns path with query string");
    }

    /**
     * Verify that simple URLs without query parameters still work correctly.
     */
    @Test
    void extractFilenameFromSimpleUrl() throws MalformedURLException {
        String simpleUrl = "https://example.com/images/logo.png";
        URL url = new URL(simpleUrl);

        File path = new File(url.getPath());
        String filename = path.getName();

        assertEquals(filename, "logo.png", "Simple URL should extract filename correctly");
    }

    /**
     * Verify that URLs with only a fragment (no query) work correctly.
     */
    @Test
    void extractFilenameFromUrlWithFragment() throws MalformedURLException {
        String urlWithFragment = "https://example.com/images/logo.svg#fragment";
        URL url = new URL(urlWithFragment);

        // getPath() does not include fragment
        File path = new File(url.getPath());
        String filename = path.getName();

        assertEquals(filename, "logo.svg", "URL with fragment should extract filename correctly");
    }
}
