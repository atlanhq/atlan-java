/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

/** Interface implemented by all enums to get the actual string value required by the Atlan API. */
public interface AtlanEnum {
  String getValue();
}
