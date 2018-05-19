package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.repository.FileRepository;

@Repository
public class FileRepositoryFacade {
	
	@Autowired
	private FileRepository fileRepository;
	
	public FileEntity get(Integer fileId) {
		return fileRepository.findOne(fileId);
	}

	public void save(FileEntity entity) {
		fileRepository.save(entity);
	}

	public void delete(Integer fileId) {
		fileRepository.delete(fileId);
	}
	
}
