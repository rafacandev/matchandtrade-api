package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.persistence.facade.WantItemRepositoryFacade;

@Service
public class WantItemService {

	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private WantItemRepositoryFacade wantItemRepositoryFacade;

	@Transactional
	public void create(WantItemEntity wantItem, Integer itemId) {
		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getWantItems().add(wantItem);
		itemRepositoryFacade.save(item);
		wantItem.setWantItemId(item.getWantItems().iterator().next().getWantItemId());
	}

	public long countWantItemInItem(Integer itemId, Integer desiredItemId) {
		return wantItemRepositoryFacade.countWantItemInItem(itemId, desiredItemId);
	}

	public long countWantItemPriorityInItem(Integer itemId, Integer priority) {
		return wantItemRepositoryFacade.countWantItemPriorityInItem(itemId, priority);
	}

	public SearchResult<WantItemEntity> search(Integer tradeMembershipId, Integer itemId, Integer _pageNumber, Integer _pageSize) {
		return wantItemRepositoryFacade.findByTradeMembershipAndItemId(tradeMembershipId, itemId, new Pagination(_pageNumber, _pageSize));
	}

	public WantItemEntity get(Integer tradeMembershipId, Integer itemId, Integer wantItemId) {
		return wantItemRepositoryFacade.findByTradeMembershipAndItemIdAndWantItemId(tradeMembershipId, itemId, wantItemId);
	}

}
