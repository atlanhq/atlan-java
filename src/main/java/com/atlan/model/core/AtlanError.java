/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AtlanError extends AtlanObject {

  /** A short string indicating the error code reported. */
  String errorCode;

  /** A human-readable message providing more details about the error. */
  String errorMessage;
}
