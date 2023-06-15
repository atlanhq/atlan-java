/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package ${packageRoot}.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanPolicyAction;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.PersonaMetadataAction;
import com.atlan.model.enums.PersonaGlossaryAction;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KeywordFields;
<#list attributes as attribute>
<#if attribute.type.type == "ENUM">
import ${packageRoot}.enums.${attribute.type.name};
<#elseif attribute.type.type == "STRUCT">
import ${packageRoot}.structs.${attribute.type.name};
</#if>
</#list>

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
public interface I${className} {

    <#list attributes as attribute>
    /** ${attribute.description} */
    ${attribute.fullType} get${attribute.renamed?cap_first}();

    </#list>
}
