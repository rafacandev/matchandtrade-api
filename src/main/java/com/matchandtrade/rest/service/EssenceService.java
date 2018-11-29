package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.persistence.facade.EssenceRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EssenceService {
	@Autowired
	private EssenceRepositoryFacade essenceRepositoryFacade;

	public void save(EssenceEntity essence) {
		essenceRepositoryFacade.save(essence);
	}
}