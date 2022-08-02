package com.atlan.functional;

import com.atlan.exception.AtlanException;
import com.atlan.model.Glossary;
import com.atlan.model.GlossaryCategory;
import com.atlan.model.GlossaryTerm;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.MutatedEntities;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class GlossaryTest extends BaseAtlanTest {

  public static final String GLOSSARY_NAME = "JavaClient Test Glossary";
  public static final String CATEGORY_NAME = "JavaClient Test Category";
  public static final String TERM_NAME = "JavaClient Test Term";

  private static String glossaryGuid = null;
  private static String glossaryQame = null;

  private static String categoryGuid = null;
  private static String categoryQame = null;

  private static String termGuid = null;
  private static String termQame = null;

  @Test(groups = {"glossary.create"})
  void createGlossary() {
    Glossary glossary = Glossary.createRequest(GLOSSARY_NAME, GLOSSARY_NAME);
    try {
      EntityMutationResponse response = Entity.create(glossary);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      assertNull(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getCREATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossary");
      assertTrue(one instanceof Glossary);
      glossary = (Glossary) one;
      glossaryGuid = glossary.getGuid();
      assertNotNull(glossaryGuid);
      assertNotNull(glossary.getAttributes());
      glossaryQame = glossary.getAttributes().getQualifiedName();
      assertNotNull(glossaryQame);
      assertEquals(glossary.getAttributes().getName(), GLOSSARY_NAME);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"category.create"}, dependsOnGroups = {"glossary.create"})
  void createCategory() {
    GlossaryCategory category = GlossaryCategory.createRequest(CATEGORY_NAME, CATEGORY_NAME, glossaryGuid, null);
    try {
      EntityMutationResponse response = Entity.create(category);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      validateGlossaryUpdate(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getCREATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryCategory");
      assertTrue(one instanceof GlossaryCategory);
      category = (GlossaryCategory) one;
      categoryGuid = category.getGuid();
      assertNotNull(categoryGuid);
      assertNotNull(category.getAttributes());
      categoryQame = category.getAttributes().getQualifiedName();
      assertNotNull(categoryQame);
      assertEquals(category.getAttributes().getName(), CATEGORY_NAME);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"term.create"}, dependsOnGroups = {"glossary.create"})
  void createTerm() {
    GlossaryTerm term = GlossaryTerm.createRequest(TERM_NAME, TERM_NAME, glossaryGuid, null);
    try {
      EntityMutationResponse response = Entity.create(term);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      validateGlossaryUpdate(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getCREATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryTerm");
      assertTrue(one instanceof GlossaryTerm);
      term = (GlossaryTerm) one;
      termGuid = term.getGuid();
      assertNotNull(termGuid);
      assertNotNull(term.getAttributes());
      termQame = term.getAttributes().getQualifiedName();
      assertNotNull(termQame);
      assertEquals(term.getAttributes().getName(), TERM_NAME);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"glossary.update"}, dependsOnGroups = {"glossary.create"})
  void updateGlossary() {
    Glossary glossary = Glossary.updateRequest(glossaryGuid, GLOSSARY_NAME, GLOSSARY_NAME);
    glossary = glossary.toBuilder()
      .attributes(glossary.getAttributes().toBuilder()
        .certificateStatus(AtlanCertificateStatus.VERIFIED)
        .announcementType(AtlanAnnouncementType.INFORMATION)
        .announcementTitle(ANNOUNCEMENT_TITLE)
        .announcementMessage(ANNOUNCEMENT_MESSAGE)
        .build())
      .build();
    try {
      EntityMutationResponse response = Entity.update(glossary);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      assertNull(mutatedEntities.getCREATE());
      List<Entity> entities = mutatedEntities.getUPDATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossary");
      assertTrue(one instanceof Glossary);
      glossary = (Glossary) one;
      assertEquals(glossary.getGuid(), glossaryGuid);
      assertNotNull(glossary.getAttributes());
      assertEquals(glossary.getAttributes().getQualifiedName(), glossaryQame);
      assertEquals(glossary.getAttributes().getName(), GLOSSARY_NAME);
      assertEquals(glossary.getAttributes().getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
      assertEquals(glossary.getAttributes().getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
      assertEquals(glossary.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(glossary.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"category.update"}, dependsOnGroups = {"category.create"})
  void updateCategory() {
    GlossaryCategory category = GlossaryCategory.updateRequest(categoryQame, CATEGORY_NAME, glossaryGuid, null);
    category = category.toBuilder()
      .attributes(category.getAttributes().toBuilder()
        .certificateStatus(AtlanCertificateStatus.DRAFT)
        .announcementType(AtlanAnnouncementType.WARNING)
        .announcementTitle(ANNOUNCEMENT_TITLE)
        .announcementMessage(ANNOUNCEMENT_MESSAGE)
        .build())
      .build();
    try {
      EntityMutationResponse response = Entity.update(category);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      assertNull(mutatedEntities.getCREATE());
      List<Entity> entities = mutatedEntities.getUPDATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryCategory");
      assertTrue(one instanceof GlossaryCategory);
      category = (GlossaryCategory) one;
      assertEquals(category.getGuid(), categoryGuid);
      assertNotNull(category.getAttributes());
      assertEquals(category.getAttributes().getQualifiedName(), categoryQame);
      assertEquals(category.getAttributes().getName(), CATEGORY_NAME);
      assertEquals(category.getAttributes().getCertificateStatus(), AtlanCertificateStatus.DRAFT);
      assertEquals(category.getAttributes().getAnnouncementType(), AtlanAnnouncementType.WARNING);
      assertEquals(category.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(category.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"term.update"}, dependsOnGroups = {"term.create", "category.create"})
  void updateTerm() {
    GlossaryTerm term = GlossaryTerm.updateRequest(termQame, TERM_NAME, glossaryGuid, null);
    term = term.toBuilder()
      .attributes(term.getAttributes().toBuilder()
        .certificateStatus(AtlanCertificateStatus.DEPRECATED)
        .announcementType(AtlanAnnouncementType.ISSUE)
        .announcementTitle(ANNOUNCEMENT_TITLE)
        .announcementMessage(ANNOUNCEMENT_MESSAGE)
        .build())
      .build();
    try {
      EntityMutationResponse response = Entity.update(term);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getDELETE());
      assertNull(mutatedEntities.getCREATE());
      List<Entity> entities = mutatedEntities.getUPDATE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryTerm");
      assertTrue(one instanceof GlossaryTerm);
      term = (GlossaryTerm) one;
      assertEquals(term.getGuid(), termGuid);
      assertNotNull(term.getAttributes());
      assertEquals(term.getAttributes().getQualifiedName(), termQame);
      assertEquals(term.getAttributes().getName(), TERM_NAME);
      assertEquals(term.getAttributes().getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
      assertEquals(term.getAttributes().getAnnouncementType(), AtlanAnnouncementType.ISSUE);
      assertEquals(term.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(term.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"term.delete"}, dependsOnGroups = {"term.update"})
  void deleteTerm() {
    try {
      EntityMutationResponse response = Entity.delete(termGuid, AtlanDeleteType.HARD);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getCREATE());
      assertNull(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getDELETE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryTerm");
      assertTrue(one instanceof GlossaryTerm);
      GlossaryTerm term = (GlossaryTerm) one;
      assertEquals(term.getGuid(), termGuid);
      assertNotNull(term.getAttributes());
      assertEquals(term.getAttributes().getQualifiedName(), termQame);
      assertEquals(term.getAttributes().getName(), TERM_NAME);
      assertEquals(term.getAttributes().getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
      assertEquals(term.getAttributes().getAnnouncementType(), AtlanAnnouncementType.ISSUE);
      assertEquals(term.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(term.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
      // TODO: verify deleted status (and deletion handler)
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"category.delete"}, dependsOnGroups = {"term.delete", "category.update"})
  void deleteCategory() {
    try {
      EntityMutationResponse response = Entity.delete(categoryGuid, AtlanDeleteType.HARD);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getCREATE());
      assertNull(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getDELETE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossaryCategory");
      assertTrue(one instanceof GlossaryCategory);
      GlossaryCategory category = (GlossaryCategory) one;
      assertEquals(category.getGuid(), categoryGuid);
      assertNotNull(category.getAttributes());
      assertEquals(category.getAttributes().getQualifiedName(), categoryQame);
      assertEquals(category.getAttributes().getName(), CATEGORY_NAME);
      assertEquals(category.getAttributes().getCertificateStatus(), AtlanCertificateStatus.DRAFT);
      assertEquals(category.getAttributes().getAnnouncementType(), AtlanAnnouncementType.WARNING);
      assertEquals(category.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(category.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
      // TODO: verify deleted status (and deletion handler)
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  @Test(groups = {"glossary.delete"}, dependsOnGroups = {"category.delete"})
  void deleteGlossary() {
    try {
      EntityMutationResponse response = Entity.delete(glossaryGuid, AtlanDeleteType.HARD);
      assertNotNull(response);
      MutatedEntities mutatedEntities = response.getMutatedEntities();
      assertNotNull(mutatedEntities);
      assertNull(mutatedEntities.getCREATE());
      assertNull(mutatedEntities.getUPDATE());
      List<Entity> entities = mutatedEntities.getDELETE();
      assertNotNull(entities);
      assertEquals(entities.size(), 1);
      Entity one = entities.get(0);
      assertNotNull(one);
      assertEquals(one.getTypeName(), "AtlasGlossary");
      assertTrue(one instanceof Glossary);
      Glossary glossary = (Glossary) one;
      assertEquals(glossary.getGuid(), glossaryGuid);
      assertNotNull(glossary.getAttributes());
      assertEquals(glossary.getAttributes().getQualifiedName(), glossaryQame);
      assertEquals(glossary.getAttributes().getName(), GLOSSARY_NAME);
      assertEquals(glossary.getAttributes().getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
      assertEquals(glossary.getAttributes().getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
      assertEquals(glossary.getAttributes().getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
      assertEquals(glossary.getAttributes().getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
      // TODO: verify deleted status (and deletion handler)
    } catch (AtlanException e) {
      assertNull(e, "Unexpected exception: " + e.getMessage());
    }
  }

  private void validateGlossaryUpdate(List<Entity> entities) {
    assertNotNull(entities);
    assertEquals(entities.size(), 1);
    Entity one = entities.get(0);
    assertNotNull(one);
    assertEquals(one.getTypeName(), "AtlasGlossary");
    assertTrue(one instanceof Glossary);
    Glossary glossary = (Glossary) one;
    assertEquals(glossary.getGuid(), glossaryGuid);
    assertNotNull(glossary.getAttributes());
    assertEquals(glossary.getAttributes().getQualifiedName(), glossaryQame);
  }
}
