package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemAttachmentControllerPostIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
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
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemFileController(false);
		}
		file = fileRandom.nextPersistedEntity();
	}
	
	@Test
	public void shouldAddFileToItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		AttachmentJson response = fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getAttachmentId());
		assertNotNull(response);
		assertEquals(file.getAttachmentId(), response.getAttachmentId());
		SearchResult<AttachmentEntity> files = fileRepositoryFacade.findAttachmentsByItemId(item.getItemId(), 1, 10);
		assertEquals(1, files.getResultList().size());
		assertEquals(file.getAttachmentId(), files.getResultList().get(0).getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void shouldFailToAddMoreThan3FilesToItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		item.getAttachments().add(fileRandom.nextPersistedEntity());
		item.getAttachments().add(fileRandom.nextPersistedEntity());
		item.getAttachments().add(fileRandom.nextPersistedEntity());
		itemRepositoryFacade.save(item);
		fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getAttachmentId());
	}

}
