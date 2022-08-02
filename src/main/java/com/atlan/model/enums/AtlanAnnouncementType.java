/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanAnnouncementType implements AtlanEnum {
  @SerializedName("information")
  INFORMATION("information"),

  @SerializedName("warning")
  WARNING("warning"),

  @SerializedName("issue")
  ISSUE("issue");

  @Getter(onMethod_ = {@Override})
  private final String value;

  AtlanAnnouncementType(String value) {
    this.value = value;
  }
}
