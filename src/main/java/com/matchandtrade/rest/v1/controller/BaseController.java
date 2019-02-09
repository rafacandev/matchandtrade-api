package com.matchandtrade.rest.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is an empty controller to assist on the creation of links (e.g.: via LinkAssemblers)
 */
@RestController
@RequestMapping(path = "/matchandtrade-api/v1")
public class BaseController implements Controller {

}