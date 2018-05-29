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
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.FileRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemFileControllerPostIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	private FileEntity file;
	@Autowired
	private FileRandom fileRandom;
	private ItemFileController fixture;
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
		FileJson response = fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
		assertNotNull(response);
		assertEquals(file.getFileId(), response.getFileId());
		SearchResult<FileEntity> files = itemRepositoryFacade.findFilesByItemId(item.getItemId(), 1, 10);
		assertEquals(1, files.getResultList().size());
		assertEquals(file.getFileId(), files.getResultList().get(0).getFileId());
	}

	@Test(expected = RestException.class)
	public void shouldFailToAddMoreThan3FilesToItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		item.getFiles().add(fileRandom.nextPersistedEntity());
		item.getFiles().add(fileRandom.nextPersistedEntity());
		item.getFiles().add(fileRandom.nextPersistedEntity());
		itemRepositoryFacade.save(item);
		fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
	}

}
