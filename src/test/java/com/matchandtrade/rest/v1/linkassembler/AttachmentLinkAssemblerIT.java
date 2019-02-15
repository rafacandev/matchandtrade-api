package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AttachmentLinkAssemblerIT {
	@Autowired
	private AttachmentHelper attachmentHelper;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();
	private AttachmentEntity existingAttachmentEntity;
	private AttachmentJson existingAttachmentJson;
	@Autowired
	private AttachmentLinkAssembler fixture;

	@Before
	public void before() {
		existingAttachmentEntity = attachmentHelper.createPersistedEntity();
		existingAttachmentJson = attachmentTransformer.transform(existingAttachmentEntity);
	}

	@Test
	public void assemble_When_HasAttachmentId_Then_AssembleLinks() {
		fixture.assemble(existingAttachmentJson);
		verifyLinks();
	}

	@Test
	public void assemble_When_SearchResultContainsJsonWithAttachmentId_Then_AssembleLinks() {
		SearchResult<AttachmentJson> searchResult = new SearchResult<>(singletonList(existingAttachmentJson), new Pagination());
		fixture.assemble(searchResult);
		verifyLinks();
	}

	private String buildSelfUrl(AttachmentJson attachment) {
		return "http://localhost/matchandtrade-api/v1/attachments/" + attachment.getAttachmentId();
	}

	private Map.Entry<String, String> obtainLink(String self, List<Map.Entry<String, String>> links) {
		return links.stream().filter(link -> self.equals(link.getKey())).findFirst().get();
	}

	private EssenceEntity obtainEssence(EssenceEntity.Type type, AttachmentEntity existingAttachment) {
		return existingAttachment.getEssences().stream().filter(e -> type.equals(e.getType())).findFirst().get();
	}

	private String obtainEssenceHref(EssenceEntity expectedOriginalEssence) {
		return "http://localhost/matchandtrade-api/essences/" + expectedOriginalEssence.getRelativePath();
	}

	private void verifyLinks() {
		Map.Entry<String, String> actualSelfLink = obtainLink("self", existingAttachmentJson.getLinks());
		assertEquals(buildSelfUrl(existingAttachmentJson), actualSelfLink.getValue());
		Map.Entry<String, String> actualOriginalLink = obtainLink("original", existingAttachmentJson.getLinks());
		EssenceEntity expectedOriginalEssence = obtainEssence(EssenceEntity.Type.ORIGINAL, existingAttachmentEntity);
		assertEquals(obtainEssenceHref(expectedOriginalEssence), actualOriginalLink.getValue());
		Map.Entry<String, String> actualThumbnailLink = obtainLink("thumbnail", existingAttachmentJson.getLinks());
		EssenceEntity expectedThumbnailEssence = obtainEssence(EssenceEntity.Type.THUMBNAIL, existingAttachmentEntity);
		assertEquals(obtainEssenceHref(expectedThumbnailEssence), actualThumbnailLink.getValue());
	}
}
