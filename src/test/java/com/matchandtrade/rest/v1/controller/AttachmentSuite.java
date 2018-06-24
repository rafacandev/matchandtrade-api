package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	AttachmentControllerPostIT.class,
	AttachmentControllerGetIT.class,
	ItemAttachmentControllerPostIT.class,
	ItemAttachmentControllerGetIT.class,
	ItemAttachmentControllerDeleteIT.class
})
public class AttachmentSuite {

}
