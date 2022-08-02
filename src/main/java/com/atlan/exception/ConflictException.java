/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.exception;

public class ConflictException extends AtlanException {
  private static final long serialVersionUID = 2L;

  public ConflictException(
      String message, String requestId, String code, Integer statusCode, Throwable e) {
    super(message, requestId, code, statusCode, e);
  }
}
