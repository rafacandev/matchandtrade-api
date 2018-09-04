package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemAttachmentControllerDeleteIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ArticleRepositoryFacade itemRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRepositoryFacade;
	private AttachmentEntity file;
	@Autowired
	private AttachmentRandom fileRandom;
	private ItemAttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemFileController(false);
		}
		file = fileRandom.nextPersistedEntity();
	}
	
	@Test
	public void shouldDeleteFileFromItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity item = itemRandom.nextPersistedEntity(membership);
		item.getAttachments().add(file);
		itemRepositoryFacade.save(item);
		fixture.delete(membership.getTradeMembershipId(), item.getArticleId(), file.getAttachmentId());
		SearchResult<AttachmentEntity> files = fileRepositoryFacade.findAttachmentsByArticleId(item.getArticleId(), 1, 10);
		assertEquals(0, files.getResultList().size());
		assertEquals(0, files.getPagination().getTotal());
	}

	@Test(expected = RestException.class)
	public void shouldErrorIfItemDoesNotBelongToUser() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity item = itemRandom.nextPersistedEntity(membership);
		item.getAttachments().add(file);
		itemRepositoryFacade.save(item);
		fixture.delete(membership.getTradeMembershipId(), item.getArticleId(), file.getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void shouldErrorIfFileDoesNotExist() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity item = itemRandom.nextPersistedEntity(membership);
		fixture.delete(membership.getTradeMembershipId(), item.getArticleId(), -1);
	}

}
