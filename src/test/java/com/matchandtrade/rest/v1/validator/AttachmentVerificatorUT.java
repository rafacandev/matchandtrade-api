package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.random.AttachmentRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentVerificatorUT {

	private AttachmentVerificator fixture;
	private MockMultipartFile fileMock;

	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;
	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;

	@Before
	public void before() {
		fixture = new AttachmentVerificator();

		// Mock userRepositoryFacadeMock
		UserEntity articleOwner = new UserEntity();
		articleOwner.setUserId(1);
		doReturn(articleOwner).when(userRepositoryFacadeMock).findByArticleId(1);
		fixture.userRepositoryFacade = userRepositoryFacadeMock;

		// Mock articleRepositoryFacadeMock
		doReturn(new ArticleEntity()).when(articleRepositoryFacadeMock).get(any());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;

		fileMock = AttachmentRandom.newSampleMockMultiPartFile();
	}

	@Test
	public void validatePost_When_UserOwnsArticle_Then_Succeeds() {
		fixture.validatePost(1, 1, fileMock);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotOwnArticle_Then_BadRequest() {
		try {
			fixture.validatePost(0, 1, fileMock);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
