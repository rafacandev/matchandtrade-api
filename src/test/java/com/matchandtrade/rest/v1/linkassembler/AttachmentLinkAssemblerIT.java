package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AttachmentLinkAssemblerIT {

	private AttachmentJson attachment;
	@Autowired
	private AttachmentHelper attachmentHelper;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();
	private AttachmentEntity existingAttachment;
	@Autowired
	private AttachmentLinkAssembler fixture;

	// TODO: MockBeans dirties the context. Can we create a mock factory for better performance?
	@MockBean(name = "mockedAttachmentService")
	private AttachmentService mockedAttachmentService;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		existingAttachment = attachmentHelper.createPersistedEntity();
		attachment = attachmentTransformer.transform(existingAttachment);

		when(mockedAttachmentService.findByAttachmentId(attachment.getAttachmentId())).thenReturn(existingAttachment);
		fixture.attachmentService = mockedAttachmentService;
	}

	@Test
	public void assemble_When_HasAttachmentId_Then_AssembleLinks() {
		fixture.assemble(attachment);
		verifyLinks();
	}

	@Test
	public void assemble_When_SearchResultContainsJsonWithAttachmentId_Then_AssembleLinks() {
		SearchResult<AttachmentJson> searchResult = new SearchResult<>(singletonList(attachment), new Pagination());
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
		return "http://localhost/matchandtrade-api/v1/essences/" + expectedOriginalEssence.getRelativePath();
	}

	private void verifyLinks() {
		Map.Entry<String, String> actualSelfLink = obtainLink("self", attachment.getLinks());
		assertEquals(buildSelfUrl(attachment), actualSelfLink.getValue());
		Map.Entry<String, String> actualOriginalLink = obtainLink("original", attachment.getLinks());
		EssenceEntity expectedOriginalEssence = obtainEssence(EssenceEntity.Type.ORIGINAL, existingAttachment);
		assertEquals(obtainEssenceHref(expectedOriginalEssence), actualOriginalLink.getValue());
		Map.Entry<String, String> actualThumbnailLink = obtainLink("thumbnail", attachment.getLinks());
		EssenceEntity expectedThumbnailEssence = obtainEssence(EssenceEntity.Type.THUMBNAIL, existingAttachment);
		assertEquals(obtainEssenceHref(expectedThumbnailEssence), actualThumbnailLink.getValue());
	}
}
