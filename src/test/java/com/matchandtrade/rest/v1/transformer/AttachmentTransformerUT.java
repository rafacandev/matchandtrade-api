package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AttachmentTransformerUT {
	private AttachmentTransformer fixture = new AttachmentTransformer();

	@Test
	public void transform_When_GivenEntity_Then_ReturnEquivalentJson() {
		AttachmentEntity expected = new AttachmentEntity();
		expected.setAttachmentId(UUID.randomUUID());
		expected.setContentType("image/png");
		expected.setName("name");
		AttachmentJson actual = fixture.transform(expected);
		assertEquals(expected.getAttachmentId(), actual.getAttachmentId());
		assertEquals(expected.getContentType(), actual.getContentType());
		assertEquals(expected.getName(), actual.getName());
	}

	@Test
	public void transform_When_GivenJson_Then_ReturnEquivalentEntity() {
		AttachmentJson expected = new AttachmentJson();
		expected.setAttachmentId(UUID.randomUUID());
		expected.setContentType("image/png");
		expected.setName("name");
		AttachmentEntity actual = fixture.transform(expected);
		assertEquals(expected.getAttachmentId(), actual.getAttachmentId());
		assertEquals(expected.getContentType(), actual.getContentType());
		assertEquals(expected.getName(), actual.getName());
	}
}
