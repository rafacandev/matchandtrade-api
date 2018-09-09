package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.repository.ArticleRepository;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleAttachmentControllerGetIT {
	
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private ArticleRepository articleRepository;
	private ArticleEntity article;
	private AttachmentEntity file;
	@Autowired
	private AttachmentRandom fileRandom;
	private ArticleAttachmentController fixture;
	private MembershipEntity membership;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleFileController(false);
		}
		file = fileRandom.nextPersistedEntity();
		membership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		article = articleRandom.nextPersistedEntity(membership);
		article.getAttachments().add(file);
		articleRepository.save(article);
	}
	
	@Test
	public void shouldGetArticleFiles() {
		SearchResult<AttachmentJson> response = fixture.get(membership.getMembershipId(), article.getArticleId(), 1, 1);
		assertNotNull(response);
		assertEquals(1, response.getPagination().getNumber());
		assertEquals(1, response.getPagination().getTotal());
		assertEquals(1, response.getPagination().getSize());
		assertEquals(1, response.getResultList().size());
		AttachmentJson fileResponse = response.getResultList().get(0);
		assertEquals(fileResponse.getAttachmentId(), file.getAttachmentId());
		assertEquals(fileResponse.getContentType(), file.getContentType());
		assertEquals(fileResponse.getName(), file.getName());
		// Two essences are expected one for ORIGINAL and one for THUMBNAIL
		assertEquals(1, fileResponse.getLinks().stream().filter(v -> v.getRel().equals("original")).collect(Collectors.toList()).size());
		assertEquals(1, fileResponse.getLinks().stream().filter(v -> v.getRel().equals("thumbnail")).collect(Collectors.toList()).size());
		assertEquals(2, file.getEssences().size());
	}

}
