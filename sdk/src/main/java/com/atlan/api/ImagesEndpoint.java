/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanImage;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * API endpoints for managing images that can be uploaded to Atlan.
 */
public class ImagesEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/images";

    public ImagesEndpoint(AtlanClient client) {
        super(client);
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
        return upload(fromUrl, null);
    }

    /**
     * Upload an image from a given URL.
     *
     * @param fromUrl URL from which to retrieve the image (must be network-accessible from client running the code)
     * @param options to override default client settings
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     * @throws MalformedURLException if the provided URL is invalid
     * @throws IOException on any issues accessing or reading from the provided URL
     */
    public AtlanImage upload(String fromUrl, RequestOptions options)
            throws AtlanException, MalformedURLException, IOException {
        URL url = new URL(fromUrl);
        // Use getPath() to exclude query parameters from the filename
        File path = new File(url.getPath());
        // Set Accept header to indicate we expect an image, so CDNs that
        // use content negotiation (e.g. Brandfetch/CloudFront) serve the
        // actual image instead of redirecting to an HTML page
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept", "image/*,*/*;q=0.8");
        return upload(connection.getInputStream(), path.getName(), options);
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
        return upload(file, null);
    }

    /**
     * Upload an image from a local file.
     *
     * @param file local file containing the image
     * @param options to override default client settings
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     * @throws IOException on any issues accessing or reading from the provided file
     */
    public AtlanImage upload(File file, RequestOptions options) throws AtlanException, IOException {
        return upload(new FileInputStream(file), file.getName(), options);
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
        return upload(imageSrc, filename, null);
    }

    /**
     * Upload an image from a given input stream.
     *
     * @param imageSrc source of the image, as an input stream
     * @param filename name of the file the InputStream is reading (must include an extension that accurately represents the type of the image file)
     * @param options to override default client settings
     * @return details of the uploaded image
     * @throws AtlanException on any API communication issues
     */
    public AtlanImage upload(InputStream imageSrc, String filename, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        Map<String, String> extras = Map.of("name", "image");
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, imageSrc, filename, AtlanImage.class, extras, options);
    }
}
