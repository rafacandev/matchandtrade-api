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
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.FileRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemFileControllerGetIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepository itemRepository;
	private ItemEntity item;
	private FileEntity file;
	@Autowired
	private FileRandom fileRandom;
	private ItemFileController fixture;
	private TradeMembershipEntity membership;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemFileController(false);
		}
		file = fileRandom.nextPersistedEntity();
		membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		item = itemRandom.nextPersistedEntity(membership);
		item.getFiles().add(file);
		itemRepository.save(item);
	}
	
	@Test
	public void shouldGetItemFiles() {
		SearchResult<FileJson> response = fixture.get(membership.getTradeMembershipId(), item.getItemId(), 1, 1);
		assertNotNull(response);
		assertEquals(1, response.getPagination().getNumber());
		assertEquals(1, response.getPagination().getTotal());
		assertEquals(1, response.getPagination().getSize());
		assertEquals(1, response.getResultList().size());
		FileJson fileResponse = response.getResultList().get(0);
		assertEquals(fileResponse.getFileId(), file.getFileId());
		assertEquals(fileResponse.getContentType(), file.getContentType());
		assertEquals(fileResponse.getName(), file.getName());
		// Two essences are expected one for ORIGINAL and one for THUMBNAIL
		assertEquals(1, fileResponse.getLinks().stream().filter(v -> v.getRel().equals("original")).collect(Collectors.toList()).size());
		assertEquals(1, fileResponse.getLinks().stream().filter(v -> v.getRel().equals("thumbnail")).collect(Collectors.toList()).size());
		assertEquals(2, file.getEssences().size());
	}

}
