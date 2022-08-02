/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.exception;

public class PermissionException extends AuthenticationException {
  private static final long serialVersionUID = 2L;

  public PermissionException(String message, String requestId, String code, Integer statusCode) {
    super(message, requestId, code, statusCode);
  }
}
