/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.util.StringUtils;
import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class ApiResource extends AtlanObject implements AtlanResponseInterface {
  public static final Charset CHARSET = StandardCharsets.UTF_8;

  private static AtlanResponseGetter atlanResponseGetter = new LiveAtlanResponseGetter();

  public static void setAtlanResponseGetter(AtlanResponseGetter arg) {
    ApiResource.atlanResponseGetter = arg;
  }

  private static String className(Class<?> clazz) {
    // Convert CamelCase to snake_case
    String className = StringUtils.toSnakeCase(clazz.getSimpleName());

    // Handle namespaced resources by checking if the class is in a sub-package, and if so prepend
    // it to the class name
    String[] parts = clazz.getPackage().getName().split("\\.", -1);
    assert parts.length == 3 || parts.length == 4;
    if (parts.length == 4) {
      // The first three parts are always "com.atlan.model", the fourth part is the sub-package
      className = parts[3] + "/" + className;
    }

    // Handle special cases
    switch (className) {
      case "invoice_item":
        return "invoiceitem";
      case "file_upload":
        return "file";
      default:
        return className;
    }
  }

  private transient AtlanResponse lastResponse;

  private transient JsonObject rawJsonObject;

  @Override
  public AtlanResponse getLastResponse() {
    return lastResponse;
  }

  @Override
  public void setLastResponse(AtlanResponse response) {
    this.lastResponse = response;
  }

  /**
   * Returns the raw JsonObject exposed by the Gson library. This can be used to access properties
   * that are not directly exposed by Atlan's Java library.
   *
   * <p>Note: You should always prefer using the standard property accessors whenever possible.
   * Because this method exposes Gson's underlying API, it is not considered fully stable. Atlan's
   * Java library might move off Gson in the future and this method would be removed or change
   * significantly.
   *
   * @return The raw JsonObject.
   */
  public JsonObject getRawJsonObject() {
    // Lazily initialize this the first time the getter is called.
    if ((this.rawJsonObject == null) && (this.getLastResponse() != null)) {
      this.rawJsonObject =
          ApiResource.GSON.fromJson(this.getLastResponse().body(), JsonObject.class);
    }

    return this.rawJsonObject;
  }

  protected static String singleClassUrl(Class<?> clazz) {
    return singleClassUrl(clazz, Atlan.getApiBase());
  }

  protected static String singleClassUrl(Class<?> clazz, String apiBase) {
    return String.format("%s/v1/%s", apiBase, className(clazz));
  }

  protected static String classUrl(Class<?> clazz) {
    return classUrl(clazz, Atlan.getApiBase());
  }

  protected static String classUrl(Class<?> clazz, String apiBase) {
    return String.format("%ss", singleClassUrl(clazz, apiBase));
  }

  protected static String instanceUrl(Class<?> clazz, String id) throws InvalidRequestException {
    return instanceUrl(clazz, id, Atlan.getApiBase());
  }

  protected static String instanceUrl(Class<?> clazz, String id, String apiBase)
      throws InvalidRequestException {
    return String.format("%s/%s", classUrl(clazz, apiBase), urlEncode(id));
  }

  protected static String subresourceUrl(Class<?> clazz, String id, Class<?> subClazz)
      throws InvalidRequestException {
    return subresourceUrl(clazz, id, subClazz, Atlan.getApiBase());
  }

  private static String subresourceUrl(Class<?> clazz, String id, Class<?> subClazz, String apiBase)
      throws InvalidRequestException {
    return String.format("%s/%s/%ss", classUrl(clazz, apiBase), urlEncode(id), className(subClazz));
  }

  public enum RequestMethod {
    GET,
    POST,
    PUT,
    DELETE
  }

  /** URL-encodes a string. */
  public static String urlEncode(String str) {
    // Preserve original behavior that passing null for an object id will lead
    // to us actually making a request to /v1/foo/null
    if (str == null) {
      return null;
    }

    try {
      // Don't use strict form encoding by changing the square bracket control
      // characters back to their literals. This is fine by the server, and
      // makes these parameter strings easier to read.
      return URLEncoder.encode(str, CHARSET.name()).replaceAll("%5B", "[").replaceAll("%5D", "]");
    } catch (UnsupportedEncodingException e) {
      // This can literally never happen, and lets us avoid having to catch
      // UnsupportedEncodingException in callers.
      throw new AssertionError("UTF-8 is unknown");
    }
  }

  /** URL-encode a string ID in url path formatting. */
  public static String urlEncodeId(String id) throws InvalidRequestException {
    if (id == null) {
      throw new InvalidRequestException(
          "Invalid null ID found for url path formatting. This can be because your string ID "
              + "argument to the API method is null, or the ID field in your Atlan object "
              + "instance is null. Please contact support@atlan.com on the latter case. ",
          null,
          null,
          null,
          0,
          null);
    }

    return urlEncode(id);
  }

  public static <T extends AtlanResponseInterface> T request(
      ApiResource.RequestMethod method,
      String url,
      AtlanObject payload,
      Class<T> clazz,
      RequestOptions options)
      throws AtlanException {
    checkNullTypedParams(url, payload);
    return request(method, url, payload.toJson(), clazz, options);
  }

  public static <T extends AtlanResponseInterface> T request(
      ApiResource.RequestMethod method,
      String url,
      String body,
      Class<T> clazz,
      RequestOptions options)
      throws AtlanException {
    return ApiResource.atlanResponseGetter.request(method, url, body, clazz, options);
  }

  /**
   * Invalidate null typed parameters.
   *
   * @param url request url associated with the given parameters.
   * @param params typed parameters to check for null value.
   */
  public static void checkNullTypedParams(String url, AtlanObject params) {
    if (params == null) {
      throw new IllegalArgumentException(
          String.format(
              "Found null params for %s. "
                  + "Please pass empty params using param builder via `builder().build()` instead.",
              url));
    }
  }
}
