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
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemAttachmentControllerGetIT {
	
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private ItemRepository itemRepository;
	private ItemEntity item;
	private AttachmentEntity file;
	@Autowired
	private AttachmentRandom fileRandom;
	private ItemAttachmentController fixture;
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
		item.getAttachments().add(file);
		itemRepository.save(item);
	}
	
	@Test
	public void shouldGetItemFiles() {
		SearchResult<AttachmentJson> response = fixture.get(membership.getTradeMembershipId(), item.getArticleId(), 1, 1);
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
