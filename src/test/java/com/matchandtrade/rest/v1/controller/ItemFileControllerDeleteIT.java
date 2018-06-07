package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

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
import com.matchandtrade.persistence.facade.FileRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.FileRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemFileControllerDeleteIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private FileRepositoryFacade fileRepositoryFacade;
	private FileEntity file;
	@Autowired
	private FileRandom fileRandom;
	private ItemFileController fixture;
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
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		item.getFiles().add(file);
		itemRepositoryFacade.save(item);
		fixture.delete(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
		SearchResult<FileEntity> files = fileRepositoryFacade.findFilesByItemId(item.getItemId(), 1, 10);
		assertEquals(0, files.getResultList().size());
		assertEquals(0, files.getPagination().getTotal());
	}

	@Test(expected = RestException.class)
	public void shouldErrorIfItemDoesNotBelongToUser() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		item.getFiles().add(file);
		itemRepositoryFacade.save(item);
		fixture.delete(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
	}

	@Test(expected = RestException.class)
	public void shouldErrorIfFileDoesNotExist() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		fixture.delete(membership.getTradeMembershipId(), item.getItemId(), -1);
	}

}
