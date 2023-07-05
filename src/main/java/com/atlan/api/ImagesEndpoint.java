/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanImage;
import com.atlan.net.ApiResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * API endpoints for managing images that can be uploaded to Atlan.
 */
public class ImagesEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/images";

    private final AtlanClient client;

    public ImagesEndpoint(AtlanClient client) {
        this.client = client;
    }

    /**
     * Upload an image from a given URL.
     *
     * @param fromUrl URL from which to retrieve the image (must be network-accessible from client running the code)
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     * @throws MalformedURLException if the provided URL is invalid
     * @throws IOException on any issues accessing or reading from the provided URL
     */
    public AtlanImage upload(String fromUrl) throws AtlanException, MalformedURLException, IOException {
        URL url = new URL(fromUrl);
        return upload(url.openStream(), url.getFile());
    }

    /**
     * Upload an image from a local file.
     *
     * @param file local file containing the image
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     * @throws IOException on any issues accessing or reading from the provided file
     */
    public AtlanImage upload(File file) throws AtlanException, IOException {
        return upload(new FileInputStream(file), file.getName());
    }

    /**
     * Upload an image from a given input stream.
     *
     * @param imageSrc source of the image, as an input stream
     * @param filename name of the file the InputStream is reading (must include an extension that accurately represents the type of the image file)
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     */
    public AtlanImage upload(InputStream imageSrc, String filename) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, imageSrc, filename, AtlanImage.class, null);
    }
}
