package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
// TODO create AttachmentControllerDeleteIT
@SuiteClasses({
	AttachmentControllerPostIT.class,
	AttachmentControllerGetIT.class,
	ArticleAttachmentControllerPostIT.class,
	ArticleAttachmentControllerGetIT.class,
	ArticleAttachmentControllerDeleteIT.class
})
public class AttachmentSuite {

}
