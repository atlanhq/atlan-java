/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.DataModelType;
import org.testng.annotations.Test;

/**
 * Regression tests for CONNECT-50 / BLDX-1482.
 *
 * <p>The version-agnostic {@code ModelEntity.creator(...)} and {@code ModelAttribute.creator(...)}
 * overloads used to set only {@code modelVersionAgnosticQualifiedName} and never {@code qualifiedName}.
 * Since {@code qualifiedName} is a mandatory primary key for Model assets (and is generated
 * client-side, not derived server-side on the non-versioned path), creating these assets without a
 * {@code modelBusinessDate} silently failed at {@code POST /api/meta/entity/bulk}.
 *
 * <p>These are pure builder assertions -- no live tenant -- deliberately exercising the
 * <b>non-versioned</b> path (no {@code modelBusinessDate}), which the integration tests never covered.
 */
public class ModelCreatorTest {

    private static final String CONNECTION_QN = "default/model/1234567890";
    private static final String MODEL_NAME = "CDO";
    private static final String MODEL_TYPE = DataModelType.LOGICAL.toString();

    private static ModelDataModel model() {
        return ModelDataModel.creator(MODEL_NAME, CONNECTION_QN, DataModelType.LOGICAL)
                .build();
    }

    @Test
    void entityCreatorFromModelSetsQualifiedName() throws AtlanException {
        ModelDataModel model = model();
        ModelEntity entity = ModelEntity.creator("MessageBase", model).build();
        assertNotNull(
                entity.getQualifiedName(),
                "qualifiedName must be set on the version-agnostic entity creator (CONNECT-50).");
        assertEquals(
                entity.getQualifiedName(),
                entity.getModelVersionAgnosticQualifiedName(),
                "qualifiedName should mirror modelVersionAgnosticQualifiedName, matching the other Model* creators.");
        assertTrue(entity.getQualifiedName().startsWith(model.getQualifiedName() + "/"));
    }

    @Test
    void entityCreatorFromModelQualifiedNameSetsQualifiedName() {
        String modelQualifiedName = CONNECTION_QN + "/" + MODEL_NAME;
        ModelEntity entity = ModelEntity.creator("MessageBase", modelQualifiedName, MODEL_TYPE)
                .build();
        assertNotNull(entity.getQualifiedName(), "qualifiedName must be set on the 3-arg entity creator (CONNECT-50).");
        assertEquals(entity.getQualifiedName(), entity.getModelVersionAgnosticQualifiedName());
        assertTrue(entity.getQualifiedName().startsWith(modelQualifiedName + "/"));
    }

    @Test
    void attributeCreatorSetsQualifiedName() {
        String entityQualifiedName = CONNECTION_QN + "/" + MODEL_NAME + "/messagebase";
        ModelAttribute attribute = ModelAttribute.creator("CorrelationId", entityQualifiedName, MODEL_TYPE)
                .build();
        assertNotNull(
                attribute.getQualifiedName(),
                "qualifiedName must be set on the version-agnostic attribute creator (CONNECT-50).");
        assertEquals(attribute.getQualifiedName(), attribute.getModelVersionAgnosticQualifiedName());
        assertTrue(attribute.getQualifiedName().startsWith(entityQualifiedName + "/"));
    }
}
