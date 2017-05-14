package com.matchandtrade.rest.v1.controller;

/**
 * Marker interface for rest controllers. Generally speaking each controller should follow the same sequence of steps for consistency sake and easy maintainability.
 * 
 * See below the Common steps for different types of HTTP method requests:
 * 
 * <b>POST, PUT, GET</b>
 * <ul>
 * 	<li>Validate request identity</li>
 * 	<li>Validate the request</li>
 *  <li>Transform the request (except for GET)</li>
 * 	<li>Delegate to service layer</li>
 * 	<li>Transform the response</li>
 * 	<li>Assemble links</li>
 *  <li>By default it returns HTTP status code 201 for POST, 200 for GET and PUT, 204 for DELETE.</li>
 * </ul>
 * 
 * <b>DELETE</b>
 * <ul>
 * 	<li>Validate request identity</li>
 * 	<li>Validate the request</li>
 * 	<li>Delegate to Service layer</li>
 *  <li>By default it return HTTP status code 204</li>
 * </ul>
 *   
 * @author rafael.santos.bra@gmail.com
 *
 */
public interface Controller {

}
