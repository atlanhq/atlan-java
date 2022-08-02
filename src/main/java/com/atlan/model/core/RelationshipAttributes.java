/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class RelationshipAttributes extends AtlanObject {}
