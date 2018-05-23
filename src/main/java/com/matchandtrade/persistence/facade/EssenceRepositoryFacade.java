package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.persistence.repository.EssenceRepository;

@Repository
public class EssenceRepositoryFacade {
	
	@Autowired
	private EssenceRepository essenceRepository;
	
	public void save(EssenceEntity entity) {
		essenceRepository.save(entity);
	}

	public void delete(Integer essenceId) {
		essenceRepository.delete(essenceId);
	}

}
