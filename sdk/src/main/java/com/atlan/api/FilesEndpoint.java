/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanFile;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * API endpoints for managing files that can be uploaded to Atlan.
 */
public class FilesEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/files";

    public FilesEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Upload a file from a given URL.
     *
     * @param fromUrl URL from which to retrieve the file (must be network-accessible from client running the code)
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     * @throws MalformedURLException if the provided URL is invalid
     * @throws IOException on any issues accessing or reading from the provided URL
     */
    public AtlanFile upload(String fromUrl) throws AtlanException, MalformedURLException, IOException {
        return upload(fromUrl, null);
    }

    /**
     * Upload a file from a given URL.
     *
     * @param fromUrl URL from which to retrieve the file (must be network-accessible from client running the code)
     * @param options to override default client settings
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     * @throws MalformedURLException if the provided URL is invalid
     * @throws IOException on any issues accessing or reading from the provided URL
     */
    public AtlanFile upload(String fromUrl, RequestOptions options)
            throws AtlanException, MalformedURLException, IOException {
        URL url = new URL(fromUrl);
        return upload(url.openStream(), url.getFile(), options);
    }

    /**
     * Upload a file from a local file.
     *
     * @param file local file containing the file
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     * @throws IOException on any issues accessing or reading from the provided file
     */
    public AtlanFile upload(File file) throws AtlanException, IOException {
        return upload(file, null);
    }

    /**
     * Upload a file from a local file.
     *
     * @param file local file containing the file
     * @param options to override default client settings
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     * @throws IOException on any issues accessing or reading from the provided file
     */
    public AtlanFile upload(File file, RequestOptions options) throws AtlanException, IOException {
        return upload(new FileInputStream(file), file.getName(), options);
    }

    /**
     * Upload a file from a given input stream.
     *
     * @param fileSrc source of the file, as an input stream
     * @param filename name of the file the InputStream is reading (must include an extension that accurately represents the type of the file)
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     */
    public AtlanFile upload(InputStream fileSrc, String filename) throws AtlanException {
        return upload(fileSrc, filename, null);
    }

    /**
     * Upload a file from a given input stream.
     *
     * @param fileSrc source of the file, as an input stream
     * @param filename name of the file the InputStream is reading (must include an extension that accurately represents the type of the file)
     * @param options to override default client settings
     * @return details of the uploaded file
     * @throws AtlanException on any API communication issues
     */
    public AtlanFile upload(InputStream fileSrc, String filename, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        Map<String, String> extras = Map.of(
                "name", "name",
                "prefix", "custom_file_upload",
                "force", "false",
                "excludePrefix", "false");
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, fileSrc, filename, AtlanFile.class, extras, options);
    }
}
