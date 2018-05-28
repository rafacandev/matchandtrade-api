package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.FileRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemFileControllerIT {
	
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepository itemRepository;
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddFileToItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		FileJson response = fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
		assertNotNull(response);
		assertEquals(file.getFileId(), response.getFileId());
		
		// Simplest way to get files from an item without running in lazy loading exception nor dealing with aspect
		List<FileEntity> actualFiles = (List<FileEntity>) entityManager
				.createQuery("SELECT i.files FROM ItemEntity i WHERE i.itemId = :itemId")
				.setParameter("itemId", item.getItemId())
				.getResultList();
		assertEquals(1, actualFiles.size());
		assertEquals(file.getFileId(), actualFiles.get(0).getFileId());
	}

	@Test(expected = RestException.class)
	public void shouldFailToAddMoreThan3FilesToItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		item.getFiles().add(fileRandom.nextPersistedEntity());
		item.getFiles().add(fileRandom.nextPersistedEntity());
		item.getFiles().add(fileRandom.nextPersistedEntity());
		itemRepository.save(item);
		fixture.post(membership.getTradeMembershipId(), item.getItemId(), file.getFileId());
	}

}
