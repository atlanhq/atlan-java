/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanResponse;

public interface AtlanResponseInterface {
  AtlanResponse getLastResponse();

  void setLastResponse(AtlanResponse response);
}
